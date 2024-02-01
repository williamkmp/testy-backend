package com.mito.sectask.services.block;

import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.repositories.BlockRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.values.BLOCK_TYPE;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
    public Optional<Block> save(Block block) {
        if (block == null) return Optional.empty();
        return Optional.of(blockRepository.save(block));
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

    @Override
    @Transactional
    public Block moveBlock(String blockId, String newPrevId, String newNextId) {
        Block targetBlock = blockRepository
            .findById(blockId)
            .orElseThrow(ResourceNotFoundException::new);
        Block oldPrevBlock = targetBlock.getPrev();
        Block oldNextBlock = targetBlock.getNext();
        Block newPrevBlock = newPrevId != null
            ? blockRepository.findById(newPrevId).orElse(null)
            : null;
        Block newNextBlock = newNextId != null
            ? blockRepository.findById(newNextId).orElse(null)
            : null;

        try {
            // Lifting targetBlock from it's old position
            targetBlock.setNext(null);
            targetBlock.setPrev(null);
            targetBlock = blockRepository.saveAndFlush(targetBlock);
            if (oldPrevBlock != null) {
                oldPrevBlock.setNext(
                    oldNextBlock != null ? oldNextBlock : null
                );
                blockRepository.saveAndFlush(oldPrevBlock);
            }
            if (oldNextBlock != null) {
                oldNextBlock.setPrev(
                    oldPrevBlock != null ? oldPrevBlock : null
                );
                blockRepository.saveAndFlush(oldNextBlock);
            }

            // Detach newPrevBlock and newNextBlock
            if (newPrevBlock != null) {
                newPrevBlock.setNext(null);
                newPrevBlock = blockRepository.saveAndFlush(newPrevBlock);
            }
            if (newNextBlock != null) {
                newNextBlock.setPrev(null);
                newNextBlock = blockRepository.saveAndFlush(newNextBlock);
            }

            // Insert targetBlock to it's new position
            if (newPrevBlock != null) {
                targetBlock.setPrev(newPrevBlock);
                newPrevBlock.setNext(targetBlock);
                blockRepository.saveAndFlush(newPrevBlock);
            }
            if (newNextBlock != null) {
                targetBlock.setNext(newNextBlock);
                newNextBlock.setPrev(targetBlock);
                blockRepository.saveAndFlush(newNextBlock);
            }

            return blockRepository.saveAndFlush(targetBlock);
        } catch (Exception e) {
            log.info("Error moving block: {}", blockId);
            e.printStackTrace();
            throw new ResourceNotFoundException();
        }
    }

    @Override
    @Transactional
    public Optional<Block> insertBlockAfter(String prevId, Block newBlock) {
        Block prevBlock = prevId != null
            ? blockRepository.findById(prevId).orElse(null)
            : null;
        Block nextBlock = prevBlock != null ? prevBlock.getNext() : null;
        newBlock = blockRepository.saveAndFlush(newBlock);
        try {
            if (prevBlock != null) {
                prevBlock.setNext(newBlock);
                prevBlock = blockRepository.saveAndFlush(prevBlock);
            }
            if (nextBlock != null) {
                nextBlock.setPrev(newBlock);
                nextBlock = blockRepository.saveAndFlush(nextBlock);
            }
            newBlock.setPrev(prevBlock);
            newBlock.setNext(nextBlock);
            newBlock = blockRepository.saveAndFlush(newBlock);
            log.info("Insert block: {}", newBlock.getId());
            return Optional.of(newBlock);
        } catch (Exception e) {
            log.error("Error inserting block: {}", newBlock.getId());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<Block> deleteBlock(String blockId) {
        try {
            Block targetBlock = blockRepository.findById(blockId).orElseThrow(ResourceNotFoundException::new);
            Block prevBlock = targetBlock.getPrev();
            Block nextBlock = targetBlock.getNext();

            targetBlock.setPrev(null);
            targetBlock.setNext(null);
            blockRepository.saveAndFlush(targetBlock);

            if(prevBlock != null) {
                prevBlock.setNext(nextBlock);
                blockRepository.saveAndFlush(prevBlock); 
            }
            if(nextBlock != null) {
                nextBlock.setPrev(prevBlock);
                blockRepository.saveAndFlush(nextBlock);
            }
            blockRepository.delete(targetBlock);
            return Optional.of(targetBlock);
        } catch (Exception e) {
            log.error("Error deleting block: {}", blockId);
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
