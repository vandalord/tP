package doctorwho.logic.commands;

import static doctorwho.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static doctorwho.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DURATION;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_NOTE;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_STARTTIME;
import static doctorwho.logic.parser.CliSyntax.PREFIX_CONDITION;
import static doctorwho.logic.parser.CliSyntax.PREFIX_DOB;
import static doctorwho.logic.parser.CliSyntax.PREFIX_EMAIL;
import static doctorwho.logic.parser.CliSyntax.PREFIX_NAME;
import static doctorwho.logic.parser.CliSyntax.PREFIX_NRIC;
import static doctorwho.logic.parser.CliSyntax.PREFIX_PHONE;
import static doctorwho.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import doctorwho.commons.core.index.Index;
import doctorwho.logic.commands.exceptions.CommandException;
import doctorwho.model.AddressBook;
import doctorwho.model.Model;
import doctorwho.model.patient.NameContainsKeywordsPredicate;
import doctorwho.model.patient.Patient;
import doctorwho.testutil.EditPatientDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_NRIC_AMY = "S1234567D";
    public static final String VALID_NRIC_BOB = "S2345678H";
    public static final String VALID_DOB_AMY = "01-04-2003";
    public static final String VALID_DOB_BOB = "02-04-2003";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_ALLERGY_IBUPROFEN = "Ibuprofen";
    public static final String VALID_ALLERGY_ASPIRIN = "Aspirin";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String NRIC_DESC_AMY = " " + PREFIX_NRIC + VALID_NRIC_AMY;
    public static final String NRIC_DESC_BOB = " " + PREFIX_NRIC + VALID_NRIC_BOB;
    public static final String DOB_DESC_AMY = " " + PREFIX_DOB + VALID_DOB_AMY;
    public static final String DOB_DESC_BOB = " " + PREFIX_DOB + VALID_DOB_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_NRIC_DESC = " " + PREFIX_NRIC + "1234567A"; // missing prefix letter
    public static final String INVALID_DOB_DESC = " " + PREFIX_DOB + "2003-02-04"; // yyyy-mm-dd
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_ALLERGY_DESC = " "
                                            + PREFIX_ALLERGY + "ibuprofen*"; // '*' not allowed in allergies
    public static final String INVALID_CONDITION_DESC = " "
                                            + PREFIX_CONDITION + "diab*"; // '*' not allowed in conditions

    public static final String VALID_ALLERGY_SULFONAMIDES = "Sulfonamides";
    public static final String VALID_CONDITION_DIABETES = "Diabetes";
    public static final String VALID_CONDITION_HYPERTENSION = "Hypertension";
    public static final String VALID_CONDITION_ASTHMA = "Asthma";

    public static final String ALLERGY_DESC_ASPIRIN = " " + PREFIX_ALLERGY + VALID_ALLERGY_ASPIRIN;
    public static final String ALLERGY_DESC_IBUPROFEN = " " + PREFIX_ALLERGY + VALID_ALLERGY_IBUPROFEN;
    public static final String ALLERGY_DESC_SULFONAMIDES = " " + PREFIX_ALLERGY + VALID_ALLERGY_SULFONAMIDES;

    public static final String CONDITION_DESC_DIABETES = " " + PREFIX_CONDITION + VALID_CONDITION_DIABETES;
    public static final String CONDITION_DESC_HYPERTENSION = " " + PREFIX_CONDITION + VALID_CONDITION_HYPERTENSION;
    public static final String CONDITION_DESC_ASTHMA = " " + PREFIX_CONDITION + VALID_CONDITION_ASTHMA;

    /* ===================== Appointment ===================== */

    // Valid values
    public static final String VALID_APPOINTMENT_STARTTIME = "12-03-2026 14:00";
    public static final int VALID_APPOINTMENT_DURATION = 30;
    public static final String VALID_APPOINTMENT_NOTE = "Routine Checkup";
    public static final String INVALID_APPOINTMENT_STARTTIME = "2026/03/12";
    public static final String INVALID_APPOINTMENT_DURATION = "-10";

    // Starts at 14:15 — within the 14:00–14:30 window
    public static final String VALID_APPOINTMENT_STARTTIME_OVERLAPPING = "12-03-2026 14:15";
    public static final int VALID_APPOINTMENT_DURATION_OVERLAPPING = 30;
    public static final String VALID_APPOINTMENT_NOTE_OVERLAPPING = "Follow-up Checkup";

    // A third appointment that does NOT overlap (starts after 14:30)
    public static final String VALID_APPOINTMENT_STARTTIME_NON_OVERLAPPING = "12-03-2026 15:00";
    public static final int VALID_APPOINTMENT_DURATION_NON_OVERLAPPING = 30;
    public static final String VALID_APPOINTMENT_NOTE_NON_OVERLAPPING = "Evening Consultation";

    public static final String APPOINTMENT_STARTTIME_DESC_VALID =
            " " + PREFIX_APPOINTMENT_STARTTIME + VALID_APPOINTMENT_STARTTIME;
    public static final String APPOINTMENT_DURATION_DESC_VALID =
            " " + PREFIX_APPOINTMENT_DURATION + VALID_APPOINTMENT_DURATION;
    public static final String APPOINTMENT_NOTE_DESC_VALID =
            " " + PREFIX_APPOINTMENT_NOTE + VALID_APPOINTMENT_NOTE;
    public static final String INVALID_APPOINTMENT_STARTTIME_DESC =
            " " + PREFIX_APPOINTMENT_STARTTIME + INVALID_APPOINTMENT_STARTTIME;
    public static final String INVALID_APPOINTMENT_DURATION_DESC =
            " " + PREFIX_APPOINTMENT_DURATION + INVALID_APPOINTMENT_DURATION;

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPatientDescriptor DESC_AMY;
    public static final EditCommand.EditPatientDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPatientDescriptorBuilder().withName(VALID_NAME_AMY)
                .withNric(VALID_NRIC_AMY).withDateOfBirth(VALID_DOB_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withAllergies(VALID_ALLERGY_ASPIRIN).withConditions(VALID_CONDITION_ASTHMA).build();
        DESC_BOB = new EditPatientDescriptorBuilder().withName(VALID_NAME_BOB)
                .withNric(VALID_NRIC_BOB).withDateOfBirth(VALID_DOB_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withAllergies(VALID_ALLERGY_IBUPROFEN, VALID_ALLERGY_ASPIRIN)
                .withConditions(VALID_CONDITION_HYPERTENSION).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered patient list and selected patient in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Patient> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPatientList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPatientList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the patient at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPatientAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPatientList().size());

        Patient patient = model.getFilteredPatientList().get(targetIndex.getZeroBased());
        final String[] splitName = patient.getName().fullName.split("\\s+");
        model.updateFilteredPatientList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPatientList().size());
    }

}
