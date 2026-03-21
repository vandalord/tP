package doctorwho.model;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_IBUPROFEN;
import static doctorwho.testutil.Assert.assertThrows;
import static doctorwho.testutil.TypicalPatients.ALICE;
import static doctorwho.testutil.TypicalPatients.getTypicalAddressBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import doctorwho.model.patient.Patient;
import doctorwho.model.patient.exceptions.DuplicatePatientException;
import doctorwho.testutil.PatientBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPatientList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatepatients_throwsDuplicatePatientException() {
        // Two patients with the same identity fields
        Patient editedAlice = new PatientBuilder(ALICE).withAddress(VALID_ADDRESS_BOB)
                .withAllergies(VALID_ALLERGY_IBUPROFEN)
                .build();
        List<Patient> newPatients = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPatients);

        assertThrows(DuplicatePatientException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPatient_nullPatient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPatient(null));
    }

    @Test
    public void hasPatient_PatientNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPatient(ALICE));
    }

    @Test
    public void hasPatient_PatientInAddressBook_returnsTrue() {
        addressBook.addPatient(ALICE);
        assertTrue(addressBook.hasPatient(ALICE));
    }

    @Test
    public void hasPatient_PatientWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPatient(ALICE);
        Patient editedAlice = new PatientBuilder(ALICE).withAddress(VALID_ADDRESS_BOB)
                .withAllergies(VALID_ALLERGY_IBUPROFEN)
                .build();
        assertTrue(addressBook.hasPatient(editedAlice));
    }

    @Test
    public void getPatientList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPatientList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{patients=" + addressBook.getPatientList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose patients list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Patient> patients = FXCollections.observableArrayList();

        AddressBookStub(Collection<Patient> patients) {
            this.patients.setAll(patients);
        }

        @Override
        public ObservableList<Patient> getPatientList() {
            return patients;
        }
    }

}
