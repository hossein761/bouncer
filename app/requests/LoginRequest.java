package requests;

import play.data.validation.Constraints;

/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class LoginRequest {

	@Constraints.Required
    public String emailOrUserName;
    @Constraints.Required
    public String password;

    public String validate() {
        // TODO: Do validation here if needed
        return null;
    }

    @Override
   	public String toString() {
   		return "LoginRequest [emailOrUserName=" + emailOrUserName
   				+ ", password=" + password + "]";
   	}

}
