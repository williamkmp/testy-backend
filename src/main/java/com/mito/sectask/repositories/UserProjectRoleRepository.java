package com.mito.sectask.repositories;

import com.mito.sectask.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProjectRoleRepository
    extends JpaRepository<Authority, Long> {}
