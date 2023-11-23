package user;

import api.model.User;
import api.steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginUserTest {
    private String email;
    private String password;
    private String name;
    private UserSteps userSteps;
    private User user;
    private User authUser;

    @Before
    public void startUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        name = RandomStringUtils.randomAlphanumeric(3, 20);
        email = RandomStringUtils.randomAlphanumeric(6, 10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphanumeric(12, 20);
        userSteps = new UserSteps();
        user = new User(name, email, password);
        authUser = new User();
        userSteps.sendPostRequestApiAuthRegister(user);

    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя с зарегистрированными случайными данными. Сервер отвечает с кодом 200")
    public void authorizationTest() {

        Response response = userSteps.sendPostRequestApiAuthLogin(user);
        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", Matchers.is(true))
                .and()
                .body("accessToken", Matchers.notNullValue())
                .and()
                .body("refreshToken", Matchers.notNullValue())
                .and()
                .body("user.email", Matchers.is(user.getEmail().toLowerCase()))
                .and()
                .body("user.name", Matchers.is(name));
    }

    @Test
    @DisplayName("Авторизация пользователя без email")
    @Description("Авторизация пользователя без email. Сервер отвечает с кодом 403")
    public void authorizationWithoutEmailTest() {

        authUser.setPassword(password);
        Response response = userSteps.sendPostRequestApiAuthLogin(authUser);
        userSteps.checkFailedResponseApiAuthLogin(response);

    }

    @Test
    @DisplayName("Авторизация пользователя без пароля.")
    @Description("Авторизация пользователя без пароля. Проверка неуспешного ответа сервера.")
    public void authorizationWithoutPasswordTest() {

        authUser.setEmail(email);
        Response response = userSteps.sendPostRequestApiAuthLogin(authUser);
        userSteps.checkFailedResponseApiAuthLogin(response);
    }

    @Test
    @DisplayName("Авторизация пользователя без email и пароля.")
    @Description("Авторизация пользователя без email и пароля. Сервер отвечает с кодом 403")
    public void authorizationWithoutEmailAndPasswordTest() {

        Response response = userSteps.sendPostRequestApiAuthLogin(authUser);
        userSteps.checkFailedResponseApiAuthLogin(response);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным email")
    @Description("Авторизация пользователя с неверным email и с верными именем и паролем. Сервер отвечает с кодом 403")
    public void authorizationWithWrongEmailTest() {

        authUser = new User(name, email, password);
        authUser.setEmail("new_email" + email);
        Response response = userSteps.sendPostRequestApiAuthLogin(authUser);
        userSteps.checkFailedResponseApiAuthLogin(response);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем.")
    @Description("Авторизация пользователя с неверным паролем и с верными email и именем. Сервер отвечает с кодом 403")
    public void authorizationWithWrongPasswordTest() {

        authUser = new User(name, email, password);
        authUser.setPassword(password + "123");
        Response response = userSteps.sendPostRequestApiAuthLogin(authUser);
        userSteps.checkFailedResponseApiAuthLogin(response);
    }

    @After
    @DisplayName("Удаление пользователя")
    @Description("Удаление пользователя с созданными рандомными данными")
    public void tearDown() {

        UserSteps.deleteUser(user);
    }
}
