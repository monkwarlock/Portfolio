package ru.yandex.practicum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.practicum.util.EnvConfig;

import java.time.Duration;

import static ru.yandex.practicum.util.EnvConfig.LOGIN_PAGE;

public class LoginPage {
    // Поле Email
    private final By emailField = By.xpath("//label[contains(text(), 'Email')]/following-sibling::*[1]");
    // Поле Пароль
    private final By passwordField = By.xpath("//label[contains(text(), 'Пароль')]/following-sibling::*[1]");
    // Кнопка Войти
    private final By loginButton = By.cssSelector(".button_button__33qZ0.button_button_type_primary__1O7Bx.button_button_size_medium__3zxIa");
    // Кнопка Зарегистрироваться
    private final By registrationButton = By.cssSelector(".Auth_link__1fOlj[href='/register']");
    // Кнопка Восстановить пароль
    private final By passwordRecoveryButton = By.cssSelector(".Auth_link__1fOlj[href='/forgot-password']");

    private final WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Нажатие кнопки регистрация в форме авторизации")
    public RegistrationPage clickRegistrationButton() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(LOGIN_PAGE));
        driver.findElement(registrationButton).click();
        return new RegistrationPage(driver);
    }

    @Step("Авторизация пользователя")
    public void loginUser(String email, String password) {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(LOGIN_PAGE));
        // Найти и заполнить поле Email
        driver.findElement(emailField).sendKeys(email);
        // Найти и заполнить поле Пароль
        driver.findElement(passwordField).sendKeys(password);
        // Нажатие кнопки Войти
        driver.findElement(loginButton).click();

    }

    @Step("Нажатие кнопки Восстановление пароля в форме авторизации")
    public PasswordRecoveryPage clickPasswordRecoveryButton() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(LOGIN_PAGE));
        driver.findElement(passwordRecoveryButton).click();
        return new PasswordRecoveryPage(driver);
    }
}