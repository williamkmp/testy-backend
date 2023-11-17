package com.mito.sectask.interceptors;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.dto.dto.JwtPayload;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.auth.AuthService;
import com.mito.sectask.values.KEY;
import com.mito.sectask.values.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        if (!isHandlerProtected((HandlerMethod) handler)) {
            return true;
        }

        Optional<String> maybeToken = extractAccessToken(request);
        if (maybeToken.isEmpty()) {
            throw new UnauthorizedHttpException();
        }
        String accessToken = maybeToken.get();

        Optional<JwtPayload> maybePayload = authService.verifyAccessToken(
            accessToken
        );

        if (maybePayload.isEmpty()) {
            throw new UnauthorizedHttpException(Message.ERROR_INVALID_TOKEN);
        }

        JwtPayload caller = maybePayload.get();
        request.setAttribute(KEY.REQUEST_CALLER_INFO, caller);
        return true;
    }

    /**
     * extract access token from an incomig request
     *
     * @param   request {@link HttpsServletRequest}
     *          the incoming request object with Authorization
     *          header containing bearer token
     *
     * @return  {@link Optional}<{@link String}>
     *          containing the access token, else
     *          Optional.empty() if no beare token
     *          detected
     */
    private Optional<String> extractAccessToken(HttpServletRequest request) {
        final String BEARER = "Bearer ";
        String authorizationHeader = request.getHeader("Authorization");
        if (
            authorizationHeader == null ||
            !authorizationHeader.startsWith(BEARER)
        ) {
            return Optional.empty();
        }
        String accessToken = authorizationHeader.substring(BEARER.length());
        return Optional.of(accessToken);
    }

    /**
     * check for the presence of Authenticated annotation
     * for a certaion handler. Returns the value of
     * the Authenticated value.
     *
     * @param   handler {@link MethodHandler}
     *          the route handler
     *
     * @return  true if the handler need
     *          authentication, else false
     */
    private boolean isHandlerProtected(HandlerMethod handler) {
        boolean isNeedAuth = false;
        boolean classIsAnnotated = handler
            .getBeanType()
            .isAnnotationPresent(Authenticated.class);
        boolean methodIsAnnotated = handler
            .getMethod()
            .isAnnotationPresent(Authenticated.class);

        if (classIsAnnotated) {
            isNeedAuth =
                handler
                    .getBeanType()
                    .getAnnotation(Authenticated.class)
                    .value();
        }

        if (methodIsAnnotated) {
            isNeedAuth =
                handler.getMethod().getAnnotation(Authenticated.class).value();
        }

        return isNeedAuth;
    }
}
