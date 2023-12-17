package com.mito.sectask.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.request.page.PageCreateRequest;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.dto.response.page.PageData;
import com.mito.sectask.dto.response.page.PagePreview;
import com.mito.sectask.entities.File;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.ForbiddenHttpException;
import com.mito.sectask.exceptions.httpexceptions.InternalServerErrorHttpException;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.utils.Util;
import com.mito.sectask.values.MESSAGES;
import com.mito.sectask.values.USER_ROLE;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/page")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;
    private final ImageService imageService;
    private final RoleService roleService;

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public Response<PageData> createPage(
        @RequestBody PageCreateRequest request,
        @Caller User caller
    ) {
        // Get parent and Image data
        Long parentId = Util.String.toLong(request.getParentId()).orElse(null);
        Long imageId = Util.String.toLong(request.getImageId()).orElse(null);
        Page parentPage = pageService.getPageById(parentId).orElse(null);
        File coverImagFile = imageService.findById(imageId).orElse(null);

        Page newPage = new Page()
            .setIconKey(request.getIconKey())
            .setName(request.getTitle())
            .setImage(coverImagFile)
            .setImagePosition(request.getImagePosition())
            .setParent(parentPage);

        Page createdPage = (parentPage == null)
            ? pageService
                .createRootPage(newPage, caller.getId(), request.getMembers())
                .orElseThrow(InternalServerErrorHttpException::new)
            : pageService
                .save(newPage)
                .orElseThrow(InternalServerErrorHttpException::new);

        return new Response<PageData>(HttpStatus.CREATED)
            .setData(
                new PageData()
                    .setTitle(createdPage.getName())
                    .setId(createdPage.getId().toString())
                    .setIconKey(createdPage.getIconKey())
                    .setAuthority(USER_ROLE.FULL_ACCESS)
                    .setImageId(Util.String.valueOf(imageId).orElse(null))
                    .setImagePosition(createdPage.getImagePosition())
            );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<PagePreview[]> getUserRootPages(@Caller User caller) {
        List<Page> userRootPages = pageService.getUserPages(caller.getId());
        PagePreview[] pagePreviews = userRootPages
            .stream()
            .map(page ->
                new PagePreview()
                    .setId(page.getId().toString())
                    .setIconKey(page.getIconKey())
                    .setTitle(page.getName())
            )
            .collect(Collectors.toList())
            .toArray(new PagePreview[0]);

        return new Response<PagePreview[]>(HttpStatus.OK).setData(pagePreviews);
    }

    @GetMapping(path = "/{pageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<PageData> getPageInformation(
        @PathVariable("pageId") Long pageId,
        @Caller User caller
    ) {
        Optional<USER_ROLE> authority = roleService.getPageAuthorityOfUser(
            caller.getId(), 
            pageId
        );
        if(authority.isEmpty()) {
            throw new ForbiddenHttpException();
        }

        Optional<Page> maybePage = pageService.getPageById(pageId);
        if (maybePage.isEmpty()) {
            return new Response<PageData>(HttpStatus.BAD_REQUEST)
                .setMessage(MESSAGES.ERROR_RESOURCE_NOT_FOUND);
        }
        Page page = maybePage.get();
        String imageId = (page.getImage() != null)
            ? page.getImage().getId().toString()
            : null;

        return new Response<PageData>(HttpStatus.OK)
            .setData(
                new PageData()
                    .setId(page.getId().toString())
                    .setIconKey(page.getIconKey())
                    .setTitle(page.getName())
                    .setImagePosition(page.getImagePosition())
                    .setImageId(imageId)
                    .setAuthority(authority.get())
            );
    }

    @GetMapping(
        path = "/{pageId}/children",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public Response<PagePreview[]> getPageChildrens(
        @PathVariable("pageId") Long pageId
    ) {
        List<Page> pageChildrens = pageService.getChildren(pageId);
        PagePreview[] pagePreviews = pageChildrens
            .stream()
            .map(page ->
                new PagePreview()
                    .setIconKey(page.getIconKey())
                    .setId(page.getId().toString())
                    .setTitle(page.getName())
            )
            .collect(Collectors.toList())
            .toArray(new PagePreview[0]);

        return new Response<PagePreview[]>(HttpStatus.OK).setData(pagePreviews);
    }
}
