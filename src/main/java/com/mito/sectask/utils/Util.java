package com.mito.sectask.utils;

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
    public static void doNothing(String reason) {
        // do nothing
        return;
    }
}
