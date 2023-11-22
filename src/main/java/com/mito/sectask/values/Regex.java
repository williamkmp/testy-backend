package com.mito.sectask.values;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Regex {
    public static final String ALPHANUM = "^[a-zA-Z0-9]+$";
    public static final String TAGNAME = "^[a-zA-Z0-9_.]+$";
}
