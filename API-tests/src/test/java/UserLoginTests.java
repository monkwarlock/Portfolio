import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.models.UserCreatingDTO;
import ru.yandex.practicum.models.UserLoginDTO;
import ru.yandex.practicum.steps.UserSteps;
import ru.yandex.practicum.util.RestConfig;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static ru.yandex.practicum.util.RestConfig.*;

public class UserLoginTests extends BaseTest {
    UserSteps userSteps = new UserSteps();
    private User user;
    private ValidatableResponse creatingResponse;

    @Before
    public void setUp() {
        user = new User();
        user.withEmail(RandomStringUtils.randomAlphabetic(12).toLowerCase() + DOMAIN_YANDEX)
                .withPassword(RandomStringUtils.randomAlphabetic(8))
                .withName(RandomStringUtils.randomAlphabetic(9));
        // Создание пользователя
        UserCreatingDTO userCreatingDTO = new UserCreatingDTO(user.getEmail(), user.getPassword(), user.getName());
        creatingResponse = userSteps.createUser(userCreatingDTO).statusCode(200);
    }

    @Test
    @DisplayName("Тест: вход под существующим пользователем")
    @Description("Тест для проверки ручки /api/auth/login на авторизацию пользователя")
    public void loginUnderExistingUserTest() {
        // Авторизация пользователя
        UserLoginDTO userLoginDTO = new UserLoginDTO(user.getEmail(), user.getPassword());
        ValidatableResponse loginResponse = userSteps.loginUser(userLoginDTO);
        user.setAccessToken(loginResponse.extract().body().path("accessToken"));
        user.setRefreshToken(loginResponse.extract().body().path("refreshToken"));
        // Проверка кода ответа
        loginResponse.statusCode(200);
        // Проверка тела ответа
        String expectedBody = RestConfig.getUserLoginBody(user.getAccessToken(),user.getRefreshToken(), user.getEmail(), user.getName());
        String actualBody = loginResponse.extract().asPrettyString();
        assertEquals("Тело ответа не соответствует ожидаемому", expectedBody, actualBody);
    }

    @Test
    @DisplayName("Тест: вход с неверным логином")
    @Description("Тест для проверки ручки /api/auth/login на авторизацию пользователя с неверным логином")
    public void loginUserWithInvalidLoginTest() {
        // Авторизация пользователя
        UserLoginDTO userLoginDTO = new UserLoginDTO(INVALID_EMAIL, user.getPassword());
        ValidatableResponse loginResponse = userSteps.loginUser(userLoginDTO);
        user.setAccessToken(loginResponse.extract().body().path("accessToken"));
        // Проверка кода и тела ответа
        loginResponse.statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Тест: вход с неверным паролем")
    @Description("Тест для проверки ручки /api/auth/login на авторизацию пользователя с неверным паролем")
    public void loginUserWithInvalidPasswordTest() {
        // Авторизация пользователя
        UserLoginDTO userLoginDTO = new UserLoginDTO(user.getEmail(), INVALID_PASSWORD);
        ValidatableResponse loginResponse = userSteps.loginUser(userLoginDTO);
        user.setAccessToken(loginResponse.extract().body().path("accessToken"));
        // Проверка кода и тела ответа
        loginResponse.statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Тест: вход с неверным логином и паролем")
    @Description("Тест для проверки ручки /api/auth/login на авторизацию пользователя с неверным логином и паролем")
    public void loginUserWithInvalidLoginAndPasswordTest() {
        // Авторизация пользователя
        UserLoginDTO userLoginDTO = new UserLoginDTO(INVALID_EMAIL, INVALID_PASSWORD);
        ValidatableResponse loginResponse = userSteps.loginUser(userLoginDTO);
        user.setAccessToken(loginResponse.extract().body().path("accessToken"));
        // Проверка кода и тела ответа
        loginResponse.statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        user.setAccessToken(creatingResponse.extract().body().path("accessToken"));
        // Удаление пользователя
        if (user.getAccessToken() != null) {
            userSteps.deleteUser(user.getAccessToken()).statusCode(202);
        }
    }
}