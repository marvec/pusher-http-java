package org.marvec.pusher.data;

public class AuthData {

    private final String auth;
    private final String channelData;

    /**
     * Private channel constructor
     *
     * @param key App key
     * @param signature Auth signature
     */
    public AuthData(final String key, final String signature) {
        this(key, signature, null);
    }

    /**
     * Presence channel constructor
     *
     * @param key App key
     * @param signature Auth signature
     * @param channelData Extra user data
     */
    public AuthData(final String key, final String signature, final String channelData) {
        this.auth = key + ":" + signature;
        this.channelData = channelData;
    }

    public String getAuth() {
        return auth;
    }

    public String getChannelData() {
        return channelData;
    }
}
