package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="likes")
public class Like extends Model {
    public static Model.Finder<Long,Like> find = new Model.Finder<Long,Like>(Long.class, Like.class);

    @Id
    public Long id;

    @Constraints.Required
    public Long user_id;

    @Constraints.Required
    public Long post_id;

    public static long getLikeCount(Long post_id) {
      return find.where().eq("post_id", post_id).findRowCount();
    }

    public static long addLike(Long user_id, Long post_id) {
      List<Like> list = find.where().eq("user_id", post_id).eq("post_id", post_id).findList();
      Like       like;

      if (list.size() == 0) {
        like = new Like();
        like.user_id = user_id;
        like.post_id = post_id;
        like.save();
      }
      else
        like = list.get(0);
      return like.id;
    }

    public static void removeLike(Long user_id, Long post_id) {
      List<Like> list = find.where().eq("user_id", post_id).eq("post_id", post_id).findList();

      for (Like like : list)
        like.delete();
    }
}
