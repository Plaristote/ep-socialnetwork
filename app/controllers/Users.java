package controllers;

import com.avaje.ebean.Page;
import com.avaje.ebean.PagingList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.User;
import play.libs.Json;
import play.mvc.*;

public class Users extends Application {
    private static Result updateUser(User user) {
        JsonNode   json   = request().body().asJson();
        ObjectNode result = Json.newObject();

        if (json == null)
            return badRequest("Expecting JSON data");
        user.updateFromJson(json).save();
        result.put("id", user.id);
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
        User     user = new User();

        user.signup_at = new java.util.Date();
        return updateUser(user);
    }

    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update() {
        User     user = getCurrentUser();

        return updateUser(user);
    }

    public static Result show(Long id) {
        User       user   = User.find.byId(id);

        return ok(views.User.render(user));
    }

    public static Result index() {
      PagingList<User> users        = User.find.where().findPagingList(getItemsPerPage(30));
      ObjectNode       result       = views.User.render(users.getPage(getQueryPage()).getList());

      result.put("page", getQueryPage());
      if (mustDisplayTotalResources())
        result.put("page_count", users.getTotalPageCount());
      return ok(result);
    }
}
