package org.ikernits.sample.util;

import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecureRandomUtils {
    private static final SecureRandom random;

    static {
        try {
            random = SecureRandom.getInstance("NativePRNGNonBlocking");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String randomHexString(int bytes) {
        byte[] tokenBytes = new byte[bytes];
        random.nextBytes(tokenBytes);
        return BaseEncoding.base16().encode(tokenBytes);
    }

    public static String randomAlphanumeric(int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null, random);
    }
}
