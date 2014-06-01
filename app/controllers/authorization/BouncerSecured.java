package controllers.authorization;

import org.apache.commons.lang3.StringUtils;
import play.cache.Cache;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import utils.CacheKeyUtils;

/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
//TODO: make async and refactor!
public class BouncerSecured extends Action<BouncerSecured> {
    public final static String ACCESS_TOKEN_HEADER = "X-ACCESS-TOKEN";
    @Override
    public F.Promise<Result> call(final Http.Context ctx) throws Throwable {
        final String[] accessTokenHeader = ctx.request().headers().get(ACCESS_TOKEN_HEADER);
        if((accessTokenHeader != null) && (accessTokenHeader.length == 1) && (accessTokenHeader[0] != null) ){
            // check if it exist in the cache
            final String accessToken = (String)Cache.get(CacheKeyUtils.getAccessTokenCacheKey(accessTokenHeader[0]));
            if(StringUtils.isEmpty(accessToken)){
                return F.Promise.pure((Result)forbidden("You are not logged in!"));
            }


        }else{
            return F.Promise.pure((Result)forbidden("You are not logged in!"));
        }
        return delegate.call(ctx);
    }
}
