package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.annotations.caller.Caller;
import com.mito.sectask.annotations.callersession.CallerSession;
import com.mito.sectask.dto.dto.AuthorityMessageDto;
import com.mito.sectask.dto.dto.PreviewMessageDto;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.Authority;
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
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.values.ACTION;
import com.mito.sectask.values.DESTINATION;
import com.mito.sectask.values.KEY;
import com.mito.sectask.values.PREVIEW_ACTION;
import com.mito.sectask.values.USER_ROLE;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public Response<Object> addMemberToPage(
        @PathVariable("pageId") Long pageId,
        @RequestBody AuthorityMessageDto request,
        @Caller User caller,
        @CallerSession String sessionId
    ) {
        try {
            Role userRole = roleService.find(USER_ROLE.VIEWERS);
            roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            Page page = pageService
                .getRootOfPage(pageId)
                .orElseThrow(ResourceNotFoundException::new);
            Page rootPage = pageService
                .getRootOfPage(pageId)
                .orElseThrow(ResourceNotFoundException::new);
            User user = userService
                .findById(Long.valueOf(request.getId()))
                .orElseThrow(ResourceNotFoundException::new);

            roleService.save(
                new Authority()
                    .setIsPending(Boolean.FALSE)
                    .setPage(rootPage)
                    .setUser(user)
                    .setRole(userRole)
            );

            socket.convertAndSend(
                DESTINATION.userPreview(user.getId()),
                new PreviewMessageDto()
                    .setAction(PREVIEW_ACTION.ADD)
                    .setId(page.getId().toString())
                    .setName(page.getName())
                    .setIconKey(page.getIconKey())
                    .setParentId(
                        page.getCollection() != null
                            ? page.getCollection().getId()
                            : null
                    ),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, caller.getId().toString()),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );
            return new Response<>(HttpStatus.OK);
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

    @PutMapping("/page/{pageId}/member")
    @Authenticated(true)
    public Response<Object> updatePageMemberAuthority(
        @PathVariable("pageId") Long pageId,
        @RequestBody List<AuthorityMessageDto> requests,
        @Caller User caller,
        @CallerSession String sessionId
    ) {
        try {
            roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            Page page = pageService
                .findById(pageId)
                .orElseThrow(ResourceNotFoundException::new);
            String pageCollectionId = page.getCollection() != null
                ? page.getCollection().getId()
                : null;

            for (AuthorityMessageDto request : requests) {
                Long userId = Long.valueOf(request.getId());
                if (Objects.equals(request.getAction(), ACTION.UPDATE)) {
                    roleService.updateAuthority(
                        userId,
                        pageId,
                        request.getRole()
                    );
                } else if (Objects.equals(request.getAction(), ACTION.DELETE)) {
                    roleService.deleteAuthority(userId, pageId);
                    socket.convertAndSend(
                        DESTINATION.userPreview(userId),
                        new PreviewMessageDto()
                            .setAction(PREVIEW_ACTION.DELETE)
                            .setId(page.getId().toString())
                            .setName(page.getName())
                            .setIconKey(page.getIconKey())
                            .setParentId(pageCollectionId),
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
            return new Response<>(HttpStatus.OK);
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

    @DeleteMapping("/page/{pageId}/member")
    @Authenticated(true)
    public Response<Object> deletePageMember(
        @PathVariable("pageId") Long pageId,
        @Caller User caller,
        @CallerSession String sessionId
    ) {
        try {
            roleService
                .getUserPageAuthority(caller.getId(), pageId)
                .orElseThrow(ForbiddenException::new);
            Page page = pageService
                .findById(pageId)
                .orElseThrow(ResourceNotFoundException::new);
            String pageCollectionId = page.getCollection() != null
                ? page.getCollection().getId()
                : null;

            roleService.deleteAuthority(caller.getId(), pageId);
            socket.convertAndSend(
                DESTINATION.userPreview(caller.getId()),
                new PreviewMessageDto()
                    .setAction(PREVIEW_ACTION.DELETE)
                    .setId(page.getId().toString())
                    .setName(page.getName())
                    .setIconKey(page.getIconKey())
                    .setParentId(pageCollectionId),
                Map.ofEntries(
                    Map.entry(KEY.SENDER_USER_ID, caller.getId().toString()),
                    Map.entry(KEY.SENDER_SESSION_ID, sessionId)
                )
            );

            return new Response<>(HttpStatus.OK);
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
