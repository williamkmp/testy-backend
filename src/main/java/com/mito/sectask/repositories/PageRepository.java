package com.mito.sectask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mito.sectask.entities.Page;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {}
