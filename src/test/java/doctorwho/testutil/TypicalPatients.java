package doctorwho.testutil;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_IBUPROFEN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DURATION;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NOTE;
import static doctorwho.logic.commands.CommandTestUtil.VALID_APPOINTMENT_STARTTIME;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_ASTHMA;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_DIABETES;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_HYPERTENSION;
import static doctorwho.logic.commands.CommandTestUtil.VALID_DOB_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_DOB_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NRIC_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_NRIC_BOB;
import static doctorwho.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static doctorwho.logic.commands.CommandTestUtil.VALID_PHONE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import doctorwho.model.AddressBook;
import doctorwho.model.patient.Appointment;
import doctorwho.model.patient.Patient;

/**
 * A utility class containing a list of {@code Patient} objects to be used in tests.
 */
public class TypicalPatients {

    public static final Patient ALICE = new PatientBuilder().withName("Alice Pauline")
            .withNric("S9435125A").withDateOfBirth("01-04-2003")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withAllergies(VALID_ALLERGY_ASPIRIN).withConditions(VALID_CONDITION_DIABETES)
            .withAppointment(new Appointment(
                    VALID_APPOINTMENT_STARTTIME,
                    VALID_APPOINTMENT_DURATION,
                    VALID_APPOINTMENT_NOTE))
            .build();
    public static final Patient BENSON = new PatientBuilder().withName("Benson Meier")
            .withNric("S9876543C").withDateOfBirth("02-04-2003")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withAllergies(VALID_ALLERGY_IBUPROFEN, VALID_ALLERGY_ASPIRIN).build();
    public static final Patient CARL = new PatientBuilder().withName("Carl Kurz").withNric("S9535256A")
            .withPhone("95352563").withDateOfBirth("03-04-2003")
            .withEmail("heinz@example.com").withAddress("wall street").withConditions(VALID_CONDITION_ASTHMA).build();
    public static final Patient DANIEL = new PatientBuilder().withName("Daniel Meier").withNric("S8765253Z")
            .withPhone("87652533").withDateOfBirth("04-04-2003")
            .withEmail("cornelia@example.com").withAddress("10th street").withAllergies(VALID_ALLERGY_IBUPROFEN)
            .build();
    public static final Patient ELLE = new PatientBuilder().withName("Elle Meyer").withNric("S0948222I")
            .withPhone("9482224").withDateOfBirth("05-04-2003")
            .withEmail("werner@example.com").withAddress("michegan ave").build();
    public static final Patient FIONA = new PatientBuilder().withName("Fiona Kunz").withNric("S0948242C")
            .withPhone("9482427").withDateOfBirth("06-04-2003")
            .withEmail("lydia@example.com").withAddress("little tokyo")
            .withAllergies(VALID_ALLERGY_ASPIRIN, VALID_ALLERGY_IBUPROFEN).withConditions(VALID_CONDITION_HYPERTENSION)
            .build();
    public static final Patient GEORGE = new PatientBuilder().withName("George Best").withNric("S0948244Z")
            .withPhone("9482442").withDateOfBirth("07-04-2003")
            .withEmail("anna@example.com").withAddress("4th street").build();

    // Manually added
    public static final Patient HOON = new PatientBuilder().withName("Hoon Meier").withNric("S0848242Z")
            .withPhone("8482424").withDateOfBirth("08-04-2003")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Patient IDA = new PatientBuilder().withName("Ida Mueller").withNric("S0848213F")
            .withPhone("8482131").withDateOfBirth("09-04-2003")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Patient's details found in {@code CommandTestUtil}
    public static final Patient AMY = new PatientBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
        .withNric(VALID_NRIC_AMY).withDateOfBirth(VALID_DOB_AMY)
        .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withAllergies(VALID_ALLERGY_ASPIRIN)
        .withConditions(VALID_CONDITION_ASTHMA).build();
    public static final Patient BOB = new PatientBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
        .withNric(VALID_NRIC_BOB).withDateOfBirth(VALID_DOB_BOB)
        .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
        .withAllergies(VALID_ALLERGY_IBUPROFEN, VALID_ALLERGY_ASPIRIN).withConditions(VALID_CONDITION_HYPERTENSION)
        .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPatients() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical patients.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Patient patient : getTypicalPatients()) {
            ab.addPatient(patient);
        }
        return ab;
    }

    /**
     * Returns a list of all typical patients.
     */
    public static List<Patient> getTypicalPatients() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
