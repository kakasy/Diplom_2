package api.steps;

import api.model.Order;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;



import static io.restassured.RestAssured.given;

public class OrderSteps extends BaseClient {

    @Step("Получить ингредиенты. GET-запрос на эндпоинт /api/ingredients")
    public ValidatableResponse getIngredients() {

        return given()
                .spec(getSpec())
                .when()
                .get(API_INGREDIENTS)
                .then();

    }

    @Step("Создать заказ с авторизацией. POST-запрос на эндпоинт /api/orders")
    public Response createOrderWithAuthorization(Order order, String token) {

        return given()
                .log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .header("authorization", token)
                .body(order)
                .when()
                .post(API_ORDERS);
    }

    @Step("Создать заказ без авторизации. POST-запрос на эндпоинт /api/orders")
    public  Response createOrderWithoutAuthorization(Order order) {

        return given()
                .log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(API_ORDERS);
    }

    @Step("Сервер отвечает на создание заказа без ингредиентов кодом 400")
    public void checkFailedResponseApiOrders(Response response) {

        response.then()
                .log().all()
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("Ingredient ids must be provided"))
                .and()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

}
