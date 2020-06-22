import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.http.DefaultHttpErrorHandler;
import play.api.routing.Router;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public ErrorHandler(
            Config config,
            Environment environment,
            OptionalSourceMapper sourceMapper,
            Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    protected CompletionStage<Result> onProdServerError(Http.RequestHeader request, UsefulException exception) {
        return CompletableFuture.completedFuture(Results.internalServerError("A server error occurred: " + exception.getMessage()));
    }

    protected CompletionStage<Result> onForbidden(Http.RequestHeader request, String message) {
        return CompletableFuture.completedFuture(Results.forbidden("You're not allowed to access this resource."));
    }

    protected CompletionStage<Result> onNotFound(Http.RequestHeader request, String message) {
        return CompletableFuture.completedFuture(Results.notFound(views.html.notFound.render()));
    }
}
