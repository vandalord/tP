package doctorwho.logic.commands;

import static doctorwho.logic.commands.CommandTestUtil.DESC_AMY;
import static doctorwho.logic.commands.CommandTestUtil.DESC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_IBUPROFEN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_DIABETES;
import static doctorwho.logic.commands.CommandTestUtil.VALID_DOB_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NRIC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import doctorwho.logic.commands.EditCommand.EditPatientDescriptor;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.testutil.EditPatientDescriptorBuilder;

public class EditPatientDescriptorTest {

    @Test
    public void isAnyFieldEdited_onlyAllergiesSet_returnsTrue() {
        EditPatientDescriptor descriptor = new EditPatientDescriptor();
        descriptor.setAllergies(Set.of(new Allergy(VALID_ALLERGY_ASPIRIN)));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_onlyConditionsSet_returnsTrue() {
        EditPatientDescriptor descriptor = new EditPatientDescriptor();
        descriptor.setConditions(Set.of(new Condition(VALID_CONDITION_DIABETES)));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void copyConstructor_copiesAllergiesAndConditions() {
        EditPatientDescriptor original = new EditPatientDescriptorBuilder().withAllergies(VALID_ALLERGY_ASPIRIN)
                .withConditions(VALID_CONDITION_DIABETES).build();
        EditPatientDescriptor copy = new EditPatientDescriptor(original);

        assertEquals(original.getAllergies(), copy.getAllergies());
        assertEquals(original.getConditions(), copy.getConditions());
    }

    @Test
    public void equals() {
        // same values -> returns true
        EditPatientDescriptor descriptorWithSameValues = new EditPatientDescriptor(DESC_AMY);
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
        EditPatientDescriptor editedAmy = new EditPatientDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPatientDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different nric -> returns false
        editedAmy = new EditPatientDescriptorBuilder(DESC_AMY).withNric(VALID_NRIC_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different dob -> returns false
        editedAmy = new EditPatientDescriptorBuilder(DESC_AMY).withDateOfBirth(VALID_DOB_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPatientDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditPatientDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different allergies -> returns false
        editedAmy = new EditPatientDescriptorBuilder(DESC_AMY).withAllergies(VALID_ALLERGY_IBUPROFEN).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different conditions -> returns false
        editedAmy = new EditPatientDescriptorBuilder(DESC_AMY).withConditions(VALID_CONDITION_DIABETES).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPatientDescriptor editPatientDescriptor = new EditPatientDescriptor();
        String expected = EditPatientDescriptor.class.getCanonicalName()
                + "{name=" + editPatientDescriptor.getName().orElse(null)
                + ", nric=" + editPatientDescriptor.getNric().orElse(null)
                + ", dob=" + editPatientDescriptor.getDateOfBirth().orElse(null)
                + ", phone=" + editPatientDescriptor.getPhone().orElse(null)
                + ", email=" + editPatientDescriptor.getEmail().orElse(null)
                + ", address=" + editPatientDescriptor.getAddress().orElse(null)
                + ", allergies=" + editPatientDescriptor.getAllergies().orElse(null)
                + ", conditions=" + editPatientDescriptor.getConditions().orElse(null)
                + "}";
        assertEquals(expected, editPatientDescriptor.toString());
    }
}
