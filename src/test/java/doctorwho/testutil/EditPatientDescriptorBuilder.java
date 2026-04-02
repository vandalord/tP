package doctorwho.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import doctorwho.logic.commands.EditCommand.EditPatientDescriptor;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.DateOfBirth;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Nric;
import doctorwho.model.patient.Patient;
import doctorwho.model.patient.Phone;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

/**
 * A utility class to help with building EditPatientDescriptor objects.
 */
public class EditPatientDescriptorBuilder {

    private EditPatientDescriptor descriptor;

    public EditPatientDescriptorBuilder() {
        descriptor = new EditPatientDescriptor();
    }

    public EditPatientDescriptorBuilder(EditPatientDescriptor descriptor) {
        this.descriptor = new EditPatientDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPatientDescriptor} with fields containing {@code patient}'s details
     */
    public EditPatientDescriptorBuilder(Patient patient) {
        descriptor = new EditPatientDescriptor();
        descriptor.setName(patient.getName());
        descriptor.setNric(patient.getNric());
        descriptor.setDateOfBirth(patient.getDateOfBirth());
        descriptor.setPhone(patient.getPhone());
        descriptor.setEmail(patient.getEmail());
        descriptor.setAddress(patient.getAddress());

        Set<Tag> existingTags = patient.getTags();
        Set<Tag> allergies = existingTags.stream()
                .filter(t -> t instanceof Allergy)
                .collect(Collectors.toSet());
        Set<Tag> conditions = existingTags.stream()
                .filter(t -> t instanceof Condition)
                .collect(Collectors.toSet());

        descriptor.setAllergies(allergies);
        descriptor.setConditions(conditions);
    }

    /**
     * Sets the {@code Name} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Nric} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withNric(String nric) {
        descriptor.setNric(new Nric(nric));
        return this;
    }

    /**
     * Sets the {@code DateOfBirth} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withDateOfBirth(String dob) {
        descriptor.setDateOfBirth(new DateOfBirth(dob));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code allergies} into a {@code Set<Allergy>} and sets it to the {@code EditPatientDescriptor}
     * that we are building.
     */
    public EditPatientDescriptorBuilder withAllergies(String... allergies) {
        Set<Tag> allergySet = Stream.of(allergies).map(Allergy::new).collect(Collectors.toSet());
        descriptor.setAllergies(allergySet);
        return this;
    }

    /**
     * Parses the {@code conditions} into a {@code Set<Condition>} and sets it to the
     * {@code EditPatientDescriptor} that we are building.
     */
    public EditPatientDescriptorBuilder withConditions(String... conditions) {
        Set<Tag> conditionSet = Stream.of(conditions).map(Condition::new).collect(Collectors.toSet());
        descriptor.setConditions(conditionSet);
        return this;
    }

    public EditPatientDescriptor build() {
        return descriptor;
    }
}
