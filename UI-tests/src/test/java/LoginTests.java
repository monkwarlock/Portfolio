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
import ru.yandex.practicum.models.UserCreatingDTO;
import ru.yandex.practicum.pages.LoginPage;
import ru.yandex.practicum.pages.MainPage;
import ru.yandex.practicum.pages.PasswordRecoveryPage;
import ru.yandex.practicum.pages.RegistrationPage;
import ru.yandex.practicum.steps.UserSteps;
import ru.yandex.practicum.util.APIConfiguration;
import ru.yandex.practicum.util.DriverFactory;
import ru.yandex.practicum.util.EnvConfig;

import static ru.yandex.practicum.util.EnvConfig.DOMAIN_YANDEX;

public class LoginTests extends APIConfiguration {
    private User user;
    private UserSteps userSteps;

    @Rule
    public DriverFactory factory = new DriverFactory(EnvConfig.BASE_URL);

    @Before
    public void setUp() {
        user = new User();
        user.withEmail(RandomStringUtils.randomAlphabetic(12).toLowerCase() + DOMAIN_YANDEX)
                .withPassword(RandomStringUtils.randomAlphabetic(8))
                .withName(RandomStringUtils.randomAlphabetic(9));
        // Создание пользователя через API
        UserCreatingDTO userCreatingDTO = new UserCreatingDTO(user.getEmail(), user.getPassword(), user.getName());
        userSteps = new UserSteps();
        ValidatableResponse creatingResponse = userSteps.createUser(userCreatingDTO).statusCode(200);
        user.setAccessToken(creatingResponse.extract().body().path("accessToken"));
    }

    @Test
    @DisplayName("Тест: вход по кнопке «Войти в аккаунт» на главной")
    @Description("Тест: проверка входа по кнопке «Войти в аккаунт» на главной странице")
    public void loginUnderLogInToYourAccountButton() {
        WebDriver driver = factory.getDriver();
        var mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.clickOnLogInToYourAccountButton();
        loginPage.loginUser(user.getEmail(), user.getPassword());
        mainPage.clickOnPersonalAccountButtonAndCheckAuthorization();
    }

    @Test
    @DisplayName("Тест: вход через кнопку «Личный кабинет»")
    @Description("Тест: проверка входа через кнопку «Личный кабинет»")
    public void loginUnderPersonalAccountButton() {
        WebDriver driver = factory.getDriver();
        var mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.clickOnPersonalAccountButton();
        loginPage.loginUser(user.getEmail(), user.getPassword());
        mainPage.clickOnPersonalAccountButtonAndCheckAuthorization();
    }

    @Test
    @DisplayName("Тест: вход через кнопку в форме регистрации")
    @Description("Тест: проверка входа через кнопку в форме регистрации")
    public void loginViaRegistrationForm() {
        WebDriver driver = factory.getDriver();
        var mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.clickOnPersonalAccountButton();
        RegistrationPage registrationPage = loginPage.clickRegistrationButton();
        registrationPage.clickLoginButton();
        loginPage.loginUser(user.getEmail(), user.getPassword());
        mainPage.clickOnPersonalAccountButtonAndCheckAuthorization();
    }

    @Test
    @DisplayName("Тест: вход через кнопку в форме восстановления пароля")
    @Description("Тест: проверка входа через кнопку в форме восстановления пароля")
    public void voidViaAButtonInThePasswordRecoveryForm() {
        WebDriver driver = factory.getDriver();
        var mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.clickOnPersonalAccountButton();
        PasswordRecoveryPage passwordRecoveryPage = loginPage.clickPasswordRecoveryButton();
        passwordRecoveryPage.clickLoginButton();
        loginPage.loginUser(user.getEmail(), user.getPassword());
        mainPage.clickOnPersonalAccountButtonAndCheckAuthorization();
    }

    @After
    public void tearDown() {
        // Удаление пользователя через API
        if (user.getAccessToken() != null) {
            userSteps.deleteUser(user.getAccessToken()).statusCode(202);
        }
    }
}