package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.libs.Json;
import play.mvc.*;

public class Session extends Application {
    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
      ObjectNode result = Json.newObject();
      JsonNode json     = request().body().asJson();
      String   username = json.findValue("email").asText();
      String   password = User.encryptPassword(json.findValue("password").asText());
      User     user     = User.find.where()
                                     .eq("email", username)
                                     .eq("password", password).findUnique();

      enableCors();
      if (user == null)
        return unauthorized();
      session().put("user_id", user.id.toString());
      result.put("id", user.id.toString());
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result destroy() {
      ObjectNode result = Json.newObject();

      enableCors();
      ctx().session().remove("user_id");
      result.put("disconnected", true);
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result get() {
      return controllers.Users.show(getCurrentUserId().toString());
    }
}
