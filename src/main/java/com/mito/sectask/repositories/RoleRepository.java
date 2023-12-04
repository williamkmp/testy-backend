package com.mito.sectask.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
