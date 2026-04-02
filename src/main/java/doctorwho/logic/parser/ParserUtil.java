package doctorwho.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import doctorwho.commons.core.index.Index;
import doctorwho.commons.util.StringUtil;
import doctorwho.logic.parser.exceptions.ParseException;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Nric;
import doctorwho.model.patient.Phone;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_POSITIVE_INTEGER = "Number is not a non-zero unsigned integer. "
            + "Ensure the number is more than 0 and less than or equal to " + Integer.MAX_VALUE;
    public static final String MESSAGE_INVALID_DATE_FORMAT = "Date should be in 'dd-MM-yyyy' format.";
    public static final String MESSAGE_INVALID_DATE = "Date is invalid.";

    private static final DateTimeFormatter APPOINTMENT_DATE_FORMATTER = DateTimeFormatter
        .ofPattern("dd-MM-uuuu")
        .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String nric} into a {@code Nric}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code nric} is invalid.
     */
    public static Nric parseNric(String nric) throws ParseException {
        requireNonNull(nric);
        String trimmedNric = nric.trim();
        if (!Nric.isValidNric(trimmedNric)) {
            throw new ParseException(Nric.MESSAGE_CONSTRAINTS);
        }
        return new Nric(trimmedNric);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} of allergies.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if any given allergy is invalid.
     */
    public static Set<Tag> parseAllergies(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            try {
                tagSet.add(new Allergy(tagName.trim()));
            } catch (IllegalArgumentException e) {
                throw new ParseException(Allergy.MESSAGE_CONSTRAINTS);
            }
        }
        return tagSet;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} of conditions.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if any given condition is invalid.
     */
    public static Set<Tag> parseConditions(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            try {
                tagSet.add(new Condition(tagName.trim()));
            } catch (IllegalArgumentException e) {
                throw new ParseException(Condition.MESSAGE_CONSTRAINTS);
            }
        }
        return tagSet;
    }

    /**
     * Parses {@code candidate} integer into an int and returns it. Leading and trailing
     * whitespaces will be trimmed.
     *
     * @throws ParseException if the specified candidate is invalid (not non-zero unsigned integer).
     */
    public static int parsePositiveInteger(String candidate) throws ParseException {
        String trimmedCandidate = candidate.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedCandidate)) {
            throw new ParseException(MESSAGE_INVALID_POSITIVE_INTEGER);
        }
        return Integer.parseInt(candidate);
    }

    /**
     * Parses {@code date} into a {@code LocalDate} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the specified date is invalid.
     */
    public static LocalDate parseAppointmentDate(String date) throws ParseException {
        requireNonNull(date);
        String trimmedDate = date.trim();

        if (!trimmedDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
            throw new ParseException(MESSAGE_INVALID_DATE_FORMAT);
        }

        try {
            return LocalDate.parse(trimmedDate, APPOINTMENT_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INVALID_DATE, e);
        }
    }
}
