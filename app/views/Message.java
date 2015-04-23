package views;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import play.libs.Json;

public class Message {
    public static ObjectNode render(models.Message message) {
      ObjectNode result = Json.newObject();

      result.put("id",      message.id);
      result.put("from_id", message.from_id);
      result.put("to_id",   message.to_id);
      result.put("message", message.message);
      result.put("at",      message.at.toString());
      result.put("read",    message.read);
      return result;
    }

    public static ObjectNode render(List<models.Message> messages) {
      ObjectNode result        = Json.newObject();
      ArrayNode   messageArray = result.putArray("messages");

      for (models.Message message : messages)
        messageArray.add(render(message));
      return result;
    }
}
