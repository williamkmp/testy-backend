package com.mito.sectask.repositories;

import com.mito.sectask.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email IN :emails")
    List<User> findByEmails(@Param("emails") List<String> emails);

    Optional<User> findByTagName(String tagName);
}
