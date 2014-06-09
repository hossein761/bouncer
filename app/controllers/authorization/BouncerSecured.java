package controllers.authorization;

import annotations.BouncerSecuredAction;

import controllers.authentication.AuthConstants;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.cache.Cache;
import play.libs.F;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import responses.AuthErrorCodes;
import responses.AuthErrorResponse;
import utils.CacheKeyUtils;

/**
 * Main Action that intercepts the calls that are annotated with @{BouncerSecuredAction}
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class BouncerSecured extends Action<BouncerSecuredAction> {
	private final static Logger.ALogger logger = Logger.of(BouncerSecured.class);
    @Override
    public F.Promise<Result> call(final Http.Context ctx) throws Throwable {
        final String[] accessTokenHeader = ctx.request().headers().get(AuthConstants.ACCESS_TOKEN_HEADER);
        AuthErrorResponse authErrorResponse;
        if((accessTokenHeader != null) && (accessTokenHeader.length == 1) && (accessTokenHeader[0] != null) ){
            // check if it exist in the cache
            final String accessToken = (String)Cache.get(CacheKeyUtils.getAccessTokenCacheKey(accessTokenHeader[0]));
            if(StringUtils.isEmpty(accessToken)){
                authErrorResponse = new AuthErrorResponse(AuthErrorCodes.ACCESS_DENIED.getErrorCode(),
                        AuthErrorCodes.ACCESS_DENIED.getErrorMessage());
                return F.Promise.pure((Result)forbidden(Json.toJson(authErrorResponse)));
            }


        }else{
            authErrorResponse = new AuthErrorResponse(AuthErrorCodes.ACCESS_DENIED.getErrorCode(),
                    AuthErrorCodes.ACCESS_DENIED.getErrorMessage());
            return F.Promise.pure((Result)forbidden(Json.toJson(authErrorResponse)));
        }
        logger.info("Access denied!");
        return delegate.call(ctx);
    }
}
