package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Friendship;
import play.libs.Json;
import play.mvc.*;
import java.util.List;
import java.util.UUID;

public class Friends extends Application {
    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result addFriend() {
      ObjectNode result     = Json.newObject();
      JsonNode   json       = request().body().asJson();
      UUID       friendId   = UUID.fromString(json.findValue("friend").asText());
      Friendship friendship = Friendship.find.where()
                                             .eq("user_id",   getCurrentUserId())
                                             .eq("friend_id", friendId).findUnique();

      enableCors();
      if (friendship != null)
        return forbidden();
      Friendship.createFriendship(getCurrentUserId(), friendId);
      result.put("message", "friend added");
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result index() {
        return indexForUser(getCurrentUserId().toString());
    }

    public static Result indexForUser(String user_id) {
        ObjectNode       result         = Json.newObject();
        ArrayNode        result_friends = result.putArray("friends");
        List<Friendship> friendships    = Friendship.find.where().eq("user_id", UUID.fromString(user_id)).findList();

        enableCors();
        for (Friendship friendship : friendships)
          result_friends.add(friendship.friend_id.toString());
        return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result removeFriend(String friendId) {
        ObjectNode result  = Json.newObject();

        enableCors();
        Friendship.removeFriendship(getCurrentUserId(), UUID.fromString(friendId));
        result.put("message", "friend removed");
        return ok(result);
    }
}
