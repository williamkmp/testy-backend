package com.mito.sectask.repositories;

import com.mito.sectask.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository
    extends JpaRepository<Block, String> {}
