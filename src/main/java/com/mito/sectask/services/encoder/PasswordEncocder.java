package com.mito.sectask.services.encoder;

/**
 * Encoding & hasing utilty
 * @author william.kmp
 */
public interface PasswordEncocder {
    /**
     * Encode the raw password to string hash
     * @param rawPassword string
     * @return encoded password
     */
    String encode(CharSequence rawPassword);

    /**
     * Verify a raw string againts an encoded hashed string. 
     * Returns true if the passwords match, false if
     * they do not. The encoded string itself is never decoded.
     * @param rawPassword the raw password not encoded string
     * @param encodedPassword the hash, encoded string to compare with
     * @return true if the match else false
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
