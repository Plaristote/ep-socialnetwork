package controllers;

import models.User;
import play.mvc.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

public class Application extends PaginateComponent {
  protected static UUID getCurrentUserId() {
    return UUID.fromString(ctx().session().get("user_id"));
  }

  protected static User getCurrentUser() {
    return User.find.byId(getCurrentUserId());
  }

  public static Result preflightCheckWithParam(String p) {
    return  preflightCheck();
  }

  public static Result preflightCheck() {
    enableCors();
    return ok();
  }

  protected static void enableCors() {
    response().setHeader("Access-Control-Allow-Origin", allowedOrigins());
    response().setHeader("Access-Control-Allow-Methods", "POST, PUT, DELETE, GET, OPTIONS");
    response().setHeader("Access-Control-Allow-Headers", "accept, origin, Content-type");
    response().setHeader("Access-Control-Allow-Credentials", "true");
  }

  private static String allowedOrigins() {
    if (request().hasHeader("Origin"))
      return request().getHeader("Origin").toString();
    return ("*");
  }
}
