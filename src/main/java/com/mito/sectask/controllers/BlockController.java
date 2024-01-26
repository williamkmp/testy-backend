package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.dto.MenuPreviewDto;
import com.mito.sectask.dto.request.block.BlockMessageDto;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.File;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.exceptions.ForbiddenException;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.exceptions.httpexceptions.ForbiddenHttpException;
import com.mito.sectask.exceptions.httpexceptions.InternalServerErrorHttpException;
import com.mito.sectask.exceptions.httpexceptions.ResourceNotFoundHttpException;
import com.mito.sectask.services.block.BlockService;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.utils.Util;
import com.mito.sectask.values.BLOCK_ACTION;
import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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
  private final ImageService imageService;

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
      List<MenuPreviewDto> previews = collection
        .getPages()
        .stream()
        .map(page ->
          new MenuPreviewDto()
            .setId(page.getId().toString())
            .setIconKey(page.getIconKey())
            .setTitle(page.getName())
        )
        .collect(Collectors.toList());
      MenuPreviewDto[] responseData = previews.toArray(new MenuPreviewDto[0]);
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

  @SendTo("/page/{pageId}/block")
  @MessageMapping("/page/{pageId}/block.transaction")
  public void applyBlockUpdate(@Payload BlockMessageDto request, @PathVariable("pageId") Long pageId) {
    if (request.getAction() == BLOCK_ACTION.ADD) {
      Block block = new Block();
      Long imageId = Util.String.toLong(request.getFileId()).orElse(null);
      Optional<Page> page = pageService.findById(pageId);

      File coverImagFile = imageService.findById(imageId).orElse(null);
      block
        .setBlockType(request.getType())
        .setPage(page.orElse(null))
        .setFile(coverImagFile)
        .setContent(request.getContent())
        .setIconKey(request.getIconKey())
        .setId(UUID.randomUUID().toString())
        .setWidth(request.getWidth());

      if (request.getPreviousId() != null) {
        Block previousBlock = blockService
          .findById(request.getPreviousId())
          .orElse(null);
        if (previousBlock != null) {
          block.setPrev(previousBlock);
          blockService.createBlock(block);
          previousBlock.setNext(block);
          blockService.updateBlock(previousBlock);
        }
      }
      if (request.getNextId() != null) {
        Block nextBlock = blockService
          .findById(request.getNextId())
          .orElse(null);
        if (nextBlock != null) {
          block.setNext(nextBlock);
          blockService.createBlock(block);
          nextBlock.setPrev(block);
          blockService.updateBlock(nextBlock);
        }
      }
    } else if (request.getAction() == BLOCK_ACTION.UPDATE) {
      Block block = blockService
        .findById(request.getBlockId())
        .orElseThrow(ResourceNotFoundException::new);
      Long imageId = Util.String.toLong(request.getFileId()).orElse(null);
      Optional<Page> page = pageService.findById(pageId);
      Block previousBlock = blockService
        .findById(request.getPreviousId())
        .orElse(null);
      Block nextBlock = blockService.findById(request.getNextId()).orElse(null);
      File coverImagFile = imageService.findById(imageId).orElse(null);
      block
        .setBlockType(request.getType())
        .setPage(page.orElse(null))
        .setFile(coverImagFile)
        .setContent(request.getContent())
        .setIconKey(request.getIconKey())
        .setPrev(previousBlock)
        .setNext(nextBlock)
        .setWidth(request.getWidth());
      blockService.updateBlock(block);
    } else if (request.getAction() == BLOCK_ACTION.DELETE) {
      Block block = blockService
        .findById(request.getBlockId())
        .orElseThrow(ResourceNotFoundException::new);
      Block previousBlock = block.getPrev();
      Block nextBlock = block.getNext();
      if (previousBlock != null) {
        previousBlock.setNext(nextBlock);
        blockService.updateBlock(previousBlock);
      }
      if (nextBlock != null) {
        nextBlock.setPrev(previousBlock);
        blockService.updateBlock(nextBlock);
      }
      blockService.deleteBlock(block);
    }
  }

  @MessageMapping("/page/collection")
  public void applyCollectionUpdate(@Payload BlockMessageDto request) {
    // TODO: imeplement add block method
    log.info("page block update" + request.toString());
  }
}
