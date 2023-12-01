package com.mito.sectask.repositories;

import com.mito.sectask.entities.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByEmail(String email);    
    
    Optional<UserEntity> findByTagName(String tagName);

    @Query("FROM UserEntity AS u WHERE u.email IN :emails")
    List<UserEntity> findByEmailList(@Param("emails") List<String> emails);
}
