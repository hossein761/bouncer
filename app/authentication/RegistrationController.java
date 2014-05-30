package authentication;

import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class RegistrationController extends Controller {

    public static F.Promise<Result> signUpRequest(){
        //TODO: get the signup request check for errors
        //TODO: continue on step 1 on the flow sheet
        return null;
    }

    public static F.Promise<Result> confirmEmail(){
        //TODO: get the token id from request
        //TODO: follow on step 2 on the flow sheet
        return null;
    }
}
