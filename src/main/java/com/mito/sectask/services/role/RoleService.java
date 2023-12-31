package com.mito.sectask.services.role;

import java.util.Optional;
import com.mito.sectask.entities.Role;
import com.mito.sectask.values.USER_ROLE;

public interface RoleService {

    /**
     * get role by name, never null if role not found
     * throws an Exception
     * 
     * @param   role {@link USER_ROLE}
     *          get role instance 
     * 
     * @return  {@link Role}, never <code>null</code>
     */
    public Role getRole(USER_ROLE role);

    /**
     * check if a user has access to a given page, if so 
     * return the USER_ROLE, else return Optional.empty(),
     * if the given page is a sub-page this method will
     * search trough the parent relation for user access.
     * 
     * @param   userId  {@link Long}
     *          user id
     * 
     * @param   pageId {@link Long}
     *          page id
     * 
     * @return  {@link Optional}<{@link USER_ROLE}>
     *          role of a user to a given page, else 
     *          Optioan.empty() if the user has no access
     */
    public Optional<USER_ROLE> getPageAuthorityOfUser(Long userId, Long pageId);
}
