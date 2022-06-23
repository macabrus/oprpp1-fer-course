package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectStackTest {

    private ObjectStack stack;

    @BeforeEach
    private void setup() {
        stack = new ObjectStack();
    }

    @Test
    public void pushAndPopTest() {
        // pushing onto stack
        for (int i = 0; i < 1000; i++) {
            stack.push(i);
        }
        // doesn't modify stack...
        stack.peek();
        // popping elements from stack
        for (int i = 999; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }
    }

    @Test
    public void pushNullThrows() {
        assertThrows(NullPointerException.class, () -> stack.push(null));
    }

}