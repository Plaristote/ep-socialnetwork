package controllers;

import com.avaje.ebean.Page;
import com.avaje.ebean.PagingList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.User;
import play.libs.Json;
import play.mvc.*;

import javax.persistence.PersistenceException;
import java.util.UUID;

public class Users extends Application {
    private static Result updateUser(User user) {
        JsonNode   json   = request().body().asJson();
        ObjectNode result = Json.newObject();

        if (json == null) {
          result.put("error", "Excpecting JSON data");
          return badRequest(result);
        }
        if (User.find.where().eq("email", json.get("email").asText()).findRowCount() > 0) {
          result.put("error", "This email is already registered");
          return badRequest(result);
        } else {
          user.updateFromJson(json).save();
          result.put("id", user.id.toString());
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
        User     user = new User();

        enableCors();
        user.signup_at = new java.util.Date();
        return updateUser(user);
    }

    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update() {
        User     user = getCurrentUser();

        enableCors();
        return updateUser(user);
    }

    public static Result show(String _id) {
        UUID id   = UUID.fromString(_id);
        User user = User.find.byId(id);

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

    @Security.Authenticated(Private.class)
    public static Result destroy() {
      enableCors();
      getCurrentUser().delete();
      return controllers.Session.destroy();
    }
}
