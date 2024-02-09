package com.mito.sectask.repositories;

import com.mito.sectask.entities.Authority;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Query(
        """
        SELECT a
        FROM Authority a JOIN a.user u JOIN a.page p
        WHERE u.id = :userId AND p.id = :pageId
        """
    )
    public Optional<Authority> findByUserAndPage(
        @Param("userId") Long userId,
        @Param("pageId") Long pageId
    );
}
