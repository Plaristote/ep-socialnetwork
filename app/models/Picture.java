package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.data.format.*;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.security.MessageDigest;
import java.util.Date;

@Entity
public class Picture extends Model {
  public static Finder<Long,Picture> find = new Finder<Long,Picture>(Long.class, Picture.class);

  @Id
  public Long   id;

  @Constraints.Required
  public Long   user_id;

  public String description;

  public String uri;

  public Picture updateFromJson(JsonNode json) {
    if (json.hasNonNull("description"))
      this.description = json.findValue("description").asText();
    if (json.hasNonNull("uri"))
      this.uri = json.findValue("uri").asText();
    return this;
  }
}

