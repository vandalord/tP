package doctorwho.model.patient;

import static doctorwho.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class AppointmentTest {

    private static final String VALID_START = "12-03-2026 14:00";
    private static final int VALID_DURATION = 30;
    private static final String VALID_NOTE = "Routine Checkup";

    @Test
    public void constructor_nullStartDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Appointment(null, VALID_DURATION, VALID_NOTE));
    }

    @Test
    public void constructor_invalidDateTimeFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Appointment(
                "2026/03/12 14:00", VALID_DURATION, VALID_NOTE)); // wrong separator amd order
        assertThrows(IllegalArgumentException.class, () -> new Appointment(
                "1-1-2024 09:00", VALID_DURATION, VALID_NOTE)); // missing leading 0s
        assertThrows(IllegalArgumentException.class, () -> new Appointment(
                "01/01/2024 09:00", VALID_DURATION, VALID_NOTE)); // wrong separator
        assertThrows(IllegalArgumentException.class, () -> new Appointment(
                "01-01-2024", VALID_DURATION, VALID_NOTE)); // missing time
    }

    @Test
    public void constructor_invalidDuration_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Appointment(VALID_START, 0, VALID_NOTE));
        assertThrows(IllegalArgumentException.class, () -> new Appointment(VALID_START, -10, VALID_NOTE));
    }


    @Test
    void isValidDateTime_invalidCalendarDate_returnsFalse() {
        assertFalse(Appointment.isValidDateTime("31-02-2024 10:00")); // Feb 31
        assertFalse(Appointment.isValidDateTime("29-02-2023 10:00")); // not leap year
    }

    @Test
    void isValidDateTime_validLeapYear_returnsTrue() {
        assertTrue(Appointment.isValidDateTime("29-02-2024 10:00")); // valid leap year
    }

    @Test
    void isValidDateTime_invalidTimeBounds_returnsFalse() {
        assertFalse(Appointment.isValidDateTime("01-01-2024 24:00")); // invalid hour
        assertFalse(Appointment.isValidDateTime("01-01-2024 23:60")); // invalid minute
    }

    @Test
    void isValidDateTime_bceYear_returnsFalse() {
        assertFalse(Appointment.isValidDateTime("01-01--0001 00:00")); // allowed by uuuu
    }

    @Test
    void isValidDateTime_yearZeroEdgeCase_returnsTrue() {
        assertTrue(Appointment.isValidDateTime("01-01-0000 00:00")); // allowed by uuuu
    }

    @Test
    public void getStartTime_success() {
        String startTimeStr = "12-03-2026 14:00";
        Appointment appt = new Appointment(startTimeStr, VALID_DURATION, VALID_NOTE);

        LocalDateTime expectedTime = LocalDateTime.parse(startTimeStr, Appointment.FORMATTER);
        assertEquals(expectedTime, appt.getStartTime());
    }

    @Test
    public void getDuration_success() {
        int duration = 45;
        Appointment appt = new Appointment(VALID_START, duration, VALID_NOTE);
        assertEquals(duration, appt.getDuration());
    }

    @Test
    public void getNote_success() {
        String note = "Patient requires follow-up in 2 weeks.";
        Appointment appt = new Appointment(VALID_START, VALID_DURATION, note);
        assertEquals(note, appt.getNote());
    }

    @Test
    public void getNote_blankNote_success() {
        String blankNote = "";
        Appointment appt = new Appointment(VALID_START, VALID_DURATION, blankNote);
        assertEquals("", appt.getNote());
    }

    @Test
    public void constructor_invalidStartDate_throwsIllegalArgumentException() {
        String wrongPattern = "2026-03-12 14:00";
        assertThrows(IllegalArgumentException.class, () ->
                new Appointment(wrongPattern, VALID_DURATION, VALID_NOTE));

        String impossibleDate = "32-01-2026 14:00";
        assertThrows(IllegalArgumentException.class, () ->
                new Appointment(impossibleDate, VALID_DURATION, VALID_NOTE));
    }

    @Test
    public void getEndTime_success() {
        Appointment appt = new Appointment("12-03-2026 14:00", 30, VALID_NOTE);
        LocalDateTime expectedEnd = LocalDateTime.of(2026, 3, 12, 14, 30);
        assertEquals(expectedEnd, appt.getEndTime());
    }

    @Test
    public void isOverlapping_overlappingAppointment_returnsTrue() {
        Appointment appt = new Appointment("12-03-2026 14:00", 60, VALID_NOTE); // 14:00 - 15:00

        // Overlaps: Starts during appt
        assertTrue(appt.isOverlapping(new Appointment("12-03-2026 14:30", 30, "Overlap")));

        // Overlaps: Ends during appt
        assertTrue(appt.isOverlapping(new Appointment("12-03-2026 13:30", 45, "Overlap")));
    }

    @Test
    public void isOverlapping_nonOverlappingAppointment_returnsFalse() {
        Appointment appt = new Appointment("12-03-2026 14:00", 60, VALID_NOTE); // 14:00 - 15:00

        // Does not overlap: Back-to-back
        assertFalse(appt.isOverlapping(new Appointment("12-03-2026 15:00", 30, "Follow-up")));

        // Does not overlap: Different day
        assertFalse(appt.isOverlapping(new Appointment("13-03-2026 14:00", 60, "Tomorrow")));
    }

    @Test
    public void isValidNote_nullNote_throwsNullPointerException() {
        // null note
        assertThrows(NullPointerException.class, () -> Appointment.isValidNote(null));
    }

    @Test
    public void isValidNote_invalidNotes_returnsFalse() {
        // length strictly greater than 500 characters
        assertFalse(Appointment.isValidNote("a".repeat(501)));

        // contains illegal invisible control characters (e.g., form feed \f)
        assertFalse(Appointment.isValidNote("Patient \f record"));
    }

    @Test
    public void isValidNote_validNotes_returnsTrue() {
        // blank/empty notes
        assertTrue(Appointment.isValidNote("")); // empty string
        assertTrue(Appointment.isValidNote("   ")); // spaces only

        // standard notes
        assertTrue(Appointment.isValidNote("Patient is recovering well."));

        // notes with symbols and command-like prefixes
        assertTrue(Appointment.isValidNote("Temp: 38.5°C, BP: 120|80. Check d|c status."));

        // notes with HTML-like tags (ensuring no aggressive escaping happens here)
        assertTrue(Appointment.isValidNote("Patient needs a <break>"));

        // boundary condition: exactly maximum allowed length
        assertTrue(Appointment.isValidNote("a".repeat(500)));
    }

    @Test
    public void equals() {
        Appointment appt = new Appointment(VALID_START, VALID_DURATION, VALID_NOTE);

        // same values -> returns true
        assertTrue(appt.equals(new Appointment(VALID_START, VALID_DURATION, VALID_NOTE)));

        // same object -> returns true
        assertTrue(appt.equals(appt));

        // null -> returns false
        assertFalse(appt.equals(null));

        // different types -> returns false
        assertFalse(appt.equals(5.0f));

        // different startTime -> returns false
        assertFalse(appt.equals(new Appointment("12-03-2026 15:00", VALID_DURATION, VALID_NOTE)));

        // different duration -> returns false
        assertFalse(appt.equals(new Appointment(VALID_START, 60, VALID_NOTE)));

        // different note -> returns false
        assertFalse(appt.equals(new Appointment(VALID_START, VALID_DURATION, "Different Note")));
    }

    @Test
    public void toStringMethod() {
        Appointment appt = new Appointment("12-03-2026 14:00", 30, "Checkup");
        String expected = "At: 12-03-2026 14:00 (30 mins) | Note: Checkup";
        assertEquals(expected, appt.toString());
    }
}
