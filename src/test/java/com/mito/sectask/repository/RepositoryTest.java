package com.mito.sectask.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    private EntityManagerFactory connectionFactory;

    @Test
    @DisplayName("run playground")
    void runTest() {
        EntityManager database = connectionFactory.createEntityManager();
        Query query = database.createNativeQuery("SELECT * FROM users");
    }
}
