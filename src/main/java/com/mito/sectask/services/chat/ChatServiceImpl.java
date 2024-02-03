package com.mito.sectask.services.chat;

import com.mito.sectask.repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    //TODO: implement chatserviceimpl
}
