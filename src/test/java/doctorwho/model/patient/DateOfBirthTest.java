package doctorwho.model.patient;

import static doctorwho.logic.parser.ParserUtil.DATE_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class DateOfBirthTest {
    @Test
    public void constructor_validDate_success() {
        // EP: Valid date, correct format (dd-MM-uuuu)
        DateOfBirth dob = new DateOfBirth("05-09-2002");
        assertEquals("05-09-2002", dob.toString());
    }

    @Test
    public void constructor_invalidDate_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new DateOfBirth("2002-09-05"));
    }

    // Key Equivalent Partitions and Boundary Values for date formats
    // Valid Inputs
    // 1. Correct format + valid date (normal case)
    // 2. Boundary valid dates (e.g. "today")
    // 3. Leap year valid date
    // Invalid Inputs
    // 4. Wrong format
    // 5. Non-existent dates and invalid numeric ranges (day/month)
    // 6. Future dates
    // 7. Null input
    // 8. Empty input

    // EP: Valid date normal
    @Test
    public void isValidDateOfBirth_validNormalDate_returnsTrue() {
        // BVA year 0000 to now
        assertTrue(DateOfBirth.isValidDateOfBirth("01-01-0000"));
        assertTrue(DateOfBirth.isValidDateOfBirth("15-08-1995"));
        assertTrue(DateOfBirth.isValidDateOfBirth(LocalDate.now().minusDays(1)
                .format(DATE_FORMATTER))); // yesterday
        assertTrue(DateOfBirth.isValidDateOfBirth(LocalDate.now().format(DATE_FORMATTER)));
    }

    // EP: Valid leap year date
    @Test
    public void isValidDateOfBirth_validLeapYear_returnsTrue() {
        assertTrue(DateOfBirth.isValidDateOfBirth("29-02-2020"));
    }

    // EP: Wrong format
    @Test
    public void isValidDateOfBirth_wrongFormat_returnsFalse() {
        // Wrong format (yyyy-MM-dd) or (dd/MM/yyyy)
        assertFalse(DateOfBirth.isValidDateOfBirth("2002-09-05"));
        assertFalse(DateOfBirth.isValidDateOfBirth("05/09/2002"));
        // Valid format violation (single digit day/month)
        assertFalse(DateOfBirth.isValidDateOfBirth("5-9-2002"));
    }

    // EP: Non-existent dates
    @Test
    public void isValidDateOfBirth_nonExistentDate_returnsFalse() {
        // Non-existent (February doesn't have 31st)
        assertFalse(DateOfBirth.isValidDateOfBirth("31-02-2020"));
        // Day out of range (>31)
        assertFalse(DateOfBirth.isValidDateOfBirth("32-01-2000"));
        // Month out of range (>12)
        assertFalse(DateOfBirth.isValidDateOfBirth("10-13-2000"));
    }

    // EP: Invalid leap date
    @Test
    public void isValidDateOfBirth_invalidLeapDate_returnsFalse() {
        assertFalse(DateOfBirth.isValidDateOfBirth("29-02-2019"));
    }

    // EP: Future dates
    @Test
    public void isValidDateOfBirth_futureDate_returnsFalse() {
        // BVA: tomorrow til max
        assertFalse(DateOfBirth.isValidDateOfBirth(LocalDate.now().plusDays(1).format(DATE_FORMATTER)));
    }

    // EP: Null input
    @Test
    public void constructor_null_throwsNullPointerException() {
        // EP: Null input
        assertThrows(NullPointerException.class, () -> new DateOfBirth(null));
    }

    // EP: Empty input
    @Test
    public void isValidDateOfBirth_emptyString_returnsFalse() {
        // EP: Empty string
        assertFalse(DateOfBirth.isValidDateOfBirth(""));
    }

    // Hard code the "today" date in tests to that expected outputs stay static.
    // This is Regression Testing with some Stubs concepts from Unit Testing.

    // ================= YEARS =================
    @Test
    public void getAge_moreThanOneYear_returnsYears() {
        // EP: Age > 1 year
        DateOfBirth dob = new DateOfBirth("05-09-2000");
        LocalDate today = LocalDate.of(2025, 9, 6);

        assertEquals("25 years", dob.getAge(today));
    }

    @Test
    public void getAge_exactlyOneYear_returnsOneYear() {
        // Boundary Case: Exactly 1 year old
        DateOfBirth dob = new DateOfBirth("05-09-2024");
        LocalDate today = LocalDate.of(2025, 9, 5);

        assertEquals("1 year", dob.getAge(today));
    }

    @Test
    public void getAge_oneYearMinusOneDay_returnsElevenMonths() {
        // Boundary Case: Exactly 1 year old
        DateOfBirth dob = new DateOfBirth("06-09-2024");
        LocalDate today = LocalDate.of(2025, 9, 5);

        assertEquals("11 months", dob.getAge(today));
    }

    @Test
    public void getAge_justBeforeBirthday_returnsPreviousYear() {
        // Boundary Case: One day before turning older
        DateOfBirth dob = new DateOfBirth("05-09-2000");
        LocalDate today = LocalDate.of(2025, 9, 4);

        assertEquals("24 years", dob.getAge(today));
    }

    @Test
    public void getAge_birthdayToday_returnsIncrementedAge() {
        // Boundary Case: Birthday today
        DateOfBirth dob = new DateOfBirth("05-09-2000");
        LocalDate today = LocalDate.of(2025, 9, 5);

        assertEquals("25 years", dob.getAge(today));
    }

    // ================= MONTHS =================
    @Test
    public void getAge_lessThanOneYear_returnsMonths() {
        // EP: Age < 1 year
        DateOfBirth dob = new DateOfBirth("01-01-2025");
        LocalDate today = LocalDate.of(2025, 4, 1);

        assertEquals("3 months", dob.getAge(today));
    }

    @Test
    public void getAge_exactlyOneMonth_returnsOneMonth() {
        // Boundary Case: Exactly 1 month old
        DateOfBirth dob = new DateOfBirth("01-03-2025");
        LocalDate today = LocalDate.of(2025, 4, 1);

        assertEquals("1 month", dob.getAge(today));
    }

    @Test
    public void getAge_multipleMonths_returnsCorrectMonths() {
        // EP: Multiple months (< 1 year)
        DateOfBirth dob = new DateOfBirth("01-01-2025");
        LocalDate today = LocalDate.of(2025, 6, 1);

        assertEquals("5 months", dob.getAge(today));
    }

    // ================= LESS THAN 1 MONTH =================
    @Test
    public void getAge_lessThanOneMonth_returnsZeroMonths() {
        // Edge Case: Less than 1 month old
        DateOfBirth dob = new DateOfBirth("15-03-2025");
        LocalDate today = LocalDate.of(2025, 3, 20);

        assertEquals("0 months", dob.getAge(today));
    }

    @Test
    public void getAge_today_returnsZeroMonths() {
        // Edge Case: Less than 1 month old
        DateOfBirth dob = new DateOfBirth("20-03-2025");
        LocalDate today = LocalDate.of(2025, 3, 20);

        assertEquals("0 months", dob.getAge(today));
    }

    // ================= LEAP YEAR =================
    @Test
    public void getAge_leapYearBirthday_nonLeapYear() {
        // Edge Case: Born on Feb 29
        DateOfBirth dob = new DateOfBirth("29-02-2020");
        LocalDate today = LocalDate.of(2025, 2, 28);

        assertEquals("4 years", dob.getAge(today));
    }

    @Test
    public void getAge_leapYearBirthday_onLeapYear() {
        // Edge Case: Leap year exact birthday
        DateOfBirth dob = new DateOfBirth("29-02-2020");
        LocalDate today = LocalDate.of(2024, 2, 29);

        assertEquals("4 years", dob.getAge(today));
    }

    @Test
    public void getAge_actualToday_returnsZeroMonths() {
        LocalDate today = LocalDate.now();
        DateOfBirth dob = new DateOfBirth(today.format(DATE_FORMATTER));

        assertEquals("0 months", dob.getAge(today));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        // EP: Same date values
        DateOfBirth dob1 = new DateOfBirth("05-09-2002");
        DateOfBirth dob2 = new DateOfBirth("05-09-2002");
        assertEquals(dob1, dob2);
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        // EP: Different valid dates
        DateOfBirth dob1 = new DateOfBirth("05-09-2002");
        DateOfBirth dob2 = new DateOfBirth("06-09-2002");
        assertNotEquals(dob1, dob2);
    }

    @Test
    public void equals() {
        DateOfBirth dob = new DateOfBirth("01-04-2003");

        // same values -> returns true
        assertTrue(dob.equals(new DateOfBirth("01-04-2003")));

        // same object -> returns truenew Nric("S7654321F")
        assertTrue(dob.equals(dob));

        // null -> returns false
        assertFalse(dob.equals(null));

        // different types -> returns false
        assertFalse(dob.equals(5.0f));

        // different values -> returns false
        assertFalse(dob.equals(new DateOfBirth("02-04-2003")));
    }
}
