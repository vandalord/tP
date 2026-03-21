package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import doctorwho.logic.commands.EditCommand.EditPersonDescriptor;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.testutil.EditPersonDescriptorBuilder;

import java.util.Set;

public class EditPatientDescriptorTest {

    @Test
    public void isAnyFieldEdited_onlyAllergiesSet_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setAllergies(Set.of(new Allergy(VALID_ALLERGY_ASPIRIN)));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_onlyConditionsSet_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setConditions(Set.of(new Condition(VALID_CONDITION_DIABETES)));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void copyConstructor_copiesAllergiesAndConditions() {
        EditPersonDescriptor original = new EditPersonDescriptorBuilder()
            .withAllergies(VALID_ALLERGY_ASPIRIN)
            .withConditions(VALID_CONDITION_DIABETES)
            .build();
        EditPersonDescriptor copy = new EditPersonDescriptor(original);

        assertEquals(original.getAllergies(), copy.getAllergies());
        assertEquals(original.getConditions(), copy.getConditions());
    }

    @Test
    public void equals() {
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditPersonDescriptor editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different allergies -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withAllergies(VALID_ALLERGY_IBUPROFEN).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different conditions -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withConditions(VALID_CONDITION_DIABETES).build();
        assertFalse(DESC_AMY.equals(editedAmy));

    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
                + editPersonDescriptor.getName().orElse(null) + ", phone="
                + editPersonDescriptor.getPhone().orElse(null) + ", email="
                + editPersonDescriptor.getEmail().orElse(null) + ", address="
                + editPersonDescriptor.getAddress().orElse(null) + ", allergies="
                + editPersonDescriptor.getAllergies().orElse(null) + ", conditions="
                + editPersonDescriptor.getConditions().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
