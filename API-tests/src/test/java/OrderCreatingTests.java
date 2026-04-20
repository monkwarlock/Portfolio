import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.models.UserCreatingDTO;
import ru.yandex.practicum.steps.OrderSteps;
import ru.yandex.practicum.steps.UserSteps;
import ru.yandex.practicum.util.RestConfig;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static ru.yandex.practicum.util.Endpoints.LOGIN;
import static ru.yandex.practicum.util.RestConfig.DOMAIN_YANDEX;

public class OrderCreatingTests extends BaseTest {
    OrderSteps orderSteps = new OrderSteps();
    UserSteps userSteps = new UserSteps();
    private Order order;
    private User user;

    @Before
    public void setUp() {
        order = new Order();
        // Получение списка ингредиентов
        ValidatableResponse responseIngredients = orderSteps.gettingListOfIngredients().statusCode(200);
        order.setIngredients(responseIngredients.extract().body().path("data._id"));
        // Создание пользователя
        user = new User();
        user.withEmail(RandomStringUtils.randomAlphabetic(12).toLowerCase() + DOMAIN_YANDEX)
                .withPassword(RandomStringUtils.randomAlphabetic(8))
                .withName(RandomStringUtils.randomAlphabetic(9));
        UserCreatingDTO userCreatingDTO = new UserCreatingDTO(user.getEmail(), user.getPassword(), user.getName());
        ValidatableResponse responseCreatingUser = userSteps.createUser(userCreatingDTO).statusCode(200);
        // Получение токена
        user.setAccessToken(responseCreatingUser.extract().body().path("accessToken"));
    }

    @Test
    @DisplayName("Тест: создать заказ с авторизацией и ингредиентами")
    @Description("Тест для проверки ручки /api/ingredients на создание заказа авторизованным пользователем")
    public void creatingOrderWithAuthorizationTest() {
        // Вызываем метод для создания заказа
        ValidatableResponse creatingOrderResponse = orderSteps.createOrderWithAuthorization(order, user.getAccessToken());
        // Проверяем код ответа
        creatingOrderResponse.statusCode(200);
        // Проверяем тело ответа
        String expectedBody = RestConfig.getOrderCreatingBody(creatingOrderResponse.extract().body().path("name"),
                creatingOrderResponse.extract().body().path("order.number"));
        String actualBody = creatingOrderResponse.extract().asPrettyString();
        assertEquals("Тело ответа не соответствует ожидаемому", expectedBody, actualBody);
    }

    @Test
    @DisplayName("Тест: создать заказ без авторизации")
    @Description("Тест для проверки ручки /api/ingredients на создание заказа не авторизованным пользователем")
    public void creatingOrderWithoutAuthorizationTest() {
        // Вызываем метод для создания заказа без авторизации
        Response creatingOrderResponse = orderSteps.createOrderWithoutAuthorization(order);
        // Проверяем наличие заголовка Location
        String location = creatingOrderResponse.header("Location");
        assertNotNull("Заголовок 'Location' отсутствует, не произошел редирект на форму авторизации", location);
        // Проверяем, что редирект ведёт на эндпоинт авторизации
        assertTrue("Редирект должен вести на сервис авторизации",
                location.startsWith(RestConfig.HOST + LOGIN));
    }

    @Test
    @DisplayName("Тест: создать заказ без ингредиентов")
    @Description("Тест для проверки ручки /api/ingredients на создание заказа без ингредиентов")
    public void creatingOrderWithoutIngredientsTest() {
        // Вызываем метод для создания заказа
        ValidatableResponse creatingOrderResponse = orderSteps.createOrderWithoutIngredients(user.getAccessToken());
        // Проверяем код и тело ответа
        creatingOrderResponse.statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Тест: создать заказ с неверным хешем")
    @Description("Тест для проверки ручки /api/ingredients на создание заказа с неверным хешем")
    public void creatingOrderWithInvalidHashTest() {
        // Вызываем метод для создания заказа
        Order order1 = new Order(RestConfig.getIngredientsWithInvalidHash());
        ValidatableResponse creatingOrderResponse = orderSteps.createOrderWithAuthorization(order1, user.getAccessToken());
        // Проверяем код ответа
        creatingOrderResponse.statusCode(500);
    }

    @After
    public void tearDown() {
        // Удаление пользователя
        if (user.getAccessToken() != null) {
            userSteps.deleteUser(user.getAccessToken()).statusCode(202);
        }
    }
}