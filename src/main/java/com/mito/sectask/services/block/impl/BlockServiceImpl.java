package com.mito.sectask.services.block.impl;

import com.mito.sectask.entities.Block;
import com.mito.sectask.repositories.BlockRepository;
import com.mito.sectask.services.block.BlockService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;

    @Override
    public Optional<Block> findById(String blockId) {
        if (blockId == null) return Optional.empty();
        return blockRepository.findById(blockId);
    }
}
