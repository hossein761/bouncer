package controllers.authentication;

import annotations.BouncerSecuredAction;

import com.typesafe.config.ConfigFactory;

import domain.AccessToken;
import models.db.User;

import org.springframework.util.StringUtils;

import play.Logger;
import play.cache.Cache;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import requests.LoginRequest;
import responses.AuthErrorCodes;
import responses.AuthMessages;
import utils.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

/**
 * LoginController containing endpoints for post-registration actions
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class LoginController extends Controller {

    private final static Logger.ALogger logger = Logger.of(LoginController.class);

    public static final String AUTH_TOKEN = "Access-Token";

    //TODO: see http://stackoverflow.com/questions/2927044/redirect-on-ajax-jquery-call for redirecting

    public static F.Promise<Result> login(){
        return F.Promise.promise(new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                final Form<LoginRequest> loginRequestForm = form(LoginRequest.class).bindFromRequest();
                if(loginRequestForm.hasErrors()){
                    return badRequest(getErrorCodes(loginRequestForm.errors()));
                }
                final LoginRequest loginRequest = loginRequestForm.get();
                logger.info("LoginRequest: {}", loginRequest );
                final String emailOrUserName =  loginRequest.emailOrUserName;
                final String incomingPassword =  loginRequest.password;

                User user = getUseWithEmailOrUsername(emailOrUserName);

                // if not fail
                if(user == null){
                    Logger.info("User not found!");
                    return unauthorized(AuthErrorCodes.INVALID_CREDENTIALS.getErrorCode());
                }
                if(user.status == models.Status.PENDING){
                    Logger.info("User found but not registered!");
                    return unauthorized(AuthErrorCodes.REGISTRATION_INCOMPLETE.getErrorCode());
                }
                if(user.status == models.Status.REGISTERED) {
                    // if found check if the passwords match then return an authorization code
                    final boolean passwordValid = PasswordHash.validatePassword(incomingPassword,
                            new PBKDF2Hash(user.passwordHash,
                                    user.salt,
                                    user.iterations));
                    if (!passwordValid) {
                        Logger.info("Invalid password for user: {}" , user.id);
                        return unauthorized(AuthErrorCodes.INVALID_CREDENTIALS.getErrorCode());
                    }
                    final String authCode = AuthorizationUtils.generateAuthorizationCode();
                    // and put it in cache with some expiry date
                    final int authCodeExpiryTime = ConfigFactory.load().getInt("auth.authCode.expiry");
                    Cache.set(CacheKeyUtils.getAuthCodeCacheKey(authCode), user.id, authCodeExpiryTime);
                    logger.debug("AuthCode created for User {}", user);
                    user.lastLoginTime = new Timestamp(System.currentTimeMillis());
                    user.update();
                    return ok(authCode);
                }
                return unauthorized(AuthErrorCodes.INVALID_CREDENTIALS.getErrorCode());
            }
        });

    }

    private static String getErrorCodes(Map<String, List<ValidationError>> errors) {
        return new ArrayList<>(errors.keySet()).toString();
    }

    private static User getUseWithEmailOrUsername(String emailOrUserName) {
        final boolean isEmail = PatternUtils.isEmail(emailOrUserName);
        if(isEmail) {
            return User.findByEmail(emailOrUserName);
        }else {
            return User.findByUserName(emailOrUserName);
        }
    }


    public static F.Promise<Result> requestAccessToken(final String authCode){
        return F.Promise.promise(new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                // check if you can find the auth token in cache?
                final String userId = (String) Cache.get(CacheKeyUtils.getAuthCodeCacheKey(authCode));
                // if not fail
                if(StringUtils.isEmpty(userId)){
                    return unauthorized(AuthErrorCodes.AUTH_CODE_NOT_VALID.getErrorCode());
                }
                // if found create an accessToken and return with refresh token as well
                Cache.remove(CacheKeyUtils.getAuthCodeCacheKey(authCode));

                final AccessToken accessToken = AuthorizationUtils.createAccessToken(userId);
                final String accessTokenString = Json.stringify(Json.toJson(accessToken));
                response().setCookie(AUTH_TOKEN, accessTokenString);
                response().setHeader(AuthConstants.ACCESS_TOKEN_HEADER, accessToken.token);
                // put in cache as well
                Cache.set(CacheKeyUtils.getAccessTokenCacheKey(accessToken.token),
                          userId,
                          accessToken.expiryTime);

                return ok(accessTokenString);
            }
        });

    }

    @BouncerSecuredAction
    public static F.Promise<Result> logout(){
        return F.Promise.promise( new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                response().discardCookie(AUTH_TOKEN);
                final String[] accessTokenHeader = ctx().request().headers().get(AuthConstants.ACCESS_TOKEN_HEADER);
                Cache.remove(CacheKeyUtils.getAccessTokenCacheKey(accessTokenHeader[0]));
                return ok(AuthMessages.LOGGED_OUT.getMessageCode());
            }
        });
    }
}
