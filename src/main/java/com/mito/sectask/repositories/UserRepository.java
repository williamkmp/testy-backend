package com.mito.sectask.repositories;

import com.mito.sectask.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * get user by email
     * @param email user's email
     * @return maybeUser
     */
    Optional<UserEntity> findByEmail(String email);
}
