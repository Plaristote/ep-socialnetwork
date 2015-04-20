package controllers;

import models.User;
import play.mvc.*;

public class Application extends Controller {
    protected static Long getCurrentUserId() {
        return Long.parseLong(ctx().session().get("user_id"));
    }

    protected static User getCurrentUser() {
        return User.find.byId(getCurrentUserId());
    }
}
