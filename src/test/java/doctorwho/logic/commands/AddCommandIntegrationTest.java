package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.assertCommandFailure;
import static doctorwho.logic.commands.CommandTestUtil.assertCommandSuccess;
import static doctorwho.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import doctorwho.logic.Messages;
import doctorwho.model.Model;
import doctorwho.model.ModelManager;
import doctorwho.model.UserPrefs;
import doctorwho.model.patient.Patient;
import doctorwho.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Patient validPatient = new PatientBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPatient(validPatient);

        assertCommandSuccess(new AddCommand(validPatient), model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPatient)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Patient patientInList = model.getAddressBook().getPatientList().get(0);
        assertCommandFailure(new AddCommand(patientInList), model,
                AddCommand.MESSAGE_DUPLICATE_PATIENT);
    }

}
