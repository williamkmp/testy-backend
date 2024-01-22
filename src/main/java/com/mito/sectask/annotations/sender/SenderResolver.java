package com.mito.sectask.annotations.sender;

import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.values.KEY;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SenderResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (
            parameter.hasParameterAnnotation(Sender.class) &&
            User.class.isAssignableFrom(parameter.getParameterType())
        );
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        Message<?> message
    ) throws Exception {
        SimpMessageHeaderAccessor header = SimpMessageHeaderAccessor.wrap(
            message
        );
        Long senderId = Long.valueOf(
            header.getFirstNativeHeader(KEY.SENDER_HEADER_KEY)
        );
        return userRepository.findById(senderId).orElse(null);
    }
}
