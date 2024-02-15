package pt.ua.deti.tqs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for TqsStack.
 */
public class AppTest {
    private TqsStack<Integer> stack;

    @BeforeEach
    void setup() {
        this.stack = new TqsStack<>();
    }

    @Test
    void stackIsEmptyAtStart() {
        assertTrue(stack.isEmpty());
    }

    @Test
    void stackSizeIsZeroAtStart() {
        assertEquals(0, stack.size());
    }

    @Test
    void stackSizeIsNAfterNPushes() {
        int n = 3;
        for (int i = 0; i < n; i++) {
            stack.push(0);
        }

        assertFalse(stack.isEmpty());
        assertEquals(n, stack.size());
    }

    @Test
    void testPushAndPop() {
        stack.push(0);
        assertEquals(0, stack.pop());
    }

    @Test
    void testPushAndPeek() {
        stack.push(0);
        assertEquals(0, stack.peek());
        assertEquals(1, stack.size());
    }

    @Test
    void stackIsEmptyAfterNPushesAndPops() {
        int n = 3;
        for (int i = 0; i < n; i++) {
            stack.push(0);
        }
        for (int i = 0; i < n; i++) {
            stack.pop();
        }

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    void popFromExptyStack() {
        assertThrows(NoSuchElementException.class, stack::pop);
    }

    @Test
    void peekFromExptyStack() {
        assertThrows(NoSuchElementException.class, stack::peek);
    }

    @Test
    void pushAboveLimit() {
        int n = 15;
        assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i < n; i++) {
                stack.push(0);
            }
        });
    }
}
