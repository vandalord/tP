package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_STARTTIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.patient.Address;
import seedu.address.model.patient.Appointment;
import seedu.address.model.patient.Email;
import seedu.address.model.patient.Name;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.Phone;
import seedu.address.model.tag.Tag;

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

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Added appointment to: %1$s";

    private final Index index;
    private final Appointment appointment;

    /**
     * @param index       of the patient in the filtered patient list to edit
     * @param appointment appointment to add to the patient
     */
    public AddAppointmentCommand(Index index, Appointment appointment) {
        requireNonNull(index);
        requireNonNull(appointment);

        this.index = index;
        this.appointment = appointment;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Patient editedPatient = addAppointmentToPerson(patientToEdit, appointment);

        model.setPerson(patientToEdit, editedPatient);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPatient)));
    }

    /**
     * Creates and returns a {@code Patient} with the details of {@code patientToEdit}
     * with {@code appointment} added, regardless of whether {@code patientToEdit} has an existing appointment.
     */
    private static Patient addAppointmentToPerson(Patient patientToEdit, Appointment appointmentToAdd) {
        assert patientToEdit != null;

        Name name = patientToEdit.getName();
        Phone phone = patientToEdit.getPhone();
        Email email = patientToEdit.getEmail();
        Address address = patientToEdit.getAddress();
        Set<Tag> tags = patientToEdit.getTags();
        Appointment appointment = appointmentToAdd;

        return new Patient(name, phone, email, address, tags, appointment);
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
