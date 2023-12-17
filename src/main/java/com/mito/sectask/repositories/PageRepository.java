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
        nativeQuery = true,
        value = """
            SELECT 
                p.* 
            FROM 
                pages AS p 
                JOIN authorities AS a ON p.id = a.page_id
                JOIN users AS u ON a.user_id = u.id
            WHERE
                u.id = :userId
                AND a.is_pending = false
            """
    )
    public List<Page> getUserPages(@Param("userId") Long userId);
}
