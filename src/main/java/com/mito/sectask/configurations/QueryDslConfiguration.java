package com.mito.sectask.configurations;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfiguration {

    @Bean
    public JPAQueryFactory jpaQueryFactory(
        EntityManagerFactory entityManagerFactory
    ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }
}
