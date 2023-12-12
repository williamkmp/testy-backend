package com.mito.sectask.services.role;

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
}
