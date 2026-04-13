package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.assertCommandSuccess;
import static doctorwho.logic.commands.CommandTestUtil.showPatientAtIndex;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static doctorwho.testutil.TypicalPatients.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import doctorwho.model.AddressBook;
import doctorwho.model.Model;
import doctorwho.model.ModelManager;
import doctorwho.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_emptyList_showsEmptyMessage() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        assertCommandSuccess(new ListCommand(), emptyModel, ListCommand.MESSAGE_EMPTY, emptyModel);
    }
}
