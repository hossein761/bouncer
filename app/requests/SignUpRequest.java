package requests;

import models.db.User;
import org.apache.commons.lang3.StringUtils;
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

    public String name;
    public String lastName;
    public String userName;
    public String password;
    public String email;
    public String userType;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();

        if(StringUtils.isEmpty(name)){
            errors.add(new ValidationError(AuthErrorCodes.FIRST_NAME_EMPTY.getErrorCode(),
                                           AuthErrorCodes.FIRST_NAME_EMPTY.getErrorMessage()));
        }

        if(StringUtils.isEmpty(lastName)){
            errors.add(new ValidationError(AuthErrorCodes.LAST_NAME_EMPTY.getErrorCode(),
                                           AuthErrorCodes.LAST_NAME_EMPTY.getErrorMessage()));
        }

        if(StringUtils.isEmpty(password)){
            errors.add(new ValidationError(AuthErrorCodes.PASSWORD_EMPTY.getErrorCode(),
                                           AuthErrorCodes.PASSWORD_EMPTY.getErrorMessage()));
        }

        // user with userName should not exist
        if (User.findByUserName(userName) != null){
            errors.add(new ValidationError(AuthErrorCodes.USER_NAME_ALREADY_TAKEN.getErrorCode(),
                                           AuthErrorCodes.USER_NAME_ALREADY_TAKEN.getErrorMessage()));
        }

        Constraints.EmailValidator vl = new Constraints.EmailValidator();
        if(StringUtils.isEmpty(email)){
            errors.add(new ValidationError(AuthErrorCodes.EMAIL_EMPTY.getErrorCode(),
                                           AuthErrorCodes.EMAIL_EMPTY.getErrorMessage()));
        }else if(!vl.isValid(email)){
            errors.add(new ValidationError(AuthErrorCodes.EMAIL_NOT_WELL_FORMED.getErrorCode(),
                                           AuthErrorCodes.EMAIL_NOT_WELL_FORMED.getErrorMessage()));
        }
        // user with email should not exist
        if(!StringUtils.isEmpty(email) && User.findByEmail(email) != null){
            errors.add(new ValidationError(AuthErrorCodes.EMAIL_ALREADY_TAKEN.getErrorCode(),
                                           AuthErrorCodes.EMAIL_ALREADY_TAKEN.getErrorMessage()));
        }
        return errors.isEmpty() ? null : errors;
    }

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
