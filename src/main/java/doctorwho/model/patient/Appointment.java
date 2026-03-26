package doctorwho.model.patient;

import static doctorwho.commons.util.AppUtil.checkArgument;
import static doctorwho.commons.util.CollectionUtil.requireAllNonNull;
import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Represents a Patient's appointment in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateTime(String)}
 */
public class Appointment {

    public static final String STARTTIME_CONSTRAINTS =
            "Appointments should have a start time in 'dd-MM-yyyy HH:mm' format";

    public static final String DURATION_CONSTRAINTS =
            "Appointments should have a finite positive integer for duration (minutes).";

    public static final String NOTE_CONSTRAINTS =
            "Appointments should have a start time in 'dd-MM-yyyy HH:mm' format, "
                    + "a positive integer for duration (minutes), and a note.";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static final String VALIDATION_REGEX = "[^\\x00-\\x1F]*";
    public static final int MAX_LENGTH = 500;

    private final LocalDateTime startTime;
    private final int duration; // in minutes
    private final String note;

    /**
     * Constructs an {@code Appointment}.
     *
     * @param startTimeStr A valid start date-time string.
     * @param duration     A positive integer representing duration in minutes.
     * @param note         A description or comment for the appointment.
     */
    public Appointment(String startTimeStr, int duration, String note) {
        requireAllNonNull(startTimeStr, duration);
        checkArgument(isValidDateTime(startTimeStr), STARTTIME_CONSTRAINTS);
        checkArgument(duration > 0 && duration < Integer.MAX_VALUE, DURATION_CONSTRAINTS);
        checkArgument(isValidNote(note), NOTE_CONSTRAINTS);

        this.startTime = LocalDateTime.parse(startTimeStr, FORMATTER);
        this.duration = duration;
        this.note = note;
    }

    /**
     * Returns true if a given string is in the correct date-time format.
     */
    public static boolean isValidDateTime(String test) {
        try {
            LocalDateTime.parse(test, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true if the duration is a positive integer.
     */
    public static boolean isValidDuration(int duration) {
        return duration > 0 & duration <= Integer.MAX_VALUE;
    }

    /**
     * Returns true if the note is valid.
     * Since notes can be blank, this now returns true for any non-null string.
     */
    public static boolean isValidNote(String test) {
        if (test.length() > MAX_LENGTH) {
            return false;
        }
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Helper method to calculate the end time based on duration.
     */
    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    /**
     * Checks if this appointment overlaps with another appointment.
     */
    public boolean isOverlapping(Appointment other) {
        requireNonNull(other);
        return this.startTime.isBefore(other.getEndTime()) && other.startTime.isBefore(this.getEndTime());
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return String.format("At: %s (%d mins) | Note: %s",
                startTime.format(FORMATTER), duration, note.isEmpty() ? "NA" : note);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Appointment)) {
            return false;
        }
        Appointment o = (Appointment) other;
        return startTime.equals(o.startTime)
                && duration == o.duration
                && note.equals(o.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, duration, note);
    }
}
