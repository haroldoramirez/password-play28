package repository;

import io.ebean.*;
import models.Password;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PasswordRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public PasswordRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Map<String, String>> options() {
        return supplyAsync(() -> ebeanServer.find(Password.class).orderBy("name").findList(), executionContext)
                .thenApply(list -> {
                    HashMap<String, String> options = new LinkedHashMap<String, String>();
                    for (Password p : list) {
                        options.put(p.id.toString(), p.name);
                    }
                    return options;
                });
    }

    /**
     * Return a paged list of Password
     *
     * @param page     Page to display
     * @param pageSize Number of Passwords per page
     * @param sortBy   Password property used for sorting
     * @param order    Sort order (either or asc or desc)
     * @param filter   Filter applied on the name column
     */
    public CompletionStage<PagedList<Password>> page(int page, int pageSize, String sortBy, String order, String filter) {
        return supplyAsync(() ->
                ebeanServer.find(Password.class).where()
                        .ilike("name", "%" + filter + "%")
                        .orderBy(sortBy + " " + order)
                        .setFirstRow(page * pageSize)
                        .setMaxRows(pageSize)
                        .findPagedList(), executionContext);
    }

    public CompletionStage<Optional<Password>> lookup(Long id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(Password.class).setId(id).findOne()), executionContext);
    }

    public CompletionStage<Optional<Long>> update(Long id, Password newPasswordData) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Long> value = Optional.empty();
            try {
                Password savedPassword = ebeanServer.find(Password.class).setId(id).findOne();
                if (savedPassword != null) {
                    savedPassword.name = newPasswordData.name;
                    savedPassword.username = newPasswordData.username;
                    savedPassword.url = newPasswordData.url;
                    savedPassword.password = newPasswordData.password;

                    savedPassword.update();
                    txn.commit();
                    value = Optional.of(id);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }

    public CompletionStage<Optional<Long>>  delete(Long id) {
        return supplyAsync(() -> {
            try {
                final Optional<Password> PasswordOptional = Optional.ofNullable(ebeanServer.find(Password.class).setId(id).findOne());
                PasswordOptional.ifPresent(Model::delete);
                return PasswordOptional.map(c -> c.id);
            } catch (Exception e) {
                return Optional.empty();
            }
        }, executionContext);
    }

    public CompletionStage<Long> insert(Password Password) {
        return supplyAsync(() -> {
            Password.id = System.currentTimeMillis(); // not ideal, but it works
            ebeanServer.insert(Password);
            return Password.id;
        }, executionContext);
    }
}
