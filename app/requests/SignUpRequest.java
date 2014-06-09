package requests;

import models.db.User;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import responses.AuthErrorCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain class holding info from a signup request
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class SignUpRequest {

	@Constraints.Required
    public String name;
    @Constraints.Required
    public String lastName;
    @Constraints.Required
    public String userName;
    @Constraints.Required
    public String password;
    @Constraints.Required
    @Constraints.Email
    public String email;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        // user with userName should not exist
        if (User.findByUserName(userName) != null){
            errors.add(new ValidationError(AuthErrorCodes.EMAIL_ALREADY_TAKEN.getErrorCode(),
                                           AuthErrorCodes.EMAIL_ALREADY_TAKEN.getErrorMessage()));
        }

        // user with email should not exist
        if(User.findByEmail(email) != null){
            errors.add(new ValidationError(AuthErrorCodes.USER_NAME_ALREADY_TAKEN.getErrorCode(),
                    AuthErrorCodes.USER_NAME_ALREADY_TAKEN.getErrorMessage()));
        }
        System.out.println("  \n{}"+errors);
        return errors.isEmpty() ? null : errors;
    }

    @Override
	public String toString() {
		return "SignUpRequest [name=" + name + ", lastName=" + lastName
				+ ", userName=" + userName + ", password=" + password
				+ ", email=" + email + "]";
	}
}
