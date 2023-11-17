package com.mito.sectask.configurations;

import com.mito.sectask.annotations.caller.CallerResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResoverConfiguration implements WebMvcConfigurer {

    @Autowired
    private CallerResolver callerResolver;

    @Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> resolvers
    ) {
        resolvers.add(callerResolver);
    }
}
