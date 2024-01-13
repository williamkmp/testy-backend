package com.mito.sectask.services.role;

import com.mito.sectask.entities.Role;
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
     */
    public Optional<Role> getUserPageAuthority(Long userId, Long pageId);
}
