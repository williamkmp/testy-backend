package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.repositories.BlockRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.seeder.Seeder;
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
    private final PageRepository pageRepository;

    @Override
    @Transactional
    public void seed() throws Exception {

        Page testyPage = pageRepository.findById(1L).orElseThrow(() -> new Exception("Page not found"));
        Page bimayPage = pageRepository.findById(2L).orElseThrow(() -> new Exception("Page not found"));
        Page bimobPage = pageRepository.findById(3L).orElseThrow(() -> new Exception("Page not found"));

        Block testyFinding = new Block()
                .setId(UUID.randomUUID().toString())
                .setIconKey("emoji-1265") // 📋
                .setBlockType(BLOCK_TYPE.COLLECTION)
                .setContent("Application Findings");

        Block bimayFinding = new Block()
                .setId(UUID.randomUUID().toString())
                .setIconKey("emoji-883") // 🌐
                .setBlockType(BLOCK_TYPE.COLLECTION)
                .setContent("Binusmaya Website Findings");

        Block bimobFinding = new Block()
                .setId(UUID.randomUUID().toString())
                .setIconKey("emoji-1170") // 📱
                .setBlockType(BLOCK_TYPE.COLLECTION)
                .setContent("Binus Mobile Findings");

        appendCollectionToPage(testyFinding, testyPage);
        appendCollectionToPage(bimayFinding, bimayPage);
        appendCollectionToPage(bimobFinding, bimobPage);
    }

    @Transactional
    private void appendCollectionToPage(Block block, Page page) {
        block.setPage(page);
        Block collection = blockRepository.saveAndFlush(block);
        List<Block> pageBlocks = blockRepository.findAllByPageId(page.getId());
        Block tailBlock = pageBlocks.get(pageBlocks.size() - 1);

        tailBlock.setNext(collection);
        collection.setPrev(tailBlock);

        blockRepository.saveAndFlush(tailBlock);
        blockRepository.saveAndFlush(collection);
    }
}
