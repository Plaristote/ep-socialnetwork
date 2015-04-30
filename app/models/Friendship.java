package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Constraints;
import java.util.UUID;

@Entity
@Table(name="friends")
public class Friendship extends Model {
  public static Finder<UUID,Friendship> find = new Finder<UUID,Friendship>(UUID.class, Friendship.class);

  @Constraints.Required
  @Column(columnDefinition="uuid",name="user_1")
  @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
  public UUID user_id;

  @Constraints.Required
  @Column(columnDefinition="uuid",name="user_2")
  @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
  public UUID friend_id;

  public static Friendship createFriendship(UUID user_id, UUID friend_id) {
    Friendship friendship = new Friendship();

    friendship.user_id   = user_id;
    friendship.friend_id = friend_id;
    friendship.save();
    return friendship;
  }

  public static void removeFriendship(UUID user_id, UUID friend_id) {
    Friendship.find.where().
            eq("user_id", user_id).
            eq("friend_id", friend_id).findUnique().delete();
  }
}
