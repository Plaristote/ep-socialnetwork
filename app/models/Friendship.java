package models;

import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints;

@Entity
@Table(name="friendships")
public class Friendship extends Model {
  public static Finder<Long,Friendship> find = new Finder<Long,Friendship>(Long.class, Friendship.class);

  @Id
  public Long id;

  @Constraints.Required
  public Long user_id;

  @Constraints.Required
  public Long friend_id;

  public static Friendship createFriendship(Long user_id, Long friend_id) {
    Friendship friendship = new Friendship();

    friendship.user_id   = user_id;
    friendship.friend_id = friend_id;
    friendship.save();
    return friendship;
  }

  public static void removeFriendship(Long user_id, Long friend_id) {
    Friendship.find.where().
            ieq("user_id", user_id.toString()).
            ieq("friend_id", friend_id.toString()).findUnique().delete();
  }
}
