package doctorwho.logic.commands;

import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DATE;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import doctorwho.commons.util.ToStringBuilder;
import doctorwho.model.Model;
import doctorwho.model.patient.Appointment;
import doctorwho.model.patient.Patient;

/**
 * Lists appointments, optionally filtering by a specific appointment date.
 */
public class ListAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "lsapt";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists appointments in ascending date-time order.\n"
            + "Parameters: [" + PREFIX_APPOINTMENT_DATE + "DATE] (DATE in dd-MM-yyyy format)\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_APPOINTMENT_DATE + "12-03-2026";

    public static final String MESSAGE_SUCCESS = "%1$d appointment(s) listed.";

    private final Optional<LocalDate> appointmentDate;

    /**
     * Creates a {@code ListAppointmentCommand} that lists all appointments.
     */
    public ListAppointmentCommand() {
        this.appointmentDate = Optional.empty();
    }

    /**
     * Creates a {@code ListAppointmentCommand} that filters appointments by {@code appointmentDate}.
     */
    public ListAppointmentCommand(LocalDate appointmentDate) {
        requireNonNull(appointmentDate);
        this.appointmentDate = Optional.of(appointmentDate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Predicate<Patient> appointmentPredicate = createAppointmentPredicate();
        model.updateFilteredPatientList(appointmentPredicate);
        model.setPatientListComparator(Comparator.comparing(ListAppointmentCommand::getAppointmentStartTime));

        int listedCount = model.getFilteredPatientList().size();
        return new CommandResult(String.format(MESSAGE_SUCCESS, listedCount));
    }

    private Predicate<Patient> createAppointmentPredicate() {
        return patient -> patient.getAppointment()
                .filter(appointment -> isMatchingDateFilter(appointment, appointmentDate))
                .isPresent();
    }

    private static boolean isMatchingDateFilter(Appointment appointment, Optional<LocalDate> appointmentDate) {
        return appointmentDate
                .map(date -> appointment.getStartTime().toLocalDate().equals(date))
                .orElse(true);
    }

    private static LocalDateTime getAppointmentStartTime(Patient patient) {
        return patient.getAppointment()
                .map(Appointment::getStartTime)
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListAppointmentCommand)) {
            return false;
        }

        ListAppointmentCommand otherCommand = (ListAppointmentCommand) other;
        return appointmentDate.equals(otherCommand.appointmentDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("appointmentDate", appointmentDate)
                .toString();
    }
}
