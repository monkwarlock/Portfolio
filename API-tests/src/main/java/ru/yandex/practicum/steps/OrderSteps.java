package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.config.RedirectConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.Order;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static ru.yandex.practicum.util.Endpoints.*;

public class OrderSteps {

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuthorization(Order order, String token) {
        return given()
                .header("Authorization", token)
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuthorization(Order order) {
        return given()
                .config(config().redirect(RedirectConfig.redirectConfig().followRedirects(false)))
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .extract()
                .response();
    }

    @Step("Создание заказа без ингредиентов")
    public ValidatableResponse createOrderWithoutIngredients(String token) {
        return given()
                .header("Authorization", token)
                .body("")
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Получение ингредиентов")
    public ValidatableResponse gettingListOfIngredients() {
        return given()
                .when()
                .get(INGREDIENTS)
                .then();
    }
}