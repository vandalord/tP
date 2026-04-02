package doctorwho.logic.parser;

import static doctorwho.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static doctorwho.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static doctorwho.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static doctorwho.logic.parser.CliSyntax.PREFIX_CONDITION;
import static doctorwho.logic.parser.CliSyntax.PREFIX_DOB;
import static doctorwho.logic.parser.CliSyntax.PREFIX_EMAIL;
import static doctorwho.logic.parser.CliSyntax.PREFIX_NAME;
import static doctorwho.logic.parser.CliSyntax.PREFIX_NRIC;
import static doctorwho.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Set;
import java.util.stream.Stream;

import doctorwho.logic.commands.AddCommand;
import doctorwho.logic.parser.exceptions.ParseException;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.DateOfBirth;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Nric;
import doctorwho.model.patient.Patient;
import doctorwho.model.patient.Phone;
import doctorwho.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_NRIC, PREFIX_DOB, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_ALLERGY, PREFIX_CONDITION);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_NRIC,
                PREFIX_DOB, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_NRIC, PREFIX_DOB,
                PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Nric nric = ParserUtil.parseNric(argMultimap.getValue(PREFIX_NRIC).get());
        DateOfBirth dob = ParserUtil.parseDateOfBirth(argMultimap.getValue(PREFIX_DOB).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Tag> tags = ParserUtil.parseAllergies(argMultimap.getAllValues(PREFIX_ALLERGY));
        tags.addAll(ParserUtil.parseConditions(argMultimap.getAllValues(PREFIX_CONDITION)));

        Patient patient = new Patient(name, nric, dob, phone, email, address, tags);

        return new AddCommand(patient);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
