package utils.validation;

import org.apache.commons.lang3.StringUtils;
import requests.SignUpRequest;
import responses.AuthErrorResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Validation methods
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class ValidationUtils {
    public static List<AuthErrorResponse> validatedSignUpRequest(SignUpRequest signUpRequest){
        List<AuthErrorResponse> errors = new ArrayList<>();
        if(StringUtils.isEmpty(signUpRequest.name)){
            errors.add(new AuthErrorResponse())
        }
        if(StringUtils.isEmpty(signUpRequest.lastName)){

        }
        if(StringUtils.isEmpty(signUpRequest.email)){

        }
        if(StringUtils.isEmpty(signUpRequest.userName)){

        }
        if(StringUtils.isEmpty(signUpRequest.password)){

        }

        return errors;
    }
}
