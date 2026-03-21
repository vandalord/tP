package doctorwho.logic.parser;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static doctorwho.testutil.Assert.assertThrows;
import static doctorwho.testutil.TypicalIndexes.INDEX_FIRST_PATIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import doctorwho.logic.parser.exceptions.ParseException;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Phone;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_ALLERGY = "#aspirin";
    private static final String INVALID_CONDITION = "#diabetes";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_ALLERGY_1 = VALID_ALLERGY_ASPIRIN;
    private static final String VALID_CONDITION_2 = "diabetes";

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
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
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
}
