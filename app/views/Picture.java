package views;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import play.libs.Json;

public class Picture {
    public static ObjectNode render(models.Picture picture) {
      ObjectNode result = Json.newObject();

      result.put("id",          picture.id.toString());
      result.put("user_id",     picture.user_id.toString());
      result.put("description", picture.description);
      result.put("uri",         picture.uri);
      return result;
    }

    public static ObjectNode render(List<models.Picture> pictures) {
      ObjectNode result = Json.newObject();
      ArrayNode  array  = result.putArray("pictures");

      for (models.Picture picture : pictures)
        array.add(render(picture));
      return result;
    }
}
