package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="likes")
public class Like extends Model {
    public static Model.Finder<UUID,Like> find = new Model.Finder<UUID,Like>(UUID.class, Like.class);

    @Id
    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID id;

    @Constraints.Required
    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID user_id;

    @Constraints.Required
    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID post_id;

    public static long getLikeCount(UUID post_id) {
      return find.where().eq("post_id", post_id).findRowCount();
    }

    public static UUID addLike(UUID user_id, UUID post_id) {
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

    public static void removeLike(UUID user_id, UUID post_id) {
      List<Like> list = find.where().eq("user_id", post_id).eq("post_id", post_id).findList();

      for (Like like : list)
        like.delete();
    }
}
