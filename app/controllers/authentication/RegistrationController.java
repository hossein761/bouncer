package controllers.authentication;

import com.fasterxml.jackson.databind.node.NullNode;
import com.typesafe.config.ConfigFactory;

import models.db.User;
import models.db.RegistrationToken;
import play.Logger;
import play.cache.Cache;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import requests.SignUpRequest;
import responses.AuthErrorCodes;
import responses.AuthErrorResponse;
import utils.CacheKeyUtils;
import utils.EmailUtils;
import utils.PBKDF2Hash;
import utils.PasswordHash;
import utils.validation.ValidationUtils;

import java.util.UUID;

import static play.data.Form.form;


/**
 * RegistrationController containing endpoints for registration and pre-registration actions
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class RegistrationController extends Controller {

    private final static Logger.ALogger LOGGER = Logger.of(RegistrationController.class);

    public static F.Promise<Result> signUpRequest(){
        return F.Promise.promise(new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                final SignUpRequest signUpRequest = Json.fromJson(request().body().asJson(), SignUpRequest.class);
                ValidationUtils.validatedSignUpRequest(signUpRequest);
                //TODO: double check
                if(signUpRequestForm.hasErrors()){
                    System.out.println(signUpRequestForm.errorsAsJson());
                    return badRequest(signUpRequestForm.errorsAsJson());
                }
                final SignUpRequest signUpRequest = signUpRequestForm.get();
                Logger.info("Signing Up user with info {}", signUpRequest);
                User user = new User();
                user.id = UUID.randomUUID().toString();
                user.firstName = signUpRequest.name;
                user.lastName = signUpRequest.lastName;
                user.email = signUpRequest.email;
                user.userName = signUpRequest.userName;
                user.status = models.Status.PENDING;
                final PBKDF2Hash hash = PasswordHash.createHash(signUpRequest.password);
                user.passwordHash = hash.hash;
                user.salt = hash.salt;
                user.iterations = hash.iterations;
                user.save();

                RegistrationToken registrationToken = new RegistrationToken();
                registrationToken.id = UUID.randomUUID().toString();
                registrationToken.user = user;
                registrationToken.save();
                //put in cache
                final int registrationTokenExpiryTime = ConfigFactory.load().getInt("auth.registrationToken.expiry");
                Cache.set(CacheKeyUtils.getRegistrationTokenCacheKey(registrationToken.id),
                          registrationToken,
                          registrationTokenExpiryTime);
                // send email
                LOGGER.info("Sending sign up confirmation email to user {}", user.id);
                EmailUtils.sendSignUpEmail(registrationToken.id, user);
                return ok("Check your mail box");
            }
        });

    }

    public static F.Promise<Result> signUpConfirm(final String registrationTokenId){
        return F.Promise.promise(new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                RegistrationToken registrationToken;
                registrationToken = (RegistrationToken) Cache.get(CacheKeyUtils.getRegistrationTokenCacheKey(registrationTokenId));
                if (registrationToken == null) {
                    registrationToken = RegistrationToken.findById(registrationTokenId);
                    if (registrationToken == null) {
                        final AuthErrorResponse authErrorResponse = new AuthErrorResponse(AuthErrorCodes.INVALID_REGISTRATION_TOKEN.getErrorCode(),
                                AuthErrorCodes.INVALID_REGISTRATION_TOKEN.getErrorMessage());
                        return unauthorized(Json.toJson(authErrorResponse));
                    }
                }
                User user = User.findById(registrationToken.user.id);
                user.status = models.Status.REGISTERED;
                user.save();
                Cache.remove(CacheKeyUtils.getRegistrationTokenCacheKey(registrationToken.id));
                return ok("You may now login with your username/email and password");
            }
        });

    }
}
