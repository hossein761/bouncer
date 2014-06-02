package domain;

/**
 * Object containing access token and all related info
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class AccessToken {
    public String token;
    public int expiryTime;
    public String refreshToken;
}
