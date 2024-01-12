package com.mito.sectask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mito.sectask.entities.Block;

@Repository
public interface BlockRepository extends JpaRepository<Block, String> {}
