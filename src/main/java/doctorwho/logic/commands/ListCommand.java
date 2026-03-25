package doctorwho.logic.commands;

import static doctorwho.model.Model.PREDICATE_SHOW_ALL_PATIENTS;
import static java.util.Objects.requireNonNull;

import doctorwho.model.Model;

/**
 * Lists all patients in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all patients.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
