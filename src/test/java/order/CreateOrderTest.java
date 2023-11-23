package order;


import api.model.Order;
import api.model.User;
import api.steps.OrderSteps;
import api.steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.util.ArrayList;
import java.util.List;


public class CreateOrderTest {
    private String name;
    private String email;
    private String password;
    private UserSteps userSteps;
    private User user;
    private String accessToken;
    private OrderSteps orderSteps;
    private List<String> ingredients;
    private Order order;


    @Before
    public void startUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = RandomStringUtils.randomAlphanumeric(3, 20);
        email = RandomStringUtils.randomAlphanumeric(6, 10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphanumeric(12, 20);

        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        user = new User(name, email, password);

        Response resp = userSteps.sendPostRequestApiAuthRegister(user);
        accessToken = JsonPath.from(resp.getBody().asString()).get("accessToken");
        ingredients = new ArrayList<>();
        order = new Order(ingredients);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Создание заказа без авторизации. Проверка что запрос выполнился успешно")
    public void createOrderWithoutAuthorizationTest() {

        String ingredient = orderSteps.getIngredients().extract().jsonPath().get("data[0]._id");
        ingredients.add(ingredient);

        Response response = orderSteps.createOrderWithoutAuthorization(order);
        response.then()
                .log().all()
                .assertThat()
                .body("success", Matchers.is(true))
                .and()
                .body("name", Matchers.notNullValue())
                .and()
                .body("order.number", Matchers.any(Integer.class))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией.")
    @Description("Создание заказа с авторизацией. Проверка что сервер вернул код 200")
    public void createOrderWithAuthorizationTest() {

        String ingredient1 = orderSteps.getIngredients().extract().jsonPath().get("data[0]._id");
        String ingredient2 = orderSteps.getIngredients().extract().jsonPath().get("data[1]._id");
        String ingredient3 = orderSteps.getIngredients().extract().jsonPath().get("data[2]._id");

        int price1 = orderSteps.getIngredients().extract().jsonPath().get("data[0].price");
        int price2 = orderSteps.getIngredients().extract().jsonPath().get("data[1].price");
        int price3 = orderSteps.getIngredients().extract().jsonPath().get("data[2].price");


        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);

        int sumPrice = price1 + price2 + price3;


        Response response = orderSteps.createOrderWithAuthorization(order, accessToken);
        response.then()
                .log().all()
                .assertThat()
                .body("success", Matchers.is(true))
                .and()
                .body("name", Matchers.notNullValue())
                .and()
                .body("order.number", Matchers.any(Integer.class))
                .and()
                .body("order.ingredients", Matchers.notNullValue())
                .and()
                .body("order._id", Matchers.notNullValue())
                .and()
                .body("order.owner.name", Matchers.is(name))
                .and()
                .body("order.owner.email", Matchers.is(email.toLowerCase()))
                .and()
                .body("order.status", Matchers.is("done"))
                .and()
                .body("order.name", Matchers.notNullValue())
                .and()
                .body("order.price", Matchers.is(sumPrice))
                .and()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов и без авторизации")
    @Description("Создание заказа без ингредиентов и без авторизации. Проверка что пришел ответ с кодом 400 от сервера")
    public void createEmptyOrderWithoutAuthorization() {

        Response response = orderSteps.createOrderWithoutAuthorization(order);
        orderSteps.checkFailedResponseApiOrders(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов с авторизацией.")
    @Description("Создание заказа без ингредиентов с авторизацией. Проверка что пришел ответ с кодом 400 от сервера")
    public void createEmptyOrderWithAuthorization() {

        Response response = orderSteps.createOrderWithAuthorization(order, accessToken);
        orderSteps.checkFailedResponseApiOrders(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации с неверным хэшем ингредиентов.")
    @Description("Создание заказа без авторизации с неверным хэшем ингредиентов. Проверка ошибки сервера")
    public void createOrderWithoutAuthorizationWithWrongHashTest() {

        String ingredient1 = orderSteps.getIngredients().extract().jsonPath().get("data[0]._id");
        String ingredient2 = orderSteps.getIngredients().extract().jsonPath().get("data[1]._id");

        ingredients.add(ingredient1 + "135meh246");
        ingredients.add(ingredient2 + "975hem420");

        Response response = orderSteps.createOrderWithoutAuthorization(order);
        response.then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией с неверным хешем ингредиентов.")
    @Description("Создание заказа с авторизацией с неверным хешем ингредиентов. Проверка ошибки сервера")
    public void createOrderWithAuthorizationWithWrongHashTest() {

        String ingredient1 = orderSteps.getIngredients().extract().jsonPath().get("data[0]._id");
        String ingredient2 = orderSteps.getIngredients().extract().jsonPath().get("data[1]._id");

        ingredients.add(ingredient1 + "135meh246");
        ingredients.add(ingredient2 + "975hem420");

        Response response = orderSteps.createOrderWithAuthorization(order, accessToken);
        response.then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @After
    @DisplayName("Удаление пользователя")
    @Description("Удаление пользователя с созданными рандомными данными")
    public void tearDown() {

        UserSteps.deleteUser(user);
    }
}
