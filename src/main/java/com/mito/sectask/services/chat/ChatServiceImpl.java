package com.mito.sectask.services.chat;

import com.mito.sectask.entities.Chat;
import com.mito.sectask.repositories.ChatRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public List<Chat> findAllByPageId(Long pageId, Pageable paging) {
        return chatRepository.findAllByPageIdOrderBySentAtDesc(pageId, paging);
    }

    @Override
    public Optional<Chat> save(Chat newChat) {
        try {
            return Optional.of(chatRepository.saveAndFlush(newChat));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
