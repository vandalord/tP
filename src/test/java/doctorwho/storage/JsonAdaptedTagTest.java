package doctorwho.storage;

import static doctorwho.logic.commands.CommandTestUtil.VALID_ALLERGY_ASPIRIN;
import static doctorwho.logic.commands.CommandTestUtil.VALID_CONDITION_ASTHMA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import doctorwho.commons.exceptions.IllegalValueException;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

public class JsonAdaptedTagTest {
    /**
     * Tests that a valid condition tag string is correctly converted to a Condition model type.
     */
    @Test
    public void toModelType_validConditionTag_returnsCondition() throws Exception {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag("condition:" + VALID_CONDITION_ASTHMA);
        Tag modelTag = adaptedTag.toModelType();

        assertEquals(Condition.class, modelTag.getClass());
    }

    /**
     * Tests that an invalid allergy tag string throws an IllegalValueException.
     */
    @Test
    public void toModelType_invalidAllergyTag_throwsException() {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag("allergy:#invalid");

        assertThrows(IllegalValueException.class, adaptedTag::toModelType);
    }

    /**
     * Tests that an invalid condition tag string throws an IllegalValueException.
     */
    @Test
    public void toModelType_invalidTag_throwsException() {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag("condition:#invalid");

        assertThrows(IllegalValueException.class, adaptedTag::toModelType);
    }


    /**
     * Tests that a tag string with an unknown prefix throws an IllegalValueException.
     */
    @Test
    public void toModelType_unknownPrefix_throwsException() {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag("unknown:sometag");

        assertThrows(IllegalValueException.class, adaptedTag::toModelType);
    }

    /**
     * Tests that constructing a JsonAdaptedTag from a model Allergy tag preserves the correct type.
     */
    @Test
    public void constructor_validTag_createsCorrectType() throws Exception {
        Tag allergy = new Allergy(VALID_ALLERGY_ASPIRIN);
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag(allergy);

        Tag result = adaptedTag.toModelType();

        assertEquals(Allergy.class, result.getClass());
    }

    /**
     * Tests that the JSON creator constructor fails if the tag is without a prefix.
     */
    @Test
    public void constructor_invalidTagFormat_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new JsonAdaptedTag(VALID_ALLERGY_ASPIRIN));
    }

    /**
     * Tests that a valid allergy tag string is correctly converted to an Allergy model type.
     */
    @Test
    public void toJson_allergyTagString_returnsCorrectFormat() {
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag("allergy:" + VALID_ALLERGY_ASPIRIN);
        assertEquals("allergy:" + VALID_ALLERGY_ASPIRIN, adaptedTag.toJson());
    }

    /**
     * Tests that a Condition tag is serialised to JSON with the correct condition prefix.
     */
    @Test
    public void toJson_conditionTag_returnsConditionPrefix() {
        Tag condition = new Condition(VALID_CONDITION_ASTHMA);
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag(condition);
        assertEquals("condition:" + VALID_CONDITION_ASTHMA, adaptedTag.toJson());
    }

    /**
     * Tests that an Allergy tag is serialised to JSON with the correct allergy prefix.
     */
    @Test
    public void toJson_allergyTag_returnsAllergyPrefix() {
        Tag allergy = new Allergy(VALID_ALLERGY_ASPIRIN);
        JsonAdaptedTag adaptedTag = new JsonAdaptedTag(allergy);
        assertEquals("allergy:" + VALID_ALLERGY_ASPIRIN, adaptedTag.toJson());
    }
}
