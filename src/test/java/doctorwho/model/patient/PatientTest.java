package doctorwho.model.patient;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_IBUPROFEN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_DIABETES;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_HYPERTENSION;
import static doctorwho.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static doctorwho.testutil.Assert.assertThrows;
import static doctorwho.testutil.TypicalPatients.ALICE;
import static doctorwho.testutil.TypicalPatients.BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import doctorwho.testutil.PatientBuilder;

public class PatientTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Patient patient = new PatientBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> patient.getTags().remove(0));
    }

    @Test
    public void isSamePatient() {
        // same object -> returns true
        assertTrue(ALICE.isSamePatient(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePatient(null));

        // same name, all other attributes different -> returns true
        Patient editedAlice = new PatientBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withAllergies(VALID_ALLERGY_IBUPROFEN).build();
        assertTrue(ALICE.isSamePatient(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PatientBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePatient(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Patient editedBob = new PatientBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePatient(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PatientBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePatient(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Patient aliceCopy = new PatientBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different patient -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Patient editedAlice = new PatientBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PatientBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PatientBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PatientBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different allergies -> returns false
        editedAlice = new PatientBuilder(ALICE).withAllergies(VALID_ALLERGY_IBUPROFEN).build();
        assertFalse(ALICE.equals(editedAlice));

        // same allergies -> returns true
        Patient patientWithAllergies = new PatientBuilder(ALICE).withAllergies(VALID_ALLERGY_IBUPROFEN).build();
        Patient patientWithSameAllergies = new PatientBuilder(ALICE).withAllergies(VALID_ALLERGY_IBUPROFEN).build();
        assertTrue(patientWithAllergies.equals(patientWithSameAllergies));

        // same conditions -> returns true
        Patient patientWithConditions = new PatientBuilder(ALICE).withConditions(VALID_CONDITION_DIABETES).build();
        Patient patientWithSameConditions = new PatientBuilder(ALICE).withConditions(VALID_CONDITION_DIABETES).build();
        assertTrue(patientWithConditions.equals(patientWithSameConditions));

        // same allergies and conditions -> returns true
        Patient patientWithBoth = new PatientBuilder(ALICE).withAllergies(VALID_ALLERGY_IBUPROFEN)
            .withConditions(VALID_CONDITION_DIABETES).build();
        Patient patientWithSameBoth = new PatientBuilder(ALICE).withAllergies(VALID_ALLERGY_IBUPROFEN)
            .withConditions(VALID_CONDITION_DIABETES).build();
        assertTrue(patientWithBoth.equals(patientWithSameBoth));

        // different conditions -> returns false
        editedAlice = new PatientBuilder(ALICE).withConditions(VALID_CONDITION_HYPERTENSION).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Patient.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress() + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
