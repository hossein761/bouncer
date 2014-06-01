package controllers.authentication;

import models.RegistrationToken;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import requests.SignUpRequest;
import utils.EmailUtils;

import java.util.UUID;

import static play.data.Form.form;


/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class RegistrationController extends Controller {

    public static F.Promise<Result> signUpRequest(){
        return F.Promise.promise(new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                final Form<SignUpRequest> signUpRequestForm = form(SignUpRequest.class);
                if(signUpRequestForm.hasErrors()){
                    return badRequest(signUpRequestForm.errorsAsJson());
                }
                final SignUpRequest signUpRequest = signUpRequestForm.get();
                RegistrationToken registrationToken = new RegistrationToken();
                registrationToken.id = UUID.randomUUID().toString();
                registrationToken.name = signUpRequest.name;
                registrationToken.lastName = signUpRequest.lastName;
                registrationToken.email = signUpRequest.email;
                registrationToken.save();
                // send email
                EmailUtils.sendSignUpEmail(registrationToken);
                return ok("Check your mail box");
            }
        });

        //TODO: create the link and send an email
    }

    public static F.Promise<Result> confirmEmail(){
        //TODO: get the token id from request
        //TODO: follow on step 2 on the flow sheet
        return null;
    }
}
