package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.data.format.*;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.MessageDigest;
import java.util.Date;

@Entity
public class User extends Model {
  public static Finder<Long,User> find = new Finder<Long,User>(Long.class, User.class);

  @Id
  public Long   id;

  @Constraints.Required
  @Constraints.Email
  @Column(unique=true)
  public String email;

  @Constraints.Required
  public String first_name;

  @Constraints.Required
  public String last_name;

  @Constraints.Required
  public String password;

  @Formats.DateTime(pattern="dd/MM/yyyy")
  public Date   signup_at;

  @Formats.DateTime(pattern="dd/MM/yyyy")
  public Date   birthday;

  public Long   picture_id = new Long(0);

  public String phone;

  public String location;

  public String about;

  // Relationships
  private Picture preloadedPicture = null;

  public boolean hasPicture() {
    return picture_id != 0;
  }

  public Picture getPicture() {
    if (hasPicture() && preloadedPicture == null)
      preloadedPicture = Picture.find.byId(picture_id);
    return preloadedPicture;
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
      this.picture_id = json.findValue("picture_id").asLong();
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
