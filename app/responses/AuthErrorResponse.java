package responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * //
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class AuthErrorResponse {
    @JsonProperty
    private String key;
    @JsonProperty
    private String message;

    public AuthErrorResponse() {
    }

    public AuthErrorResponse(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String key(){
        return key;
    }

    public String message(){
        return message;
    }

}
