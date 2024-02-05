package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.annotations.callersession.CallerSession;
import com.mito.sectask.dto.dto.ChatDto;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.Chat;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.exceptions.ForbiddenException;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.exceptions.exceptions.UserNotFoundException;
import com.mito.sectask.exceptions.httpexceptions.ForbiddenHttpException;
import com.mito.sectask.exceptions.httpexceptions.InternalServerErrorHttpException;
import com.mito.sectask.exceptions.httpexceptions.ResourceNotFoundHttpException;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.chat.ChatService;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.values.DESTINATION;
import com.mito.sectask.values.KEY;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/chat")
public class ChatController {

    private Integer PAGE_SIZE = 50;
    private final ChatService chatService;
    private final RoleService roleService;
    private final PageService pageService;
    private final SimpMessagingTemplate socket;

    @GetMapping("/page/{pageId}")
    @Authenticated(true)
    public Response<ChatDto[]> getChats(
        @Caller User caller,
        @PathVariable("pageId") Long pageId,
        @RequestParam(name = "page", defaultValue = "0") Integer pageNo
    ) {
        try {
            roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            List<Chat> chatRecord = chatService.findAllByPageId(pageId);
            List<ChatDto> chatList = chatRecord
                .stream()
                .map(chat ->
                    new ChatDto()
                        .setId(chat.getId().toString())
                        .setContent(chat.getContent())
                        .setSenderId(chat.getSender().getId().toString())
                        .setSentAt(chat.getSentAt())
                )
                .toList();
            ChatDto[] responseBody = chatList.toArray(new ChatDto[0]);
            return new Response<ChatDto[]>(HttpStatus.OK).setData(responseBody);
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

    @PostMapping("/page/{pageId}")
    @Authenticated(true)
    public Response<ChatDto> sendChat(
        @Caller User caller,
        @CallerSession String sessionId,
        @PathVariable("pageId") Long pageId,
        @RequestBody ChatDto request
    ) {
        try {
            roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);

            Page page = pageService
                .findById(pageId)
                .orElseThrow(ResourceNotFoundException::new);

            Chat newChat = chatService
                .save(
                    new Chat()
                        .setContent(request.getContent())
                        .setPage(page)
                        .setSender(caller)
                )
                .orElseThrow(Exception::new);

            socket.convertAndSend(
                DESTINATION.pageChat(pageId),
                new ChatDto()
                    .setId(newChat.getId().toString())
                    .setContent(newChat.getContent())
                    .setSenderId(newChat.getSender().getId().toString())
                    .setPageId(page.getId().toString())
                    .setSentAt(newChat.getSentAt()),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, caller.getId().toString()),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );

            return new Response<ChatDto>(HttpStatus.CREATED)
                .setData(
                    new ChatDto()
                        .setId(newChat.getId().toString())
                        .setContent(newChat.getContent())
                        .setSenderId(newChat.getSender().getId().toString())
                        .setPageId(page.getId().toString())
                        .setSentAt(newChat.getSentAt())
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
}
