package views;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import play.libs.Json;

public class User {
    public static ObjectNode render(models.User user) {
      ObjectNode result = Json.newObject();

      result.put("email", user.email);
      result.put("first_name", user.first_name);
      result.put("last_name",  user.last_name);
      if (user.signup_at != null)
        result.put("signup_at",  user.signup_at.toString());
      if (user.birthday != null)
        result.put("birthday",   user.birthday.toString());
      if (user.hasPicture())
        result.put("picture",  user.getAvatarUrl());
      result.put("phone",      user.phone);
      result.put("location",   user.location);
      result.put("about",      user.about);
      return result;
    }

    public static ObjectNode render(List<models.User> users) {
      ObjectNode result       = Json.newObject();
      ArrayNode  result_users = result.putArray("users");

      for (models.User user : users) {
        ObjectNode result_user = Json.newObject();

        result_user.put("email",      user.email);
        result_user.put("first_name", user.first_name);
        result_user.put("last_name",  user.last_name);
        result_user.put("picture",    user.getAvatarUrl());
        result_users.add(result_user);
      }
      return result;
    }
}
