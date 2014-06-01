package utils;

import com.typesafe.config.ConfigFactory;
import models.RegistrationToken;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import play.Logger;

/**
 * // TODO: Add class description here.
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


    public static void sendSignUpEmail(RegistrationToken registrationToken) {
        Email email = new SimpleEmail();
        email.setHostName(HOST_NAME);
        email.setSmtpPort(SMTP_PORT);
        email.setAuthenticator(new DefaultAuthenticator(USER_NAME, PASSWORD));
        email.setSSLOnConnect(USE_SSL);
        email.setSubject("Registration email");
        try {
            email.setFrom(FROM);
            email.setMsg("Click on: \n " + generateSignUpUrl(registrationToken) );
            email.addTo(registrationToken.email);
            email.send();
        }
        catch(EmailException e){
            logger.error("Error sending email {}", e);
        }
    }

    //TODO: use email templates!
    private static String generateSignUpUrl(RegistrationToken registrationToken) {
        return new StringBuilder().append(controllers.authentication.routes.RegistrationController.confirmEmail(registrationToken.email));
    }
}
