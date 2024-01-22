package com.mito.sectask.annotations.caller;

import com.mito.sectask.dto.dto.JwtPayload;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.httpexceptions.UnauthorizedHttpException;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.values.KEY;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CallerResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (
            parameter.hasParameterAnnotation(Caller.class) &&
            User.class.isAssignableFrom(parameter.getParameterType())
        );
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws Exception {
        Object payload = webRequest.getAttribute(
            KEY.REQUEST_CALLER_INFO,
            RequestAttributes.SCOPE_REQUEST
        );

        if (!(payload instanceof JwtPayload)) {
            throw new UnauthorizedHttpException();
        }

        JwtPayload callerInfo = (JwtPayload) payload;
        Optional<User> callerData = userService.findById(callerInfo.getId());

        if (callerData.isEmpty()) {
            throw new UnauthorizedHttpException();
        }

        return callerData.get();
    }
}
