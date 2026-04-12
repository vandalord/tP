package doctorwho.logic.commands;

import static doctorwho.commons.util.CollectionUtil.requireAllNonNull;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DURATION;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_NOTE;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_STARTTIME;
import static doctorwho.model.Model.PREDICATE_SHOW_ALL_PATIENTS;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

import doctorwho.commons.core.index.Index;
import doctorwho.commons.util.ToStringBuilder;
import doctorwho.logic.Messages;
import doctorwho.logic.commands.exceptions.CommandException;
import doctorwho.model.Model;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.Appointment;
import doctorwho.model.patient.DateOfBirth;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Nric;
import doctorwho.model.patient.Patient;
import doctorwho.model.patient.Phone;
import doctorwho.model.patient.Sex;
import doctorwho.model.tag.Tag;

/**
 * Adds an appointment to an existing patient in the address book.
 */
public class AddAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "apt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to the patient identified "
            + "by the index number used in the displayed patient list. "
            + "Supply the start date and time, duration and an optional note. "
            + "Existing appointment will be overwritten by the new appointment.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_APPOINTMENT_STARTTIME + "DATETIME (in the format: dd-MM-yyyy HH:mm) "
            + PREFIX_APPOINTMENT_DURATION + "DURATION (in minutes) "
            + "[" + PREFIX_APPOINTMENT_NOTE + "NOTE] "
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_APPOINTMENT_STARTTIME + "12-03-2026 14:00 "
            + PREFIX_APPOINTMENT_DURATION + "30 "
            + PREFIX_APPOINTMENT_NOTE + "Routine Checkup";

    public static final String MESSAGE_HAS_OVERLAPPING_APPOINTMENT =
            "The appointment clashes with an existing appointment";
    public static final String MESSAGE_EDIT_PATIENT_SUCCESS = "Added appointment to: %1$s";
    public static final String MESSAGE_UHCHANGED = "The appointment is identical to the patient's current appointment.";

    private final Index index;
    private final Appointment appointment;

    /**
     * @param index       of the patient in the filtered patient list to edit
     * @param appointment appointment to add to the patient
     */
    public AddAppointmentCommand(Index index, Appointment appointment) {
        requireAllNonNull(index, appointment);
        this.index = index;
        this.appointment = appointment;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        if (appointment.equals(patientToEdit.getAppointment().orElse(null))) {
            throw new CommandException(MESSAGE_UHCHANGED);
        }
        Patient editedPatient = addAppointmentToPatient(patientToEdit, appointment);

        List<Patient> fullList = model.getFullPatientList();
        if (editedPatient.hasOverlappingAppointmentInList(fullList)) {
            throw new CommandException(MESSAGE_HAS_OVERLAPPING_APPOINTMENT);
        }

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_PATIENT_SUCCESS, Messages.format(editedPatient)));
    }

    /**
     * Creates and returns a {@code Patient} with the details of {@code patientToEdit}
     * with {@code appointment} added, regardless of whether {@code patientToEdit} has an existing appointment.
     */
    private static Patient addAppointmentToPatient(Patient patientToEdit, Appointment appointmentToAdd) {
        assert patientToEdit != null;

        Name name = patientToEdit.getName();
        Nric nric = patientToEdit.getNric();
        Sex sex = patientToEdit.getSex();
        DateOfBirth dob = patientToEdit.getDateOfBirth();
        Phone phone = patientToEdit.getPhone();
        Email email = patientToEdit.getEmail();
        Address address = patientToEdit.getAddress();
        Set<Tag> tags = patientToEdit.getTags();
        Appointment appointment = appointmentToAdd;

        return new Patient(name, nric, sex, dob, phone, email, address, tags, appointmentToAdd);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddAppointmentCommand)) {
            return false;
        }

        AddAppointmentCommand otherAddAppointmentCommand = (AddAppointmentCommand) other;
        return index.equals(otherAddAppointmentCommand.index)
                && appointment.equals(otherAddAppointmentCommand.appointment);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("appointment", appointment)
                .toString();
    }
}
