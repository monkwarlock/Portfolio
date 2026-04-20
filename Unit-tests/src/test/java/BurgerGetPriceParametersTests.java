import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BurgerGetPriceParametersTests {
    private final Bun stubBun;
    private final Ingredient[] stubIngredients;
    private final float expectedPrice;

    public BurgerGetPriceParametersTests(Bun stubBun, Ingredient[] stubIngredients, float expectedPrice) {
        this.stubBun = stubBun;
        this.stubIngredients = stubIngredients;
        this.expectedPrice = expectedPrice;
    }

    // Стаб для Bun
    private static Bun createStubBun(float price) {
        Bun bun = Mockito.mock(Bun.class);
        Mockito.when(bun.getPrice()).thenReturn(price);
        return bun;
    }

    // Стаб для Ingredient
    private static Ingredient createStubIngredient(float price) {
        Ingredient ingredient = Mockito.mock(Ingredient.class);
        Mockito.when(ingredient.getPrice()).thenReturn(price);
        return ingredient;
    }

    @Parameterized.Parameters(name = "Тест: проверка расчёта стоимости")
    public static Collection<Object[]> getData() {
        return Arrays.asList(new Object[][]{
                {createStubBun(25.0f), new Ingredient[]{}, 50.0f},
                {createStubBun(25.0f), new Ingredient[]{createStubIngredient(100.0f)}, 150.0f},
                {createStubBun(25.0f), new Ingredient[]{createStubIngredient(100.0f), createStubIngredient(75.0f)}, 225.0f},
                {createStubBun(25.0f), new Ingredient[]{createStubIngredient(100.0f), createStubIngredient(75.0f),
                        createStubIngredient(25.0f), createStubIngredient(50.0f), createStubIngredient(60.0f), createStubIngredient(78.0f)}, 438}
        });
    }

    @Test
    public void getPriceBurgerTest() {
        Burger burger = new Burger();
        burger.setBuns(stubBun);
        for (Ingredient ingredient : stubIngredients) {
            burger.addIngredient(ingredient);
        }
        float actualPrice = burger.getPrice();
        assertEquals("Цена бургера не совпадает с ожидаемой", expectedPrice, actualPrice, 0.001);
    }
}