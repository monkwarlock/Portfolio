package ru.yandex.practicum.pages;

import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.practicum.util.EnvConfig;

import java.time.Duration;

import static ru.yandex.practicum.util.EnvConfig.*;

public class MainPage {
    // Кнопка личный кабинет
    private final By personalAccountButton = By.xpath("//p[contains(text(), 'Личный Кабинет')]");
    // Кнопка профиль
    private final By profile = By.cssSelector(".Account_link__2ETsJ.text.text_type_main-medium.text_color_inactive.Account_link_active__2opc9");
    // Кнопка Войти в аккаунт
    private final By logInToYourAccountButton = By.cssSelector(".button_button__33qZ0.button_button_type_primary__1O7Bx.button_button_size_large__G21Vg");
    // Кнопка раздела Булки
    private final By bunButton = By.xpath("//div[@style='display: flex;']/div[1]");
    // Кнопка раздела Соусы
    private final By sauceButton = By.xpath("//div[@style='display: flex;']/div[2]");
    // Кнопка раздела Начинки
    private final By fillingButton = By.xpath("//div[@style='display: flex;']/div[3]");
    // Заголовок Булки
    private final By bunHeading = By.xpath("//h2[contains(text(), 'Булки')]");
    // Заголовок Соусы
    private final By sauceHeading = By.xpath("//h2[contains(text(), 'Соусы')]");
    // Заголовок Начинки
    private final By fillingHeading = By.xpath("//h2[contains(text(), 'Начинки')]");

    private final WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Нажатие на кнопку Личный кабинет")
    public LoginPage clickOnPersonalAccountButton() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(BASE_URL));
        driver.findElement(personalAccountButton).click();
        return new LoginPage(driver);
    }

    @Step("Нажатие на кнопку Личный кабинет и проверка регистрации")
    public void clickOnPersonalAccountButtonAndCheckRegistration() {
        driver.findElement(personalAccountButton).click();
        //Ждем когда страница загрузится
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(PERSONAL_ACCOUNT));
        // Проверяем вход в аккаунт
        Assert.assertTrue(driver.findElement(profile).isDisplayed());
    }

    @Step("Нажатие на кнопку Войти в аккаунт")
    public LoginPage clickOnLogInToYourAccountButton() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(BASE_URL));
        driver.findElement(logInToYourAccountButton).click();
        return new LoginPage(driver);
    }

    @Step("Нажатие на кнопку Личный кабинет и проверка авторизации")
    public void clickOnPersonalAccountButtonAndCheckAuthorization() {
        driver.findElement(personalAccountButton).click();
        //Ждем когда страница загрузится
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(PERSONAL_ACCOUNT));
        // Проверяем вход в аккаунт
        Assert.assertTrue(driver.findElement(profile).isDisplayed());
    }

    @Step("Нажатие на кнопку Булки в конструкторе")
    public void clickBunButton() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(BASE_URL));
        driver.findElement(bunButton).click();
    }

    @Step("Нажатие на кнопку Соусы в конструкторе")
    public void clickSauceButton() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(BASE_URL));
        driver.findElement(sauceButton).click();
    }

    @Step("Нажатие на кнопку Начинки в конструкторе")
    public void clickFillingButton() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(BASE_URL));
        driver.findElement(fillingButton).click();
    }

    @Step("Проверка перехода в раздел Булки")
    public void checkingTheTransitionToTheBunsSection() {
        WebElement button = driver.findElement(bunButton);
        String newClass = button.getAttribute("class");
        if (newClass != null) {
            Assert.assertTrue("Переход не сработал, кнопка не стала активной", newClass.contains("tab_tab_type_current__2BEPc"));
            Assert.assertTrue("Переход не сработал, список ингредиентов не прокрутился до Булки", driver.findElement(bunHeading).isDisplayed());
        }
    }

    @Step("Проверка перехода в раздел Соусы")
    public void checkingTheTransitionToTheSousesSection() {
        WebElement button = driver.findElement(sauceButton);
        String newClass = button.getAttribute("class");
        if (newClass != null) {
            Assert.assertTrue("Переход не сработал, кнопка не стала активной", newClass.contains("tab_tab_type_current__2BEPc"));
            Assert.assertTrue("Переход не сработал, список ингредиентов не прокрутился до Булки", driver.findElement(sauceHeading).isDisplayed());
        }
    }

    @Step("Проверка перехода в раздел Начинки")
    public void checkingTheTransitionToTheFillingsSection() {
        WebElement button = driver.findElement(fillingButton);
        String newClass = button.getAttribute("class");
        if (newClass != null) {
            Assert.assertTrue("Переход не сработал, кнопка не стала активной", newClass.contains("tab_tab_type_current__2BEPc"));
            Assert.assertTrue("Переход не сработал, список ингредиентов не прокрутился до Булки", driver.findElement(fillingHeading).isDisplayed());
        }
    }
}