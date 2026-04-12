package doctorwho.logic.parser;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.logic.parser.ParserUtil.MESSAGE_INVALID_DATE;
import static doctorwho.logic.parser.ParserUtil.MESSAGE_INVALID_DATE_FORMAT;
import static doctorwho.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static doctorwho.model.patient.Appointment.MAX_DUR;
import static doctorwho.testutil.Assert.assertThrows;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import doctorwho.logic.parser.exceptions.ParseException;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.DateOfBirth;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Nric;
import doctorwho.model.patient.Phone;
import doctorwho.model.patient.Sex;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_PHONE_TOO_LONG = "1234567890123456"; // 16 digits
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_ALLERGY = "#aspirin";
    private static final String INVALID_CONDITION = "#diabetes";
    private static final String INVALID_APPOINTMENT_DATE = "2026-03-12";
    private static final String INVALID_APPOINTMENT_DATE_NON_LEAP = "29-02-2026";
    private static final String INVALID_SEX = "X"; // 'X' not allowed
    private static final String INVALID_NRIC = "1234567A"; // missing prefix letter
    private static final String INVALID_DOB = "2003-02-04"; // wrong format yyyy-mm-dd

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_SEX = "M";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_ALLERGY_1 = VALID_ALLERGY_ASPIRIN;
    private static final String VALID_CONDITION_2 = "diabetes";
    private static final String VALID_APPOINTMENT_DATE = "12-03-2026";
    private static final String VALID_NRIC = "S1234567D";
    private static final String VALID_DOB = "04-02-2003";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PATIENT, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PATIENT, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseSex_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseSex((String) null));
    }

    @Test
    public void parseSex_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseSex(INVALID_SEX));
    }

    @Test
    public void parseSex_validValueWithoutWhitespace_returnsSex() throws Exception {
        Sex expectedSex = new Sex(VALID_SEX);
        assertEquals(expectedSex, ParserUtil.parseSex(VALID_SEX));
    }

    @Test
    public void parseSex_validValueWithWhitespace_returnsTrimmedSex() throws Exception {
        String sexWithWhitespace = WHITESPACE + VALID_SEX + WHITESPACE;
        Sex expectedSex = new Sex(VALID_SEX);
        assertEquals(expectedSex, ParserUtil.parseSex(sexWithWhitespace));
    }

    @Test
    public void parseNric_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseNric((String) null));
    }

    @Test
    public void parseNric_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseNric(INVALID_NRIC));
    }

    @Test
    public void parseNric_validValueWithoutWhitespace_returnsNric() throws Exception {
        Nric expectedNric = new Nric(VALID_NRIC);
        assertEquals(expectedNric, ParserUtil.parseNric(VALID_NRIC));
    }

    @Test
    public void parseNric_validValueWithWhitespace_returnsTrimmedNric() throws Exception {
        String nricWithWhitespace = WHITESPACE + VALID_NRIC + WHITESPACE;
        Nric expectedNric = new Nric(VALID_NRIC);
        assertEquals(expectedNric, ParserUtil.parseNric(nricWithWhitespace));
    }

    @Test
    public void parseDateOfBirth_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDateOfBirth((String) null));
    }

    @Test
    public void parseDateOfBirth_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDateOfBirth(INVALID_DOB));
    }

    @Test
    public void parseDateOfBirth_validValueWithoutWhitespace_returnsDateOfBirth() throws Exception {
        DateOfBirth expectedDob = new DateOfBirth(VALID_DOB);
        assertEquals(expectedDob, ParserUtil.parseDateOfBirth(VALID_DOB));
    }

    @Test
    public void parseDateOfBirth_validValueWithWhitespace_returnsTrimmedDateOfBirth() throws Exception {
        String dobWithWhitespace = WHITESPACE + VALID_DOB + WHITESPACE;
        DateOfBirth expectedDob = new DateOfBirth(VALID_DOB);
        assertEquals(expectedDob, ParserUtil.parseDateOfBirth(dobWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_tooLongValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE_TOO_LONG));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseAllergy_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAllergies(null));
    }

    @Test
    public void parseAllergy_validValueWithoutWhitespace_returnsAllergy() throws Exception {
        List<String> allergyList = new ArrayList<>();
        allergyList.add(VALID_ALLERGY_1);
        Set<Tag> expected = new HashSet<>();
        expected.add(new Allergy(VALID_ALLERGY_1));
        assertEquals(expected, ParserUtil.parseAllergies(allergyList));
    }

    @Test
    public void parseAllergy_validValueWithWhitespace_returnsTrimmedAllergy() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_ALLERGY_1 + WHITESPACE;
        List<String> allergyList = new ArrayList<>();
        allergyList.add(tagWithWhitespace);
        Set<Tag> expected = new HashSet<>();
        expected.add(new Allergy(VALID_ALLERGY_1));
        assertEquals(expected, ParserUtil.parseAllergies(allergyList));
    }

    @Test
    public void parseAllergies_validTag_returnsAllergy() throws ParseException {
        List<String> allergyList = new ArrayList<>();
        allergyList.add(VALID_ALLERGY_ASPIRIN);
        Set<Tag> allergy = ParserUtil.parseAllergies(allergyList);
        assertEquals("[[Aspirin]]", allergy.toString());
    }

    @Test
    public void parseAllergy_invalidTag_throwsParseException() {
        List<String> allergyList = new ArrayList<>();
        allergyList.add("!!!");
        assertThrows(ParseException.class, () -> ParserUtil.parseAllergies(allergyList));
    }

    @Test
    public void parseAllergies_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAllergies(null));
    }

    @Test
    public void parseAllergies_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil
                .parseAllergies(Arrays.asList(VALID_ALLERGY_1, INVALID_ALLERGY)));
    }

    @Test
    public void parseAllergies_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseAllergies(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseCondition_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseConditions(null));
    }

    @Test
    public void parseCondition_validValueWithoutWhitespace_returnsCondition() throws Exception {
        List<String> conditionList = new ArrayList<>();
        conditionList.add(VALID_CONDITION_2);
        Set<Tag> expected = new HashSet<>();
        expected.add(new Condition(VALID_CONDITION_2));
        assertEquals(expected, ParserUtil.parseConditions(conditionList));
    }

    @Test
    public void parseCondition_validValueWithWhitespace_returnsTrimmedCondition() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_CONDITION_2 + WHITESPACE;
        List<String> conditionList = new ArrayList<>();
        conditionList.add(tagWithWhitespace);
        Set<Tag> expected = new HashSet<>();
        expected.add(new Condition(VALID_CONDITION_2));
        assertEquals(expected, ParserUtil.parseConditions(conditionList));
    }

    @Test
    public void parseConditions_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseConditions(null));
    }

    @Test
    public void parseConditions_collectionWithValidTags_returnsconditionSet() throws Exception {
        Set<Condition> expected = new HashSet<>(Arrays.asList(
                new Condition(VALID_CONDITION_2), new Condition("asthma")));
        assertEquals(expected, ParserUtil.parseConditions(
                Arrays.asList(VALID_CONDITION_2, "asthma")));
    }

    @Test
    public void parseConditions_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseConditions(
                Arrays.asList(VALID_CONDITION_2, INVALID_ALLERGY)));
    }

    @Test
    public void parseCondition_invalidValue_throwsParseException() {
        List<String> conditionList = new ArrayList<>();
        conditionList.add(INVALID_CONDITION);
        assertThrows(ParseException.class, () -> ParserUtil.parseConditions(conditionList));
    }

    @Test
    public void parseConditions_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseConditions(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseCondition_validTag_returnsCondition() throws ParseException {
        List<String> conditionsList = new ArrayList<>();
        conditionsList.add("Asthma");
        Set<Tag> conditions = ParserUtil.parseConditions(conditionsList);
        assertEquals("[[Asthma]]", conditions.toString());
    }

    @Test
    public void parseCondition_invalidTag_throwsParseException() {
        List<String> conditionsList = new ArrayList<>();
        conditionsList.add("!!!");
        assertThrows(ParseException.class, () -> ParserUtil.parseConditions(conditionsList));
    }

    @Test
    public void parseAppointmentDuration_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAppointmentDuration(null));
    }

    // BVA for duration
    // 0 : invalid
    // 1 to MAX_DUR : valid
    // >MAX_DUR : invalid

    @Test
    public void parseAppointmentDuration_validDuration_success() throws ParseException {
        assertEquals(1, ParserUtil.parseAppointmentDuration(String.valueOf(1)));
        assertEquals(MAX_DUR, ParserUtil.parseAppointmentDuration(String.valueOf(MAX_DUR)));
    }

    @Test
    public void parseAppointmentDuration_invalidDuration_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseAppointmentDuration(String.valueOf(Integer.MAX_VALUE)));
        assertThrows(ParseException.class, () ->
                ParserUtil.parseAppointmentDuration(String.valueOf(MAX_DUR + 1)));
        assertThrows(ParseException.class, () ->
                ParserUtil.parseAppointmentDuration(String.valueOf(0)));
    }

    @Test
    public void parseAppointmentDate_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDate(null));
    }

    @Test
    public void parseAppointmentDate_invalidFormat_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_DATE_FORMAT, () ->
                ParserUtil.parseDate(INVALID_APPOINTMENT_DATE));
    }

    @Test
    public void parseAppointmentDate_nonLeapDate_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_DATE, () ->
                ParserUtil.parseDate(INVALID_APPOINTMENT_DATE_NON_LEAP));
    }

    @Test
    public void parseAppointmentDate_validValueWithoutWhitespace_returnsLocalDate() throws Exception {
        LocalDate expectedDate = LocalDate.of(2026, 3, 12);
        assertEquals(expectedDate, ParserUtil.parseDate(VALID_APPOINTMENT_DATE));
    }

    @Test
    public void parseAppointmentDate_validValueWithWhitespace_returnsTrimmedLocalDate() throws Exception {
        String dateWithWhitespace = WHITESPACE + VALID_APPOINTMENT_DATE + WHITESPACE;
        LocalDate expectedDate = LocalDate.of(2026, 3, 12);
        assertEquals(expectedDate, ParserUtil.parseDate(dateWithWhitespace));
    }
}
