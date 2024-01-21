package com.mito.sectask.values;

/**
 * enum for action or operation client can do
 * when connected to this {@link com.mito.sectask.values.DESTINATION#pageBlock(Long) STOMP Channel}  
 */
public enum BLOCK_ACTION {
    NEW,
    UPDATE,
    DELETE
}
