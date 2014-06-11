package controllers.authentication;

import annotations.BouncerSecuredAction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;

import domain.AccessToken;
import models.db.User;

import org.springframework.util.StringUtils;

import play.Logger;
import play.cache.Cache;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import requests.LoginRequest;
import responses.AuthErrorCodes;
import responses.ValidationError;
import responses.AuthMessages;
import utils.*;
import utils.validation.ValidationUtils;

import java.sql.Timestamp;
import java.util.List;

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
                final LoginRequest loginRequest = Json.fromJson(request().body().asJson(), LoginRequest.class);
                List<ValidationError> validationError = ValidationUtils.validateLoginRequest(loginRequest);
                if(validationError.size() > 0){
                    return badRequest(Json.toJson(validationError));
                }
                logger.info("LoginRequest: {}", loginRequest );
                final String emailOrUserName =  loginRequest.emailOrUserName;
                final String incomingPassword = loginRequest.password;

                User user = getUseWithEmailOrUsername(emailOrUserName);

                ValidationError authErrorResponse;
                // if not fail
                if(user == null){
                    Logger.info("User not found!");
                    authErrorResponse = new ValidationError(AuthErrorCodes.INVALID_CREDENTIALS.getErrorCode(),
                                                            AuthErrorCodes.INVALID_CREDENTIALS.getErrorMessage());
                    return unauthorized(Json.toJson(authErrorResponse));
                }
                if(user.status == models.Status.PENDING){
                    Logger.info("User found but not registered!");
                    authErrorResponse = new ValidationError(AuthErrorCodes.REGISTRATION_INCOMPLETE.getErrorCode(),
                                                            AuthErrorCodes.REGISTRATION_INCOMPLETE.getErrorMessage());
                    return unauthorized(Json.toJson(authErrorResponse));
                }
                if(user.status == models.Status.REGISTERED) {
                    // if found check if the passwords match then return an authorization code
                    final boolean passwordValid = PasswordHash.validatePassword(incomingPassword,
                            new PBKDF2Hash(user.passwordHash,
                                    user.salt,
                                    user.iterations));
                    if (!passwordValid) {
                        Logger.info("Invalid password for user: {}" , user.id);
                        authErrorResponse = new ValidationError(AuthErrorCodes.INVALID_CREDENTIALS.getErrorCode(),
                                AuthErrorCodes.INVALID_CREDENTIALS.getErrorMessage());
                        return unauthorized(Json.toJson(authErrorResponse));
                    }
                    final String authCode = AuthorizationUtils.generateAuthorizationCode();
                    // and put it in cache with some expiry date
                    final int authCodeExpiryTime = ConfigFactory.load().getInt("auth.authCode.expiry");
                    Cache.set(CacheKeyUtils.getAuthCodeCacheKey(authCode), user.id, authCodeExpiryTime);
                    logger.debug("AuthCode created for User {}", user);
                    user.lastLoginTime = new Timestamp(System.currentTimeMillis());
                    user.update();
                    final ObjectNode response = Json.newObject();
                    response.put("auth-code",authCode);
                    return ok(response);
                }
                authErrorResponse = new ValidationError(AuthErrorCodes.INVALID_CREDENTIALS.getErrorCode(),
                                                          AuthErrorCodes.INVALID_CREDENTIALS.getErrorMessage());
                return unauthorized(Json.toJson(authErrorResponse));
            }
        });

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
                    final ValidationError validationError = new ValidationError(AuthErrorCodes.AUTH_CODE_NOT_VALID.getErrorCode(),
                                                                                      AuthErrorCodes.AUTH_CODE_NOT_VALID.getErrorMessage());
                    return unauthorized(Json.toJson(validationError));
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
                final ObjectNode response = Json.newObject();
                response.put("key", AuthMessages.CHECK_EMAIL.getMessageCode());
                response.put("message", AuthMessages.CHECK_EMAIL.getMessageText());
                return ok(response);
            }
        });
    }
}
