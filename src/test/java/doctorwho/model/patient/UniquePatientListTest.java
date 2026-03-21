package doctorwho.model.patient;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_IBUPROFEN;
import static doctorwho.testutil.Assert.assertThrows;
import static doctorwho.testutil.TypicalPatients.ALICE;
import static doctorwho.testutil.TypicalPatients.BOB;
import static doctorwho.testutil.TypicalPatients.ALICE;
import static doctorwho.testutil.TypicalPatients.BOB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import doctorwho.model.patient.exceptions.DuplicatePatientException;
import doctorwho.model.patient.exceptions.PatientNotFoundException;
import doctorwho.testutil.PatientBuilder;

public class UniquePatientListTest {

    private final UniquePatientList UniquePatientList = new UniquePatientList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> UniquePatientList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(UniquePatientList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        UniquePatientList.add(ALICE);
        assertTrue(UniquePatientList.contains(ALICE));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        UniquePatientList.add(ALICE);
        Patient editedAlice = new PatientBuilder(ALICE).withAddress(VALID_ADDRESS_BOB)
                .withAllergies(VALID_ALLERGY_IBUPROFEN)
                .build();
        assertTrue(UniquePatientList.contains(editedAlice));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> UniquePatientList.add(null));
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePatientException() {
        UniquePatientList.add(ALICE);
        assertThrows(DuplicatePatientException.class, () -> UniquePatientList.add(ALICE));
    }

    @Test
    public void setPatient_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> UniquePatientList.setPatient(null, ALICE));
    }

    @Test
    public void setPatient_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> UniquePatientList.setPatient(ALICE, null));
    }

    @Test
    public void setPatient_targetPersonNotInList_throwsPatientNotFoundException() {
        assertThrows(PatientNotFoundException.class, () -> UniquePatientList.setPatient(ALICE, ALICE));
    }

    @Test
    public void setPatient_editedPersonIsSamePerson_success() {
        UniquePatientList.add(ALICE);
        UniquePatientList.setPatient(ALICE, ALICE);
        UniquePatientList expectedUniquePatientList = new UniquePatientList();
        expectedUniquePatientList.add(ALICE);
        assertEquals(expectedUniquePatientList, UniquePatientList);
    }

    @Test
    public void setPatient_editedPersonHasSameIdentity_success() {
        UniquePatientList.add(ALICE);
        Patient editedAlice = new PatientBuilder(ALICE).withAddress(VALID_ADDRESS_BOB)
                .withAllergies(VALID_ALLERGY_IBUPROFEN)
                .build();
        UniquePatientList.setPatient(ALICE, editedAlice);
        UniquePatientList expectedUniquePatientList = new UniquePatientList();
        expectedUniquePatientList.add(editedAlice);
        assertEquals(expectedUniquePatientList, UniquePatientList);
    }

    @Test
    public void setPatient_editedPersonHasDifferentIdentity_success() {
        UniquePatientList.add(ALICE);
        UniquePatientList.setPatient(ALICE, BOB);
        UniquePatientList expectedUniquePatientList = new UniquePatientList();
        expectedUniquePatientList.add(BOB);
        assertEquals(expectedUniquePatientList, UniquePatientList);
    }

    @Test
    public void setPatient_editedPersonHasNonUniqueIdentity_throwsDuplicatePatientException() {
        UniquePatientList.add(ALICE);
        UniquePatientList.add(BOB);
        assertThrows(DuplicatePatientException.class, () -> UniquePatientList.setPatient(ALICE, BOB));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> UniquePatientList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPatientNotFoundException() {
        assertThrows(PatientNotFoundException.class, () -> UniquePatientList.remove(ALICE));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        UniquePatientList.add(ALICE);
        UniquePatientList.remove(ALICE);
        UniquePatientList expectedUniquePatientList = new UniquePatientList();
        assertEquals(expectedUniquePatientList, UniquePatientList);
    }

    @Test
    public void setPatients_nullUniquePatientList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> UniquePatientList.setPatients((UniquePatientList) null));
    }

    @Test
    public void setPatients_UniquePatientList_replacesOwnListWithProvidedUniquePatientList() {
        UniquePatientList.add(ALICE);
        UniquePatientList expectedUniquePatientList = new UniquePatientList();
        expectedUniquePatientList.add(BOB);
        UniquePatientList.setPatients(expectedUniquePatientList);
        assertEquals(expectedUniquePatientList, UniquePatientList);
    }

    @Test
    public void setPatients_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> UniquePatientList.setPatients((List<Patient>) null));
    }

    @Test
    public void setPatients_list_replacesOwnListWithProvidedList() {
        UniquePatientList.add(ALICE);
        List<Patient> patientList = Collections.singletonList(BOB);
        UniquePatientList.setPatients(patientList);
        UniquePatientList expectedUniquePatientList = new UniquePatientList();
        expectedUniquePatientList.add(BOB);
        assertEquals(expectedUniquePatientList, UniquePatientList);
    }

    @Test
    public void setPatients_listWithDuplicatePersons_throwsDuplicatePatientException() {
        List<Patient> listWithDuplicatePatients = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicatePatientException.class, () -> UniquePatientList.setPatients(listWithDuplicatePatients));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> UniquePatientList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(UniquePatientList.asUnmodifiableObservableList().toString(), UniquePatientList.toString());
    }
}
