package org.marvec.pusher.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class Prerequisites {

    private static final Pattern VALID_CHANNEL = Pattern.compile("\\A[-a-zA-Z0-9_=@,.;]+\\z");
    private static final Pattern VALID_SOCKET_ID = Pattern.compile("\\A\\d+\\.\\d+\\z");

    private static final Set<String> RESERVED_QUERY_KEYS = new HashSet<String>(
            Arrays.asList(new String[] { "auth_key", "auth_timestamp", "auth_version", "auth_signature", "body_md5" }));

    public static void nonNull(final String name, final Object ref) {
        if (ref == null) throw new IllegalArgumentException("Parameter [" + name + "] must not be null");
    }

    public static void nonEmpty(final String name, final String ref) {
        nonNull(name, ref);
        if (ref.length() == 0) throw new IllegalArgumentException("Parameter [" + name + "] must not be empty");
    }

    public static void maxLength(final String name, final int max, final List<?> ref) {
        if (ref.size() > max) throw new IllegalArgumentException("Parameter [" + name + "] must have size < " + max);
    }

    public static void noNullMembers(final String name, final List<?> ref) {
        for (Object e : ref) {
            if (e == null) throw new IllegalArgumentException("Parameter [" + name + "] must not contain null elements");
        }
    }

    public static void noReservedKeys(final Map<String, String> params) {
        for (String k : params.keySet()) {
            if (RESERVED_QUERY_KEYS.contains(k.toLowerCase())) {
                throw new IllegalArgumentException("Query parameter key [" + k + "] is reserved and should not be submitted. It will be generated by the signature generation.");
            }
        }
    }

    public static void isValidSha256Key(final String name, final String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(), "SHA256"));
            // If that goes OK, then we're good to go
        }
        catch (final NoSuchAlgorithmException e) {
            // Out of luck.
            throw new RuntimeException("The Pusher HTTP client requires HmacSHA256 support", e);
        }
        catch (final InvalidKeyException e) {
            // Failed the test
            throw new IllegalArgumentException("Parameter [" + name + "] must be a valid SHA256 key", e);
        }
    }

    public static void areValidChannels(final List<String> channels) {
        for (String channel : channels) {
            isValidChannel(channel);
        }
    }

    public static void isValidChannel(final String channel) {
        matchesRegex("channel", VALID_CHANNEL, channel);
    }

    public static void isValidSocketId(final String socketId) {
        if (socketId != null) {
            matchesRegex("socket_id", VALID_SOCKET_ID, socketId);
        }
    }

    private static void matchesRegex(final String name, final Pattern regex, final String toMatch) {
        nonNull(name, toMatch);
        if (!regex.matcher(toMatch).matches()) {
            throw new IllegalArgumentException(name + " [" + toMatch + "] is not valid");
        }
    }
}
