package requests;

import play.data.validation.Constraints;

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

    public String validate() {
        //TODO: Do validation here if needed
        return null;
    }
}
