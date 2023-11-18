package com.mito.sectask.values;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    public static final String ERROR_INTERNAL_SERVER = "server_error";
    public static final String ERROR_RESOURCE_NOT_FOUND = "resource_not_found";
    public static final String ERROR_UNAUTHORIZED = "unauthoried";
    public static final String ERROR_INVALID_TOKEN = "invalid_token";
    public static final String ERROR_UPLOAD_FAILED = "upload.fail";
}
