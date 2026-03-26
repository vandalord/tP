package doctorwho.model.tag;

/**
 * Represents an allergy tag of a patient.
 */
public class Allergy extends Tag {

    public static final String MESSAGE_CONSTRAINTS =
        "Allergy names should be alphanumeric, hyphens/spaces allowed, max 30 characters";
    private static final String VALIDATION_REGEX =
        "^(?=.{1,30}$)\\p{Alnum}+(-\\p{Alnum}+)*([ ]\\p{Alnum}+(-\\p{Alnum}+)*)*$";

    public Allergy(String tagName) {
        super(tagName);
    }

    @Override
    public boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    protected String getMessageConstraints() {
        return MESSAGE_CONSTRAINTS;
    }
}
