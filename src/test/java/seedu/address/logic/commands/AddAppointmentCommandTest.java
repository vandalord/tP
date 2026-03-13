package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.patient.Appointment;
import seedu.address.model.patient.Patient;
import seedu.address.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddAppointmentCommand}.
 */
public class AddAppointmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private Appointment appointment =
            new Appointment("12-03-2026 14:00", 30, "Routine Checkup");

    @Test
    public void execute_addAppointmentUnfilteredList_success() {
        Patient patientToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Patient editedPatient = new PatientBuilder(patientToEdit)
                .withAppointment(appointment)
                .build();

        AddAppointmentCommand addAppointmentCommand =
                new AddAppointmentCommand(INDEX_FIRST_PERSON, appointment);

        String expectedMessage = String.format(
                AddAppointmentCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPatient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());

        expectedModel.setPerson(patientToEdit, editedPatient);

        assertCommandSuccess(addAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addAppointmentFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Patient patientInFilteredList =
                model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Patient editedPatient = new PatientBuilder(patientInFilteredList)
                .withAppointment(appointment)
                .build();

        AddAppointmentCommand command =
                new AddAppointmentCommand(INDEX_FIRST_PERSON, appointment);

        String expectedMessage = String.format(
                AddAppointmentCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPatient));

        Model expectedModel =
                new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPatient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex =
                Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        AddAppointmentCommand command =
                new AddAppointmentCommand(outOfBoundIndex, appointment);

        assertCommandFailure(
                command,
                model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Index larger than filtered list but within address book.
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;

        assertTrue(outOfBoundIndex.getZeroBased()
                < model.getAddressBook().getPersonList().size());

        AddAppointmentCommand command =
                new AddAppointmentCommand(outOfBoundIndex, appointment);

        assertCommandFailure(
                command,
                model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Appointment otherAppointment =
                new Appointment("12-03-2026 14:00", 60, "Consultation");

        AddAppointmentCommand standardCommand =
                new AddAppointmentCommand(INDEX_FIRST_PERSON, appointment);

        // same values -> true
        AddAppointmentCommand commandWithSameValues =
                new AddAppointmentCommand(INDEX_FIRST_PERSON, appointment);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> false
        assertFalse(standardCommand.equals(null));

        // different type -> false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> false
        assertFalse(standardCommand.equals(
                new AddAppointmentCommand(INDEX_SECOND_PERSON, appointment)));

        // different appointment -> false
        assertFalse(standardCommand.equals(
                new AddAppointmentCommand(INDEX_FIRST_PERSON, otherAppointment)));
    }

    @Test
    public void toStringMethod() {
        AddAppointmentCommand command =
                new AddAppointmentCommand(INDEX_FIRST_PERSON, appointment);

        String expected = AddAppointmentCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON
                + ", appointment=" + appointment + "}";

        assertEquals(expected, command.toString());
    }
}
