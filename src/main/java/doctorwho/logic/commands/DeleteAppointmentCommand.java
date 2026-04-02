package doctorwho.logic.commands;

import static doctorwho.model.Model.PREDICATE_SHOW_ALL_PATIENTS;
import static java.util.Objects.requireNonNull;

import java.util.List;

import doctorwho.commons.core.index.Index;
import doctorwho.commons.util.ToStringBuilder;
import doctorwho.logic.Messages;
import doctorwho.logic.commands.exceptions.CommandException;
import doctorwho.model.Model;
import doctorwho.model.patient.Patient;

/**
 * Deletes the appointment of the patient identified by the displayed index from DoctorWho.
 */
public class DeleteAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "dapt";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the appointment of the patient identified "
            + "by the index number used in the displayed patient list.\n"
            + "Parameters: PATIENT_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_APPOINTMENT_SUCCESS =
            "Appointment deleted for %1$s.";
    public static final String MESSAGE_PATIENT_HAS_NO_APPOINTMENT = "The selected patient: %1$s has no appointment.";

    private final Index targetIndex;

    /**
     * @param targetIndex of the patient in the filtered patient list whose appointment is to be deleted
     */
    public DeleteAppointmentCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    /**
     * Executes the delete appointment command by removing the appointment of the patient
     * at the specified {@code targetIndex} in the filtered patient list.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(targetIndex.getZeroBased());

        if (patientToEdit.getAppointment().isEmpty()) {
            throw new CommandException(String.format(MESSAGE_PATIENT_HAS_NO_APPOINTMENT, patientToEdit.getName()));
        }

        Patient updatedPatient = new Patient(
                patientToEdit.getName(),
                patientToEdit.getNric(),
                patientToEdit.getDateOfBirth(),
                patientToEdit.getPhone(),
                patientToEdit.getEmail(),
                patientToEdit.getAddress(),
                patientToEdit.getTags(),
                null
        );

        model.setPatient(patientToEdit, updatedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);
        return new CommandResult(
                String.format(MESSAGE_DELETE_APPOINTMENT_SUCCESS, updatedPatient.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteAppointmentCommand)) {
            return false;
        }

        DeleteAppointmentCommand otherCommand = (DeleteAppointmentCommand) other;
        return targetIndex.equals(otherCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
