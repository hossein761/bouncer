package controllers.authentication;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;
import domain.AccessToken;
import models.BaseUser;
import org.springframework.util.StringUtils;
import play.Logger;
import play.cache.Cache;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import requests.LoginRequest;
import utils.*;

import static play.data.Form.form;

/**
 * LoginController containing endpoints for post-registration actions
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class LoginController extends Controller {

    private final static Logger.ALogger logger = Logger.of(LoginController.class);

    public static final String AUTH_TOKEN = "Access-Token";
    public final static String ACCESS_TOKEN_HEADER = "X-ACCESS-TOKEN";

    //TODO: see http://stackoverflow.com/questions/2927044/redirect-on-ajax-jquery-call for redirecting

    // TODO: convert to async
    public static F.Promise<Result> login(){
        final Form<LoginRequest> loginRequestForm = form(LoginRequest.class);
        if(loginRequestForm.hasErrors()){
            return F.Promise.pure((Result)badRequest(loginRequestForm.errorsAsJson()));
        }
        final LoginRequest loginRequest = loginRequestForm.bindFromRequest().get();
        logger.info("LoginRequest: {}", loginRequest );
        final String emailOrUserName =  loginRequest.emailOrUserName;
        final String incomingPassword =  loginRequest.password;

        BaseUser baseUser;
        final boolean isEmail = PatternUtils.isEmail(emailOrUserName);
        if(isEmail) {
            baseUser = BaseUser.findByEmail(emailOrUserName);
        }else {
            baseUser = BaseUser.findByUserName(emailOrUserName);
        }
        // if not fail
        if(baseUser == null){
        		Logger.info("User not found!");
            return F.Promise.pure((Result)unauthorized("Invalid credentials!"));
        }
        if(baseUser.status == models.Status.PENDING){
        		Logger.info("User found but not registered!");
            return F.Promise.pure((Result)unauthorized("You haven't confirmed your registration, check your email!"));
        }
        if(baseUser.status == models.Status.REGISTERED) {
            // if found check if the passwords match then return an authorization code
            final boolean passwordValid = PasswordHash.validatePassword(incomingPassword,
                    new PBKDF2Hash(baseUser.passwordHash,
                            baseUser.salt,
                            baseUser.iterations));
            if (!passwordValid) {
        			Logger.info("Invalid password for user: {}" , baseUser.id);
                return F.Promise.pure((Result) unauthorized("Invalid credentials"));
            }
            final String authCode = AuthorizationUtils.generateAuthorizationCode();
            // and put it in cache with some expiry date
            final int authCodeExpiryTime = ConfigFactory.load().getInt("auth.authCode.expiry");
            Cache.set(CacheKeyUtils.getAuthCodeCacheKey(authCode), baseUser.id, authCodeExpiryTime);
            logger.debug("AuthCode created for User {}", baseUser);
            return F.Promise.pure((Result) ok(authCode));
        }
        return F.Promise.pure((Result)unauthorized("Invalid credentials!"));
    }


    public static F.Promise<Result> requestAccessToken(final String authCode){
        return F.Promise.promise(new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                // check if you can find the auth token in cache?
                final String userId = (String) Cache.get(CacheKeyUtils.getAuthCodeCacheKey(authCode));
                // if not fail
                if(StringUtils.isEmpty(userId)){
                    return forbidden("AuthCode is not valid!");
                }
                // if found create an accessToken and return with refresh token as well
                Cache.remove(CacheKeyUtils.getAuthCodeCacheKey(authCode));

                final AccessToken accessToken = AuthorizationUtils.createAccessToken(userId);
                final String accessTokenString = Json.stringify(Json.toJson(accessToken));
                response().setCookie(AUTH_TOKEN, accessTokenString);
                response().setHeader(ACCESS_TOKEN_HEADER, accessToken.token);
                // put in cache as well
                Cache.set(CacheKeyUtils.getAccessTokenCacheKey(accessToken.token),
                          userId,
                          accessToken.expiryTime);

                return ok(accessTokenString);
            }
        });

    }

    public static F.Promise<Result> logout(){
        return F.Promise.promise( new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                response().discardCookie(AUTH_TOKEN);
                final String[] accessTokenHeader = ctx().request().headers().get(ACCESS_TOKEN_HEADER);
                Cache.remove(CacheKeyUtils.getAccessTokenCacheKey(accessTokenHeader[0]));
                return ok("you have been logged out!");
            }
        });
    }
}
