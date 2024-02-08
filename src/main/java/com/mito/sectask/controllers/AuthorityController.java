package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.annotations.callersession.CallerSession;
import com.mito.sectask.dto.dto.AuthorityMessageDto;
import com.mito.sectask.entities.User;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthorityController {

    private final UserService userService;
    private final PageService pageService;
    private final RoleService roleService;
    private final SimpMessagingTemplate socket;

    @PostMapping("/page/{pageId}/member")
    @Authenticated(true)
    public void addMemberToPage(
        @PathVariable("pageId") Long pageId,
        @RequestBody AuthorityMessageDto request,
        @Caller User caller,
        @CallerSession String sessionId
    ) {
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @PutMapping("/page/{pageId}/member")
    @Authenticated(true)
    public void updatePageMemberAuthority(
        @PathVariable("pageId") Long pageId,
        @RequestBody AuthorityMessageDto request,
        @Caller User caller,
        @CallerSession String sessionId
    ) {
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @DeleteMapping("/page/{pageId}/member")
    @Authenticated(true)
    public void deletePageMember(
        @PathVariable("pageId") Long pageId,
        @RequestBody AuthorityMessageDto request,
        @Caller User caller,
        @CallerSession String sessionId
    ) {
        try {
            // TODO: implement
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
