package controllers;

import models.User;
import play.mvc.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Application extends PaginateComponent {
  protected static Long getCurrentUserId() {
    return Long.parseLong(ctx().session().get("user_id"));
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
    response().setHeader("Access-Control-Allow-Origin", request().getHeader("Origin").toString());
    response().setHeader("Access-Control-Allow-Methods", "POST, PUT, DELETE");
    response().setHeader("Access-Control-Allow-Headers", "accept, origin, Content-type");
    response().setHeader("Access-Control-Allow-Credentials", "true");
  }
}
