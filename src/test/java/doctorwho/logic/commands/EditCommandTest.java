package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.DESC_AMY;
import static doctorwho.logic.commands.CommandTestUtil.DESC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_IBUPROFEN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_DIABETES;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static doctorwho.logic.commands.CommandTestUtil.assertCommandFailure;
import static doctorwho.logic.commands.CommandTestUtil.assertCommandSuccess;
import static doctorwho.logic.commands.CommandTestUtil.showPatientAtIndex;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static doctorwho.testutil.TypicalIndexes.INDEX_SECOND_PATIENT;
import static doctorwho.testutil.TypicalPatients.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import doctorwho.commons.core.index.Index;
import doctorwho.logic.Messages;
import doctorwho.logic.commands.EditCommand.EditPatientDescriptor;
import doctorwho.model.AddressBook;
import doctorwho.model.Model;
import doctorwho.model.ModelManager;
import doctorwho.model.UserPrefs;
import doctorwho.model.patient.Patient;
import doctorwho.testutil.EditPatientDescriptorBuilder;
import doctorwho.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Patient editedPatient = new PatientBuilder().build();
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder(editedPatient).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PATIENT, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
            Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(model.getFilteredPatientList().get(0), editedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPatient = Index.fromOneBased(model.getFilteredPatientList().size());
        Patient lastPatient = model.getFilteredPatientList().get(indexLastPatient.getZeroBased());

        PatientBuilder patientInList = new PatientBuilder(lastPatient);
        Patient editedPatient = patientInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withAllergies(VALID_ALLERGY_IBUPROFEN).build();

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withAllergies(VALID_ALLERGY_IBUPROFEN).build();
        EditCommand editCommand = new EditCommand(indexLastPatient, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
            Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(lastPatient, editedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PATIENT, new EditPatientDescriptor());
        Patient editedPatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
            Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);

        Patient patientInFilteredList = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        Patient editedPatient = new PatientBuilder(patientInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PATIENT,
                new EditPatientDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
            Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(model.getFilteredPatientList().get(0), editedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePatientUnfilteredList_failure() {
        Patient firstPatient = model.getFilteredPatientList().get(INDEX_FIRST_PATIENT.getZeroBased());
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder(firstPatient).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PATIENT, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PATIENT);
    }

    @Test
    public void execute_duplicatePatientFilteredList_failure() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);

        // edit patient in filtered list into a duplicate in address book
        Patient patientInList = model.getAddressBook().getPatientList().get(INDEX_SECOND_PATIENT.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PATIENT,
                new EditPatientDescriptorBuilder(patientInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PATIENT);
    }

    @Test
    public void execute_invalidPatientIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPatientList().size() + 1);
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPatientIndexFilteredList_failure() {
        showPatientAtIndex(model, INDEX_FIRST_PATIENT);
        Index outOfBoundIndex = INDEX_SECOND_PATIENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPatientList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPatientDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
    }

    /**
     * Tests that editing allergies only keeps existing conditions unchanged.
     */
    @Test
    public void execute_editAllergyOnly_keepsOtherTags() {
        Patient patientToEdit = new PatientBuilder()
                .withAllergies(VALID_ALLERGY_ASPIRIN, "penicillin")
                .withConditions("diabetes")
                .build();
        model.addPatient(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPatientList().size());

        // edit allergies only
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withAllergies("sulfonamide").build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        // only allergies change, conditions stay
        Patient expectedPatient = new PatientBuilder()
                .withAllergies("sulfonamide")
                .withConditions("diabetes")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
                Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Tests that editing conditions only keeps existing allergies unchanged.
     */
    @Test
    public void execute_editConditionOnly_keepsOtherTags() {
        Patient patientToEdit = new PatientBuilder().withAllergies("ibuprofen", VALID_ALLERGY_ASPIRIN)
                .withConditions("diabetes")
                .build();
        model.addPatient(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPatientList().size());

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
                .withConditions("asthma").build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder().withAllergies("ibuprofen", VALID_ALLERGY_ASPIRIN)
                .withConditions("asthma")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
                Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Tests that multiple conditions can be edited at once.
     */
    @Test
    public void execute_editMultipleConditions_success() {
        Patient patientToEdit = new PatientBuilder()
                .withAllergies(VALID_ALLERGY_ASPIRIN)
                .withConditions("diabetes", "hypertension")
                .build();
        model.addPatient(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPatientList().size());

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
                .withConditions("asthma", "hypertension").build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
                .withAllergies(VALID_ALLERGY_ASPIRIN)
                .withConditions("asthma", "hypertension")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
                Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Tests that editing both allergies and conditions changes both.
     */
    @Test
    public void execute_editAllergyAndCondition_bothChange() {
        Patient patientToEdit = new PatientBuilder()
                .withAllergies(VALID_ALLERGY_ASPIRIN)
                .withConditions("diabetes")
                .build();
        model.addPatient(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPatientList().size());

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
                .withAllergies("penicillin")
                .withConditions("asthma").build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
                .withAllergies("penicillin")
                .withConditions("asthma")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
                Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Tests that providing an empty allergy prefix clears all existing allergies.
     */
    @Test
    public void execute_resetAllergies_success() {
        Patient patientToEdit = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        model.addPatient(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPatientList().size());

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
            .withAllergies()
            .build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
            .withAllergies()
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
            Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Tests that providing an empty condition prefix clears all existing conditions.
     */
    @Test
    public void execute_resetConditions_success() {
        Patient patientToEdit = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        model.addPatient(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPatientList().size());

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
            .withConditions()
            .build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions()
            .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
            Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Tests that allergies can be added to a patient who has none.
     */
    @Test
    public void execute_addAllergiesToPatientWithNone_success() {
        Patient patientToEdit = new PatientBuilder()
            .withAllergies()
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        model.addPatient(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPatientList().size());

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
            Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Tests that conditions can be added to a patient who has none.
     */
    @Test
    public void execute_addConditionsToPatientWithNone_success() {
        Patient patientToEdit = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions()
            .build();
        model.addPatient(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPatientList().size());

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PATIENT_SUCCESS,
            Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPatient(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PATIENT, DESC_AMY);

        // same values -> returns true
        EditPatientDescriptor copyDescriptor = new EditPatientDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PATIENT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PATIENT, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PATIENT, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPatientDescriptor editPatientDescriptor = new EditPatientDescriptor();
        EditCommand editCommand = new EditCommand(index, editPatientDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPatientDescriptor="
                + editPatientDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }
}
