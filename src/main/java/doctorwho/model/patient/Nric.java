package doctorwho.model.patient;

import static doctorwho.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Patient's NRIC in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNric(String)}
 */
public class Nric {

    public static final String MESSAGE_CONSTRAINTS =
        "NRIC must be in the format S/T/F/G/M followed by 7 digits and a valid checksum letter, "
                    + "e.g. S1234567D";
    public static final String VALIDATION_REGEX = "(?i)[STFGM]\\d{7}[A-Z]";

    private static final int[] WEIGHTS = {2, 7, 6, 5, 4, 3, 2};
    private static final String ST_CHECKSUM_TABLE = "ABCDEFGHIZJ";
    private static final String FG_CHECKSUM_TABLE = "KLMNPQRTUWX";
    private static final String M_CHECKSUM_TABLE = "KLJNPQRTUWX";

    public final String value;

    /**
     * Constructs a {@code Nric}.
     *
     * @param nric A valid NRIC.
     */
    public Nric(String nric) {
        requireNonNull(nric);
        checkArgument(isValidNric(nric), MESSAGE_CONSTRAINTS);
        value = nric.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid NRIC.
     */
    public static boolean isValidNric(String test) {
        requireNonNull(test);
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }

        String nric = test.toUpperCase();
        char prefix = nric.charAt(0);
        String digits = nric.substring(1, 8);
        char expectedChecksumLetter = getChecksumLetter(prefix, digits);

        return nric.charAt(8) == expectedChecksumLetter;
    }

    /**
     * Returns the expected checksum letter for the given NRIC/FIN prefix and 7-digit body.
     */
    private static char getChecksumLetter(char prefix, String digits) {
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * WEIGHTS[i];
        }

        if (prefix == 'G' || prefix == 'T') {
            sum += 4;
        } else if (prefix == 'M') {
            sum += 3;
        }

        int remainder = sum % 11;
        int checkDigit = 11 - (remainder + 1);

        if (prefix == 'S' || prefix == 'T') {
            return ST_CHECKSUM_TABLE.charAt(checkDigit);
        } else if (prefix == 'F' || prefix == 'G') {
            return FG_CHECKSUM_TABLE.charAt(checkDigit);
        }

        return M_CHECKSUM_TABLE.charAt(checkDigit);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Nric)) {
            return false;
        }

        Nric otherNric = (Nric) other;
        return value.equals(otherNric.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
