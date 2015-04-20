package controllers;

import play.mvc.*;
import play.mvc.Http.*;
import play.mvc.Security;

public class Private extends Security.Authenticator {
  @Override
  public String getUsername(Context ctx) {
    return ctx.session().get("user_id");
  }

  @Override
  public Result onUnauthorized(Context ctx) {
    return forbidden();
  }
}
