package doctorwho.storage;

import static doctorwho.storage.JsonAdaptedPatient.MISSING_FIELD_MESSAGE_FORMAT;
import static doctorwho.testutil.Assert.assertThrows;
import static doctorwho.testutil.TypicalPatients.ALICE;
import static doctorwho.testutil.TypicalPatients.BENSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import doctorwho.commons.exceptions.IllegalValueException;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.Appointment;
import doctorwho.model.patient.DateOfBirth;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Nric;
import doctorwho.model.patient.Phone;
import doctorwho.model.patient.Sex;

public class JsonAdaptedPatientTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_PHONE_TOO_LONG = "1234567890123456";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_NRIC = "1234567A";
    private static final String INVALID_SEX = "O";
    private static final String INVALID_DOB = "2003-02-03";
    private static final String INVALID_TAG = "allergy:" + "#friend";
    private static final String INVALID_APPT_START = "2026/03/10 14:00";
    private static final Integer INVALID_APPT_DURATION = -10;
    private static final String INVALID_APPT_NOTE = // 501 characters
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse cursus lectus ac leo iaculis, at posu"
                    + "ere massa maximus. Phasellus bibendum, lacus non posuere sodales, odio massa aliquam mi, vel sod"
                    + "ales nunc est ut neque. Sed quis est ac nisi accumsan pellentesque. Quisque gravida tortor et ma"
                    + "ssa posuere egestas. Vestibulum interdum nibh dui, a accumsan libero condimentum id. Maecenas in"
                    + " vestibulum sem. Donec ac erat sodales, accumsan nisi non, elementum erat. Cras enim risus, semp"
                    + "er sit nam. ";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_NRIC = BENSON.getNric().toString();
    private static final String VALID_SEX = BENSON.getSex().toString();
    private static final String VALID_DOB = BENSON.getDateOfBirth().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final String VALID_APPT_START = "10-03-2026 14:00";
    private static final Integer VALID_APPT_DURATION = 30;
    private static final String VALID_APPT_NOTE = "General checkup";

    @Test
    public void toModelType_validPatientDetails_returnsPatient() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(BENSON);
        assertEquals(BENSON, patient.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(INVALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(null, VALID_NRIC, VALID_SEX, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidNric_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, INVALID_NRIC, VALID_SEX, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = Nric.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_nullNric_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, null, VALID_SEX, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Nric.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidSex_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, INVALID_SEX, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = Sex.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_nullSex_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, null, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Sex.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidDob_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, INVALID_DOB,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = DateOfBirth.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_nullDob_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, null,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, DateOfBirth.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidPhoneTooLong_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                INVALID_PHONE_TOO_LONG, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                null, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                VALID_PHONE, null, VALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, null,
                VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                invalidTags, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        assertThrows(IllegalValueException.class, patient::toModelType);
    }

    @Test
    public void toModelType_nullTags_returnsPatient() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB,
                VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                null, VALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        assertEquals(true, patient.toModelType().getTags().isEmpty());
    }

    // Combining Test Inputs with Appointment details
    // Appointments have 3 fields: startTime, duration and note
    // fields can be VV, IV or null
    // To maximize efficiency & effectiveness and to achieve 100% branch coverage we have the following testcases
    // Testcase | startTime | duration  | note      | expected
    // 1        | VV        | VV        | VV        | success
    // 2        | IV        | VV        | VV        | exception
    // 3        | VV        | IV        | VV        | exception
    // 4        | VV        | VV        | IV        | exception
    // 5        | null      | VV        | VV        | load without appointment
    // 6        | VV        | null      | VV        | load without appointment
    // 7        | VV        | VV        | null      | load without appointment

    // Testcase 1
    @Test
    public void toModelType_validAppointment_returnsPatient() throws Exception {
        // Alice is a valid patient with a valid appointment
        JsonAdaptedPatient patient = new JsonAdaptedPatient(ALICE);
        assertEquals(ALICE, patient.toModelType());
    }

    // Testcase 2
    @Test
    public void toModelType_invalidAppointmentStartTime_throwsIllegalValueException() {
        JsonAdaptedPatient patient =
                new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_TAGS, INVALID_APPT_START, VALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = Appointment.STARTTIME_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    // Testcase 3
    @Test
    public void toModelType_invalidAppointmentDuration_throwsIllegalValueException() {
        JsonAdaptedPatient patient =
                new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_TAGS, VALID_APPT_START, INVALID_APPT_DURATION, VALID_APPT_NOTE);
        String expectedMessage = Appointment.DURATION_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    // Testcase 4
    @Test
    public void toModelType_invalidAppointmentNote_throwsIllegalValueException() {
        JsonAdaptedPatient patient =
                new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, INVALID_APPT_NOTE);
        String expectedMessage = Appointment.NOTE_CONSTRAINTS;

        assertThrows(IllegalValueException.class, expectedMessage, patient::toModelType);
    }

    // Testcase 5
    @Test
    public void toModelType_nullAppointmentStartTime_returnsPatient() throws Exception {
        // Patients without appointments should load successfully
        JsonAdaptedPatient patient =
                new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_TAGS, null, VALID_APPT_DURATION, VALID_APPT_NOTE);
        assertEquals(false, patient.toModelType().getAppointment().isPresent());
    }

    // Testcase 6
    @Test
    public void toModelType_nullAppointmentDuration_returnsPatient() throws Exception {
        // Patients without appointments should load successfully
        JsonAdaptedPatient patient =
                new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_TAGS, VALID_APPT_START, null, VALID_APPT_NOTE);
        assertEquals(false, patient.toModelType().getAppointment().isPresent());
    }

    // Testcase 7
    @Test
    public void toModelType_nullAppointmentNote_returnsPatient() throws Exception {
        // Patients without appointments should load successfully
        JsonAdaptedPatient patient =
                new JsonAdaptedPatient(VALID_NAME, VALID_NRIC, VALID_SEX, VALID_DOB, VALID_PHONE, VALID_EMAIL,
                        VALID_ADDRESS, VALID_TAGS, VALID_APPT_START, VALID_APPT_DURATION, null);
        assertEquals(false, patient.toModelType().getAppointment().isPresent());
    }
}
