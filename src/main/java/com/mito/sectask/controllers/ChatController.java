package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.dto.dto.ChatDto;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.User;
import com.mito.sectask.services.chat.ChatService;
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

    private final ChatService chatService;
    private final SimpMessagingTemplate socket;

    @GetMapping("/page/{pageId}")
    @Authenticated(true)
    public Response<Object> getChats(
        @Caller User user,
        @PathVariable("pageId") Long pageId,
        @RequestParam(name = "page", defaultValue = "0") Long pageNo
    ) {
        // TODO: implement get all chat route
        return new Response<>(HttpStatus.OK);
    }

    @PostMapping("/page/{pageId}")
    @Authenticated(true)
    public Response<Object> sendChat(
        @Caller User user,
        @PathVariable("pageId") Long pageId,
        @RequestBody ChatDto request
    ) {
        // TODO: implement get all chat route
        return new Response<>(HttpStatus.OK);
    }
}
