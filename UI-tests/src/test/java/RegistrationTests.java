import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.models.UserLoginDTO;
import ru.yandex.practicum.pages.LoginPage;
import ru.yandex.practicum.pages.MainPage;
import ru.yandex.practicum.pages.RegistrationPage;
import ru.yandex.practicum.steps.UserSteps;
import ru.yandex.practicum.util.APIConfiguration;
import ru.yandex.practicum.util.DriverFactory;
import ru.yandex.practicum.util.EnvConfig;
import ru.yandex.practicum.util.TokenCreatingUser;

import static ru.yandex.practicum.util.EnvConfig.DOMAIN_YANDEX;
import static ru.yandex.practicum.util.EnvConfig.INVALID_PASSWORD;

public class RegistrationTests extends APIConfiguration {
    private User user;
    private TokenCreatingUser tokenCreatingUser;

    @Rule
    public DriverFactory factory = new DriverFactory(EnvConfig.BASE_URL);

    @Before
    public void setUp() {
        user = new User();
        user.withEmail(RandomStringUtils.randomAlphabetic(12).toLowerCase() + DOMAIN_YANDEX)
                .withPassword(RandomStringUtils.randomAlphabetic(8))
                .withName(RandomStringUtils.randomAlphabetic(9));
    }

    @Test
    @DisplayName("Тест: проверка успешной регистрации")
    @Description("Тест: регистрации пользователя в сервисе и дальнейшая проверка создания пользователя через авторизацию в сервисе")
    public void registrationTest() {
        WebDriver driver = factory.getDriver();
        var mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.clickOnPersonalAccountButton();
        RegistrationPage registrationPage = loginPage.clickRegistrationButton();
        registrationPage.fillingFieldsAndPressRegistrationButton(user.getName(), user.getEmail(), user.getPassword());
        loginPage.loginUser(user.getEmail(), user.getPassword());
        mainPage.clickOnPersonalAccountButtonAndCheckRegistration();
        tokenCreatingUser = new TokenCreatingUser(1);
    }

    @Test
    @DisplayName("Тест: ошибка для некорректного пароля. Минимальный пароль — шесть символов")
    @Description("Тест: проверка наличия сообщения об ошибке в поле пароль при регистрации")
    public void passwordErrorTest() {
        WebDriver driver = factory.getDriver();
        var mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.clickOnPersonalAccountButton();
        RegistrationPage registrationPage = loginPage.clickRegistrationButton();
        registrationPage.fillingFieldsAndPressRegistrationButton(user.getName(), user.getEmail(), INVALID_PASSWORD);
        registrationPage.checkPasswordErrorTest();
        tokenCreatingUser = new TokenCreatingUser(0);
    }

    @After
    public void tearDown() {
        // Удаление пользователя через API
        if (tokenCreatingUser.getTokenCreatingUser() == 1) {
            UserSteps userSteps = new UserSteps();
            UserLoginDTO userLoginDTO = new UserLoginDTO(user.getEmail(), user.getPassword());
            ValidatableResponse response = userSteps.loginUser(userLoginDTO).statusCode(200);
            user.setAccessToken(response.extract().body().path("accessToken"));
            userSteps.deleteUser(user.getAccessToken()).statusCode(202);
        }
    }
}