package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.data.format.*;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name="messages")
public class Message extends Model {
    public static Finder<Long,Message> find = new Finder<Long,Message>(Long.class, Message.class);

    @Id
    public Long    id;

    @Constraints.Required
    public Long    from_id;

    @Constraints.Required
    public Long    to_id;

    @Constraints.Required
    public Date    at;

    @Constraints.Required
    public String  message;

    public Boolean read;

    public Message updateFromJson(JsonNode json) {
        if (json.hasNonNull("to"))
            this.to_id = json.findValue("to").asLong();
        if (json.hasNonNull("message"))
            this.message = json.findValue("message").asText();
        if (json.hasNonNull("read"))
            this.read = json.findValue("read").asBoolean();
        return this;
    }
}
