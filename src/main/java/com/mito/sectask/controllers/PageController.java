package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.annotations.callersession.CallerSession;
import com.mito.sectask.dto.dto.BlockDto;
import com.mito.sectask.dto.dto.MemberDto;
import com.mito.sectask.dto.dto.MenuPreviewDto;
import com.mito.sectask.dto.dto.PageDeleteDto;
import com.mito.sectask.dto.dto.PageDto;
import com.mito.sectask.dto.dto.PageMessagingExceptionDto;
import com.mito.sectask.dto.dto.PreviewMessageDto;
import com.mito.sectask.dto.request.page.PageCreateRequest;
import com.mito.sectask.dto.request.page.PageUpdateRequest;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.File;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.exceptions.ForbiddenException;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.exceptions.exceptions.UnauthorizedException;
import com.mito.sectask.exceptions.exceptions.UserNotFoundException;
import com.mito.sectask.exceptions.httpexceptions.ForbiddenHttpException;
import com.mito.sectask.exceptions.httpexceptions.InternalServerErrorHttpException;
import com.mito.sectask.exceptions.httpexceptions.ResourceNotFoundHttpException;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.block.BlockService;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.utils.Util;
import com.mito.sectask.values.DESTINATION;
import com.mito.sectask.values.KEY;
import com.mito.sectask.values.MESSAGES;
import com.mito.sectask.values.PREVIEW_ACTION;
import com.mito.sectask.values.USER_ROLE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;
    private final ImageService imageService;
    private final RoleService roleService;
    private final BlockService blockService;
    private final UserService userService;
    private final SimpMessagingTemplate socket;

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public Response<PageDto> createPage(
        @RequestBody PageCreateRequest request,
        @CallerSession String sessionId,
        @Caller User caller
    ) {
        // Get parent and Image data
        String collectionId = request.getCollectionId();
        Long imageId = Util.String.toLong(request.getImageId()).orElse(null);
        Block collection = blockService.findById(collectionId).orElse(null);
        File coverImagFile = imageService.findById(imageId).orElse(null);

        Page newPage = new Page()
            .setIconKey(request.getIconKey())
            .setName(request.getTitle())
            .setImage(coverImagFile)
            .setImagePosition(request.getImagePosition());

        Page createdPage = (collection == null)
            ? pageService
                .createRootPage(newPage, caller.getId(), request.getMembers())
                .orElseThrow(InternalServerErrorHttpException::new)
            : pageService
                .createSubPage(newPage, collectionId)
                .orElseThrow(InternalServerErrorHttpException::new);

        // Notify user for update
        String senderId = caller.getId().toString();
        List<User> members = userService.findMembersOfPage(createdPage.getId());
        for (User member : members) {
            socket.convertAndSend(
                DESTINATION.userPreview(member.getId()),
                new PreviewMessageDto()
                    .setAction(PREVIEW_ACTION.ADD)
                    .setParentId(request.getCollectionId())
                    .setIconKey(createdPage.getIconKey())
                    .setName(createdPage.getName())
                    .setId(createdPage.getId().toString()),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, senderId),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );
        }

        // Update colletion table view
        if (createdPage.getCollection() != null) {
            socket.convertAndSend(
                DESTINATION.collectionPreview(collectionId),
                new PreviewMessageDto()
                    .setAction(PREVIEW_ACTION.ADD)
                    .setParentId(request.getCollectionId())
                    .setIconKey(createdPage.getIconKey())
                    .setName(createdPage.getName())
                    .setId(createdPage.getId().toString()),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, senderId),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );
        }

        return new Response<PageDto>(HttpStatus.CREATED)
            .setData(
                new PageDto()
                    .setTitle(createdPage.getName())
                    .setId(createdPage.getId().toString())
                    .setIconKey(createdPage.getIconKey())
                    .setAuthority(USER_ROLE.FULL_ACCESS)
                    .setImageId(Util.String.valueOf(imageId).orElse(null))
                    .setImagePosition(createdPage.getImagePosition())
            );
    }

    @GetMapping("/search/{searchText}")
    @Authenticated(true)
    public Response<MenuPreviewDto[]> searchPageByName(
        @PathVariable("searchText") String searchText,
        @Caller User caller
    ) {
        try {
            List<Page> searchResult = pageService.searchPageByName(
                searchText,
                caller.getId()
            );
            return new Response<MenuPreviewDto[]>(HttpStatus.OK)
                .setData(
                    searchResult
                        .stream()
                        .map(result ->
                            new MenuPreviewDto()
                                .setId(result.getId().toString())
                                .setIconKey(result.getIconKey())
                                .setTitle(result.getName())
                        )
                        .collect(Collectors.toList())
                        .toArray(new MenuPreviewDto[0])
                );
        } catch (Exception e) {
            throw new ResourceNotFoundHttpException();
        }
    }

    @PutMapping(
        path = "/{pageId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public Response<PageDto> updatePage(
        @PathVariable("pageId") Long pageId,
        @RequestBody PageUpdateRequest request,
        @Caller User caller,
        @CallerSession String sessionId
    ) {
        // Checking user's Role
        Role userRole = roleService
            .getUserPageAuthority(caller.getId(), pageId)
            .orElseThrow(ForbiddenHttpException::new);
        USER_ROLE authority = userRole.getName();

        final USER_ROLE[] allowedAuthority = {
            USER_ROLE.COLLABORATORS,
            USER_ROLE.FULL_ACCESS,
        };

        if (!Arrays.asList(allowedAuthority).contains(authority)) {
            throw new ForbiddenHttpException();
        }

        Long imageId = Util.String.toLong(request.getImageId()).orElse(null);
        imageService.updatePageCoverImage(pageId, imageId);
        Page page = pageService
            .findById(pageId)
            .orElseThrow(ResourceNotFoundHttpException::new);
        String oldIconKey = page.getIconKey();
        String oldTitle = page.getName();
        Float imagePosition = Optional
            .ofNullable(request.getImagePosition())
            .orElse(50f);
        page.setName(request.getTitle());
        page.setIconKey(request.getIconKey());
        page.setImagePosition(imagePosition);
        Page updatedPage = pageService
            .update(page)
            .orElseThrow(ResourceNotFoundHttpException::new);
        String updatedImageId = updatedPage.getImage() != null
            ? updatedPage.getImage().getId().toString()
            : null;

        // Notify user for preview update if title, or icon key is changed
        Boolean doNotifyMember =
            !Objects.equals(oldIconKey, updatedPage.getIconKey()) ||
            !Objects.equals(oldTitle, updatedPage.getName());
        if (Boolean.TRUE.equals(doNotifyMember)) {
            List<User> members = userService.findMembersOfPage(
                updatedPage.getId()
            );
            for (User member : members) {
                socket.convertAndSend(
                    DESTINATION.userPreview(member.getId()),
                    new PreviewMessageDto()
                        .setAction(PREVIEW_ACTION.UPDATE)
                        .setId(updatedPage.getId().toString())
                        .setIconKey(updatedPage.getIconKey())
                        .setName(updatedPage.getName())
                        .setParentId(
                            updatedPage.getCollection() != null
                                ? updatedPage.getCollection().getId()
                                : null
                        ),
                    Map.ofEntries(
                        Map.entry(
                            KEY.SENDER_USER_ID,
                            caller.getId().toString()
                        ),
                        Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                    )
                );
            }
        }

        // Notify collection preview subscriber
        if (updatedPage.getCollection() != null) {
            String collectionId = updatedPage.getCollection().getId();
            String destination = DESTINATION.collectionPreview(collectionId);
            socket.convertAndSend(
                destination,
                new PreviewMessageDto()
                    .setAction(PREVIEW_ACTION.UPDATE)
                    .setParentId(updatedPage.getCollection().getId())
                    .setIconKey(updatedPage.getIconKey())
                    .setName(updatedPage.getName())
                    .setId(updatedPage.getId().toString()),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, caller.getId().toString()),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );
        }

        // Notify all page subscriber
        socket.convertAndSend(
            DESTINATION.pageUpdate(pageId),
            new PageDto()
                .setIconKey(updatedPage.getIconKey())
                .setTitle(updatedPage.getName())
                .setImageId(updatedImageId)
                .setImagePosition(imagePosition),
            Map.ofEntries(
                Map.entry(KEY.SENDER_USER_ID, caller.getId().toString()),
                Map.entry(KEY.SENDER_SESSION_ID, sessionId)
            )
        );

        return new Response<PageDto>(HttpStatus.OK)
            .setData(
                new PageDto()
                    .setId(updatedPage.getId().toString())
                    .setTitle(updatedPage.getName())
                    .setAuthority(authority)
                    .setIconKey(updatedPage.getIconKey())
                    .setImagePosition(updatedPage.getImagePosition())
                    .setImageId(updatedImageId)
            );
    }

    @DeleteMapping("/{pageId}")
    @Authenticated(true)
    public Response<PageDeleteDto> deletePage(
        @PathVariable("pageId") Long pageId,
        @Caller User caller,
        @CallerSession String session
    ) {
        try {
            Role userRole = roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            Page page = pageService
                .findById(pageId)
                .orElseThrow(ResourceNotFoundException::new);
            Page parentPage = page.getCollection() != null
                ? page.getCollection().getPage()
                : null;
            String pageCollectionId = page.getCollection() != null
                ? page.getCollection().getId()
                : null;
            String afterDeleteRedirectUrl = page.getCollection() != null
                ? "/page/" + page.getCollection().getPage().getId().toString()
                : "/";

            Boolean pageIsRoot = page.getCollection() == null;
            Boolean canDelete =
                (pageIsRoot && userRole.getName() == USER_ROLE.FULL_ACCESS) ||
                (!pageIsRoot &&
                    userRole.getName() == USER_ROLE.COLLABORATORS) ||
                (!pageIsRoot && userRole.getName() == USER_ROLE.FULL_ACCESS);

            // Delete process
            List<User> members = userService.findMembersOfPage(pageId);
            if (Boolean.TRUE.equals(canDelete)) {
                Page deletedPage = pageService
                    .delete(pageId)
                    .orElseThrow(ResourceNotFoundException::new);

                socket.convertAndSend(
                    DESTINATION.pageUserError(pageId, caller.getId()),
                    new PageMessagingExceptionDto()
                        .setStatus(HttpStatus.NOT_FOUND.value())
                        .setMessage(MESSAGES.ERROR_RESOURCE_NOT_FOUND)
                        .setPageId(deletedPage.getId().toString())
                        .setRedirectUrl(afterDeleteRedirectUrl)
                        .setUserId(caller.getId().toString()),
                    Map.ofEntries(
                        Map.entry(
                            KEY.SENDER_USER_ID,
                            caller.getId().toString()
                        ),
                        Map.entry(KEY.SENDER_SESSION_ID, session)
                    )
                );

                // Notify member of page deletion
                for (User member : members) {
                    socket.convertAndSend(
                        DESTINATION.userPreview(member.getId()),
                        new PreviewMessageDto()
                            .setAction(PREVIEW_ACTION.DELETE)
                            .setId(pageId.toString())
                            .setParentId(pageCollectionId)
                            .setIconKey(page.getIconKey())
                            .setName(page.getName()),
                        Map.ofEntries(
                            Map.entry(
                                KEY.SENDER_USER_ID,
                                caller.getId().toString()
                            ),
                            Map.entry(KEY.SENDER_SESSION_ID, session)
                        )
                    );
                }

                // Notify collection subscriber
                if (pageCollectionId != null) {
                    socket.convertAndSend(
                        DESTINATION.collectionPreview(pageCollectionId),
                        new PreviewMessageDto()
                            .setAction(PREVIEW_ACTION.DELETE)
                            .setId(pageId.toString())
                            .setParentId(pageCollectionId)
                            .setIconKey(page.getIconKey())
                            .setName(page.getName()),
                        Map.ofEntries(
                            Map.entry(
                                KEY.SENDER_USER_ID,
                                caller.getId().toString()
                            ),
                            Map.entry(KEY.SENDER_SESSION_ID, session)
                        )
                    );
                }

                return new Response<PageDeleteDto>(HttpStatus.OK)
                    .setData(
                        new PageDeleteDto()
                            .setPageId(deletedPage.getId().toString())
                            .setRedirectPageId(
                                parentPage != null
                                    ? parentPage.getId().toString()
                                    : null
                            )
                    );
            } else {
                throw new ForbiddenException();
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundHttpException();
        } catch (ForbiddenException e) {
            throw new ForbiddenHttpException();
        } catch (UnauthorizedException e) {
            throw new UnauthorizedHttpException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException();
        }
    }

    @GetMapping(path = "/preview", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<MenuPreviewDto[]> getUserRootPagePreviews(
        @Caller User caller
    ) {
        List<Page> userRootPages = pageService.getRootPages(caller.getId());
        MenuPreviewDto[] pagePreviews = userRootPages
            .stream()
            .map(page ->
                new MenuPreviewDto()
                    .setId(page.getId().toString())
                    .setIconKey(page.getIconKey())
                    .setTitle(page.getName())
            )
            .collect(Collectors.toList())
            .toArray(new MenuPreviewDto[0]);

        return new Response<MenuPreviewDto[]>(HttpStatus.OK)
            .setData(pagePreviews);
    }

    @GetMapping("/{pageId}/user")
    @Authenticated(true)
    public Response<MemberDto[]> getPageMembers(
        @Caller User caller,
        @PathVariable("pageId") Long pageId
    ) {
        try {
            roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            Page page = pageService
                .findById(pageId)
                .orElseThrow(ResourceNotFoundException::new);

            List<User> userList = userService.findMembersOfPage(page.getId());
            List<MemberDto> memberList = new ArrayList<>();

            for (User user : userList) {
                Role userAuthority = roleService
                    .getUserPageAuthority(user.getId(), pageId)
                    .orElseThrow(Exception::new);
                memberList.add(
                    new MemberDto()
                        .setId(user.getId().toString())
                        .setEmail(user.getEmail())
                        .setTagName(user.getTagName())
                        .setFullName(user.getFullName())
                        .setAuthority(userAuthority.getName())
                        .setImageId(
                            user.getImage() != null
                                ? user.getImage().getId().toString()
                                : null
                        )
                );
            }
            return new Response<MemberDto[]>(HttpStatus.OK)
                .setData(memberList.toArray(new MemberDto[0]));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundHttpException();
        } catch (ForbiddenException e) {
            throw new ForbiddenHttpException();
        } catch (UnauthorizedException e) {
            throw new UnauthorizedHttpException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException();
        }
    }

    @GetMapping("/{pageId}/user/{userId}")
    @Authenticated(true)
    public Response<MemberDto> getMemberInformation(
        @PathVariable("pageId") Long pageId,
        @PathVariable("userId") Long userId,
        @Caller User caller
    ) {
        try {
            roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            Page page = pageService
                .findById(pageId)
                .orElseThrow(ResourceNotFoundException::new);
            User user = userService
                .findById(userId)
                .orElseThrow(ResourceNotFoundException::new);
            Role userRole = roleService
                .getUserPageAuthority(user.getId(), page.getId())
                .orElseThrow(Exception::new);

            return new Response<MemberDto>(HttpStatus.OK)
                .setData(
                    new MemberDto()
                        .setId(user.getId().toString())
                        .setEmail(user.getEmail())
                        .setTagName(user.getTagName())
                        .setFullName(user.getFullName())
                        .setAuthority(userRole.getName())
                        .setImageId(
                            user.getImage() != null
                                ? user.getImage().getId().toString()
                                : null
                        )
                );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundHttpException();
        } catch (ForbiddenException e) {
            throw new ForbiddenHttpException();
        } catch (UnauthorizedException e) {
            throw new UnauthorizedHttpException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException();
        }
    }

    @GetMapping(path = "/{pageId}/collection/preview")
    @Authenticated(true)
    public Response<MenuPreviewDto[]> getCollectionPreviewOfPage(
        @PathVariable("pageId") Long pageId,
        @Caller User caller
    ) {
        try {
            roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            List<Block> colelctions = blockService.findAllCollectionByPageId(
                pageId
            );
            List<MenuPreviewDto> colletionPreviews = colelctions
                .stream()
                .map(collection ->
                    new MenuPreviewDto()
                        .setIconKey(collection.getIconKey())
                        .setId(collection.getId())
                        .setTitle(collection.getContent())
                )
                .collect(Collectors.toList());
            MenuPreviewDto[] responseBody = colletionPreviews.toArray(
                new MenuPreviewDto[0]
            );
            return new Response<MenuPreviewDto[]>(HttpStatus.OK)
                .setData(responseBody);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundHttpException();
        } catch (ForbiddenException e) {
            throw new ForbiddenHttpException();
        } catch (UserNotFoundException e) {
            throw new UnauthorizedHttpException();
        } catch (Exception e) {
            throw new InternalServerErrorHttpException();
        }
    }

    @GetMapping(path = "/{pageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<PageDto> getPageInformation(
        @PathVariable("pageId") Long pageId,
        @Caller User caller
    ) {
        try {
            Role userRole = roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            USER_ROLE authority = userRole.getName();

            Page page = pageService
                .findById(pageId)
                .orElseThrow(ResourceNotFoundException::new);

            String imageId = (page.getImage() != null)
                ? page.getImage().getId().toString()
                : null;

            String collectionId = page.getCollection() != null
                ? page.getCollection().getId()
                : null;

            return new Response<PageDto>(HttpStatus.OK)
                .setData(
                    new PageDto()
                        .setId(page.getId().toString())
                        .setIconKey(page.getIconKey())
                        .setTitle(page.getName())
                        .setImagePosition(page.getImagePosition())
                        .setImageId(imageId)
                        .setAuthority(authority)
                        .setCollectionId(collectionId)
                );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundHttpException();
        } catch (ForbiddenException e) {
            throw new ForbiddenHttpException();
        } catch (UserNotFoundException e) {
            throw new UnauthorizedHttpException();
        } catch (Exception e) {
            throw new InternalServerErrorHttpException();
        }
    }

    @GetMapping("/{pageId}/block")
    @Authenticated(true)
    public Response<BlockDto[]> getPageBlocks(
        @PathVariable("pageId") Long pageId,
        @Caller User caller
    ) {
        try {
            roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            List<Block> blocks = blockService.findAllByPageId(pageId);
            BlockDto[] responseBody = blocks
                .stream()
                .map(block ->
                    new BlockDto()
                        .setId(block.getId())
                        .setType(block.getBlockType())
                        .setContent(block.getContent())
                        .setIconKey(block.getIconKey())
                        .setWidth(block.getWidth())
                        .setIsChecked(block.getIsChecked())
                        .setFileId(
                            block.getFile() != null
                                ? block.getFile().getId().toString()
                                : null
                        )
                )
                .collect(Collectors.toList())
                .toArray(new BlockDto[0]);
            return new Response<BlockDto[]>(HttpStatus.OK)
                .setData(responseBody);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundHttpException();
        } catch (ForbiddenException e) {
            throw new ForbiddenHttpException();
        } catch (UserNotFoundException e) {
            throw new UnauthorizedHttpException();
        } catch (Exception e) {
            throw new InternalServerErrorHttpException();
        }
    }
}
