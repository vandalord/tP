package doctorwho.logic.parser;

import static doctorwho.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static doctorwho.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static doctorwho.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.ALLERGY_DESC_ASPIRIN;
import static doctorwho.logic.commands.CommandTestUtil.ALLERGY_DESC_IBUPROFEN;
import static doctorwho.logic.commands.CommandTestUtil.CONDITION_DESC_ASTHMA;
import static doctorwho.logic.commands.CommandTestUtil.CONDITION_DESC_DIABETES;
import static doctorwho.logic.commands.CommandTestUtil.CONDITION_DESC_HYPERTENSION;
import static doctorwho.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static doctorwho.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static doctorwho.logic.commands.CommandTestUtil.INVALID_ALLERGY_DESC;
import static doctorwho.logic.commands.CommandTestUtil.INVALID_CONDITION_DESC;
import static doctorwho.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static doctorwho.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static doctorwho.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static doctorwho.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static doctorwho.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static doctorwho.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static doctorwho.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_IBUPROFEN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_ASTHMA;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_DIABETES;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_HYPERTENSION;
import static doctorwho.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static doctorwho.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static doctorwho.logic.parser.CliSyntax.PREFIX_EMAIL;
import static doctorwho.logic.parser.CliSyntax.PREFIX_NAME;
import static doctorwho.logic.parser.CliSyntax.PREFIX_PHONE;
import static doctorwho.logic.parser.CommandParserTestUtil.assertParseFailure;
import static doctorwho.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static doctorwho.testutil.TypicalPatients.AMY;
import static doctorwho.testutil.TypicalPatients.BOB;

import org.junit.jupiter.api.Test;

import doctorwho.logic.Messages;
import doctorwho.logic.commands.AddCommand;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Patient;
import doctorwho.model.patient.Phone;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.testutil.PatientBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        // test with allergy only
        Patient expectedPatient = new PatientBuilder(BOB).withAllergies(VALID_ALLERGY_IBUPROFEN)
            .withConditions().build();
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + ALLERGY_DESC_IBUPROFEN, new AddCommand(expectedPatient));

        // test with multiple allergies
        Patient expectedPatientMultipleAllergies = new PatientBuilder(BOB)
            .withAllergies(VALID_ALLERGY_ASPIRIN, VALID_ALLERGY_IBUPROFEN).withConditions().build();
        assertParseSuccess(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + ALLERGY_DESC_ASPIRIN
                + ALLERGY_DESC_IBUPROFEN,
            new AddCommand(expectedPatientMultipleAllergies));

        // test with condition only
        Patient expectedPatientConditionOnly = new PatientBuilder(BOB).withAllergies()
            .withConditions(VALID_CONDITION_HYPERTENSION).build();
        assertParseSuccess(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + CONDITION_DESC_HYPERTENSION,
            new AddCommand(expectedPatientConditionOnly));

        // test with multiple conditions
        Patient expectedPatientMultipleConditions = new PatientBuilder(BOB).withAllergies()
            .withConditions(VALID_CONDITION_HYPERTENSION, VALID_CONDITION_DIABETES).build();
        assertParseSuccess(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + CONDITION_DESC_HYPERTENSION
                + CONDITION_DESC_DIABETES,
            new AddCommand(expectedPatientMultipleConditions));

        // allergy and condition test
        Patient expectedPatientWithBoth = new PatientBuilder(BOB).withAllergies(VALID_ALLERGY_IBUPROFEN)
            .withConditions(VALID_CONDITION_HYPERTENSION).build();
        assertParseSuccess(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + ALLERGY_DESC_IBUPROFEN
                + CONDITION_DESC_HYPERTENSION,
            new AddCommand(expectedPatientWithBoth));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedpatientstring = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + ALLERGY_DESC_ASPIRIN;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedpatientstring,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedpatientstring,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedpatientstring,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedpatientstring,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedpatientstring + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + validExpectedpatientstring,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedpatientstring,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedpatientstring,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedpatientstring,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedpatientstring,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedpatientstring + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedpatientstring + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedpatientstring + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedpatientstring + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Patient expectedPatient = new PatientBuilder(AMY).withAllergies().withConditions().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                new AddCommand(expectedPatient));

        // only allergy
        Patient expectedPatientAllergyOnly = new PatientBuilder(AMY).withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + ALLERGY_DESC_ASPIRIN,
            new AddCommand(expectedPatientAllergyOnly));

        // only condition
        Patient expectedPatientConditionOnly = new PatientBuilder(AMY).withAllergies()
            .withConditions(VALID_CONDITION_ASTHMA).build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + CONDITION_DESC_ASTHMA,
            new AddCommand(expectedPatientConditionOnly));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + ALLERGY_DESC_IBUPROFEN + ALLERGY_DESC_IBUPROFEN, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + ALLERGY_DESC_IBUPROFEN + ALLERGY_DESC_ASPIRIN, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + ALLERGY_DESC_IBUPROFEN + ALLERGY_DESC_ASPIRIN, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + ALLERGY_DESC_IBUPROFEN + ALLERGY_DESC_ASPIRIN, Address.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + ALLERGY_DESC_IBUPROFEN + ALLERGY_DESC_ASPIRIN,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        // invalid allergy
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
            + INVALID_ALLERGY_DESC, Allergy.MESSAGE_CONSTRAINTS);

        // invalid condition
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
            + INVALID_CONDITION_DESC, Condition.MESSAGE_CONSTRAINTS);

    }
}
