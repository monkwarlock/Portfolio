package ru.yandex.practicum.util;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static ru.yandex.practicum.util.YandexPath.*;

public class DriverFactory extends ExternalResource {
    private WebDriver driver;
    public String baseUrl;

    private final String yandexBrowserPath = System.getProperty(
            "yandex.browser.path",
            YANDEX_BROWSER_PATH
    );

    private final String yandexDriverPath = System.getProperty(
            "webdriver.yandex.driver",
            YANDEX_DRIVER_PATH
    );

    public DriverFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public WebDriver getDriver(){
        return driver;
    }

    public void initDriver() {
        if ("yandex".equals(System.getProperty("browser"))) {
            startYandexBrowser();
        } else {
            startChrome();
        }
        driver.get(baseUrl);
    }

    private void configureDriver() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(EnvConfig.IMPLICITY_TIMEOUT));
        driver.manage().window().maximize();
    }

    private void startChrome() {
        driver = new ChromeDriver();
        configureDriver();
    }

    private void startYandexBrowser() {
        System.setProperty("webdriver.chrome.driver", yandexDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(yandexBrowserPath);
        driver = new ChromeDriver(options);
        configureDriver();
    }

    @Override
    protected void before() {
        initDriver();
    }

    protected void after() {
        driver.quit();
    }
}