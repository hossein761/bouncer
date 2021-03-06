package utils;

import com.typesafe.config.ConfigFactory;
import domain.AccessToken;
import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

/**
 * Utility class for Authorization
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class AuthorizationUtils {

    public static String generateAuthorizationCode() {
        final String uuid = UUID.randomUUID().toString();
        return uuid.replace("-","");
    }

    public static AccessToken createAccessToken(final String userId){
        AccessToken accessToken = new AccessToken();
        accessToken.token = generateAccessToken(userId);
        accessToken.expiryTime = ConfigFactory.load().getInt("auth.accessToken.expiry");
        accessToken.refreshToken = generateRefreshToken(userId);
        return accessToken;
    }

    private static String generateRefreshToken(final String userId) {
        return generateTokenString(userId);
    }

    private static String generateAccessToken(final String userId) {
        return generateTokenString(userId);

    }

    //TODO: make more secure maybe
    private static String generateTokenString(final String userId) {
        // creation time
        final String creationTime = String.valueOf(System.currentTimeMillis());
        // some random string
        final String accessTokenId = UUID.randomUUID().toString();
        final String keySource = accessTokenId + ":" +creationTime + ":"+ userId;
        return encode(keySource);

    }

    public static String encode(final String keySource) {
        byte [] tokenByte = new Base64(true).encodeBase64(keySource.getBytes());
        return new String(tokenByte);
    }

    /**
     * Decodes and accessToken
     * @param accessToken
     * @return
     */
    public static String decode(final String accessToken) {
        final byte[] decode = new Base64(true).decodeBase64(accessToken.getBytes());
        return new String(decode);
    }

    public static String getUserIdFromAccessToken(final String accessToken){
        final String at = decode(accessToken);
        return at.split(":")[2];
    }
}
