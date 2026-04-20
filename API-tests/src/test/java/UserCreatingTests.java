import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.models.UserCreatingDTO;
import ru.yandex.practicum.steps.UserSteps;
import ru.yandex.practicum.util.RestConfig;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static ru.yandex.practicum.util.RestConfig.DOMAIN_YANDEX;

public class UserCreatingTests extends BaseTest {
    UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.withEmail(RandomStringUtils.randomAlphabetic(12).toLowerCase() + DOMAIN_YANDEX)
                .withPassword(RandomStringUtils.randomAlphabetic(8))
                .withName(RandomStringUtils.randomAlphabetic(9));
    }

    @Test
    @DisplayName("Тест: создать уникального пользователя")
    @Description("Тест для проверки ручки /api/auth/register на создание пользователя")
    public void canCreateUserTest() {
        // Создание пользователя
        UserCreatingDTO userCreatingDTO = new UserCreatingDTO(user.getEmail(), user.getPassword(), user.getName());
        ValidatableResponse creatingResponse = userSteps.createUser(userCreatingDTO);
        user.setAccessToken(creatingResponse.extract().body().path("accessToken"));
        user.setRefreshToken(creatingResponse.extract().body().path("refreshToken"));
        // Проверка кода ответа
        creatingResponse.statusCode(200);
        // Проверка тела ответа
        String expectedBody = RestConfig.getUserCreatingBody(user.getAccessToken(),user.getRefreshToken(), user.getEmail(), user.getName());
        String actualBody = creatingResponse.extract().asPrettyString();
        assertEquals("Тело ответа не соответствует ожидаемому", expectedBody, actualBody);

    }

    @Test
    @DisplayName("Тест: создать пользователя, который уже зарегистрирован")
    @Description("Тест для проверки ручки /api/auth/register на создание пользователя, который уже зарегистрирован")
    public void createUserWhichAlreadyRegisteredTest() {
        // Создание пользователя
        UserCreatingDTO userCreatingDTO = new UserCreatingDTO(user.getEmail(), user.getPassword(), user.getName());
        ValidatableResponse response = userSteps.createUser(userCreatingDTO);
        user.setAccessToken(response.extract().body().path("accessToken"));
        // Создание уже зарегистрированного пользователя
        ValidatableResponse creatingResponse = userSteps.createUser(userCreatingDTO);
        // Проверка кода и тела ответа
        creatingResponse.statusCode(403)
                .body("success", is(false))
                .body("message", Matchers.equalTo("User already exists"));
    }

    @Test
    @DisplayName("Тест: создать пользователя без email")
    @Description("Тест для проверки ручки /api/auth/register на создание пользователя без email")
    public void createUserWithoutEmailTest() {
        // Создание пользователя
        UserCreatingDTO userCreatingDTO = new UserCreatingDTO("", user.getPassword(), user.getName());
        ValidatableResponse creatingResponse = userSteps.createUser(userCreatingDTO);
        // Проверка кода и тела ответа
        creatingResponse.statusCode(403)
                .body("success", is(false))
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Тест: создать пользователя без password")
    @Description("Тест для проверки ручки /api/auth/register на создание пользователя без password")
    public void createUserWithoutPasswordTest() {
        // Создание пользователя
        UserCreatingDTO userCreatingDTO = new UserCreatingDTO(user.getEmail(), "", user.getName());
        ValidatableResponse creatingResponse = userSteps.createUser(userCreatingDTO);
        // Проверка кода и тела ответа
        creatingResponse.statusCode(403)
                .body("success", is(false))
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Тест: создать пользователя без name")
    @Description("Тест для проверки ручки /api/auth/register на создание пользователя без name")
    public void createUserWithoutNameTest() {
        // Создание пользователя
        UserCreatingDTO userCreatingDTO = new UserCreatingDTO(user.getEmail(), user.getPassword(), "");
        ValidatableResponse creatingResponse = userSteps.createUser(userCreatingDTO);
        // Проверка кода и тела ответа
        creatingResponse.statusCode(403)
                .body("success", is(false))
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        // Удаление пользователя
        if (user.getAccessToken() != null) {
            userSteps.deleteUser(user.getAccessToken()).statusCode(202);
        }
    }
}