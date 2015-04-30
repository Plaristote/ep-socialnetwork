package models;

import com.avaje.ebean.PagingList;
import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.data.format.*;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="posts")
public class Post extends Model {
    public static Finder<UUID,Post> find = new Finder<UUID,Post>(UUID.class, Post.class);

    public static PagingList<Post> getPaginatedPostsForUser(UUID userId, int itemsPerPage) {
        return find.where().or(
                com.avaje.ebean.Expr.eq("from_id", userId),
                com.avaje.ebean.Expr.eq("to_id", userId)
        ).order("created_at DESC").findPagingList(itemsPerPage);
    }

    @Id
    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID id;

    @Constraints.Required
    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID    from_id;

    @Constraints.Required
    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID    to_id;

    @Constraints.Required
    @Column(name="at")
    @Temporal(TemporalType.TIMESTAMP)
    public Date    created_at;

    @Constraints.Required
    @Column(columnDefinition="text")
    public String  description;

    @Column(columnDefinition="text")
    public String  url;

    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID    picture_id;

    public boolean enable;

    public boolean highlight;

    public boolean hasUrl() {
      return url != null && url != "";
    }

    public boolean hasPicture() {
      return picture_id != null;
    }

    private Picture preloadedPicture = null;

    public Picture getPicture() {
      if (hasPicture() && preloadedPicture == null)
        preloadedPicture = Picture.find.byId(picture_id);
      return preloadedPicture;
    }

}
