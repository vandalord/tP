package seedu.address.model.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

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
    public void constructor_invalidDateTime_throwsIllegalArgumentException() {
        String invalidStart = "2026/03/12 14:00"; // Wrong format
        assertThrows(IllegalArgumentException.class, () -> new Appointment(invalidStart, VALID_DURATION, VALID_NOTE));
    }

    @Test
    public void constructor_invalidDuration_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Appointment(VALID_START, 0, VALID_NOTE));
        assertThrows(IllegalArgumentException.class, () -> new Appointment(VALID_START, -10, VALID_NOTE));
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
    public void constructor_nullNote_throwsIllegalArgumentException() {
        assertThrows(NullPointerException.class, () ->
                new Appointment(VALID_START, VALID_DURATION, null));
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
