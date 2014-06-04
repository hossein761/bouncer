package controllers;
import static play.mvc.Http.Status.OK;
import static org.fest.assertions.Assertions.*;
import static play.test.Helpers.*;

import org.junit.Test;

import play.mvc.Result;
import play.mvc.Http.Status;

import java.util.HashMap;
import java.util.Map;


public class LoginControllerTest {

	@Test
	public void LoginTest() throws Exception{
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("emailOrUserName", "hossein@hossein.com");
                params.put("password","p123455");

                Result result = callAction(controllers.authentication.routes.ref.LoginController.login(),
                			fakeRequest().withFormUrlEncodedBody(params));
                assertThat(status(result)).isEqualTo(Status.UNAUTHORIZED);
                System.out.println(contentAsString(result));
            }
        });

	}

}
