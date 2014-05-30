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
        // Do validation here if needed
        return null;
    }
}
