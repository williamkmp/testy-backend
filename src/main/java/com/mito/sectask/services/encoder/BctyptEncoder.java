package com.mito.sectask.services.encoder;

import com.mito.sectask.utils.Bcrypt;
import java.security.SecureRandom;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class BctyptEncoder implements PasswordEncocder {

    private static final BCryptVersion version = BCryptVersion.S2A;
    private static final int STRENGTH2 = 10;
    private static final Pattern BCRYPT_PATTERN = Pattern.compile(
        "\\A\\$2([ayb])?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}"
    );
    private final SecureRandom random = new SecureRandom();

    private String getSalt() {
        return Bcrypt.gensalt(
            BctyptEncoder.version.getVersion(),
            BctyptEncoder.STRENGTH2,
            this.random
        );
    }

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        String salt = getSalt();
        return Bcrypt.hashpw(rawPassword.toString(), salt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        if (encodedPassword == null || encodedPassword.length() == 0) {
            return false;
        }
        if (!BctyptEncoder.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            return false;
        }
        return Bcrypt.checkpw(rawPassword.toString(), encodedPassword);
    }

    /**
     * Stores the default bcrypt version for use in configuration.
     *
     * @author Lin Feng
     */
    public enum BCryptVersion {
        S2A("$2a"),

        S2Y("$2y"),

        S2B("$2b");

        private final String version;

        BCryptVersion(String version) {
            this.version = version;
        }

        public String getVersion() {
            return this.version;
        }
    }
}
