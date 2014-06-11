package responses;

/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public enum AuthMessages {
    CHECK_EMAIL("1000","Check you mailbox"),
    REGISTRATION_COMPLETE("1001","Registration is now complete. You may login"),
    LOGGED_OUT("1001","You have been logged out");



    private final String messageCode;
    private final String messageText;

    AuthMessages(String messageCode, String messageText) {
        this.messageCode = messageCode;
        this.messageText = messageText;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessageText() {
        return messageText;
    }

}
