package com.mito.sectask.services.block;

import java.util.Optional;
import com.mito.sectask.entities.Block;

public interface BlockService {
    
    /**
     * get a block with a certaion id
     * 
     * @param   blockId {@link String}
     *          block uuid
     * 
     * @return  {@link Optional}<{@link Block}>
     *          Block entity, else Option.empty()
     */
    public Optional<Block> findById(String blockId);

}
