package doctorwho.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import doctorwho.commons.exceptions.IllegalValueException;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Tag}.
 */
class JsonAdaptedTag {

    private final String tagName;
    private final String tagType;

    /**
     * Constructs a {@code JsonAdaptedTag} with the given {@code tagName}.
     */
    @JsonCreator
    public JsonAdaptedTag(String tagName) {
        if (tagName.contains(":")) {
            String[] parts = tagName.split(":", 2);
            this.tagType = parts[0];
            this.tagName = parts[1];
        } else {
            this.tagName = tagName;
            this.tagType = "general";
        }
    }

    /**
     * Converts a given {@code Tag} into this class for Jackson use.
     */
    @JsonCreator
    public JsonAdaptedTag(Tag source) {
        tagName = source.tagName;

        if (source instanceof Allergy) {
            tagType = "allergy";
        } else if (source instanceof Condition) {
            tagType = "condition";
        } else {
            tagType = "general";
        }
    }

    @JsonValue
    public String toJson() {
        if (tagType.equals("general")) {
            return tagName;
        }
        return tagType + ":" + tagName;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Tag} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public Tag toModelType() throws IllegalValueException {
        switch (tagType) {
        case "allergy":
            try {
                return new Allergy(tagName);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(Allergy.MESSAGE_CONSTRAINTS);
            }
        case "condition":
            try {
                return new Condition(tagName);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(Condition.MESSAGE_CONSTRAINTS);
            }
        default:
            throw new IllegalValueException("Unknown tag type: " + tagType
                + ". Tags must be prefixed with 'allergy:' or 'condition:'");
        }
    }
}
