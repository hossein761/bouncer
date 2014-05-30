package authentication;

import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import requests.LoginRequest;

import static play.data.Form.form;

/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class LoginController extends Controller {

    public static F.Promise<Result> login(){
        final Form<LoginRequest> loginRequestForm = form(LoginRequest.class);
        if(loginRequestForm.hasErrors()){
            return F.Promise.pure((Result)badRequest(loginRequestForm.errorsAsJson()));
        }
        final LoginRequest loginRequest = loginRequestForm.get();
        // TODO: follow step 3 on flow sheet
        return null;
    }


}
