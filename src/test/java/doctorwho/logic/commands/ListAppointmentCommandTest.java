package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.assertCommandSuccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

import doctorwho.model.AddressBook;
import doctorwho.model.Model;
import doctorwho.model.ModelManager;
import doctorwho.model.UserPrefs;
import doctorwho.model.patient.Appointment;
import doctorwho.model.patient.Patient;
import doctorwho.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListAppointmentCommand.
 */
public class ListAppointmentCommandTest {

    @Test
    public void execute_noDateFilter_listsAllAppointmentsSorted() {
        Patient laterAppointmentPatient = new PatientBuilder()
                .withName("Later Appointment")
                .withNric("S0948222I")
                .withAppointment(new Appointment("13-03-2026 14:00", 30, "later"))
                .build();
        Patient earlierAppointmentPatient = new PatientBuilder()
                .withName("Earlier Appointment")
                .withNric("S0948242C")
                .withAppointment(new Appointment("11-03-2026 09:00", 30, "earlier"))
                .build();
        Patient noAppointmentPatient = new PatientBuilder()
                .withName("No Appointment")
                .withNric("S0948244Z")
                .withAppointment(null)
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPatient(laterAppointmentPatient);
        addressBook.addPatient(earlierAppointmentPatient);
        addressBook.addPatient(noAppointmentPatient);

        Model model = new ModelManager(addressBook, new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());

        expectedModel.updateFilteredPatientList(patient -> patient.getAppointment().isPresent());
        expectedModel.setPatientListComparator(
                Comparator.comparing(patient -> patient.getAppointment().get().getStartTime()));

        ListAppointmentCommand command = new ListAppointmentCommand();
        String expectedMessage = String.format(ListAppointmentCommand.MESSAGE_SUCCESS, 2);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(earlierAppointmentPatient, model.getFilteredPatientList().get(0));
        assertEquals(laterAppointmentPatient, model.getFilteredPatientList().get(1));
    }

    @Test
    public void execute_withDateFilter_listsMatchingAppointmentsSorted() {
        LocalDate filterDate = LocalDate.of(2026, 3, 12);

        Patient sameDayLaterPatient = new PatientBuilder()
                .withName("Same Day Later")
                .withNric("S0848242Z")
                .withAppointment(new Appointment("12-03-2026 16:00", 30, "later same day"))
                .build();
        Patient differentDayPatient = new PatientBuilder()
                .withName("Different Day")
                .withNric("S0848213F")
                .withAppointment(new Appointment("13-03-2026 10:00", 30, "different day"))
                .build();
        Patient sameDayEarlierPatient = new PatientBuilder()
                .withName("Same Day Earlier")
                .withNric("S1234567D")
                .withAppointment(new Appointment("12-03-2026 09:00", 30, "earlier same day"))
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPatient(sameDayLaterPatient);
        addressBook.addPatient(differentDayPatient);
        addressBook.addPatient(sameDayEarlierPatient);

        Model model = new ModelManager(addressBook, new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());

        expectedModel.updateFilteredPatientList(patient -> patient.getAppointment()
                .map(appointment -> appointment.getStartTime().toLocalDate().equals(filterDate))
                .orElse(false));
        expectedModel.setPatientListComparator(
                Comparator.comparing(patient -> patient.getAppointment().get().getStartTime()));

        ListAppointmentCommand command = new ListAppointmentCommand(filterDate);
        String expectedMessage = String.format(ListAppointmentCommand.MESSAGE_SUCCESS, 2);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(sameDayEarlierPatient, model.getFilteredPatientList().get(0));
        assertEquals(sameDayLaterPatient, model.getFilteredPatientList().get(1));
    }

    @Test
    public void execute_noAppointments_returnsEmptyList() {
        Patient noAppointmentOne = new PatientBuilder().withName("No Appointment One")
                .withNric("S2345678H").withAppointment(null).build();
        Patient noAppointmentTwo = new PatientBuilder().withName("No Appointment Two")
                .withNric("S9435125A").withAppointment(null).build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPatient(noAppointmentOne);
        addressBook.addPatient(noAppointmentTwo);

        Model model = new ModelManager(addressBook, new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());

        expectedModel.updateFilteredPatientList(patient -> false);

        ListAppointmentCommand command = new ListAppointmentCommand();
        String expectedMessage = String.format(ListAppointmentCommand.MESSAGE_SUCCESS, 0);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertTrue(model.getFilteredPatientList().isEmpty());
    }

    @Test
    public void execute_thenListCommand_showsAllPatients() {
        Patient withAppointment = new PatientBuilder()
                        .withName("With Appointment")
                        .withNric("S9876543C")
                        .withAppointment(new Appointment("12-03-2026 11:00", 30, "visit"))
                        .build();
        Patient withoutAppointment = new PatientBuilder()
                        .withName("Without Appointment")
                        .withNric("S9535256A")
                        .withAppointment(null)
                        .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPatient(withAppointment);
        addressBook.addPatient(withoutAppointment);

        Model model = new ModelManager(addressBook, new UserPrefs());
        new ListAppointmentCommand().execute(model);

        new ListCommand().execute(model);

        assertEquals(2, model.getFilteredPatientList().size());
        assertTrue(model.getFilteredPatientList().contains(withAppointment));
        assertTrue(model.getFilteredPatientList().contains(withoutAppointment));
    }

    @Test
    public void equals() {
        LocalDate firstDate = LocalDate.of(2026, 3, 12);
        LocalDate secondDate = LocalDate.of(2026, 3, 13);

        ListAppointmentCommand listAllCommand = new ListAppointmentCommand();
        ListAppointmentCommand listAllCommandCopy = new ListAppointmentCommand();
        ListAppointmentCommand firstDateCommand = new ListAppointmentCommand(firstDate);
        ListAppointmentCommand secondDateCommand = new ListAppointmentCommand(secondDate);

        assertTrue(listAllCommand.equals(listAllCommand));
        assertTrue(listAllCommand.equals(listAllCommandCopy));
        assertFalse(listAllCommand.equals(firstDateCommand));
        assertFalse(firstDateCommand.equals(secondDateCommand));
        assertFalse(listAllCommand.equals(null));
        assertFalse(listAllCommand.equals(new ClearCommand()));
    }

    @Test
    public void toStringMethod() {
        LocalDate date = LocalDate.of(2026, 3, 12);
        ListAppointmentCommand command = new ListAppointmentCommand(date);

        String expected = ListAppointmentCommand.class.getCanonicalName()
                + "{appointmentDate=Optional[" + date + "]}";

        assertEquals(expected, command.toString());
    }
}
