package com.mito.sectask.values;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MESSAGES {

    // Error
    public static final String ERROR_UNAUTHORIZED = "unauthorized";
    public static final String ERROR_FORBIDDEN = "forbidden";
    public static final String ERROR_INTERNAL_SERVER    = "internal_error";
    public static final String ERROR_RESOURCE_NOT_FOUND = "no_resource";
    
    // Messages
    public static final String UPLOAD_SUCCESS = "upload_success";
    public static final String UPLOAD_FAIL = "upload_fail";
    public static final String UPDATE_SUCCESS = "update_success";
    public static final String UPDATE_FAIL = "update_fail";
}
