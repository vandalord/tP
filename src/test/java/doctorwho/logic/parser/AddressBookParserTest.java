package doctorwho.logic.parser;

import static doctorwho.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static doctorwho.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static doctorwho.testutil.Assert.assertThrows;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import doctorwho.logic.commands.AddCommand;
import doctorwho.logic.commands.ClearCommand;
import doctorwho.logic.commands.DeleteCommand;
import doctorwho.logic.commands.EditCommand;
import doctorwho.logic.commands.EditCommand.EditPatientDescriptor;
import doctorwho.logic.commands.ExitCommand;
import doctorwho.logic.commands.FindCommand;
import doctorwho.logic.commands.HelpCommand;
import doctorwho.logic.commands.ListAppointmentCommand;
import doctorwho.logic.commands.ListCommand;
import doctorwho.logic.parser.exceptions.ParseException;
import doctorwho.model.patient.NameContainsKeywordsPredicate;
import doctorwho.model.patient.Patient;
import doctorwho.testutil.EditPatientDescriptorBuilder;
import doctorwho.testutil.PatientBuilder;
import doctorwho.testutil.PatientUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Patient patient = new PatientBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PatientUtil.getAddCommand(patient));
        assertEquals(new AddCommand(patient), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PATIENT.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PATIENT), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PATIENT.getOneBased() + " " + PatientUtil.getEditPatientDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PATIENT, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_listAppointment() throws Exception {
        ListAppointmentCommand listAllCommand = (ListAppointmentCommand) parser.parseCommand(
                ListAppointmentCommand.COMMAND_WORD);
        assertEquals(new ListAppointmentCommand(), listAllCommand);

        ListAppointmentCommand listByDateCommand = (ListAppointmentCommand) parser.parseCommand(
                ListAppointmentCommand.COMMAND_WORD + " dt/12-03-2026");
        assertEquals(new ListAppointmentCommand(LocalDate.of(2026, 3, 12)), listByDateCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
