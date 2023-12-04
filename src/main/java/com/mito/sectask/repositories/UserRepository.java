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
    
    Optional<User> findByTagName(String tagName);

    @Query("FROM UserEntity AS u WHERE u.email IN :emails")
    List<User> findByEmailList(@Param("emails") List<String> emails);
}
