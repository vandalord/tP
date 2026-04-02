package doctorwho.model.patient;

import static doctorwho.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NricTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Nric(null));
    }

    @Test
    public void constructor_invalidNric_throwsIllegalArgumentException() {
        String invalidNric = "";
        assertThrows(IllegalArgumentException.class, () -> new Nric(invalidNric));
    }

    @Test
    public void constructor_lowerCaseInput_storesUpperCase() {
        Nric nric = new Nric("s1234567d");
        assertTrue(nric.value.equals("S1234567D"));
    }

    @Test
    public void isValidNric() {
        // null NRIC
        assertThrows(NullPointerException.class, () -> Nric.isValidNric(null));

        // invalid NRICs
        assertFalse(Nric.isValidNric("")); // empty string
        assertFalse(Nric.isValidNric(" ")); // spaces only
        assertFalse(Nric.isValidNric("A1234567D")); // invalid prefix
        assertFalse(Nric.isValidNric("S123456D")); // too few digits
        assertFalse(Nric.isValidNric("S12345678D")); // too many digits
        assertFalse(Nric.isValidNric("S1234567")); // missing final letter
        assertFalse(Nric.isValidNric("S1234567DD")); // extra character
        assertFalse(Nric.isValidNric("S1234A67D")); // non-digit in numeric portion
        assertFalse(Nric.isValidNric("S1234567A")); // invalid checksum letter for S prefix
        assertFalse(Nric.isValidNric("T7654321Z")); // invalid checksum letter for T prefix
        assertFalse(Nric.isValidNric("F1234567Q")); // invalid checksum letter for F prefix
        assertFalse(Nric.isValidNric("G7654321X")); // invalid checksum letter for G prefix
        assertFalse(Nric.isValidNric("M1234567A")); // invalid checksum letter for M prefix

        // valid NRICs
        assertTrue(Nric.isValidNric("S1234567D"));
        assertTrue(Nric.isValidNric("T7654321B"));
        assertTrue(Nric.isValidNric("F1234567N"));
        assertTrue(Nric.isValidNric("G7654321L"));
        assertTrue(Nric.isValidNric("M1234567K"));
        assertTrue(Nric.isValidNric("s1234567d")); // case-insensitive
    }

    @Test
    public void equals() {
        Nric nric = new Nric("S1234567D");

        // same values -> returns true
        assertTrue(nric.equals(new Nric("S1234567D")));

        // same object -> returns true
        assertTrue(nric.equals(nric));

        // null -> returns false
        assertFalse(nric.equals(null));

        // different types -> returns false
        assertFalse(nric.equals(5.0f));

        // different values -> returns false
        assertFalse(nric.equals(new Nric("S7654321F")));
    }
}
