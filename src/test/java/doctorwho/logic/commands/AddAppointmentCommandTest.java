package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DURATION;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DURATION_NON_OVERLAPPING;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DURATION_OVERLAPPING;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NOTE;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NOTE_NON_OVERLAPPING;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NOTE_OVERLAPPING;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_STARTTIME;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_STARTTIME_NON_OVERLAPPING;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_STARTTIME_OVERLAPPING;
import static doctorwho.logic.commands.CommandTestUtil.assertCommandFailure;
import static doctorwho.logic.commands.CommandTestUtil.assertCommandSuccess;
import static doctorwho.logic.commands.CommandTestUtil.showPatientAtIndex;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static doctorwho.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static doctorwho.testutil.TypicalPatients.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddAppointmentCommand}.
 */
public class AddAppointmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private final Appointment appointment = new Appointment(
            VALID_APPOINTMENT_STARTTIME,
            VALID_APPOINTMENT_DURATION,
            VALID_APPOINTMENT_NOTE);

    // starts at 14:15, overlaps the 14:00–14:30 window
    private final Appointment overlappingAppointment = new Appointment(
            VALID_APPOINTMENT_STARTTIME_OVERLAPPING,
            VALID_APPOINTMENT_DURATION_OVERLAPPING,
            VALID_APPOINTMENT_NOTE_OVERLAPPING);

    // starts at 15:00, fully outside the 14:00–14:30 window
    private final Appointment nonOverlappingAppointment = new Appointment(
            VALID_APPOINTMENT_STARTTIME_NON_OVERLAPPING,
            VALID_APPOINTMENT_DURATION_NON_OVERLAPPING,
            VALID_APPOINTMENT_NOTE_NON_OVERLAPPING);

    @Test
    public void execute_addAppointmentUnfilteredList_success() {
        Patient patientToEdit = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());

        Patient editedPatient = new PatientBuilder(patientToEdit)
                .withAppointment(nonOverlappingAppointment)
                .build();

        AddAppointmentCommand addAppointmentCommand =
                new AddAppointmentCommand(INDEX_FIRST_PATIENT, nonOverlappingAppointment);

        String expectedMessage = String.format(
                AddAppointmentCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
                Messages.format(editedPatient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, editedPatient);

        assertCommandSuccess(addAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addAppointmentFilteredList_success() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);

        Patient patientInFilteredList =
                model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());

        Patient editedPatient = new PatientBuilder(patientInFilteredList)
                .withAppointment(nonOverlappingAppointment)
                .build();

        AddAppointmentCommand command =
                new AddAppointmentCommand(INDEX_FIRST_PATIENT, nonOverlappingAppointment);

        String expectedMessage = String.format(
                AddAppointmentCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
                Messages.format(editedPatient));

        Model expectedModel =
                new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(model.getFilteredPatientList().get(0), editedPatient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_replaceWithOverlappingAppointment_failure() {
        // First, give patient 1 the base appointment
        Patient patientToEdit = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient patientWithAppointment = new PatientBuilder(patientToEdit)
                .withAppointment(appointment)
                .build();
        model.setPatient(patientToEdit, patientWithAppointment);

        // Now try to give patient 2 an overlapping appointment
        AddAppointmentCommand command =
                new AddAppointmentCommand(INDEX_SECOND_PATIENT, overlappingAppointment);

        assertCommandFailure(command, model, AddAppointmentCommand.MESSAGE_HAS_OVERLAPPING_APPOINTMENT);
    }

    @Test
    public void execute_replaceWithNonOverlappingAppointment_success() {
        // Give patient 1 the base appointment (14:00–14:30)
        Patient firstPatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient firstWithAppointment = new PatientBuilder(firstPatient)
                .withAppointment(appointment)
                .build();
        model.setPatient(firstPatient, firstWithAppointment);

        // Patient 2 gets a non-overlapping slot (15:00–15:30) — should succeed
        Patient secondPatient = model.getFilteredPatientList().get(INDEX_SECOND_PATIENT.getZeroBased());
        Patient secondEdited = new PatientBuilder(secondPatient)
                .withAppointment(nonOverlappingAppointment)
                .build();

        AddAppointmentCommand command =
                new AddAppointmentCommand(INDEX_SECOND_PATIENT, nonOverlappingAppointment);

        String expectedMessage = String.format(
                AddAppointmentCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
                Messages.format(secondEdited));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(firstPatient, firstWithAppointment);
        expectedModel.setPatient(secondPatient, secondEdited);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_replaceAppointmentWithOverlap_success() {
        // Re-assigning the same patient's appointment should not flag overlap with itself
        Patient patientToEdit = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient patientWithAppointment = new PatientBuilder(patientToEdit)
                .withAppointment(appointment)
                .build();
        model.setPatient(patientToEdit, patientWithAppointment);

        // Re-add the same appointment to the same patient - isSamePatient filters it out
        AddAppointmentCommand command =
                new AddAppointmentCommand(INDEX_FIRST_PATIENT, overlappingAppointment);

        Patient editedPatient = new PatientBuilder(patientWithAppointment)
                .withAppointment(overlappingAppointment)
                .build();

        String expectedMessage = String.format(
                AddAppointmentCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
                Messages.format(editedPatient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientWithAppointment, editedPatient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_replaceWithOriginalAppointment_failure() {
        // Give patient 1 an appointment first
        Patient patientToEdit = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient patientWithAppointment = new PatientBuilder(patientToEdit)
                .withAppointment(appointment)
                .build();
        model.setPatient(patientToEdit, patientWithAppointment);

        AddAppointmentCommand command =
                new AddAppointmentCommand(INDEX_FIRST_PATIENT, appointment);

        assertCommandFailure(command, model, AddAppointmentCommand.MESSAGE_UHCHANGED);
    }

    @Test
    public void execute_invalidPatientIndexUnfilteredList_failure() {
        Index outOfBoundIndex =
                Index.fromOneBased(model.getFilteredPatientList().size() + 1);

        AddAppointmentCommand command =
                new AddAppointmentCommand(outOfBoundIndex, appointment);

        assertCommandFailure(
                command,
                model,
                Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    /**
     * Index larger than filtered list but within address book.
     */
    @Test
    public void execute_invalidPatientIndexFilteredList_failure() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);

        Index outOfBoundIndex = INDEX_SECOND_PATIENT;

        assertTrue(outOfBoundIndex.getZeroBased()
                < model.getAddressBook().getPatientList().size());

        AddAppointmentCommand command =
                new AddAppointmentCommand(outOfBoundIndex, appointment);

        assertCommandFailure(
                command,
                model,
                Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Appointment otherAppointment =
                new Appointment("12-03-2026 14:00", 60, "Consultation");

        AddAppointmentCommand standardCommand =
                new AddAppointmentCommand(INDEX_FIRST_PATIENT, appointment);

        // same values -> true
        AddAppointmentCommand commandWithSameValues =
                new AddAppointmentCommand(INDEX_FIRST_PATIENT, appointment);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> false
        assertFalse(standardCommand.equals(null));

        // different type -> false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> false
        assertFalse(standardCommand.equals(
                new AddAppointmentCommand(INDEX_SECOND_PATIENT, appointment)));

        // different appointment -> false
        assertFalse(standardCommand.equals(
                new AddAppointmentCommand(INDEX_FIRST_PATIENT, otherAppointment)));
    }

    @Test
    public void toStringMethod() {
        AddAppointmentCommand command =
                new AddAppointmentCommand(INDEX_FIRST_PATIENT, appointment);

        String expected = AddAppointmentCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PATIENT
                + ", appointment=" + appointment + "}";

        assertEquals(expected, command.toString());
    }
}
