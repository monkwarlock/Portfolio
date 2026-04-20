import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;

import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BurgerTests {

    @Mock
    private Bun mockBun;
    @Mock
    private List<Ingredient> mockIngredients;
    @Mock
    private Ingredient mockIngredient;

    @InjectMocks
    private Burger burger;

    @Test
    public void testSetBunsCheckingTheMethodNonNull() {
        burger.setBuns(mockBun);
        assertSame("Поле bun должно быть равно переданному объекту", mockBun, burger.bun);
    }

    @Test
    public void testAddIngredientCheckingTheMethodOperation() {
        burger.addIngredient(mockIngredient);
        verify(mockIngredients, times(1)).add(mockIngredient);
    }

    @Test
    public void testRemoveIngredientCheckingTheMethodOperation() {
        burger.removeIngredient(1);
        verify(mockIngredients, times(1)).remove(1);
    }

    @Test
    public void testWhenCallingTheMoveIngredientTriggeredRemove() {
        burger.moveIngredient(2, 8);
        verify(mockIngredients, times(1)).remove(2);
    }

    @Test
    public void testMoveIngredientCheckingTheMethodOperation() {
        when(mockIngredients.remove(2)).thenReturn(mockIngredient);
        burger.moveIngredient(2, 8);
        verify(mockIngredients, times(1)).add(8, mockIngredient);
    }
}