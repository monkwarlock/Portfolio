import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import ru.yandex.practicum.pages.MainPage;
import ru.yandex.practicum.util.DriverFactory;
import ru.yandex.practicum.util.EnvConfig;

public class TheConstructorSectionTests {
    @Rule
    public DriverFactory factory = new DriverFactory(EnvConfig.BASE_URL);

    @Test
    @DisplayName("Тест: проверка перехода в раздел Соусы")
    @Description("Тест: проверка перехода в раздел Соусы в списке ингредиентов при нажатии на кнопку Соусы")
    public void goingToTheSousesSectionTest() {
        WebDriver driver = factory.getDriver();
        var mainPage = new MainPage(driver);
        mainPage.clickSauceButton();
        mainPage.checkingTheTransitionToTheSousesSection();
    }

    @Test
    @DisplayName("Тест: проверка перехода в раздел Начинки")
    @Description("Тест: проверка перехода в раздел Начинки в списке ингредиентов при нажатии на кнопку Начинки")
    public void goingToTheFillingsSectionTest() {
        WebDriver driver = factory.getDriver();
        var mainPage = new MainPage(driver);
        mainPage.clickFillingButton();
        mainPage.checkingTheTransitionToTheFillingsSection();
    }

    @Test
    @DisplayName("Тест: проверка перехода в раздел Булки")
    @Description("Тест: проверка перехода в раздел Булки в списке ингредиентов при нажатии на кнопку Булки")
    public void goingToTheBunsSectionTest() {
        WebDriver driver = factory.getDriver();
        var mainPage = new MainPage(driver);
        mainPage.clickFillingButton();
        mainPage.clickBunButton();
        mainPage.checkingTheTransitionToTheBunsSection();
    }
}