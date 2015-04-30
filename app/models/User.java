package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.data.format.*;
import play.db.ebean.Model;

import javax.persistence.*;
import java.security.MessageDigest;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="users")
public class User extends Model {
  public static Finder<UUID,User> find = new Finder<UUID,User>(UUID.class, User.class);

  @Id
  @Column(columnDefinition="uuid")
  @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
  public UUID   id;

  @Constraints.Required
  @Constraints.Email
  @Column(unique=true)
  public String email;

  @Constraints.Required
  @Column(columnDefinition="text")
  public String first_name;

  @Constraints.Required
  @Column(columnDefinition="text")
  public String last_name;

  @Constraints.Required
  public String password;

  @Temporal(TemporalType.TIMESTAMP)
  public Date   signup_at;

  @Formats.DateTime(pattern="dd/MM/yyyy")
  public Date   birthday;

  @Column(columnDefinition="uuid")
  @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
  public UUID   picture_id;

  @Column(columnDefinition="text")
  public String phone;

  @Column(columnDefinition="text")
  public String location;

  @Column(columnDefinition="text")
  public String about;

  // Relationships
  private Picture preloadedPicture = null;

  public boolean hasPicture() {
    return picture_id != null;
  }

  public Picture getPicture() {
    if (hasPicture() && preloadedPicture == null)
      preloadedPicture = Picture.find.byId(picture_id);
    return preloadedPicture;
  }

  private String gravatarUrl() {
    return "http://www.gravatar.com/avatar/" + assets.MD5Util.md5Hex(email.toLowerCase().trim());
  }

  public String getAvatarUrl() {
    if (hasPicture())
      return getPicture().uri;
    return gravatarUrl();
  }

  // JSON
  public User updateFromJson(JsonNode json) {
    if (json.hasNonNull("email"))
      this.email      = json.findValue("email").asText();
    if (json.hasNonNull("first_name"))
      this.first_name = json.findValue("first_name").asText();
    if (json.hasNonNull("last_name"))
      this.last_name  = json.findValue("last_name").asText();
    if (json.hasNonNull("picture_id"))
      this.picture_id = UUID.fromString(json.findValue("picture_id").asText());
    if (json.hasNonNull("phone"))
      this.phone      = json.findValue("phone").asText();
    if (json.hasNonNull("location"))
      this.location   = json.findValue("location").asText();
    if (json.hasNonNull("about"))
      this.about      = json.findValue("about").asText();
    if (json.hasNonNull("password"))
      this.password   = encryptPassword(json.findValue("password").asText());
    return this;
  }

  public static String encryptPassword(String password) {
    try  {
      return new String(MessageDigest.getInstance("MD5").digest(password.getBytes()));
    }
    catch (java.security.NoSuchAlgorithmException e) {
      return password;
    }
  }
}
