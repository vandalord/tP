package doctorwho.model.tag;

/**
 * Represents a condition tag of a patient.
 */
public class Condition extends Tag {

    public static final String MESSAGE_CONSTRAINTS =
        "Condition names should be alphanumeric, hyphens/spaces allowed, max 50 characters";

    private static final String VALIDATION_REGEX =
        "^(?=.{1,50}$)\\p{Alnum}+(-\\p{Alnum}+)*([ ]\\p{Alnum}+(-\\p{Alnum}+)*)*$";

    public Condition(String tagName) {
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

