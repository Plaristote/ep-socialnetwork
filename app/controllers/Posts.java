package controllers;

import com.avaje.ebean.PagingList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import models.Post;
import play.mvc.*;
import java.util.List;

public class Posts extends Application {
    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result create() {
        JsonNode   json   = request().body().asJson();
        Post       post   = new Post();

        enableCors();
        post.from_id      = getCurrentUserId();
        post.to_id        = json.findValue("to").asLong();
        if (json.has("picture_id"))
          post.picture_id = json.findValue("picture_id").asLong();
        if (json.has("url"))
          post.url        = json.findValue("url").asText();
        post.description  = json.findValue("description").asText();
        post.created_at   = new java.util.Date();
        post.enable       = true;
        post.save();
        return ok(views.Post.render(post));
    }

    @Security.Authenticated(Private.class)
    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long postId) {
        ObjectNode result  = Json.newObject();
        JsonNode   json    = request().body().asJson();
        Post       post    = Post.find.byId(postId);

        enableCors();
        if (post.from_id != getCurrentUserId())
          return forbidden();
        if (json.has("enable"))
          post.enable = json.findValue("enable").asBoolean();
        if (json.has("highlight"))
          post.highlight = json.findValue("highlight").asBoolean();
        post.save();
        return ok(views.Post.render(post));
    }

    @Security.Authenticated(Private.class)
    public static Result index() {
        return indexForUser(getCurrentUserId());
    }

    public static Result indexForUser(Long userId) {
        PagingList<Post> posts  = Post.getPaginatedPostsForUser(userId,
                                                                getItemsPerPage(30));
        ObjectNode       result = views.Post.render(posts.getPage(getQueryPage()).getList());

        result.put("page", getQueryPage());
        if (mustDisplayTotalResources())
          result.put("page_count", posts.getTotalPageCount());
        return ok(result);
    }

    public static Result show(Long postId) {
        Post post = Post.find.byId(postId);

        return ok(views.Post.renderWithDetails(post));
    }
}
