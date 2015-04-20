package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import models.Message;
import play.mvc.*;
import java.util.List;

public class Messages extends Application {
    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
      ObjectNode result  = Json.newObject();
      Message    message = new Message();
      JsonNode   json    = request().body().asJson();

      message.from_id = getCurrentUserId();
      message.to_id   = json.findValue("to").asLong();
      message.message = json.findValue("message").asText();
      message.at      = new java.util.Date();
      message.read    = false;
      message.save();
      result.put("id", message.id);
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {
      ObjectNode result  = Json.newObject();
      Message    message = Message.find.byId(id);
      JsonNode   json    = request().body().asJson();

      if (message.to_id != getCurrentUserId())
        return forbidden();
      if (json.has("read"))
      {
        message.read = json.findValue("read").asBoolean();
        message.save();
      }
      result.put("id", message.id);
      result.put("read", message.read);
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result index() {
      ObjectNode    result       = Json.newObject();
      ArrayNode     messageArray = result.putArray("messages");
      //List<Message> messages     = Message.find.where().or(
      //            com.avaje.ebean.Expr.eq("from_id", getCurrentUserId()),
      //            com.avaje.ebean.Expr.eq("to_id", getCurrentUserId())
      //         ).findList();
      List<Message> messages = Message.find.where().ieq("to_id", getCurrentUserId().toString()).findList();

      for (Message message : messages)
        messageArray.add(renderMessage(message));
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result show(Long id) {
      Message message = Message.find.byId(id);

      if (message.from_id != getCurrentUserId() && message.to_id != getCurrentUserId())
        return forbidden();
      return ok(renderMessage(message));
    }

    private static ObjectNode renderMessage(Message message) {
        ObjectNode result = Json.newObject();

        result.put("id",      message.id);
        result.put("from_id", message.from_id);
        result.put("to_id",   message.to_id);
        result.put("message", message.message);
        result.put("at",      message.at.toString());
        return result;
    }
}
