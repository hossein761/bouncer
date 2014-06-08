package utils;

import com.typesafe.config.ConfigFactory;
import models.db.User;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import play.Logger;

/**
 * Utility class for emails
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class EmailUtils {
    private static final Logger.ALogger logger = play.Logger.of(EmailUtils.class);
    private static final String HOST_NAME = ConfigFactory.load().getString("smtp.host");
    private static final Integer SMTP_PORT = ConfigFactory.load().getInt("smtp.port");
    private static final Boolean USE_SSL = ConfigFactory.load().getBoolean("smtp.ssl");
    private static final String USER_NAME = ConfigFactory.load().getString("smtp.user");
    private static final String PASSWORD = ConfigFactory.load().getString("smtp.password");
    private static final String FROM = ConfigFactory.load().getString("smtp.from");


    public static void sendSignUpEmail(final String registrationTokenId, final User user) {
        Email email = new SimpleEmail();
        email.setHostName(HOST_NAME);
        email.setSmtpPort(SMTP_PORT);
        email.setAuthenticator(new DefaultAuthenticator(USER_NAME, PASSWORD));
        email.setSSLOnConnect(USE_SSL);
        email.setSubject("Registration email");
        try {
        		final String emailMsg = "Click on: \n " + generateSignUpUrl(registrationTokenId);
            email.setFrom(FROM);
            email.setMsg(emailMsg);
            email.addTo(user.email);
            Logger.info("Sending registration email to: {} contents: {}", email.getToAddresses(), emailMsg);
            email.send();
        }
        catch(EmailException e){
            logger.error("Error sending email {}", e);
        }
    }

    //TODO: use email templates!
    private static String generateSignUpUrl(final String registrationToken) {
        return controllers.authentication.routes.RegistrationController.signUpConfirm(registrationToken).toString();
    }
}
