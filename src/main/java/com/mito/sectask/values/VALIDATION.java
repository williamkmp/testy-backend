package com.mito.sectask.values;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VALIDATION {

    // root error
    public static final String INVALID_CREDENTIAL = "invalid_credential";
    
    // common validation
    public static final String REQUIRED = "required";
    public static final String UNIQUE   = "unique";
    public static final String INVALID  = "invalid";
    public static final String WRONG    = "wrong";
    
    // string validation
    public static final String STRING_EMAIL    = "string_email";
    public static final String STRING_ALPHANUM = "string_alphanum";
    public static final String STRING_LENGTH   = "string_between:";

    // number validation
    public static final String NUMBER_MIN     = "number.min:";
    public static final String NUMBER_MAX     = "number.max:";
    public static final String NUMBER_BETWEEN = "number.between:";
}
