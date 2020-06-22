package controllers;

import play.http.HttpErrorHandler;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.PasswordRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class PasswordController extends Controller {

    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;
    private final PasswordRepository passwordRepository;
    private final HttpErrorHandler httpErrorHandler;

    @Inject
    public PasswordController(HttpExecutionContext httpExecutionContext,
                              MessagesApi messagesApi,
                              PasswordRepository passwordRepository,
                              HttpErrorHandler httpErrorHandler) {
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
        this.passwordRepository = passwordRepository;
        this.httpErrorHandler = httpErrorHandler;
    }

    /**
     * This result directly redirect to application home.
     */
    private final Result GO_LIST_PASSWORD = Results.redirect(
            routes.PasswordController.list(0, "name", "asc", "")
    );

    /**
     * Handle default path requests, redirect to computers list
     */
    public Result index() {
        return GO_LIST_PASSWORD;
    }

    /**
     * Display the paginated list of Passwords.
     *
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on Password names
     */
    public CompletionStage<Result> list(Http.Request request, int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return passwordRepository.page(page, 10, sortBy, order, filter).thenApplyAsync(list -> {
            // This is the HTTP rendering thread context
            return ok(views.html.passwords.listForm.render(list, sortBy, order, filter, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }
}
