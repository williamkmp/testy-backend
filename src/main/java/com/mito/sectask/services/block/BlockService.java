package com.mito.sectask.services.block;

import com.mito.sectask.entities.Block;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

public interface BlockService {
    /**
     * get a block with a certaion id
     *
     * @param blockId {@link String} block uuid
     * @return {@link Optional}<{@link Block}> Block entity, else Option.empty()
     */
    public Optional<Block> findById(String blockId);

    /**
     * save a block entity state
     *
     * @param blockId {@link String} block uuid
     * @return {@link Optional}<{@link Block}> updated block entity
     */
    public Optional<Block> save(Block block);

    /**
     * get list of COLLECTION typed block from a given page
     *
     * @param pageId {@link Long} page id
     * @return {@link List}<{@link Block}> list of block with type of COLEECTION
     * @throws ResourceNotFoundException when the given page not exist
     */
    public List<Block> findAllCollectionByPageId(Long pageId)
        throws ResourceNotFoundException;

    /**
     * get ordered list of all block of a given page
     *
     * @param pageId {@link Long} page id
     * @return {@link List}<{@link Block}> ordered blocks of page
     * @throws ResourceNotFoundException if page is not found
     */
    public List<Block> findAllByPageId(Long pageId)
        throws ResourceNotFoundException;

    /**
     * move and update target block to an ew postion in the linked list
     *
     * @param blockId {@link String} target block uuid
     * @param newPrevId {@link String} new previous block uuid
     * @param newNextId {@link String} new next block uuid
     * @return {@link Block} updated target block data
     */
    public Block moveBlock(String blockId, String newPrevId, String newNextId);

    /**
     * insert new block after a certain existing block
     *
     * @param prevId {@link String} previous block id, set as null if insert head
     * @param newBlock {@link Block} new block
     * @return {@link Optional}<{@link Block}> inserted block, else empty if insert operation failed
     */
    public Optional<Block> insertBlockAfter(String prevId, Block newBlock);
}
