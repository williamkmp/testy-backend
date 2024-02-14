package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.annotations.sender.Sender;
import com.mito.sectask.annotations.sendersession.SenderSession;
import com.mito.sectask.dto.dto.BlockMessageDto;
import com.mito.sectask.dto.dto.MenuPreviewDto;
import com.mito.sectask.dto.dto.PreviewMessageDto;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.File;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.exceptions.ForbiddenException;
import com.mito.sectask.exceptions.exceptions.MismatchedDataException;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.exceptions.exceptions.UnauthorizedException;
import com.mito.sectask.exceptions.httpexceptions.ForbiddenHttpException;
import com.mito.sectask.exceptions.httpexceptions.InternalServerErrorHttpException;
import com.mito.sectask.exceptions.httpexceptions.ResourceNotFoundHttpException;
import com.mito.sectask.exceptions.messsagingexceptions.ForbiddenPageOperationMessagingException;
import com.mito.sectask.exceptions.messsagingexceptions.PageDataMismatchMessagingException;
import com.mito.sectask.exceptions.messsagingexceptions.PageNotFoundMessagingException;
import com.mito.sectask.exceptions.messsagingexceptions.UnauthorizedPageAccessMessagingException;
import com.mito.sectask.services.block.BlockService;
import com.mito.sectask.services.file.FileService;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.values.BLOCK_TYPE;
import com.mito.sectask.values.DESTINATION;
import com.mito.sectask.values.KEY;
import com.mito.sectask.values.PREVIEW_ACTION;
import com.mito.sectask.values.USER_ROLE;
import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockController {

    private final BlockService blockService;
    private final RoleService roleService;
    private final PageService pageService;
    private final FileService fileService;
    private final UserService userService;
    private final SimpMessagingTemplate socket;

    @GetMapping("/collection/{collectionId}/page/preview")
    @Authenticated(true)
    public Response<MenuPreviewDto[]> getPagePreviewByCollection(
        @PathVariable("collectionId") String collectionId,
        @Caller User caller
    ) {
        try {
            Block collection = blockService
                .findById(collectionId)
                .orElseThrow(ResourceNotFoundException::new);
            Page parentPage = collection.getPage();
            roleService
                .getUserPageAuthority(caller.getId(), parentPage.getId())
                .orElseThrow(ForbiddenException::new);
            List<MenuPreviewDto> previews = pageService
                .findByCollectionId(collectionId)
                .stream()
                .map(page ->
                    new MenuPreviewDto()
                        .setId(page.getId().toString())
                        .setIconKey(page.getIconKey())
                        .setTitle(page.getName())
                )
                .collect(Collectors.toList());
            MenuPreviewDto[] responseData = previews.toArray(
                new MenuPreviewDto[0]
            );
            return new Response<MenuPreviewDto[]>(HttpStatus.OK)
                .setData(responseData);
        } catch (ForbiddenException e) {
            throw new ForbiddenHttpException();
        } catch (ResolutionException e) {
            throw new ResourceNotFoundHttpException();
        } catch (Exception e) {
            throw new InternalServerErrorHttpException();
        }
    }

    @MessageMapping("/page/{pageId}/block.transaction")
    public void receiveBlockUpdate(
        @DestinationVariable("pageId") Long pageId,
        @Payload BlockMessageDto request,
        @Sender User sender,
        @SenderSession String sessionId
    )
        throws PageNotFoundMessagingException, UnauthorizedPageAccessMessagingException, PageDataMismatchMessagingException, ForbiddenPageOperationMessagingException {
        try {
            pageService.findById(pageId).orElseThrow(NotFoundException::new);
            Role role = roleService
                .getUserPageAuthority(sender.getId(), pageId)
                .orElseThrow(UnauthorizedException::new);

            if (USER_ROLE.VIEWERS == role.getName()) {
                throw new ForbiddenException();
            }

            Block block = blockService
                .findById(request.getId())
                .orElseThrow(ResourceNotFoundException::new);
            File newFile = request.getFileId() != null
                ? fileService
                    .findById(Long.valueOf(request.getFileId()))
                    .orElse(null)
                : null;

            File previousFile = block.getFile();
            if (previousFile != null && newFile == null) {
                fileService.deleteById(previousFile.getId());
            }

            String oldContent = block.getContent();
            String oldIconKey = block.getIconKey();
            BLOCK_TYPE oldBlockType = block.getBlockType();

            block.setContent(request.getContent());
            block.setBlockType(request.getType());
            block.setWidth(request.getWidth());
            block.setIconKey(request.getIconKey());
            block.setFile(newFile);
            block.setIsChecked(request.getIsChecked());
            block =
                blockService
                    .save(block)
                    .orElseThrow(MismatchedDataException::new);

            if (block.getBlockType() == BLOCK_TYPE.COLLECTION) {
                boolean doNotify =
                    !Objects.equals(oldIconKey, block.getIconKey()) ||
                    !Objects.equals(oldContent, block.getContent());
                if (doNotify) {
                    Page parentPage = block.getPage();
                    String senderId = sender.getId().toString();
                    boolean isNewColelction = !Objects.equals(
                        oldBlockType,
                        BLOCK_TYPE.COLLECTION
                    );
                    List<User> members = userService.findMembersOfCollection(
                        block.getId()
                    );
                    for (User member : members) {
                        socket.convertAndSend(
                            DESTINATION.userPreview(member.getId()),
                            new PreviewMessageDto()
                                .setAction(
                                    isNewColelction
                                        ? PREVIEW_ACTION.ADD
                                        : PREVIEW_ACTION.UPDATE
                                )
                                .setParentId(parentPage.getId().toString())
                                .setId(block.getId())
                                .setIconKey(block.getIconKey())
                                .setName(block.getContent()),
                            Map.ofEntries(
                                Map.entry(KEY.SENDER_USER_ID, senderId),
                                Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                            )
                        );
                    }
                }
            }

            socket.convertAndSend(
                DESTINATION.pageBlockTransaction(pageId),
                new BlockMessageDto()
                    .setId(request.getId())
                    .setType(request.getType())
                    .setContent(request.getContent())
                    .setFileId(
                        newFile != null ? newFile.getId().toString() : null
                    )
                    .setWidth(request.getWidth())
                    .setIconKey(request.getIconKey())
                    .setIsChecked(request.getIsChecked()),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, sender.getId().toString()),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );
        } catch (ForbiddenException e) {
            throw new ForbiddenPageOperationMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (UnauthorizedException e) {
            throw new UnauthorizedPageAccessMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (ResourceNotFoundException e) {
            throw new PageNotFoundMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (Exception e) {
            log.error(
                "Error handling block.transaction, blockId:{} pageId:{} userId:{}",
                request.getId(),
                pageId,
                sender.getId()
            );
            e.printStackTrace();
            throw new PageDataMismatchMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        }
    }

    @MessageMapping("/page/{pageId}/block.move")
    public void receiveBlockMove(
        @DestinationVariable("pageId") Long pageId,
        @Payload BlockMessageDto request,
        @Sender User sender,
        @SenderSession String sessionId
    )
        throws PageNotFoundMessagingException, UnauthorizedPageAccessMessagingException, PageDataMismatchMessagingException, ForbiddenPageOperationMessagingException {
        try {
            // checking user access and data integrity
            pageService.findById(pageId).orElseThrow(NotFoundException::new);
            Role role = roleService
                .getUserPageAuthority(sender.getId(), pageId)
                .orElseThrow(UnauthorizedException::new);

            if (USER_ROLE.VIEWERS == role.getName()) {
                throw new ForbiddenException();
            }

            Block block = blockService.findById(request.getId()).orElse(null);

            if (block == null) return;
            String oldPrevId = block.getPrev() != null
                ? block.getPrev().getId()
                : null;
            String oldNextId = block.getNext() != null
                ? block.getNext().getId()
                : null;
            boolean isBlockMove =
                !Objects.equals(oldPrevId, request.getPrevId()) &&
                !Objects.equals(oldNextId, request.getNextId());
            if (!isBlockMove) return;

            Block updatedBlock = blockService.moveBlock(
                request.getId(),
                request.getPrevId(),
                request.getNextId()
            );

            socket.convertAndSend(
                DESTINATION.pageBlockMove(pageId),
                new BlockMessageDto()
                    .setId(updatedBlock.getId())
                    .setPrevId(
                        updatedBlock.getPrev() != null
                            ? updatedBlock.getPrev().getId()
                            : null
                    )
                    .setNextId(
                        updatedBlock.getNext() != null
                            ? updatedBlock.getNext().getId()
                            : null
                    ),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, sender.getId().toString()),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );
        } catch (ForbiddenException e) {
            throw new ForbiddenPageOperationMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (UnauthorizedException e) {
            throw new UnauthorizedPageAccessMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (ResourceNotFoundException e) {
            throw new PageNotFoundMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (Exception e) {
            log.error(
                "Error handling block.move, blockId:{} pageId:{} userId:{}",
                request.getId(),
                pageId,
                sender.getId()
            );
            e.printStackTrace();
            throw new PageDataMismatchMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        }
    }

    @MessageMapping("/page/{pageId}/block.add")
    public void receiveBlockInsert(
        @DestinationVariable("pageId") Long pageId,
        @Payload BlockMessageDto request,
        @Sender User sender,
        @SenderSession String sessionId
    )
        throws PageNotFoundMessagingException, ForbiddenPageOperationMessagingException, UnauthorizedPageAccessMessagingException, PageDataMismatchMessagingException {
        try {
            // checking user access and data integrity
            Page page = pageService
                .findById(pageId)
                .orElseThrow(NotFoundException::new);
            Role role = roleService
                .getUserPageAuthority(sender.getId(), pageId)
                .orElseThrow(ForbiddenException::new);

            if (USER_ROLE.VIEWERS == role.getName()) {
                throw new ForbiddenException();
            }

            // Add block type from client is always PARAGRAPH (UI/UX Specification)
            Block insertedBlock = blockService
                .insertBlockAfter(
                    request.getPrevId(),
                    new Block()
                        .setPage(page)
                        .setId(request.getId())
                        .setBlockType(request.getType())
                        .setContent(request.getContent())
                        .setWidth(request.getWidth())
                        .setIconKey(request.getIconKey())
                        .setIsChecked(request.getIsChecked())
                )
                .orElseThrow(NotFoundException::new);

            socket.convertAndSend(
                DESTINATION.pageBlockAdd(pageId),
                new BlockMessageDto()
                    .setId(insertedBlock.getId())
                    .setType(insertedBlock.getBlockType())
                    .setWidth(insertedBlock.getWidth())
                    .setContent(insertedBlock.getContent())
                    .setIconKey(insertedBlock.getIconKey())
                    .setIsChecked(insertedBlock.getIsChecked())
                    .setPrevId(
                        insertedBlock.getPrev() != null
                            ? insertedBlock.getPrev().getId()
                            : null
                    )
                    .setNextId(
                        insertedBlock.getNext() != null
                            ? insertedBlock.getNext().getId()
                            : null
                    ),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, sender.getId().toString()),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );
        } catch (ForbiddenException e) {
            throw new ForbiddenPageOperationMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (UnauthorizedException e) {
            throw new UnauthorizedPageAccessMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (ResourceNotFoundException e) {
            throw new PageNotFoundMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (Exception e) {
            log.error(
                "Error handling block.add pageId:{} userId:{}",
                pageId,
                sender.getId()
            );
            e.printStackTrace();
            throw new PageDataMismatchMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        }
    }

    @MessageMapping("/page/{pageId}/block.delete")
    public void receiveBlockDelete(
        @DestinationVariable("pageId") Long pageId,
        @Payload BlockMessageDto request,
        @Sender User sender,
        @SenderSession String sessionId
    )
        throws PageNotFoundMessagingException, ForbiddenPageOperationMessagingException, UnauthorizedPageAccessMessagingException, PageDataMismatchMessagingException {
        try {
            // checking user access and data integrity
            pageService.findById(pageId).orElseThrow(NotFoundException::new);
            Role role = roleService
                .getUserPageAuthority(sender.getId(), pageId)
                .orElseThrow(ForbiddenException::new);

            if (USER_ROLE.VIEWERS == role.getName()) {
                throw new ForbiddenException();
            }

            Block deletedBlock = blockService
                .deleteBlock(request.getId())
                .orElseThrow(NotFoundException::new);

            if (deletedBlock.getBlockType() == BLOCK_TYPE.COLLECTION) {
                List<User> members = userService.findMembersOfPage(pageId);
                String senderId = sender.getId().toString();
                for (User member : members) {
                    socket.convertAndSend(
                        DESTINATION.userPreview(member.getId()),
                        new PreviewMessageDto()
                            .setAction(PREVIEW_ACTION.DELETE)
                            .setParentId(pageId.toString())
                            .setId(deletedBlock.getId()),
                        Map.ofEntries(
                            Map.entry(KEY.SENDER_USER_ID, senderId),
                            Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                        )
                    );
                }
            }

            socket.convertAndSend(
                DESTINATION.pageBlockDel(pageId),
                new BlockMessageDto().setId(deletedBlock.getId()),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, sender.getId().toString()),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );
        } catch (ForbiddenException e) {
            throw new ForbiddenPageOperationMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (UnauthorizedException e) {
            throw new UnauthorizedPageAccessMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (ResourceNotFoundException e) {
            throw new PageNotFoundMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        } catch (Exception e) {
            log.error(
                "Error handling block.delete blockId:{} pageId:{} userId:{}",
                request.getId(),
                pageId,
                sender.getId()
            );
            e.printStackTrace();
            throw new PageDataMismatchMessagingException(
                sender.getId(),
                pageId,
                sessionId
            );
        }
    }
}
