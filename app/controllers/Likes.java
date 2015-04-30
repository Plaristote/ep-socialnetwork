package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Like;
import play.libs.Json;
import play.mvc.*;
import java.util.List;
import java.util.UUID;

public class Likes extends Application {
    @Security.Authenticated(Private.class)
    public static Result create(String postId) {
      ObjectNode result = Json.newObject();

      enableCors();
      Like.addLike(getCurrentUserId(), UUID.fromString(postId));
      result.put("message", "like added");
      return (ok(result));
    }

    @Security.Authenticated(Private.class)
    public static Result destroy(String postId) {
      ObjectNode result = Json.newObject();

      enableCors();
      Like.removeLike(getCurrentUserId(), UUID.fromString(postId));
      result.put("message", "like deleted");
      return (ok(result));
    }
}
