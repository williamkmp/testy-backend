package com.mito.sectask.repositories;

import com.mito.sectask.entities.Chat;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    public List<Chat> findAllByPageIdOrderBySentAtDesc(
        Long pageId,
        Pageable pageable
    );
}
