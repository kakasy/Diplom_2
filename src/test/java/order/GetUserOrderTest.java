package order;

import api.model.User;
import api.steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;


public class GetUserOrderTest {
    private String email;
    private String password;
    private String name;
    private UserSteps userSteps;
    private User user;
    private String accessToken;

    @Before
    public void startUp() {

        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = RandomStringUtils.randomAlphanumeric(3, 20);
        email = RandomStringUtils.randomAlphanumeric(6, 10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphanumeric(12, 20);

        userSteps = new UserSteps();
        user = new User(name, email, password);

        Response resp = userSteps.sendPostRequestApiAuthRegister(user);
        accessToken = JsonPath.from(resp.getBody().asString()).get("accessToken");

    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя.")
    @Description("Получение списка заказов авторизованного пользователя. Сервер ответил с кодом 200")
    public void getUserOrderWithAuthorizationTest() {

        Response response = given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("authorization", accessToken)
                .when()
                .get("/api/orders");

        response.then()
                .assertThat()
                .body("success", Matchers.is(true))
                .and()
                .body("orders", Matchers.notNullValue())
                .and()
                .body("total", Matchers.any(Integer.class))
                .and()
                .body("totalToday", Matchers.any(Integer.class))
                .and().statusCode(HttpStatus.SC_OK);
    }


    @Test
    @DisplayName("Получение списка заказов без авторизации.")
    @Description("Получение списка заказов без авторизации. Сервер ответил с кодом 401")
    public void getUserOrderWithoutAuthorizationTest() {

        Response response = given()
                .log().all()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/orders");

        response.then()
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("You should be authorised"))
                .and()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @After
    @DisplayName("Удаление пользователя")
    @Description("Удаление пользователя с созданными рандомными данными")
    public void tearDown() {
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(user)
                .delete("/api/auth/user");
    }
}
