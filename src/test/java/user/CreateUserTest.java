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


public class CreateUserTest {
    private String name;
    private String email;
    private String password;
    private UserSteps userSteps;
    private User user;

    @Before
    public void startUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Before
    public void createRandomData() {
        name = RandomStringUtils.randomAlphanumeric(3, 20);
        email = RandomStringUtils.randomAlphanumeric(6, 10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphanumeric(12, 20);
        userSteps = new UserSteps();
        user = new User();
    }


    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Регистрация уникального пользователя со случайным набором данных. Сервер отвечает с кодом 200")
    public void createUserTest() {

        user = new User(name, email, password);
        Response response = userSteps.sendPostRequestApiAuthRegister(user);

        response.then()
                .assertThat()
                .body("success", Matchers.is(true))
                .and()
                .body("user.email", Matchers.is(email.toLowerCase()))
                .and()
                .body("user.name", Matchers.is(name))
                .and()
                .body("accessToken", Matchers.notNullValue())
                .and()
                .body("refreshToken", Matchers.notNullValue())
                .and().statusCode(HttpStatus.SC_OK);
    }


    @Test
    @DisplayName("Регистрация уже созданного пользователя")
    @Description("Регистрация уже созданного пользователя со случайным набором данных. Сервер отвечает с кодом 403")
    public void createTwoIdenticalUsersTest() {

        user = new User(name, email, password);
        userSteps.sendPostRequestApiAuthRegister(user);
        Response response = userSteps.sendPostRequestApiAuthRegister(user);

        response.then()
                .assertThat()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is("User already exists"))
                .and()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Регистрация пользователя без имени")
    @Description("Регистрация пользователя без имени, но со случайными email и паролем. Сервер отвечает с кодом 403")
    public void createUserWithoutNameTest() {

        user.setEmail(email);
        user.setPassword(password);
        Response response = userSteps.sendPostRequestApiAuthRegister(user);
        userSteps.checkFailedResponseApiAuthRegister(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без email")
    @Description("Регистрация пользователя без email, но со случайными именем и паролем. Сервер отвечает с кодом 403")
    public void createUserWithoutEmailTest() {

        user.setName(name);
        user.setPassword(password);
        Response response = userSteps.sendPostRequestApiAuthRegister(user);
        userSteps.checkFailedResponseApiAuthRegister(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    @Description("Регистрация пользователя без пароля, но со случайными email и именем. Сервер отвечает с кодом 403")
    public void createUserWithoutPasswordTest() {

        user.setEmail(email);
        user.setName(name);
        Response response = userSteps.sendPostRequestApiAuthRegister(user);
        userSteps.checkFailedResponseApiAuthRegister(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без имени и email")
    @Description("Регистрация пользователя без имени и email, но со случайным паролем. Сервер отвечает с кодом 403")
    public void createUserWithoutNameAndEmailTest() {

        user.setPassword(password);
        Response response = userSteps.sendPostRequestApiAuthRegister(user);
        userSteps.checkFailedResponseApiAuthRegister(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без имени и пароля")
    @Description("Регистрация пользователя без имени и пароля, но со случайным email. Сервер отвечает с кодом 403")
    public void createUserWithoutNameAndPasswordTest() {

        user.setEmail(email);
        Response response = userSteps.sendPostRequestApiAuthRegister(user);
        userSteps.checkFailedResponseApiAuthRegister(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без email и пароля.")
    @Description("Регистрация пользователя без email и пароля, но со случайным именем. Сервер отвечает с кодом 403")
    public void createUserWithoutEmailAndPasswordTest() {

        user.setName(name);
        Response response = userSteps.sendPostRequestApiAuthRegister(user);
        userSteps.checkFailedResponseApiAuthRegister(response);
    }

    @Test
    @DisplayName("Регистрация пользователя без всех данных")
    @Description("Регистрация пользователя без всех данных. Сервер отвечает с кодом 403")
    public void createUserWithoutAllTest() {

        Response response = userSteps.sendPostRequestApiAuthRegister(user);
        userSteps.checkFailedResponseApiAuthRegister(response);
    }

    @After
    @DisplayName("Удаление пользователя")
    @Description("Удаление пользователя с созданными рандомными данными")
    public void tearDown() {

        UserSteps.deleteUser(user);
    }
}
