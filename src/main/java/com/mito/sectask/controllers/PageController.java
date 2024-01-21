package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.dto.BlockDto;
import com.mito.sectask.dto.dto.MenuPreviewDto;
import com.mito.sectask.dto.dto.PageDto;
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
import com.mito.sectask.values.MESSAGES;
import com.mito.sectask.values.PREVIEW_ACTION;
import com.mito.sectask.values.USER_ROLE;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<PageDto> createPage(@RequestBody PageCreateRequest request, @Caller User caller) {
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
                : pageService.createSubPage(newPage, collectionId).orElseThrow(InternalServerErrorHttpException::new);

        // Notify user for update
        List<User> members = userService.findMembersOfPage(createdPage.getId());
        for(User member : members) {
            socket.convertAndSend(DESTINATION.userPreview(member.getId()), new PreviewMessageDto()
                .setAction(PREVIEW_ACTION.ADD)
                .setParentId(request.getCollectionId())
                .setIconKey(createdPage.getIconKey())
                .setIconKey(createdPage.getName())
            );
        }

        return new Response<PageDto>(HttpStatus.CREATED)
                .setData(new PageDto()
                        .setTitle(createdPage.getName())
                        .setId(createdPage.getId().toString())
                        .setIconKey(createdPage.getIconKey())
                        .setAuthority(USER_ROLE.FULL_ACCESS)
                        .setImageId(Util.String.valueOf(imageId).orElse(null))
                        .setImagePosition(createdPage.getImagePosition()));
    }

    @PutMapping(
            path = "/{pageId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<PageDto> updatePage(
            @PathVariable("pageId") Long pageId, @RequestBody PageUpdateRequest request, @Caller User caller) {
        // Checking user's Role
        Role userRole =
                roleService.getUserPageAuthority(caller.getId(), pageId).orElseThrow(ForbiddenHttpException::new);
        USER_ROLE authority = userRole.getName();

        final USER_ROLE[] allowedAuthority = {
            USER_ROLE.COLLABORATORS, USER_ROLE.FULL_ACCESS,
        };

        if (!Arrays.asList(allowedAuthority).contains(authority)) {
            throw new ForbiddenHttpException();
        }

        Long imageId = Util.String.toLong(request.getImageId()).orElse(null);
        imageService.updatePageCoverImage(pageId, imageId);
        Page page = pageService.findById(pageId).orElseThrow(ResourceNotFoundHttpException::new);
        Float imagePosition = Optional.ofNullable(request.getImagePosition()).orElse(50f);
        page.setIconKey(request.getIconKey());
        page.setImagePosition(imagePosition);
        Page updatedPage = pageService.update(page).orElseThrow(ResourceNotFoundHttpException::new);
        String updatedImageId =
                updatedPage.getImage() != null ? updatedPage.getImage().getId().toString() : null;

        // Notify user for update
        List<User> members = userService.findMembersOfPage(updatedPage.getId());
        for(User member : members) {
            socket.convertAndSend(DESTINATION.userPreview(member.getId()), new PreviewMessageDto()
                .setAction(PREVIEW_ACTION.ADD)
                .setId(updatedPage.getId().toString())
                .setIconKey(updatedPage.getIconKey())
                .setIconKey(updatedPage.getName())
            );
        }

        return new Response<PageDto>(HttpStatus.OK)
                .setData(new PageDto()
                        .setId(updatedPage.getId().toString())
                        .setTitle(updatedPage.getName())
                        .setAuthority(authority)
                        .setIconKey(updatedPage.getIconKey())
                        .setImagePosition(updatedPage.getImagePosition())
                        .setImageId(updatedImageId));
    }

    @GetMapping(path = "/preview", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<MenuPreviewDto[]> getUserRootPagePreviews(@Caller User caller) {
        List<Page> userRootPages = pageService.getRootPages(caller.getId());
        MenuPreviewDto[] pagePreviews = userRootPages.stream()
                .map(page -> new MenuPreviewDto()
                        .setId(page.getId().toString())
                        .setIconKey(page.getIconKey())
                        .setTitle(page.getName()))
                .collect(Collectors.toList())
                .toArray(new MenuPreviewDto[0]);

        return new Response<MenuPreviewDto[]>(HttpStatus.OK).setData(pagePreviews);
    }

    @GetMapping(path = "/{pageId}/collection/preview")
    @Authenticated(true)
    public Response<MenuPreviewDto[]> getCollectionPreviewOfPage(
            @PathVariable("pageId") Long pageId, @Caller User caller) {
        try {
            roleService.getUserPageAuthority(caller.getId(), pageId).orElseThrow(ForbiddenException::new);
            List<Block> colelctions = blockService.findAllCollectionByPageId(pageId);
            List<MenuPreviewDto> colletionPreviews = colelctions.stream()
                    .map(collection -> new MenuPreviewDto()
                            .setIconKey(collection.getIconKey())
                            .setId(collection.getId())
                            .setTitle(collection.getContent()))
                    .collect(Collectors.toList());
            MenuPreviewDto[] responseBody = colletionPreviews.toArray(new MenuPreviewDto[0]);
            return new Response<MenuPreviewDto[]>(HttpStatus.OK).setData(responseBody);
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
    public Response<PageDto> getPageInformation(@PathVariable("pageId") Long pageId, @Caller User caller) {
        Role userRole =
                roleService.getUserPageAuthority(caller.getId(), pageId).orElseThrow(ForbiddenHttpException::new);
        USER_ROLE authority = userRole.getName();

        Optional<Page> maybePage = pageService.findById(pageId);
        if (maybePage.isEmpty()) {
            return new Response<PageDto>(HttpStatus.BAD_REQUEST).setMessage(MESSAGES.ERROR_RESOURCE_NOT_FOUND);
        }
        Page page = maybePage.get();
        String imageId = (page.getImage() != null) ? page.getImage().getId().toString() : null;

        return new Response<PageDto>(HttpStatus.OK)
                .setData(new PageDto()
                        .setId(page.getId().toString())
                        .setIconKey(page.getIconKey())
                        .setTitle(page.getName())
                        .setImagePosition(page.getImagePosition())
                        .setImageId(imageId)
                        .setAuthority(authority));
    }

    @GetMapping("/{pageId}/block")
    @Authenticated(true)
    public Response<BlockDto[]> getPageBlocks(@PathVariable("pageId") Long pageId, @Caller User caller) {
        try {
            roleService.getUserPageAuthority(caller.getId(), pageId).orElseThrow(ForbiddenException::new);
            List<Block> blocks = blockService.findAllByPageId(pageId);
            BlockDto[] responseBody = blocks.stream()
                    .map(block -> new BlockDto()
                            .setId(block.getId())
                            .setType(block.getBlockType())
                            .setContent(block.getContent())
                            .setIconKey(block.getIconKey())
                            .setWidth(block.getWidth())
                            .setFileId(
                                    block.getFile() != null
                                            ? block.getFile().getId().toString()
                                            : null))
                    .collect(Collectors.toList())
                    .toArray(new BlockDto[0]);
            return new Response<BlockDto[]>(HttpStatus.OK).setData(responseBody);
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
