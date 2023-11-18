package user;

import api.model.User;
import api.steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class ChangeUserDataTest {
    private String name;
    private String email;
    private String password;
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
    @DisplayName("Изменение имени авторизованного пользователя")
    @Description("Изменение имени авторизованного пользователя")
    public void changeUserNameWithAuthorizationTest() {

        String newName = "Nikita";
        User changeUser = new User();
        changeUser.setName(newName);
        user.setName(newName);
        Response response = userSteps.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        userSteps.checkSuccessResponseApiAuthUser(response, email, newName);
    }


    @Test
    @DisplayName("Изменение email авторизованного пользователя")
    @Description("Изменение email авторизованного пользователя")
    public void changeUserEmailWithAuthorizationTest() {

        String newEmail = "new_email" + email;
        User changeUser = new User();
        changeUser.setEmail(newEmail);
        user.setEmail(newEmail);

        Response response = userSteps.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        userSteps.checkSuccessResponseApiAuthUser(response, newEmail, name);
    }

    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    @Description("Изменение пароля авторизованного пользователя")
    public void changeUserPasswordWithAuthorizationTest() {

        String newPassword = "superstrongpassword12345";
        User changeUser = new User();
        changeUser.setPassword(newPassword);
        user.setPassword(newPassword);

        Response response = userSteps.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        userSteps.checkSuccessResponseApiAuthUser(response, email, name);
    }

    @Test
    @DisplayName("Изменение имени и email авторизованного пользователя")
    @Description("Изменение имени и email авторизованного пользователя")
    public void changeUserNameAndEmailWithAuthorizationTest() {

        String newEmail = "new_email" + email;
        String newName = "Nikita";
        User changeUser = new User();
        changeUser.setEmail(newEmail);
        changeUser.setName(newName);
        user.setEmail(newEmail);
        user.setName(newName);

        Response response = userSteps.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        userSteps.checkSuccessResponseApiAuthUser(response, newEmail, newName);
    }

    @Test
    @DisplayName("Изменение имени и пароля авторизованного пользователя")
    @Description("Изменение имени и пароля авторизованного пользователя")
    public void changeUserNameAndPasswordWithAuthorizationTest() {

        String newPassword = "superstrongpassword12345";
        String newName = "Nikita";
        User changeUser = new User();
        changeUser.setPassword(newPassword);
        changeUser.setName(newName);
        user.setPassword(newPassword);
        user.setName(newName);

        Response response = userSteps.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        userSteps.checkSuccessResponseApiAuthUser(response, email, newName);
    }

    @Test
    @DisplayName("Изменение email и пароля авторизованного пользователя")
    @Description("Изменение email и пароля авторизованного пользователя")
    public void changeUserEmailAndPasswordWithAuthorizationTest() {

        String newPassword = "superstrongpassword12345";
        String newEmail = "new_email" + email;
        User changeUser = new User();
        changeUser.setPassword(newPassword);
        changeUser.setEmail(newEmail);
        user.setPassword(newPassword);
        user.setEmail(newEmail);

        Response response = userSteps.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        userSteps.checkSuccessResponseApiAuthUser(response, newEmail, name);
    }

    @Test
    @DisplayName("Изменение всех данных авторизованного пользователя")
    @Description("Изменение всех данных авторизованного пользователя")
    public void changeAllUserFieldsWithAuthorizationTest() {

        String newPassword = "superstrongpassword12345";
        String newName = "Nikita";
        String newEmail = "new_email" + email;
        User changeUser = new User(newName, newEmail, newPassword);
        user = changeUser;

        Response response = userSteps.sendPatchRequestWithAuthorizationApiAuthUser(changeUser, accessToken);
        userSteps.checkSuccessResponseApiAuthUser(response, newEmail, newName);
    }


    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    @Description("Изменение имени пользователя без авторизации")
    public void changeUserNameWithoutAuthorizationTest() {

        String newName = "Nikita";
        User changeUser = new User();
        changeUser.setName(newName);

        Response response = userSteps.sendPatchRequestWithoutAuthorizationApiAuthUser(changeUser);
        userSteps.checkFailedResponseApiAuthUser(response);
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    @Description("Изменение email пользователя без авторизации")
    public void changeUserEmailWithoutAuthorizationTest() {

        String newEmail = "mrvlomschik@yandex.ru";
        User changeUser = new User();
        changeUser.setName(newEmail);

        Response response = userSteps.sendPatchRequestWithoutAuthorizationApiAuthUser(changeUser);
        userSteps.checkFailedResponseApiAuthUser(response);
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    @Description("Изменение пароля пользователя без авторизации")
    public void changeUserPasswordWithoutAuthorizationTest() {

        String newPassword = "notsostrongpassword";
        User changeUser = new User();
        changeUser.setName(newPassword);

        Response response = userSteps.sendPatchRequestWithoutAuthorizationApiAuthUser(changeUser);
        userSteps.checkFailedResponseApiAuthUser(response);
    }

    @Test
    @DisplayName("Изменение имени и email пользователя без авторизации")
    @Description("Изменение имени и email пользователя без авторизации")
    public void changeUserNameAndEmailWithoutAuthorizationTest() {

        String newName = "Nikita";
        String newEmail = "mrvzlomschik@yandex.ru";
        User changeUser = new User();
        changeUser.setName(newName);
        changeUser.setEmail(newEmail);

        Response response = userSteps.sendPatchRequestWithoutAuthorizationApiAuthUser(changeUser);
        userSteps.checkFailedResponseApiAuthUser(response);
    }

    @Test
    @DisplayName("Изменение имени и пароля пользователя без авторизации")
    @Description("Изменение имени и пароля пользователя без авторизации")
    public void changeUserNameAndPasswordWithoutAuthorizationTest() {

        String newPassword = "notsostrongpassword";
        String newName = "Nikita";
        User changeUser = new User();
        changeUser.setName(newPassword);
        changeUser.setName(newName);

        Response response = userSteps.sendPatchRequestWithoutAuthorizationApiAuthUser(changeUser);
        userSteps.checkFailedResponseApiAuthUser(response);
    }

    @Test
    @DisplayName("Изменение email и пароля пользователя без авторизации")
    @Description("Изменение email и пароля пользователя без авторизации")
    public void changeUserEmailAndPasswordWithoutAuthorizationTest() {

        String newEmail = "mrvzlomschik@yandex.ru";
        String newPassword = "notsostrongpassword";
        User changeUser = new User();
        changeUser.setName(newEmail);
        changeUser.setPassword(newPassword);

        Response response = userSteps.sendPatchRequestWithoutAuthorizationApiAuthUser(changeUser);
        userSteps.checkFailedResponseApiAuthUser(response);
    }

    @Test
    @DisplayName("Изменение всех данных пользователя без авторизации")
    @Description("Изменение всех данных пользователя без авторизации")
    public void changeAllUserFieldsWithoutAuthorizationTest() {

        String newEmail = "mrvzlomschik@yandex.ru";
        String newPassword = "notsostrongpassword";
        String newName = "Nikita";
        User changeUser = new User();
        changeUser.setName(newEmail);
        changeUser.setPassword(newPassword);
        changeUser.setName(newName);

        Response response = userSteps.sendPatchRequestWithoutAuthorizationApiAuthUser(changeUser);
        userSteps.checkFailedResponseApiAuthUser(response);
    }

    @After
    public void tearDown() {
        given().log().all()
                .header("Content-Type", "application/json")
                .body(user)
                .delete("/api/auth/user");
    }
}
