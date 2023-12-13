package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.dto.response.page.PageData;
import com.mito.sectask.dto.response.page.PagePreview;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.User;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.values.MESSAGES;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;
    private final ImageService imageService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<PagePreview[]> getUserRootPages(@Caller User caller) {
        List<Page> userRootPages = pageService.getUserPages(caller.getId());
        PagePreview[] pagePreviews = userRootPages
            .stream()
            .map(page ->
                new PagePreview()
                    .setId(page.getId().toString())
                    .setTitle(page.getName())
            )
            .collect(Collectors.toList())
            .toArray(new PagePreview[0]);

        return new Response<PagePreview[]>(HttpStatus.OK).setData(pagePreviews);
    }

    @GetMapping(path = "/{pageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authenticated(true)
    public Response<PageData> getPageInformation(
        @PathVariable("pageId") Long pageId
    ) {
        Optional<Page> maybePage = pageService.getPageById(pageId);
        if (maybePage.isEmpty()) {
            return new Response<PageData>(HttpStatus.BAD_REQUEST)
                .setMessage(MESSAGES.ERROR_RESOURCE_NOT_FOUND);
        }
        Page page = maybePage.get();
        String imageSrc = (page.getImage() != null)
            ? imageService.getImageUrl(page.getImage().getId()).orElse(null)
            : null;

        return new Response<PageData>(HttpStatus.OK)
            .setData(
                new PageData()
                    .setId(page.getId().toString())
                    .setTitle(page.getName())
                    .setImagePosition(page.getImagePosition())
                    .setImageSrc(imageSrc)
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
                    .setId(page.getId().toString())
                    .setTitle(page.getName())
            )
            .collect(Collectors.toList())
            .toArray(new PagePreview[0]);

        return new Response<PagePreview[]>(HttpStatus.OK).setData(pagePreviews);
    }
}
