package controllers.authentication;

import com.typesafe.config.ConfigFactory;

import models.db.User;
import models.db.RegistrationToken;
import play.Logger;
import play.cache.Cache;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import requests.SignUpRequest;
import responses.AuthErrorCodes;
import responses.AuthMessages;
import utils.CacheKeyUtils;
import utils.EmailUtils;
import utils.PBKDF2Hash;
import utils.PasswordHash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                final Form<SignUpRequest> signUpRequestForm = form(SignUpRequest.class).bindFromRequest();
                if(signUpRequestForm.hasErrors()){
                    return badRequest(getErrorCodes(signUpRequestForm.errors()));
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
                return ok(AuthMessages.CHECK_EMAIL.getMessageCode());
            }
        });

    }

    private static String getErrorCodes(Map<String, List<ValidationError>> errors) {
       return new ArrayList<>(errors.keySet()).toString();
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
                        return unauthorized(AuthErrorCodes.INVALID_REGISTRATION_TOKEN.getErrorCode());
                    }
                }
                User user = User.findById(registrationToken.user.id);
                user.status = models.Status.REGISTERED;
                user.save();
                Cache.remove(CacheKeyUtils.getRegistrationTokenCacheKey(registrationToken.id));
                return ok(AuthMessages.REGISTRATION_COMPLETE.getMessageCode());
            }
        });

    }
}
