package doctorwho.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_ALLERGY = new Prefix("al/");
    public static final Prefix PREFIX_CONDITION = new Prefix("c/");
    public static final Prefix PREFIX_APPOINTMENT_STARTTIME = new Prefix("d/");
    public static final Prefix PREFIX_APPOINTMENT_DURATION = new Prefix("dur/");
    public static final Prefix PREFIX_APPOINTMENT_NOTE = new Prefix("note/");
    public static final Prefix PREFIX_APPOINTMENT_DATE = new Prefix("d/");


}
