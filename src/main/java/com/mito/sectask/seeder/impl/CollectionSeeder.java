package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.repositories.BlockRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.values.BLOCK_TYPE;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CollectionSeeder implements Seeder {

    private final BlockRepository blockRepository;
    private final PageService pageService;

    @Override
    @Transactional
    public void seed() throws Exception {
        final Long pageId = 1l;
        Page page = pageService
                .findById(pageId)
                .orElseThrow(() -> new Exception("Page wiith id [" + pageId + "] not found"));

        Block collection = new Block()
                .setId(UUID.randomUUID().toString())
                .setIconKey("emoji-1265") // 📋
                .setBlockType(BLOCK_TYPE.COLLECTION)
                .setContent("<p>Error Findings</p>");

        appendCollectionToPage(collection, page);
    }

    private void appendCollectionToPage(Block block, Page page) {
        Block collection = blockRepository.save(block);
        List<Block> pageBlocks = blockRepository.findAllByPageId(page.getId());
        Block tailBlock = pageBlocks.get(pageBlocks.size() - 1);

        tailBlock.setNext(collection);
        collection.setPrev(tailBlock);

        blockRepository.save(tailBlock);
        blockRepository.save(collection);
    }
}
