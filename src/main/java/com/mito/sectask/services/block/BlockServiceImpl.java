package com.mito.sectask.services.block;

import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.repositories.BlockRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.values.BLOCK_TYPE;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;
    private final PageRepository pageRepository;

    @Override
    public Optional<Block> findById(String blockId) {
        if (blockId == null) return Optional.empty();
        return blockRepository.findById(blockId);
    }

    @Override
    public List<Block> findAllCollectionByPageId(Long pageId) {
        Page page = pageRepository
            .findById(pageId)
            .orElseThrow(ResourceNotFoundException::new);
        List<Block> blocks = page.getBlocks();
        return blocks
            .stream()
            .filter(block -> block.getBlockType().equals(BLOCK_TYPE.COLLECTION))
            .collect(Collectors.toList());
    }

    @Override
    public List<Block> findAllByPageId(Long pageId)
        throws ResourceNotFoundException {
        Page page = pageRepository
            .findById(pageId)
            .orElseThrow(ResourceNotFoundException::new);
        return blockRepository.findAllByPageId(page.getId());
    }
}
