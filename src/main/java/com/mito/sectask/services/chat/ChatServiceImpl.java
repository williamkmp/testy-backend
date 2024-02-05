package com.mito.sectask.services.chat;

import com.mito.sectask.entities.Chat;
import com.mito.sectask.repositories.ChatRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public List<Chat> findAllByPageId(Long pageId, Pageable paging) {
        return chatRepository.findAllByPageIdOrderBySentAtDesc(pageId, paging);
    }

    @Override
    public List<Chat> findAllByPageId(Long pageId) {
        return chatRepository.findAllByPageIdOrderBySentAtDesc(pageId);
    }

    @Override
    @Transactional
    public Optional<Chat> save(Chat newChat) {
        try {
            Chat savedChat = chatRepository.save(newChat);
            return Optional.of(savedChat);
        } catch (Exception e) {
            log.error("Error saving chat");
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
