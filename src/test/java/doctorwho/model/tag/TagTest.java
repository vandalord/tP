package doctorwho.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void equals_sameName_sameType_returnsTrue() {
        assertEquals(new Allergy("ibuprofen"), new Allergy("ibuprofen"));
        assertEquals(new Condition("diabetes"), new Condition("diabetes"));
    }

    @Test
    public void equals_sameName_differentType_returnsFalse() {
        assertNotEquals(new Allergy("ibuprofen"), new Condition("ibuprofen"));
    }

    @Test
    public void hashCode_sameName_sameType_equal() {
        assertEquals(new Allergy("ibuprofen").hashCode(), new Allergy("ibuprofen").hashCode());
    }

    @Test
    public void toString_correctFormat() {
        assertEquals("[ibuprofen]", new Allergy("ibuprofen").toString());
        assertEquals("[diabetes]", new Condition("diabetes").toString());
    }
}