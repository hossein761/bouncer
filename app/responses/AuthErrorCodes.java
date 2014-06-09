package responses;

/**
 * Defines all the possible auth errors
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public enum AuthErrorCodes {

    ACCESS_DENIED("100", "you are not logged in"),
    AUTH_CODE_NOT_VALID("101", "Auth-code is invalid"),
    REGISTRATION_INCOMPLETE("102", "Registration is not complete yet!"),
    INVALID_CREDENTIALS("103", "Invalid credentials"),
    USER_NAME_ALREADY_TAKEN("104", "Username already taken"),
    EMAIL_ALREADY_TAKEN("105", "Email already taken"),
    INVALID_REGISTRATION_TOKEN("106", "You should signup first");

    private final String errorCode;
    private final String errorMessage;

    AuthErrorCodes(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode(){
        return this.errorCode;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }
}
