package ru.yandex.practicum.pages;

import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.practicum.util.EnvConfig;

import java.time.Duration;

import static ru.yandex.practicum.util.EnvConfig.*;

public class RegistrationPage {
    // Поле Имя
    private final By nameField = By.xpath("//label[contains(@class, 'input__placeholder') and contains(text(), 'Имя')]/following-sibling::*[1]");
    // Поле Email
    private final By emailField = By.xpath("//label[contains(text(), 'Email')]/following-sibling::*[1]");
    // Поле Пароль
    private final By passwordField = By.xpath("//label[contains(text(), 'Пароль')]/following-sibling::*[1]");
    // Кнопка Зарегистрироваться
    private final By registrationButton = By.cssSelector(".button_button__33qZ0.button_button_type_primary__1O7Bx.button_button_size_medium__3zxIa");
    // Ошибка некорректного пароля
    private final By passwordError = By.cssSelector(".input__error.text_type_main-default");
    // Кнопка Войти
    private final By loginButton = By.cssSelector(".Auth_link__1fOlj");

    private final WebDriver driver;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Заполнение полей регистрации и нажатие кнопки Зарегистрироваться")
    public void fillingFieldsAndPressRegistrationButton(String name, String email, String password) {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(REGISTRATION_PAGE));
        // Найти и заполнить поле Имя
        driver.findElement(nameField).sendKeys(name);
        // Найти и заполнить поле Email
        driver.findElement(emailField).sendKeys(email);
        // Найти и заполнить поле Пароль
        driver.findElement(passwordField).sendKeys(password);
        // Нажать кнопку Зарегистрироваться
        driver.findElement(registrationButton).click();
    }

    @Step("Проверка появления сообщения о неверном пароле")
    public void checkPasswordErrorTest() {
        Assert.assertTrue("Сообщение об ошибке не появилось", driver.findElement(passwordError).isDisplayed());
    }

    @Step("Нажатие кнопки войти в форме регистрации")
    public void clickLoginButton() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(REGISTRATION_PAGE));
        driver.findElement(loginButton).click();
    }
}