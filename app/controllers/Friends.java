package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Friendship;
import play.libs.Json;
import play.mvc.*;
import java.util.List;

public class Friends extends Application {
    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result add_friend() {
      ObjectNode result     = Json.newObject();
      JsonNode   json       = request().body().asJson();
      Long       friendId   = json.findValue("friend").asLong();
      Friendship friendship = Friendship.find.where()
                                             .ieq("user_id",   getCurrentUserId().toString())
                                             .ieq("friend_id", friendId.toString()).findUnique();

      if (friendship != null)
        return forbidden();
      Friendship.createFriendship(getCurrentUserId(), friendId);
      result.put("message", "friend added");
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result index() {
        return index_of_user(getCurrentUserId());
    }

    public static Result index_of_user(Long user_id) {
        ObjectNode       result         = Json.newObject();
        ArrayNode        result_friends = result.putArray("friends");
        List<Friendship> friendships    = Friendship.find.where().ieq("user_id", getCurrentUserId().toString()).findList();

        for (Friendship friendship : friendships)
          result_friends.add(friendship.friend_id);
        return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result remove_friend(Long friendId) {
        ObjectNode result  = Json.newObject();

        Friendship.removeFriendship(getCurrentUserId(), friendId);
        result.put("message", "friend removed");
        return ok(result);
    }
}
