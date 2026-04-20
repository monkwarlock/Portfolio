package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.UserCreatingDTO;
import ru.yandex.practicum.models.UserLoginDTO;

import static io.restassured.RestAssured.given;
import static ru.yandex.practicum.util.Endpoints.*;

public class UserSteps {

    @Step("Создание пользователя")
    public ValidatableResponse createUser(UserCreatingDTO userCreatingDTO) {
        return given()
                .body(userCreatingDTO)
                .when()
                .post(REGISTER)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(UserLoginDTO userLoginDTO) {
        return given()
                .body(userLoginDTO)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .delete(DELETE_USER)
                .then();
    }
}