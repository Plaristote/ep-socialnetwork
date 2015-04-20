package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.*;
import models.Picture;
import java.util.List;

public class Pictures extends Application {
    private static Result updatePicture(Picture picture) {
        JsonNode   json   = request().body().asJson();
        ObjectNode result = Json.newObject();

        if (json == null)
            return badRequest("Expecting JSON data");
        picture.updateFromJson(json).save();
        result.put("id", picture.id);
        return ok(result);
    }

    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
      Picture picture = new Picture();

      picture.user_id = getCurrentUserId();
      return updatePicture(picture);
    }

    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {
        JsonNode json    = request().body().asJson();
        Picture  picture = Picture.find.byId(id);

        if (picture.user_id != getCurrentUserId())
            return forbidden();
        return updatePicture(picture);
    }

    public static Result indexForUser(Long user_id) {
      ObjectNode    result   = Json.newObject();
      List<Picture> pictures = Picture.find.where()
                                           .ieq("user_id", user_id.toString()).findList();
      ArrayNode     resultPictures = result.putArray("pictures");

      for (Picture picture : pictures)
        resultPictures.add(renderPicture(picture));
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result index() {
        return indexForUser(getCurrentUserId());
    }

    public static Result show(Long id) {
        Picture picture = Picture.find.byId(id);

        return ok(renderPicture(picture));
    }

    private static ObjectNode renderPicture(Picture picture) {
        ObjectNode result = Json.newObject();

        result.put("id",          picture.id);
        result.put("user_id",     picture.user_id);
        result.put("description", picture.description);
        result.put("uri",         picture.uri);
        return result;
    }
}
