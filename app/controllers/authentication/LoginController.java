package controllers.authentication;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;
import domain.AccessToken;
import models.BaseUser;
import org.springframework.util.StringUtils;
import play.cache.Cache;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import requests.LoginRequest;
import utils.AuthorizationUtils;
import utils.PBKDF2Hash;
import utils.PasswordHash;
import utils.PatternUtils;

import static play.data.Form.form;

/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class LoginController extends Controller {

    public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";


    //TODO: make proper async
    public static F.Promise<Result> login(){
        final Form<LoginRequest> loginRequestForm = form(LoginRequest.class);
        if(loginRequestForm.hasErrors()){
            return F.Promise.pure((Result)badRequest(loginRequestForm.errorsAsJson()));
        }
        final LoginRequest loginRequest = loginRequestForm.get();
        final String emailOrUserName =  loginRequest.emailOrUserName;
        final String incomingPassword =  loginRequest.password;

        // check if the user is logging in with email or username
        // check the user pass from the db
        BaseUser baseUser;
        final boolean isEmail = PatternUtils.isEmail(emailOrUserName);
        if(isEmail) {
            baseUser = BaseUser.findByEmail(emailOrUserName);
        }else {
            baseUser = BaseUser.findByUserName(emailOrUserName);
        }
        // if not fail
        if(baseUser == null){
            return F.Promise.pure((Result)unauthorized("username/email does not exist in our system"));
        }
        // if found check if the passwords match then return an authorization code
        final boolean passwordValid = PasswordHash.validatePassword(incomingPassword,
                                                                    new PBKDF2Hash(baseUser.passwordHash,
                                                                            baseUser.salt,
                                                                            baseUser.iterations));
        if(!passwordValid){
            return F.Promise.pure((Result) unauthorized("Invalid credentials"));
        }
        final String authCode = AuthorizationUtils.generateAuthorizationCode();
        // and put it in cache with some expiry date
        final int authCodeExpiryTime = ConfigFactory.load().getInt("auth.authCode.expiry");
        Cache.set(authCode, baseUser.id, authCodeExpiryTime);
        return F.Promise.pure((Result)ok(authCode));
    }


    //TODO: make properly async
    public static F.Promise<Result> requestAccessToken(final String authCode){
        // check if you can find the auth token in cache?
        final String userId = (String) Cache.get(authCode);
        // if not fail
        if(StringUtils.isEmpty(userId)){
            return F.Promise.pure((Result)forbidden("AuthCode is not valid!"));
        }
        // if found create an accessToken and return with refresh token as well
        final AccessToken accessToken = AuthorizationUtils.createAccessToken(userId);
        ObjectNode authTokenJson = Json.newObject();
        authTokenJson.put(AUTH_TOKEN, Json.toJson(accessToken));
        response().setCookie(AUTH_TOKEN, Json.stringify(authTokenJson));
        return F.Promise.pure((Result)ok(authTokenJson));
    }

    public static F.Promise<Result> logout(){
        //TODO: implement
        return null;
    }
}
