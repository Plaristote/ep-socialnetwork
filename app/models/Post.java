package models;

import com.avaje.ebean.PagingList;
import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.data.format.*;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="posts")
public class Post extends Model {
    public static Finder<Long,Post> find = new Finder<Long,Post>(Long.class, Post.class);

    public static PagingList<Post> getPaginatedPostsForUser(Long userId, int itemsPerPage) {
        return find.where().or(
                com.avaje.ebean.Expr.eq("from_id", userId),
                com.avaje.ebean.Expr.eq("to_id",   userId)
        ).findPagingList(itemsPerPage);
    }

    @Id
    public Long    id;

    @Constraints.Required
    public Long    from_id;

    @Constraints.Required
    public Long    to_id;

    @Constraints.Required
    public Date    created_at;

    @Constraints.Required
    public String  description;

    public String  url;

    public Long    picture_id;

    public boolean enable;

    public boolean highlight;

    public boolean hasUrl() {
      return url != null && url != "";
    }

    public boolean hasPicture() {
      return picture_id != null && picture_id != 0;
    }

    private Picture preloadedPicture = null;

    public Picture getPicture() {
      if (hasPicture() && preloadedPicture == null)
        preloadedPicture = Picture.find.byId(picture_id);
      return preloadedPicture;
    }

}
