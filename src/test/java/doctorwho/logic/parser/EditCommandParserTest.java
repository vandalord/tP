package doctorwho.logic.parser;

import static doctorwho.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static doctorwho.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static doctorwho.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.ALLERGY_DESC_ASPIRIN;
import static doctorwho.logic.commands.CommandTestUtil.ALLERGY_DESC_IBUPROFEN;
import static doctorwho.logic.commands.CommandTestUtil.ALLERGY_DESC_SULFONAMIDES;
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
import static doctorwho.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static doctorwho.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_IBUPROFEN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_SULFONAMIDES;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_DIABETES;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_HYPERTENSION;
import static doctorwho.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static doctorwho.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static doctorwho.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static doctorwho.logic.parser.CliSyntax.PREFIX_CONDITION;
import static doctorwho.logic.parser.CliSyntax.PREFIX_EMAIL;
import static doctorwho.logic.parser.CliSyntax.PREFIX_PHONE;
import static doctorwho.logic.parser.CommandParserTestUtil.assertParseFailure;
import static doctorwho.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static doctorwho.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static doctorwho.testutil.TypicalIndexes.INDEX_THIRD_PATIENT;

import org.junit.jupiter.api.Test;

import doctorwho.commons.core.index.Index;
import doctorwho.logic.Messages;
import doctorwho.logic.commands.EditCommand;
import doctorwho.logic.commands.EditCommand.EditPatientDescriptor;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Phone;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.testutil.EditPatientDescriptorBuilder;

public class EditCommandParserTest {
    private static final String ALLERGY_EMPTY = " " + PREFIX_ALLERGY;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, "1" + INVALID_ALLERGY_DESC, Allergy.MESSAGE_CONSTRAINTS); // invalid allergy
        assertParseFailure(parser, "1" + INVALID_CONDITION_DESC, Condition.MESSAGE_CONSTRAINTS);

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // al/ with no value (ALLERGY_EMPTY) creates an Allergy("") which fails validation
        // this should fail regardless of where the empty al/ appears in the input
        assertParseFailure(parser, "1" + ALLERGY_DESC_IBUPROFEN + ALLERGY_DESC_SULFONAMIDES + ALLERGY_EMPTY,
                Allergy.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + ALLERGY_DESC_IBUPROFEN + ALLERGY_EMPTY + ALLERGY_DESC_SULFONAMIDES,
                Allergy.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + ALLERGY_EMPTY + ALLERGY_DESC_IBUPROFEN + ALLERGY_DESC_SULFONAMIDES,
                Allergy.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PATIENT;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + ALLERGY_DESC_IBUPROFEN
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + ALLERGY_DESC_ASPIRIN + CONDITION_DESC_DIABETES;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
            .withAllergies(VALID_ALLERGY_IBUPROFEN, VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_PATIENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditPatientDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // allergy
        userInput = targetIndex.getOneBased() + ALLERGY_DESC_ASPIRIN;
        descriptor = new EditPatientDescriptorBuilder().withAllergies(VALID_ALLERGY_ASPIRIN).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // condition
        userInput = targetIndex.getOneBased() + CONDITION_DESC_DIABETES;
        descriptor = new EditPatientDescriptorBuilder().withConditions(VALID_CONDITION_DIABETES).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + ALLERGY_DESC_ASPIRIN + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY + ALLERGY_DESC_ASPIRIN
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB + VALID_ALLERGY_IBUPROFEN;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS));
    }

    /**
     * Tests that multiple allergy fields are correctly parsed into an edit command.
     */
    @Test
    public void parse_multipleAllergiesSpecified_success() {
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + ALLERGY_DESC_SULFONAMIDES + ALLERGY_DESC_ASPIRIN;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
                .withAllergies(VALID_ALLERGY_SULFONAMIDES, VALID_ALLERGY_ASPIRIN).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    /**
     * Tests that an empty allergy prefix clears all existing allergies.
     */
    @Test
    public void parse_resetAllergies_success() {
        Index targetIndex = INDEX_THIRD_PATIENT;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_ALLERGY;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withAllergies().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    /**
     * Tests that an empty condition prefix clears all existing conditions.
     */
    @Test
    public void parse_resetConditions_success() {
        Index targetIndex = INDEX_THIRD_PATIENT;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_CONDITION;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withConditions().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    /**
     * Tests that multiple condition fields are correctly parsed into an edit command.
     */
    @Test
    public void parse_multipleConditionsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PATIENT;
        String userInput = targetIndex.getOneBased() + CONDITION_DESC_DIABETES + CONDITION_DESC_HYPERTENSION;

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
                .withConditions(VALID_CONDITION_DIABETES, VALID_CONDITION_HYPERTENSION).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
