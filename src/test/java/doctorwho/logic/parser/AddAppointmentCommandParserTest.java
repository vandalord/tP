package doctorwho.logic.parser;

import static doctorwho.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static doctorwho.logic.commands.AddAppointmentCommand.MESSAGE_USAGE;
import static doctorwho.logic.commands.CommandTestUtil.APPOINTMENT_DURATION_DESC_VALID;
import static doctorwho.logic.commands.CommandTestUtil.APPOINTMENT_NOTE_DESC_VALID;
import static doctorwho.logic.commands.CommandTestUtil.APPOINTMENT_STARTTIME_DESC_VALID;
import static doctorwho.logic.commands.CommandTestUtil.INVALID_APPOINTMENT_DURATION_DESC;
import static doctorwho.logic.commands.CommandTestUtil.INVALID_APPOINTMENT_STARTTIME_DESC;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DURATION;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_NOTE;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_STARTTIME;
import static doctorwho.logic.parser.CommandParserTestUtil.assertParseFailure;
import static doctorwho.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;

import org.junit.jupiter.api.Test;

import doctorwho.commons.core.index.Index;
import doctorwho.logic.Messages;
import doctorwho.logic.commands.AddAppointmentCommand;
import doctorwho.model.patient.Appointment;

public class AddAppointmentCommandParserTest {

    private AddAppointmentCommandParser parser = new AddAppointmentCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Index targetIndex = INDEX_FIRST_PATIENT;

        String userInput = targetIndex.getOneBased()
                + APPOINTMENT_STARTTIME_DESC_VALID
                + APPOINTMENT_DURATION_DESC_VALID
                + APPOINTMENT_NOTE_DESC_VALID;

        Appointment appointment = new Appointment(
                "12-03-2026 14:00",
                30,
                "Routine Checkup"
        );

        AddAppointmentCommand expectedCommand =
                new AddAppointmentCommand(targetIndex, appointment);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_optionalNoteMissing_success() {
        Index targetIndex = INDEX_FIRST_PATIENT;

        String userInput = targetIndex.getOneBased()
                + APPOINTMENT_STARTTIME_DESC_VALID
                + APPOINTMENT_DURATION_DESC_VALID;

        Appointment appointment = new Appointment(
                "12-03-2026 14:00",
                30,
                ""
        );

        AddAppointmentCommand expectedCommand =
                new AddAppointmentCommand(targetIndex, appointment);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingStartTime_failure() {
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                + APPOINTMENT_DURATION_DESC_VALID;

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
    }

    @Test
    public void parse_missingDuration_failure() {
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                + APPOINTMENT_STARTTIME_DESC_VALID;

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndex_failure() {
        String userInput = APPOINTMENT_STARTTIME_DESC_VALID
                + APPOINTMENT_DURATION_DESC_VALID;

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidStartTime_failure() {
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                + INVALID_APPOINTMENT_STARTTIME_DESC
                + APPOINTMENT_DURATION_DESC_VALID;

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDuration_failure() {
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                + APPOINTMENT_STARTTIME_DESC_VALID
                + INVALID_APPOINTMENT_DURATION_DESC;

        assertParseFailure(parser, userInput,
                ParserUtil.MESSAGE_INVALID_DUR);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                + APPOINTMENT_STARTTIME_DESC_VALID
                + APPOINTMENT_STARTTIME_DESC_VALID
                + APPOINTMENT_DURATION_DESC_VALID;

        assertParseFailure(parser,
                userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_APPOINTMENT_STARTTIME));
    }

    @Test
    public void parse_duplicateDuration_failure() {
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                + APPOINTMENT_STARTTIME_DESC_VALID
                + APPOINTMENT_DURATION_DESC_VALID
                + APPOINTMENT_DURATION_DESC_VALID;

        assertParseFailure(parser,
                userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_APPOINTMENT_DURATION));
    }

    @Test
    public void parse_duplicateNote_failure() {
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                + APPOINTMENT_STARTTIME_DESC_VALID
                + APPOINTMENT_DURATION_DESC_VALID
                + APPOINTMENT_NOTE_DESC_VALID
                + APPOINTMENT_NOTE_DESC_VALID;

        assertParseFailure(parser,
                userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_APPOINTMENT_NOTE));
    }

    @Test
    public void parse_noteTooLong_failure() {
        String overlongNote = " note/" + "a".repeat(Appointment.MAX_LENGTH + 1);
        String userInput = INDEX_FIRST_PATIENT.getOneBased()
                        + APPOINTMENT_STARTTIME_DESC_VALID
                        + APPOINTMENT_DURATION_DESC_VALID
                        + overlongNote;

        assertParseFailure(parser, userInput, AddAppointmentCommandParser.MESSAGE_NOTE_TOO_LONG);
    }

}
