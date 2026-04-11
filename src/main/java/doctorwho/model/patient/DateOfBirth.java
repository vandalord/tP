package doctorwho.model.patient;

import static doctorwho.logic.parser.ParserUtil.DATE_FORMATTER;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.Period;

import doctorwho.logic.parser.ParserUtil;
import doctorwho.logic.parser.exceptions.ParseException;

/**
 * Represents a Person's date of birth in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateOfBirth(String)}
 */
public class DateOfBirth {

    public static final String MESSAGE_CONSTRAINTS =
            "Date of birth must be in the format 'dd-mm-yyyy' and be a valid past date.";

    public final LocalDate value;

    /**
     * Constructs a {@code DateOfBirth}.
     *
     * @param dateOfBirth A valid date of birth string.
     */
    public DateOfBirth(String dateOfBirth) {
        requireNonNull(dateOfBirth);
        if (!isValidDateOfBirth(dateOfBirth)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        value = LocalDate.parse(dateOfBirth, DATE_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date of birth.
     */
    public static boolean isValidDateOfBirth(String test) {
        requireNonNull(test);
        try {
            LocalDate date = ParserUtil.parseDate(test);
            return !date.isAfter(LocalDate.now()); // must not be in the future
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value.format(DATE_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DateOfBirth)) {
            return false;
        }

        DateOfBirth otherDateOfBirth = (DateOfBirth) other;
        return value.equals(otherDateOfBirth.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Returns the age of this date of birth with respect to the date supplied.
     * Mainly to improve testability.
     *
     * @param compare The date to compare against
     * @return The number of year since this date of birth or number of months if <1 year.
     */
    public String getAge(LocalDate compare) {
        Period period = Period.between(value, compare); // relative to compare

        int years = period.getYears();
        int months = period.getMonths();

        if (years > 0) {
            return years + (years == 1 ? " year" : " years");
        } else if (months > 0) {
            return months + (months == 1 ? " month" : " months");
        } else {
            return "0 months";
        }
    }

    /**
     * Returns the age of this date of birth with respect to the current date.
     *
     * @return The number of year since this date of birth or number of months if <1 year.
     */
    public String getAge() {
        return getAge(LocalDate.now());
    }
}
