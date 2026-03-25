package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.assertCommandFailure;
import static doctorwho.logic.commands.CommandTestUtil.assertCommandSuccess;
import static doctorwho.logic.commands.CommandTestUtil.showPatientAtIndex;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static doctorwho.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static doctorwho.testutil.TypicalPatients.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import doctorwho.commons.core.index.Index;
import doctorwho.logic.Messages;
import doctorwho.model.AddressBook;
import doctorwho.model.Model;
import doctorwho.model.ModelManager;
import doctorwho.model.UserPrefs;
import doctorwho.model.patient.Appointment;
import doctorwho.model.patient.Patient;
import doctorwho.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) for
 * {@code DeleteAppointmentCommand}.
 */
public class DeleteAppointmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Patient patientToEdit = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        DeleteAppointmentCommand command = new DeleteAppointmentCommand(INDEX_FIRST_PATIENT);

        Patient updatedPatient = new PatientBuilder(patientToEdit)
                .withAppointment(null)
                .build();

        String expectedMessage = String.format(DeleteAppointmentCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS,
                updatedPatient.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, updatedPatient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertTrue(model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased()).getAppointment().isEmpty());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPatientList().size() + 1);
        DeleteAppointmentCommand command = new DeleteAppointmentCommand(outOfBoundIndex);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_patientHasNoAppointment_success() {
        Patient originalPatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient patientWithoutAppointment = new PatientBuilder(originalPatient)
                .withAppointment(null)
                .build();
        model.setPatient(originalPatient, patientWithoutAppointment);

        DeleteAppointmentCommand command = new DeleteAppointmentCommand(INDEX_FIRST_PATIENT);
        String expectedMessage = String.format(DeleteAppointmentCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS,
                patientWithoutAppointment.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertTrue(model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased()).getAppointment().isEmpty());
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);

        Index outOfBoundIndex = INDEX_SECOND_PATIENT;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPatientList().size());

        DeleteAppointmentCommand command = new DeleteAppointmentCommand(outOfBoundIndex);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_afterListAppointmentsCommand_resetsToShowAllPatients() {
        Patient withAppointment = new PatientBuilder()
            .withName("With Appointment")
            .withAppointment(new Appointment("12-03-2026 08:00", 30, "A"))
            .build();
        Patient withoutAppointment = new PatientBuilder()
            .withName("Without Appointment")
            .withAppointment(null)
            .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPatient(withAppointment);
        addressBook.addPatient(withoutAppointment);

        Model customModel = new ModelManager(addressBook, new UserPrefs());
        new ListAppointmentCommand(LocalDate.of(2026, 3, 12)).execute(customModel);

        DeleteAppointmentCommand command = new DeleteAppointmentCommand(INDEX_FIRST_PATIENT);

        Patient updatedPatient = new PatientBuilder(withAppointment)
            .withAppointment(null)
            .build();

        String expectedMessage = String.format(DeleteAppointmentCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS,
            updatedPatient.getName());

        Model expectedModel = new ModelManager(addressBook, new UserPrefs());
        expectedModel.setPatient(withAppointment, updatedPatient);
        expectedModel.updateFilteredPatientList(Model.PREDICATE_SHOW_ALL_PATIENTS);

        assertCommandSuccess(command, customModel, expectedMessage, expectedModel);
        assertEquals(2, customModel.getFilteredPatientList().size());
        assertTrue(customModel.getFilteredPatientList().contains(updatedPatient));
        assertTrue(customModel.getFilteredPatientList().contains(withoutAppointment));
    }

    @Test
    public void equals() {
        DeleteAppointmentCommand deleteFirstCommand = new DeleteAppointmentCommand(INDEX_FIRST_PATIENT);
        DeleteAppointmentCommand deleteSecondCommand = new DeleteAppointmentCommand(INDEX_SECOND_PATIENT);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteAppointmentCommand deleteFirstCommandCopy = new DeleteAppointmentCommand(INDEX_FIRST_PATIENT);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different patient index -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteAppointmentCommand command = new DeleteAppointmentCommand(targetIndex);
        String expected = DeleteAppointmentCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, command.toString());
    }
}
