import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;
import praktikum.IngredientType;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static praktikum.IngredientType.*;

@RunWith(Parameterized.class)
public class BurgerGetReceiptParametersTests {
    private final Bun stubBun;
    private final Ingredient[] stubIngredients;
    private final String expectedReceipt;

    public BurgerGetReceiptParametersTests(Bun stubBun, Ingredient[] stubIngredients, String expectedReceipt) {
        this.stubBun = stubBun;
        this.stubIngredients = stubIngredients;
        this.expectedReceipt = expectedReceipt;
    }

    // Стаб для Bun
    private static Bun createStubBun(String nameBun, float price) {
        Bun bun = Mockito.mock(Bun.class);
        Mockito.when(bun.getPrice()).thenReturn(price);
        Mockito.when(bun.getName()).thenReturn(nameBun);
        return bun;
    }

    // Стаб для Ingredient
    private static Ingredient createStubIngredient(IngredientType type, String nameIngredient, float price) {
        Ingredient ingredient = Mockito.mock(Ingredient.class);
        Mockito.when(ingredient.getPrice()).thenReturn(price);
        Mockito.when(ingredient.getType()).thenReturn(type);
        Mockito.when(ingredient.getName()).thenReturn(nameIngredient);
        return ingredient;
    }

    @Parameterized.Parameters(name = "Тест: проверка рецепта")
    public static Collection<Object[]> getData() {
        return Arrays.asList(new Object[][]{
                {createStubBun("Пшеничная", 50.0f), new Ingredient[]{},
                        "(==== Пшеничная ====)\n" +
                                "(==== Пшеничная ====)\n" +
                                "\nPrice: 100,000000\n"},
                {createStubBun("Пшеничная", 50.0f), new Ingredient[]{createStubIngredient(FILLING, "Котлета", 70)},
                        "(==== Пшеничная ====)\n" +
                                "= filling Котлета =\n" +
                                "(==== Пшеничная ====)\n" +
                                "\nPrice: 170,000000\n"},
                {createStubBun("Пшеничная", 50.0f), new Ingredient[]{createStubIngredient(FILLING, "Котлета", 70),
                        createStubIngredient(SAUCE, "Кетчунез", 35)},
                        "(==== Пшеничная ====)\n" +
                                "= filling Котлета =\n" +
                                "= sauce Кетчунез =\n" +
                                "(==== Пшеничная ====)\n" +
                                "\nPrice: 205,000000\n"},
                {createStubBun("Пшеничная", 50.0f), new Ingredient[]{createStubIngredient(FILLING, "Котлета", 70),
                        createStubIngredient(FILLING, "Сыр", 55), createStubIngredient(SAUCE, "Кетчунез", 35),},
                        "(==== Пшеничная ====)\n" +
                                "= filling Котлета =\n" +
                                "= filling Сыр =\n" +
                                "= sauce Кетчунез =\n" +
                                "(==== Пшеничная ====)\n" +
                                "\nPrice: 260,000000\n"}
        });
    }

    @Test
    public void getReceiptBurgerTest() {
        Burger burger = new Burger();
        burger.setBuns(stubBun);
        for (Ingredient ingredient : stubIngredients) {
            burger.addIngredient(ingredient);
        }
        String actualReceipt = burger.getReceipt();
        assertEquals("Рецепт бургера не совпадает с ожидаемым",
                expectedReceipt,
                actualReceipt.replaceAll("\\r?\\n|\\r", "\n"));
    }
}