package doctorwho.logic.parser;

import static doctorwho.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DURATION;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_NOTE;
import static doctorwho.logic.parser.CliSyntax.PREFIX_APPOINTMENT_STARTTIME;
import static java.util.Objects.requireNonNull;

import java.util.stream.Stream;

import doctorwho.commons.core.index.Index;
import doctorwho.logic.commands.AddAppointmentCommand;
import doctorwho.logic.parser.exceptions.ParseException;
import doctorwho.model.patient.Appointment;

/**
 * Parses input arguments and creates a new AddAppointmentCommand object
 */
public class AddAppointmentCommandParser implements Parser<AddAppointmentCommand> {

    public static final String MESSAGE_NOTE_TOO_LONG =
            String.format("Appointment note must not exceed %d characters.", Appointment.MAX_LENGTH);

    /**
     * Parses the given {@code String} of arguments in the context of the AddAppointmentCommand
     * and returns an AddAppointmentCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAppointmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_APPOINTMENT_STARTTIME,
                PREFIX_APPOINTMENT_DURATION, PREFIX_APPOINTMENT_NOTE);

        Index index;

        // check index is present
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentCommand.MESSAGE_USAGE),
                    pe);
        }

        // check start time and duration(required arguments) are present
        if (!arePrefixesPresent(argMultimap, PREFIX_APPOINTMENT_STARTTIME, PREFIX_APPOINTMENT_DURATION)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentCommand.MESSAGE_USAGE));
        }

        // ensure no duplicate arguments
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_APPOINTMENT_STARTTIME,
                PREFIX_APPOINTMENT_DURATION, PREFIX_APPOINTMENT_NOTE);

        String startTimeStr = argMultimap.getValue(PREFIX_APPOINTMENT_STARTTIME).get();
        int duration = ParserUtil.parseAppointmentDuration(argMultimap.getValue(PREFIX_APPOINTMENT_DURATION).get());
        String note = argMultimap.getValue(PREFIX_APPOINTMENT_NOTE).orElse("");

        if (note.length() > Appointment.MAX_LENGTH) {
            throw new ParseException(MESSAGE_NOTE_TOO_LONG);
        }

        Appointment appointment;

        try {
            appointment = new Appointment(startTimeStr, duration, note);
        } catch (IllegalArgumentException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentCommand.MESSAGE_USAGE), e);
        }

        return new AddAppointmentCommand(index, appointment);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
