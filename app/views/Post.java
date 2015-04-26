package views;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import play.libs.Json;

public class Post {
    public static ObjectNode renderWithDetails(models.Post post) {
      ObjectNode result = render(post);

      result.put("likes", models.Like.getLikeCount(post.id));
      return result;
    }

    public static ObjectNode render(models.Post post) {
        ObjectNode result = Json.newObject();

        result.put("id",           post.id);
        result.put("from",         post.from_id);
        result.put("to",           post.to_id);
        result.put("description",  post.description);
        result.put("enable",       post.enable);
        result.put("highlight",    post.highlight);
        result.put("at",           post.created_at.toString());
        if (post.hasUrl())
          result.put("url",        post.url);
        if (post.hasPicture())
          result.put("picture_id", post.picture_id);
        return result;
    }

    public static ObjectNode render(List<models.Post> posts) {
        ObjectNode result       = Json.newObject();
        ArrayNode  result_posts = result.putArray("posts");

        for (models.Post post : posts)
          result_posts.add(render(post));
        return result;
    }
}
