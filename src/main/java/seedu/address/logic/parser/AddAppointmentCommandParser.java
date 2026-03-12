package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_STARTTIME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAppointmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.patient.Appointment;

/**
 * Parses input arguments and creates a new AddAppointmentCommand object
 */
public class AddAppointmentCommandParser implements Parser<AddAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddAppointmentCommand
     * and returns an AddAppointmentCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAppointmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_APPOINTMENT_STARTTIME,
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
        int duration = ParserUtil.parsePositiveInteger(argMultimap.getValue(PREFIX_APPOINTMENT_DURATION).get());
        String note = argMultimap.getValue(PREFIX_APPOINTMENT_NOTE).orElse("");

        Appointment appointment = new Appointment(startTimeStr, duration, note);

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
