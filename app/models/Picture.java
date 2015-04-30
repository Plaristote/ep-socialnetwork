package models;

import com.avaje.ebean.PagingList;
import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.data.format.*;
import play.db.ebean.Model;

import javax.persistence.*;
import java.security.MessageDigest;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="pictures")
public class Picture extends Model {
  public static Finder<UUID,Picture> find = new Finder<UUID,Picture>(UUID.class, Picture.class);

  public static PagingList<Picture> getPaginatedPicturesForUser(UUID userId, int itemsPerPage) {
    return find.where().eq("user_id", userId).findPagingList(itemsPerPage);
  }

  @Id
  @Column(columnDefinition="uuid")
  @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
  public UUID   id;

  @Constraints.Required
  @Column(columnDefinition="uuid")
  @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
  public UUID   user_id;

  @Column(columnDefinition="text")
  public String description;

  @Temporal(TemporalType.TIMESTAMP)
  public java.util.Date at;

  public String uri;

  public Picture updateFromJson(JsonNode json) {
    if (json.hasNonNull("description"))
      this.description = json.findValue("description").asText();
    if (json.hasNonNull("uri"))
      this.uri = json.findValue("uri").asText();
    return this;
  }
}

