package requests;

import org.apache.commons.lang3.StringUtils;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import responses.AuthErrorCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the Login request form data
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class LoginRequest {

    public String emailOrUserName;
    public String password;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if(StringUtils.isEmpty(emailOrUserName)){
            final ValidationError validationError = new ValidationError(AuthErrorCodes.EMAIL_OR_USER_NAME_EMPTY.getErrorCode(),
                                                                        AuthErrorCodes.EMAIL_OR_USER_NAME_EMPTY.getErrorMessage());
            errors.add(validationError);
        }
        if(StringUtils.isEmpty(password)){
            final ValidationError validationError = new ValidationError(AuthErrorCodes.PASSWORD_EMPTY.getErrorCode(),
                                                                        AuthErrorCodes.PASSWORD_EMPTY.getErrorMessage());
            errors.add(validationError);
        }
        return null;
    }

    @Override
   	public String toString() {
   		return "LoginRequest [emailOrUserName=" + emailOrUserName
   				+ ", password=" + password + "]";
   	}

}
