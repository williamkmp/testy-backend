package com.mito.sectask.services.block;

import java.util.Optional;
import com.mito.sectask.entities.Block;

public interface BlockService {
    
    /**
     * get a bloc by a certaion id
     * @param blockId
     * @return
     */
    public Optional<Block> findById(String blockId);
}
