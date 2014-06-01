package controllers.authorization;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class BouncerSecured extends Action<BouncerSecured> {
    @Override
    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        //TODO: follow the check flow
        return delegate.call(ctx);
    }
}
