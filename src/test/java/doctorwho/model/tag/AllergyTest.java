package doctorwho.model.tag;

import static doctorwho.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class AllergyTest {
    /**
     * Tests that constructing an {@code Allergy} with a null name throws a {@code NullPointerException}.
     */
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Allergy(null));
    }

    /**
     * Tests that constructing an {@code Allergy} with an invalid name throws an {@code IllegalArgumentException}.
     * Invalid names include empty/blank strings, names with leading or trailing hyphens,
     * hyphens adjacent to spaces, double hyphens, double spaces, special characters,
     * and names exceeding the 30 character limit.
     */
    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        // empty / blank
        assertThrows(IllegalArgumentException.class, () -> new Allergy(""));
        assertThrows(IllegalArgumentException.class, () -> new Allergy(" "));

        // leading/trailing hyphen
        assertThrows(IllegalArgumentException.class, () -> new Allergy("-ibuprofen"));
        assertThrows(IllegalArgumentException.class, () -> new Allergy("ibuprofen-"));

        // hyphen adjacent to space
        assertThrows(IllegalArgumentException.class, () -> new Allergy("hello -world"));
        assertThrows(IllegalArgumentException.class, () -> new Allergy("hello- world"));

        // double hyphen
        assertThrows(IllegalArgumentException.class, () -> new Allergy("hello--world"));

        // double space
        assertThrows(IllegalArgumentException.class, () -> new Allergy("hello  world"));

        // special characters
        assertThrows(IllegalArgumentException.class, () -> new Allergy("hello_world"));
        assertThrows(IllegalArgumentException.class, () -> new Allergy("hello@world"));

        // exactly 31 characters, one over the limit
        assertThrows(IllegalArgumentException.class, () ->
            new Allergy("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    /**
     * Tests that constructing an {@code Allergy} with a valid name does not throw any exception.
     * Valid names include single words, multi-word names, hyphenated names, and names
     * up to the 30 character limit. Also verifies the correct string representation.
     */
    @Test
    public void constructor_validName_doesNotThrow() {
        assertDoesNotThrow(() -> new Allergy("ibuprofen"));
        assertDoesNotThrow(() -> new Allergy("penicillin V"));
        assertDoesNotThrow(() -> new Allergy("beta-blocker"));
        assertDoesNotThrow(() -> new Allergy("amoxicillin clavulanate"));
        // exactly 30 characters
        assertDoesNotThrow(() -> new Allergy("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void equals_sameType_returnsTrue() {
        assertEquals(new Allergy("ibuprofen"), new Allergy("ibuprofen"));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertNotEquals(new Allergy("ibuprofen"), new Condition("ibuprofen"));
    }

    @Test
    public void hashCode_sameTag_equal() {
        assertEquals(new Allergy("ibuprofen").hashCode(), new Allergy("ibuprofen").hashCode());
    }

    @Test
    public void toString_correctFormat() {
        assertEquals("[ibuprofen]", new Allergy("ibuprofen").toString());
    }
}
