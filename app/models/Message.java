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
@Table(name="messages")
public class Message extends Model {
    public static Finder<UUID,Message> find = new Finder<UUID,Message>(UUID.class, Message.class);

    public static PagingList<Message> getPaginatedMessagesForUser(UUID userId, int itemsPerPage) {
        return find.where().or(
                com.avaje.ebean.Expr.eq("from_id", userId),
                com.avaje.ebean.Expr.eq("to_id", userId)
        ).order("at DESC").findPagingList(itemsPerPage);
    }

    @Id
    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID    id;

    @Constraints.Required
    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID    from_id;

    @Constraints.Required
    @Column(columnDefinition="uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    public UUID    to_id;

    @Constraints.Required
    @Temporal(TemporalType.TIMESTAMP)
    public Date    at;

    @Constraints.Required
    @Column(columnDefinition="text")
    public String  message;

    public Boolean read;

    public Message updateFromJson(JsonNode json) {
        if (json.hasNonNull("to"))
            this.to_id = UUID.fromString(json.findValue("to").asText());
        if (json.hasNonNull("message"))
            this.message = json.findValue("message").asText();
        if (json.hasNonNull("read"))
            this.read = json.findValue("read").asBoolean();
        return this;
    }
}
