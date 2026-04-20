package ru.yandex.practicum.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.practicum.util.EnvConfig;

import java.time.Duration;

import static ru.yandex.practicum.util.EnvConfig.PASSWORD_RECOVERY_PAGE;

public class PasswordRecoveryPage {
    // Кнопка Войти
    private final By loginButton = By.cssSelector(".Auth_link__1fOlj[href='/login']");

    private final WebDriver driver;

    public PasswordRecoveryPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Нажатие кнопки Войти в форме восcтановления пароля")
    public void clickLoginButton() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.EXPLICITY_TIMEOUT))
                .until(ExpectedConditions.urlToBe(PASSWORD_RECOVERY_PAGE));
        driver.findElement(loginButton).click();
    }
}