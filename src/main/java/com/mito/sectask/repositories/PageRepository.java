package com.mito.sectask.repositories;

import com.mito.sectask.entities.Page;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    @Query(
        """ 
            SELECT 
                p
            FROM 
                Page p 
                JOIN p.authorities a 
                JOIN a.user u
            WHERE
                u.id = :userId
                AND a.isPending = false
        """
    )
    public List<Page> getUserPages(@Param("userId") Long userId);
}
