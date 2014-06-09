package responses;

/**
 * Defines all the possible auth errors
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public enum AuthErrorCodes {

    //access denied
    ACCESS_DENIED("100", "you are not logged in"),
    //auth code not valid
    AUTH_CODE_NOT_VALID("101", "Auth-code is invalid"),
    // registration incomplete
    REGISTRATION_INCOMPLETE("102", "Registration is not complete yet!"),
    //invalid credentials
    INVALID_CREDENTIALS("103", "Invalid credentials"),
    //username already taken
    USER_NAME_ALREADY_TAKEN("104", "Username already taken"),
    //email already taken
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
