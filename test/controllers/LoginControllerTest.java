package controllers;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.POST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.route;
import static play.test.Helpers.status;
import static org.fest.assertions.Assertions.*;


import org.junit.Test;

import play.mvc.Result;


public class LoginControllerTest {

	@Test
	public void LoginTest() throws Exception{
		Result result = callAction(controllers.authentication.routes.ref.LoginController.login());
		assertThat(status(result)).isEqualTo(OK);
	}

}
