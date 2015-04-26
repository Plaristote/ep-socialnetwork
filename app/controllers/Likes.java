package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Like;
import play.libs.Json;
import play.mvc.*;
import java.util.List;

public class Likes extends Application {
    @Security.Authenticated(Private.class)
    public static Result create(Long postId) {
      ObjectNode result = Json.newObject();

      enableCors();
      Like.addLike(getCurrentUserId(), postId);
      result.put("message", "like added");
      return (ok(result));
    }

    @Security.Authenticated(Private.class)
    public static Result destroy(Long postId) {
      ObjectNode result = Json.newObject();

      enableCors();
      Like.removeLike(getCurrentUserId(), postId);
      result.put("message", "like deleted");
      return (ok(result));
    }
}
