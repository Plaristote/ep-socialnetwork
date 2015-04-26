package controllers;

import com.avaje.ebean.PagingList;
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

      enableCors();
      message.from_id = getCurrentUserId();
      message.to_id   = json.findValue("to").asLong();
      message.message = json.findValue("message").asText();
      message.at      = new java.util.Date();
      message.read    = false;
      message.save();
      return ok(views.Message.render(message));
    }

    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {
      ObjectNode result  = Json.newObject();
      Message    message = Message.find.byId(id);
      JsonNode   json    = request().body().asJson();

      enableCors();
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
      PagingList<Message> messages = Message.getPaginatedMessagesForUser(getCurrentUserId(),
              getItemsPerPage(30));
      ObjectNode result = views.Message.render(messages.getAsList());

      result.put("page", getQueryPage());
      if (mustDisplayTotalResources())
        result.put("page_count", messages.getTotalPageCount());
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result show(Long id) {
      Message message = Message.find.byId(id);

      if (message.from_id != getCurrentUserId() && message.to_id != getCurrentUserId())
        return forbidden();
      return ok(views.Message.render(message));
    }
}
