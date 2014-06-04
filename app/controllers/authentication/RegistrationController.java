package controllers.authentication;

import com.typesafe.config.ConfigFactory;

import models.BaseUser;
import models.RegistrationToken;
import play.Logger;
import play.cache.Cache;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import requests.SignUpRequest;
import utils.CacheKeyUtils;
import utils.EmailUtils;
import utils.PBKDF2Hash;
import utils.PasswordHash;

import java.util.UUID;

import static play.data.Form.form;


/**
 * RegistrationController containing endpoints for registration and pre-registration actions
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class RegistrationController extends Controller {

	private static final Logger.ALogger Logger = Logger.of(RegistrationController.class);

    public static F.Promise<Result> signUpRequest(){
        return F.Promise.promise(new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                final Form<SignUpRequest> signUpRequestForm = form(SignUpRequest.class);
                if(signUpRequestForm.hasErrors()){
                    return badRequest(signUpRequestForm.errorsAsJson());
                }
                final SignUpRequest signUpRequest = signUpRequestForm.bindFromRequest().get();
                Logger.info("Signing Up user with info {}", signUpRequest);
                BaseUser baseUser = new BaseUser();
                baseUser.id = UUID.randomUUID().toString();
                baseUser.name = signUpRequest.name;
                baseUser.lastName = signUpRequest.lastName;
                baseUser.email = signUpRequest.email;
                baseUser.userName = signUpRequest.userName;
                baseUser.status = models.Status.PENDING;
                final PBKDF2Hash hash = PasswordHash.createHash(signUpRequest.password);
                baseUser.passwordHash = hash.hash;
                baseUser.salt = hash.salt;
                baseUser.iterations = hash.iterations;
                baseUser.save();

                RegistrationToken registrationToken = new RegistrationToken();
                registrationToken.id = UUID.randomUUID().toString();
                registrationToken.baseUser = baseUser;
                registrationToken.save();
                //put in cache
                final int registrationTokenExpiryTime = ConfigFactory.load().getInt("auth.registrationToken.expiry");
                Cache.set(CacheKeyUtils.getRegistrationTokenCacheKey(registrationToken.id),
                          registrationToken,
                          registrationTokenExpiryTime);
                // send email
                Logger.info("Sending sign up confirmation email to user {}", baseUser.id);
                EmailUtils.sendSignUpEmail(registrationToken.id, baseUser);
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
                        return notFound("You have to signup first!");
                    }
                }
                BaseUser baseUser = BaseUser.findById(registrationToken.baseUser.id);
                baseUser.status = models.Status.REGISTERED;
                baseUser.save();
                Cache.remove(CacheKeyUtils.getRegistrationTokenCacheKey(registrationToken.id));
                return ok("You may now login with your username/email and password");
            }
        });

    }
}
