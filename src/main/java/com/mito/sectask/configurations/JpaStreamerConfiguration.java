package com.mito.sectask.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.persistence.EntityManagerFactory;

@Configuration
public class JpaStreamerConfiguration {
    
    @Bean
    JPAStreamer jpaStreamer(EntityManagerFactory em){
        return JPAStreamer.createJPAStreamerBuilder(em).build();
    }
}
