package controllers;

import models.User;
import play.mvc.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Application extends Controller {
    protected static Long getCurrentUserId() {
        return Long.parseLong(ctx().session().get("user_id"));
    }

    protected static User getCurrentUser() {
        return User.find.byId(getCurrentUserId());
    }

    protected static int getQueryPage() {
        if (request().queryString().containsKey("Page"))
          return Integer.parseInt(request().getQueryString("Page"));
        return 0;
    }

    protected static int getItemsPerPage(int def) {
        if (request().queryString().containsKey("Limit"))
          return Integer.parseInt(request().getQueryString("Limit"));
        return def;
    }

    protected static boolean mustDisplayTotalResources() {
        if (request().queryString().containsKey("TotalResources"))
          return request().getQueryString("TotalResources").toString() != "0";
        return false;
    }
}
