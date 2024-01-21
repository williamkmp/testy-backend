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

    /**
     * find all user registered as member of a given page,
     * the page must be a root page.
     *
     * @param pageId {@link Long} page id
     * @return {@link List}<{@link User}> root page members
     */
    @Query("SELECT u FROM User u JOIN u.authorities a JOIN a.page p WHERE p.id = :pageId")
    List<User> findAllByRootPageId(@Param("pageId") Long pageId);
}
