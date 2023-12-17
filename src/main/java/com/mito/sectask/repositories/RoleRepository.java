package com.mito.sectask.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mito.sectask.entities.Role;
import com.mito.sectask.values.USER_ROLE;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * find roles by it's name
     *
     * @param   name {@link USER_ROLE}
     *          role name, which is unique
     *
     * @return  optional role
     */
    public Optional<Role> findByName(USER_ROLE name);

    @Query(
        nativeQuery = true, 
        value = """
            WITH RECURSIVE cte AS (
                SELECT 
                      pages.*, 
                      0 as level
              FROM
                      pages
                WHERE 
                      id = :pageId
              UNION
              SELECT
                      pages.*, 
                      level+1 as level
              FROM
                      cte JOIN pages ON cte.parent_id = pages.id
            )
            SELECT
            r.*
            FROM 
                cte AS page 
                JOIN authorities AS a ON page.id = a.page_id
                JOIN users AS u ON u.id = a.user_id
                JOIN roles AS r ON r.id = a.role_id
            WHERE
                u.id = :userId
            ORDER BY
                page.level DESC   
            LIMIT 1      
         """
    )
    public Optional<Role> getPageAuthority(
        @Param("pageId") Long pageId,
        @Param("userId") Long userId
    );
}
