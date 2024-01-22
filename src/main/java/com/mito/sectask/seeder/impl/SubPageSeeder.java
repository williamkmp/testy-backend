package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.values.BLOCK_TYPE;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubPageSeeder implements Seeder {

    private final PageRepository pageRepository;

    @Override
    @Transactional
    public void seed() throws Exception {
        Page testyPage = pageRepository
            .findById(1L)
            .orElseThrow(() -> new Exception("Page not found"));
        Page bimayPage = pageRepository
            .findById(2L)
            .orElseThrow(() -> new Exception("Page not found"));
        Page bimobPage = pageRepository
            .findById(3L)
            .orElseThrow(() -> new Exception("Page not found"));

        Block testyCollection = getFirstCollectionByPageId(testyPage)
            .orElseThrow(() -> new Exception("Collection not found"));
        Block bimayCollection = getFirstCollectionByPageId(bimayPage)
            .orElseThrow(() -> new Exception("Collection not found"));
        Block bimobCollection = getFirstCollectionByPageId(bimobPage)
            .orElseThrow(() -> new Exception("Collection not found"));

        pageRepository.save(
            new Page()
                .setCollection(testyCollection)
                .setIconKey("emoji-1215") // 📃
                .setName("Documentation")
        );

        pageRepository.save(
            new Page()
                .setCollection(testyCollection)
                .setIconKey("emoji-1265") // 📋
                .setName("Notes")
        );

        pageRepository.save(
            new Page()
                .setCollection(testyCollection)
                .setIconKey("emoji-1001") // 🛳️
                .setName("On Boarding")
        );

        pageRepository.save(
            new Page()
                .setCollection(bimobCollection)
                .setIconKey("emoji-1085") // 🌈
                .setName("Mobile UI/UX")
        );

        pageRepository.save(
            new Page()
                .setCollection(bimobCollection)
                .setIconKey("emoji-1072") // ☁️
                .setName("System Design")
        );

        pageRepository.save(
            new Page()
                .setCollection(bimobCollection)
                .setIconKey("emoji-883") // 🌐
                .setName("API Specification")
        );

        pageRepository.save(
            new Page()
                .setCollection(bimayCollection)
                .setIconKey("emoji-875") // 🎨
                .setName("Client Documentation")
        );

        pageRepository.save(
            new Page()
                .setCollection(bimayCollection)
                .setIconKey("emoji-1214") // 📚
                .setName("Project Management")
        );

        pageRepository.save(
            new Page()
                .setCollection(bimayCollection)
                .setIconKey("emoji-1297") // ⚙️
                .setName("Server Documentation")
        );
    }

    @Transactional
    private Optional<Block> getFirstCollectionByPageId(Page page) {
        final List<Block> blocks = page.getBlocks();
        return blocks
            .stream()
            .filter(block -> block.getBlockType().equals(BLOCK_TYPE.COLLECTION))
            .findFirst();
    }
}
