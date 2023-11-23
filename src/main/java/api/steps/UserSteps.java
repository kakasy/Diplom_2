package api.steps;

import api.model.User;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;
public class UserSteps extends BaseClient {

    private final static String ERROR_MESSAGE_REGISTER = "Email, password and name are required fields";
    private final static String ERROR_MESSAGE_LOGIN = "email or password are incorrect";
    private final static String ERROR_MESSAGE_USER = "You should be authorised";


    @Step("Регистрация пользователя. POST-запрос на эндпоинт /api/auth/register")
    public Response sendPostRequestApiAuthRegister(User user) {

        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(API_AUTH_REGISTER);
    }

    @Step("Ответ сервера на попытку регистрации пользователя кодом 403")
    public void checkFailedResponseApiAuthRegister(Response response) {
        response.then()
                .log().all()
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is(ERROR_MESSAGE_REGISTER))
                .and()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }


    @Step("Авторизация пользователя. POST-запрос на эндпоинт /api/auth/login")
    public Response sendPostRequestApiAuthLogin(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(API_AUTH_LOGIN);
    }

    @Step("Ответ сервера на попытку авторизации пользователя кодом 401")
    public void checkFailedResponseApiAuthLogin(Response response) {
        response.then()
                .log().all()
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is(ERROR_MESSAGE_LOGIN))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Step("Изменение данных авторизованного пользователя. PATCH-запрос на эндпоинт /api/auth/user")
    public Response sendPatchRequestWithAuthorizationApiAuthUser(User user, String token) {
        return given()
                .spec(getSpec())
                .header("authorization", token)
                .body(user)
                .when()
                .patch(API_AUTH_USER);
    }

    @Step("Изменение данных не авторизованного пользователя. PATCH-запрос на эндпоинт /api/auth/user")
    public Response sendPatchRequestWithoutAuthorizationApiAuthUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(API_AUTH_USER);
    }

    @Step("Ответ сервера на изменение данных пользователя кодом 200")
    public void checkSuccessResponseApiAuthUser(Response response, String email, String name) {
        response.then()
                .log().all()
                .assertThat()
                .body("success", Matchers.is(true))
                .and()
                .body("user.email", Matchers.is(email.toLowerCase()))
                .and()
                .body("user.name", Matchers.is(name))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }

    @Step("Ответ сервера кодом 401 на попытку изменения данных без авторизации")
    public void checkFailedResponseApiAuthUser(Response response) {
        response.then()
                .log().all()
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is(ERROR_MESSAGE_USER))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .delete(DELETE_USER);
    }
}
