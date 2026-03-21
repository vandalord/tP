package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.*;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static doctorwho.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static doctorwho.testutil.TypicalPersons.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import doctorwho.commons.core.index.Index;
import doctorwho.logic.Messages;
import doctorwho.logic.commands.EditCommand.EditPersonDescriptor;
import doctorwho.model.AddressBook;
import doctorwho.model.Model;
import doctorwho.model.ModelManager;
import doctorwho.model.UserPrefs;
import doctorwho.model.patient.Patient;
import doctorwho.testutil.EditPersonDescriptorBuilder;
import doctorwho.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Patient editedPatient = new PatientBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPatient).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Patient lastPatient = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PatientBuilder personInList = new PatientBuilder(lastPatient);
        Patient editedPatient = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withAllergies(VALID_ALLERGY_IBUPROFEN).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withAllergies(VALID_ALLERGY_IBUPROFEN).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPatient, editedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Patient editedPatient = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Patient patientInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Patient editedPatient = new PatientBuilder(patientInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Patient firstPatient = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPatient).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit patient in filtered list into a duplicate in address book
        Patient patientInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(patientInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_editAllergyOnly_keepsOtherTags() {
        Patient patientToEdit = new PatientBuilder()
                .withAllergies(VALID_ALLERGY_ASPIRIN, "penicillin")
                .withConditions("diabetes")
                .build();
        model.addPerson(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        // edit allergies only
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withAllergies("sulfonamide").build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        // only allergies change, conditions stay
        Patient expectedPatient = new PatientBuilder()
                .withAllergies("sulfonamide")
                .withConditions("diabetes")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editConditionOnly_keepsOtherTags() {
        Patient patientToEdit = new PatientBuilder().withAllergies("ibuprofen", VALID_ALLERGY_ASPIRIN)
                .withConditions("diabetes")
                .build();
        model.addPerson(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withConditions("asthma").build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder().withAllergies("ibuprofen", VALID_ALLERGY_ASPIRIN)
                .withConditions("asthma")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editMultipleConditions_success() {
        Patient patientToEdit = new PatientBuilder()
                .withAllergies(VALID_ALLERGY_ASPIRIN)
                .withConditions("diabetes", "hypertension")
                .build();
        model.addPerson(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withConditions("asthma", "hypertension").build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
                .withAllergies(VALID_ALLERGY_ASPIRIN)
                .withConditions("asthma", "hypertension")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editAllergyAndCondition_bothChange() {
        Patient patientToEdit = new PatientBuilder()
                .withAllergies(VALID_ALLERGY_ASPIRIN)
                .withConditions("diabetes")
                .build();
        model.addPerson(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withAllergies("penicillin")
                .withConditions("asthma").build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
                .withAllergies("penicillin")
                .withConditions("asthma")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_resetAllergies_success() {
        Patient patientToEdit = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        model.addPerson(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
            .withAllergies()
            .build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
            .withAllergies()
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
            Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_resetConditions_success() {
        Patient patientToEdit = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        model.addPerson(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
            .withConditions()
            .build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions()
            .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
            Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addAllergiesToPatientWithNone_success() {
        Patient patientToEdit = new PatientBuilder()
            .withAllergies()
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        model.addPerson(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
            Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addConditionsToPatientWithNone_success() {
        Patient patientToEdit = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions()
            .build();
        model.addPerson(patientToEdit);
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        EditCommand editCommand = new EditCommand(lastIndex, descriptor);

        Patient expectedPatient = new PatientBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
            Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientToEdit, expectedPatient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }
}
