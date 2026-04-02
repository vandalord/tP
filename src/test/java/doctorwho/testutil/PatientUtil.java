package doctorwho.testutil;

import static doctorwho.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static doctorwho.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static doctorwho.logic.parser.CliSyntax.PREFIX_CONDITION;
import static doctorwho.logic.parser.CliSyntax.PREFIX_DOB;
import static doctorwho.logic.parser.CliSyntax.PREFIX_EMAIL;
import static doctorwho.logic.parser.CliSyntax.PREFIX_NAME;
import static doctorwho.logic.parser.CliSyntax.PREFIX_NRIC;
import static doctorwho.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Set;

import doctorwho.logic.commands.AddCommand;
import doctorwho.logic.commands.EditCommand.EditPatientDescriptor;
import doctorwho.model.patient.Patient;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

/**
 * A utility class containing helper methods for building command strings from {@code Patient} objects.
 */
public class PatientUtil {

    /**
     * Returns an add command string for adding the {@code patient}.
     */
    public static String getAddCommand(Patient patient) {
        return AddCommand.COMMAND_WORD + " " + getPatientDetails(patient);
    }

    /**
     * Returns the part of command string for the given {@code patient}'s details.
     */
    public static String getPatientDetails(Patient patient) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + patient.getName().fullName + " ");
        sb.append(PREFIX_NRIC + patient.getNric().value + " ");
        sb.append(PREFIX_DOB + patient.getDateOfBirth().toString() + " ");
        sb.append(PREFIX_PHONE + patient.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + patient.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + patient.getAddress().value + " ");
        patient.getTags().forEach(tag -> {
            if (tag instanceof Allergy) {
                sb.append(PREFIX_ALLERGY).append(tag.tagName).append(" ");
            } else if (tag instanceof Condition) {
                sb.append(PREFIX_CONDITION).append(tag.tagName).append(" ");
            }
        });
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPatientDescriptor}'s details.
     */
    public static String getEditPatientDescriptorDetails(EditPatientDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getNric().ifPresent(nric -> sb.append(PREFIX_NRIC).append(nric.value).append(" "));
        descriptor.getDateOfBirth().ifPresent(
                dob -> sb.append(PREFIX_DOB).append(dob.toString()).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(
                address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getAllergies().isPresent()) {
            Set<Tag> allergies = descriptor.getAllergies().get();
            if (allergies.isEmpty()) {
                sb.append(PREFIX_ALLERGY);
            } else {
                allergies.forEach(s -> sb.append(PREFIX_ALLERGY).append(s.tagName).append(" "));
            }
        }
        if (descriptor.getConditions().isPresent()) {
            Set<Tag> conditions = descriptor.getConditions().get();
            if (conditions.isEmpty()) {
                sb.append(PREFIX_CONDITION);
            } else {
                conditions.forEach(s -> sb.append(PREFIX_CONDITION).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
