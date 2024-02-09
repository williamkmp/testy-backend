package com.mito.sectask.services.role;

import com.mito.sectask.entities.Authority;
import com.mito.sectask.entities.Role;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.exceptions.exceptions.UserNotFoundException;
import com.mito.sectask.values.USER_ROLE;
import java.util.Optional;

public interface RoleService {
    /**
     * check if a user has access to a given page, if so return the USER_ROLE, else return
     * Optional.empty(), if the given page is a sub-page this method will search trough the root page
     * for user access.
     *
     * @param userId {@link Long} user id
     * @param pageId {@link Long} page id
     * @return {@link Optional}<{@link Role}> role of a user to a given page, else
     *     Optional.empty() if the user has no access
     * @throws UserNotFoundException if the used id not found
     * @throws ResourceNotFoundException if the page with given page id is not found
     */
    public Optional<Role> getUserPageAuthority(Long userId, Long pageId)
        throws UserNotFoundException, ResourceNotFoundException;

    /**
     * role by name
     *
     * @param role {@link USER_ROLE} role
     * @return {@link Role} role entity
     */
    public Role find(USER_ROLE role);

    /**
     * save an authority
     *
     * @param authority {@link Authority} authority
     * @return {@link Optional}<{@link Authority}> saved authority, else empty if opratio failed
     */
    public Optional<Authority> save(Authority authority);

    /**
     * get authority by user and page id, this method will traverse page parent relation
     * if the given page id is not root
     *
     * @param userId {@link Long} user id
     * @param pageId {@link Long} page id
     * @return
     */
    public Optional<Authority> findAuthority(Long userId, Long pageId);

    /**
     * update existing user authority, this method will traverse page parent relation
     * if the given page id is not root
     *
     * @param userId {@link Long} user id
     * @param pageId {@link Long} page id
     * @param role {@link USER_ROLE} role
     * @return
     */
    public Optional<Authority> updateAuthority(
        Long userId,
        Long pageId,
        USER_ROLE role
    );

    /**
     * delete an existing user authority, this method will traverse page parent relation
     * if the given page id is not root
     *
     * @param userId {@link Long} user id
     * @param pageId {@link Long} page id
     * @return
     */
    public void deleteAuthority(Long userId, Long pageId);
}
