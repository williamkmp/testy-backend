package com.mito.sectask.values;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationMessage {

    public static final String REQUIRED = "required";
    public static final String UNIQUE = "unique";
    public static final String INVALID = "invalid";
    public static final String ALPHANUM = "alphanum";
    public static final String INVALID_CREDENTIAL = "invalid.credential";
    public static final String INVALID_EMAIL = "invalid.email";
    public static final String STRING_LENGTH = "length.string:";
}
