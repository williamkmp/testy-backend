package com.mito.sectask.repository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.repositories.BlockRepository;
import com.mito.sectask.repositories.PageRepository;

@Slf4j
@SpringBootTest
class BlockRepositoryTest {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private PageRepository pageRepository;

    @Test
    @DisplayName("Testing findAllByPageId()")
    void getPageBlocks() throws Exception {

    	Long pageId = 1l;
    	Page testyPage = pageRepository
    			.findById(pageId)
    			.orElseThrow(() -> new Exception("Page with id " + pageId + " not found"));
    	List<Block> pageBlocks = blockRepository.findAllByPageId(testyPage.getId());

    	log.atInfo()
    			.setMessage("Testing block by Page[{}] '{}' count: {}")
    			.addArgument(testyPage.getId())
    			.addArgument(testyPage.getName())
    			.addArgument(pageBlocks.size())
    			.log();
    	;

    	for (int i = 0; i < pageBlocks.size(); i++) {
    		Block currentBlock = pageBlocks.get(i);

    		// Head block
    		if (i == 0) {
    			Assertions.assertThat(currentBlock.getPrev()).isNull();
    		}

    		// Middle block
    		if (0 < i && i < pageBlocks.size() - 1) {
    			Block previousBlock = pageBlocks.get(i - 1);
    			Block nextBlock = pageBlocks.get(i + 1);

    			Assertions.assertThat(currentBlock.getId())
    					.isNotBlank()
    					.isNotNull()
    					.isEqualTo(previousBlock.getNext().getId())
    					.isEqualTo(nextBlock.getPrev().getId());
    		}

    		// Tail block
    		if (i == 0) {
    			Assertions.assertThat(currentBlock.getPrev())
    					.as("Check first block have no previous pointer")
    					.isNull();
    		}
    	}
    }
}
