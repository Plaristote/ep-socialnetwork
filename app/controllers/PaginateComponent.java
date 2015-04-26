package controllers;

import play.mvc.Controller;

public class PaginateComponent extends Controller {
    protected static int getQueryPage() {
        if (request().queryString().containsKey("Page"))
            return Integer.parseInt(request().getQueryString("Page"));
        return 0;
    }

    protected static int getItemsPerPage(int def) {
        if (request().queryString().containsKey("Limit"))
            return Integer.parseInt(request().getQueryString("Limit"));
        return def;
    }

    protected static boolean mustDisplayTotalResources() {
        if (request().queryString().containsKey("TotalResources"))
            return request().getQueryString("TotalResources").toString() != "0";
        return false;
    }
}
