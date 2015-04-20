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
        ObjectNode result = Json.newObject();

        result.put("email", user.email);
        result.put("first_name", user.first_name);
        result.put("last_name",  user.last_name);
        if (user.signup_at != null)
          result.put("signup_at",  user.signup_at.toString());
        if (user.birthday != null)
          result.put("birthday",   user.birthday.toString());
        if (user.hasPicture())
          result.put("picture",  user.getPicture().uri);
        result.put("phone",      user.phone);
        result.put("location",   user.location);
        result.put("about",      user.about);
        return ok(result);
    }

    public static Result index(Integer page) {
      PagingList<User> users = User.find.where().findPagingList(30);
      ObjectNode result = Json.newObject();
      ArrayNode result_users = result.putArray("users");

      result.put("page", page);
      result.put("page_count", users.getTotalPageCount());
      for (User user : users.getPage(page).getList()) {
        ObjectNode result_user = Json.newObject();

        result_user.put("email",      user.email);
        result_user.put("first_name", user.first_name);
        result_user.put("last_name",  user.last_name);
        result_user.put("picture_id", user.picture_id);
        result_users.add(result_user);
      }
      return ok(result);
    }
}
