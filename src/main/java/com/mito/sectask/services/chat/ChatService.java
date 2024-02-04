package com.mito.sectask.services.chat;

import com.mito.sectask.entities.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ChatService {
    /**
     * get all page chat record sorted from newst to oldest
     *
     * @param pageId {@link Long} page id
     * @param paging {@link Pageable} paging
     * @return {@link List}<{@link Chat}> page chat record
     */
    public List<Chat> findAllByPageId(Long pageId, Pageable paging);

    /**
     * save a new chat erecord to the database
     *
     * @param newChat {@link Chat} new chat
     * @return {@link Optional}<{@link Chat}> new chat, else emty
     */
    public Optional<Chat> save(Chat newChat);
}
