package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_SULFONAMIDES;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_DIABETES;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_HYPERTENSION;
import static doctorwho.logic.commands.CommandTestUtil.assertCommandFailure;
import static doctorwho.logic.commands.CommandTestUtil.assertCommandSuccess;
import static doctorwho.testutil.TypicalPatients.getTypicalAddressBook;

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
    public void execute_newPatient_success() {
        Patient validPatient = new PatientBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPatient(validPatient);

        assertCommandSuccess(new AddCommand(validPatient), model,
                String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPatient)),
                expectedModel);
    }

    @Test
    public void execute_newPatientWithMultipleAllergies_success() {
        Patient validPatient = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN, VALID_ALLERGY_SULFONAMIDES)
            .build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPatient(validPatient);

        assertCommandSuccess(new AddCommand(validPatient), model,
            String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPatient)),
            expectedModel);
    }

    @Test
    public void execute_newPatientWithAllergiesNoConditions_success() {
        Patient validPatient = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions()
            .build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPatient(validPatient);

        assertCommandSuccess(new AddCommand(validPatient), model,
            String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPatient)),
            expectedModel);
    }

    @Test
    public void execute_newPatientWithConditionsNoAllergies_success() {
        Patient validPatient = new PatientBuilder()
            .withAllergies()
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPatient(validPatient);

        assertCommandSuccess(new AddCommand(validPatient), model,
            String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPatient)),
            expectedModel);
    }

    @Test
    public void execute_newPatientWithMultipleConditions_success() {
        Patient validPatient = new PatientBuilder()
            .withConditions(VALID_CONDITION_DIABETES, VALID_CONDITION_HYPERTENSION)
            .build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPatient(validPatient);

        assertCommandSuccess(new AddCommand(validPatient), model,
            String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPatient)),
            expectedModel);
    }

    @Test
    public void execute_newPatientWithAllergiesAndConditions_success() {
        Patient validPatient = new PatientBuilder().withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES).build();
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPatient(validPatient);

        assertCommandSuccess(new AddCommand(validPatient), model,
            String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPatient)),
            expectedModel);
    }

    @Test
    public void execute_duplicatePatient_throwsCommandException() {
        Patient patientInList = model.getAddressBook().getPatientList().get(0);
        assertCommandFailure(new AddCommand(patientInList), model,
                AddCommand.MESSAGE_DUPLICATE_PATIENT);
    }

}
