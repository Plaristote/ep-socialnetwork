package controllers;

import com.avaje.ebean.PagingList;
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

      enableCors();
      picture.user_id = getCurrentUserId();
      return updatePicture(picture);
    }

    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {
      JsonNode json    = request().body().asJson();
      Picture  picture = Picture.find.byId(id);

      enableCors();
      if (picture.user_id != getCurrentUserId())
        return forbidden();
      return updatePicture(picture);
    }

    public static Result indexForUser(Long user_id) {
      PagingList<Picture> pictures = Picture.getPaginatedPicturesForUser(user_id, getItemsPerPage(30));
      ObjectNode result = views.Picture.render(pictures.getAsList());

      result.put("page", getQueryPage());
      if (mustDisplayTotalResources())
        result.put("page_count", pictures.getTotalPageCount());
      return ok(result);
    }

    @Security.Authenticated(Private.class)
    public static Result index() {
        return indexForUser(getCurrentUserId());
    }

    public static Result show(Long id) {
        Picture picture = Picture.find.byId(id);

        return ok(views.Picture.render(picture));
    }
}
