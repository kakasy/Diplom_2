package api.steps;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseClient {
    protected static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    protected static final String API_ORDERS = "/api/orders";
    protected static final String API_INGREDIENTS = "/api/ingredients";
    protected static final String API_AUTH_REGISTER = "/api/auth/register";
    protected static final String API_AUTH_USER = "/api/auth/user";
    protected static final String API_AUTH_LOGIN = "/api/auth/login";
    protected static final String DELETE_USER = "api/auth/user";

    public static RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .log(LogDetail.ALL)
                .build();
    }
}
