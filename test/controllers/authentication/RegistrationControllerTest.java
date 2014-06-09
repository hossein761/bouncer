package controllers.authentication;

import org.junit.Test;
import play.libs.Json;
import play.mvc.Result;
import play.test.FakeRequest;
import requests.SignUpRequest;

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
                SignUpRequest sr = new SignUpRequest();
                sr.name = "hossein";
                sr.lastName = "kazemi";
                sr.email = "1@1.com";
                sr.password = "hossein";
                sr.userName = "hossein";

                Result result = callAction(controllers.authentication.routes.ref.RegistrationController.signUpRequest(),
                        new FakeRequest(POST, "/auth/signUp").withJsonBody(Json.toJson(sr)));
                assertThat(status(result)).isEqualTo(OK);

                // again
                result = callAction(controllers.authentication.routes.ref.RegistrationController.signUpRequest(),
                        new FakeRequest(POST, "/auth/signUp").withJsonBody(Json.toJson(sr)));
                assertThat(status(result)).isEqualTo(BAD_REQUEST);


            }
        });

    }

    @Test
    public void shouldSignUpConfirm() throws Exception {

    }
}