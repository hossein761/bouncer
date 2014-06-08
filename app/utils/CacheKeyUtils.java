package utils;

/**
 * Utility class to create keys for cache
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class CacheKeyUtils {
    public static String getAccessTokenCacheKey(final String accessToken){
        return String.format("auth:accesstoken:%s", accessToken);
    }

    public static String getAuthCodeCacheKey(final String authCode){
        return String.format("auth:authcode:%s", authCode);
    }

    public static String getRegistrationTokenCacheKey(final String registrationToken){
        return String.format("auth:registrationcode:%s", registrationToken);
    }
}
