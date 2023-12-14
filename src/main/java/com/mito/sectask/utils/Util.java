package com.mito.sectask.utils;

import java.util.Optional;

/**
 * Utilities class filled with static method to
 * perform common used stateless functionality
 * without executing any side effects.
 *
 * @author william.kmp
 */
public class Util {
    
    /**
     * method to do nothing, usually to be used inside
     * a try catch block when the exception is handled
     * already outside of the catch block.
     * @param   reason developer notes describing
     *          why no further action is needed
     */
    public static void doNothing(java.lang.String reason) {
        // do nothing
        return;
    }


    public static class String {
        
        private String() {}

        public static Optional<Long> toLong(java.lang.String string) {
            Long longVal = null;

            if(string == null) 
                return Optional.empty();

            try {
                longVal = Long.valueOf(string);
            } catch (NumberFormatException e) {
                doNothing("longVal is alredy null");
            }
            return Optional.ofNullable(longVal);
        }
        
    }
}
