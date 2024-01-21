package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.dto.MenuPreviewDto;
import com.mito.sectask.dto.request.block.BlockMessageDto;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.exceptions.ForbiddenException;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.exceptions.httpexceptions.ForbiddenHttpException;
import com.mito.sectask.exceptions.httpexceptions.InternalServerErrorHttpException;
import com.mito.sectask.exceptions.httpexceptions.ResourceNotFoundHttpException;
import com.mito.sectask.services.block.BlockService;
import com.mito.sectask.services.role.RoleService;
import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final SimpMessagingTemplate socket;

    @GetMapping("/collection/{collectionId}/page/preview")
    @Authenticated(true)
    public Response<MenuPreviewDto[]> getPagePreviewByCollection(
            @PathVariable("collectionId") String collectionId, @Caller User caller) {
        try {
            Block collection = blockService.findById(collectionId).orElseThrow(ResourceNotFoundException::new);
            Page parentPage = collection.getPage();
            roleService.getUserPageAuthority(caller.getId(), parentPage.getId()).orElseThrow(ForbiddenException::new);
            List<MenuPreviewDto> previews = collection.getPages().stream()
                    .map(page -> new MenuPreviewDto()
                            .setId(page.getId().toString())
                            .setIconKey(page.getIconKey())
                            .setTitle(page.getName()))
                    .collect(Collectors.toList());
            MenuPreviewDto[] responseData = previews.toArray(new MenuPreviewDto[0]);
            return new Response<MenuPreviewDto[]>(HttpStatus.OK).setData(responseData);
        } catch (ForbiddenException e) {
            throw new ForbiddenHttpException();
        } catch (ResolutionException e) {
            throw new ResourceNotFoundHttpException();
        } catch (Exception e) {
            throw new InternalServerErrorHttpException();
        }
    }

    @MessageMapping("/page/block")
    public void applyBlockUpdate(@Payload BlockMessageDto request) {
        // TODO: imeplement add block method
        log.info("page block update" + request.toString());
    }

    @MessageMapping("/page/collection")
    public void applyCollectionUpdate(@Payload BlockMessageDto request) {
        // TODO: imeplement add block method
        log.info("page block update" + request.toString());
    }
}
