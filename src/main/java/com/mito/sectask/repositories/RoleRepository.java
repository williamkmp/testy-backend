package com.mito.sectask.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mito.sectask.entities.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    
    /**
     * find roles by it's name
     * @param name role name, which is unique
     * @return optional role
     */
    public Optional<RoleEntity> findByName(String name);
}
