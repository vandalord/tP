package doctorwho.model.tag;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ConditionTest {

    /**
     * Tests that constructing a {@code Condition} with a null name throws a {@code NullPointerException}.
     */
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Condition(null));
    }

    /**
     * Tests that constructing a {@code Condition} with an invalid name throws an {@code IllegalArgumentException}.
     * Invalid names include empty/blank strings, names with leading or trailing hyphens,
     * hyphens adjacent to spaces, double hyphens, double spaces, special characters,
     * and names exceeding the 50 character limit.
     */
    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        // empty / blank
        assertThrows(IllegalArgumentException.class, () -> new Condition(""));
        assertThrows(IllegalArgumentException.class, () -> new Condition(" "));

        // leading/trailing hyphen
        assertThrows(IllegalArgumentException.class, () -> new Condition("-diabetes"));
        assertThrows(IllegalArgumentException.class, () -> new Condition("diabetes-"));

        // hyphen adjacent to space
        assertThrows(IllegalArgumentException.class, () -> new Condition("hello -world"));
        assertThrows(IllegalArgumentException.class, () -> new Condition("hello- world"));

        // double hyphen
        assertThrows(IllegalArgumentException.class, () -> new Condition("hello--world"));

        // double space
        assertThrows(IllegalArgumentException.class, () -> new Condition("hello  world"));

        // special characters
        assertThrows(IllegalArgumentException.class, () -> new Condition("hello_world"));
        assertThrows(IllegalArgumentException.class, () -> new Condition("hello@world"));

        //exactly 51 characters, one over the limit
        assertThrows(IllegalArgumentException.class, () ->
            new Condition("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    /**
     * Tests that constructing a {@code Condition} with a valid name does not throw any exception.
     * Valid names include single words, multi-word names, hyphenated names, and names
     * up to the 50 character limit. Also verifies the correct string representation.
     */
    @Test
    public void constructor_validName_doesNotThrow() {
        assertDoesNotThrow(() -> new Condition("diabetes"));
        assertDoesNotThrow(() -> new Condition("type 2 diabetes"));
        assertDoesNotThrow(() -> new Condition("chronic heart failure"));
        assertDoesNotThrow(() -> new Condition("beta-blocker"));
        assertDoesNotThrow(() -> new Condition("New-Patient Follow-Up"));
        // exactly 50 characters
        assertDoesNotThrow(() -> new Condition("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void toString_correctFormat() {
        assertEquals("[diabetes]", new Condition("diabetes").toString());
    }

    @Test
    public void equals_sameType_returnsTrue() {
        assertEquals(new Condition("diabetes"), new Condition("diabetes"));
    }

    @Test
    public void hashCode_sameTag_equal() {
        assertEquals(new Condition("diabetes").hashCode(), new Condition("diabetes").hashCode());
    }
}
