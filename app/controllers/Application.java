package controllers;

import models.User;
import play.mvc.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Application extends PaginateComponent {
    protected static Long getCurrentUserId() {
        return Long.parseLong(ctx().session().get("user_id"));
    }

    protected static User getCurrentUser() {
        return User.find.byId(getCurrentUserId());
    }
}
