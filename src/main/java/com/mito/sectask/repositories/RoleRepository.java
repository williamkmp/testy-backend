package com.mito.sectask.repositories;

import com.mito.sectask.entities.Role;
import com.mito.sectask.values.USER_ROLE;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * find roles by it's name
     *
     * @param name {@link USER_ROLE} role name, which is unique
     * @return optional role
     */
    public Role findByName(USER_ROLE name);

    /**
     * get User role for a certain
     *
     * @param rootPageId {@link Long} must be a root page id
     * @param userId {@link Long} user id
     * @return {@link Optional}<{@link Role}> conatining user role for a certain page, else Optional.empty()
     */
    @Query(
        """
        SELECT
            r
        FROM
            Role r
            JOIN FETCH r.authorities a
            JOIN FETCH a.page p
            JOIN FETCH a.user u
        WHERE
            p.id = :rootPageId
            AND u.id = :userId
        """
    )
    public Optional<Role> findByRootPageId(
        @Param("rootPageId") long rootPageId,
        @Param("userId") long userId
    );
}
