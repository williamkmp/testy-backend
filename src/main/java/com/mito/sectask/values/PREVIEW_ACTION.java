package com.mito.sectask.values;

/**
 * enum for action or operation client can do
 * when connected to this {@link com.mito.sectask.values.DESTINATION#userPreview(Long) STOMP Channel}  
 */
public enum PREVIEW_ACTION {
    NEW,
    UPDATE,
    DELETE
}
