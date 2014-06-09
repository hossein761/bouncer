package controllers.authentication;

import org.junit.Test;
import play.mvc.Result;
import play.test.FakeRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class RegistrationControllerTest {

    @Test
    public void shouldSignUpRequest() throws Exception {
        running(fakeApplication(inMemoryDatabase()),new Runnable() {
            @Override
            public void run() {
                Map<String, String> regFromParams = new HashMap<String, String>();
                regFromParams.put("name","hossein");
                regFromParams.put("lastName","kazemi");
                regFromParams.put("email","1@1.com");
                regFromParams.put("userName","hossein");
                regFromParams.put("password","hossein");
                Result result = callAction(controllers.authentication.routes.ref.RegistrationController.signUpRequest(),
                        new FakeRequest(POST, "/auth/signUp").withFormUrlEncodedBody(regFromParams));
                assertThat(status(result)).isEqualTo(OK);

                // again
                result = callAction(controllers.authentication.routes.ref.RegistrationController.signUpRequest(),
                        new FakeRequest(POST, "/auth/signUp").withFormUrlEncodedBody(regFromParams));
                System.out.println(contentAsString(result));

            }
        });

    }

    @Test
    public void shouldSignUpConfirm() throws Exception {

    }
}