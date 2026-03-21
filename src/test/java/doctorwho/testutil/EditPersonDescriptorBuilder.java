package doctorwho.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import doctorwho.logic.commands.EditCommand.EditPersonDescriptor;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Patient;
import doctorwho.model.patient.Phone;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code patient}'s details
     */
    public EditPersonDescriptorBuilder(Patient patient) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(patient.getName());
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
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code allergies} into a {@code Set<Allergy>} and sets it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withAllergies(String... allergies) {
        Set<Tag> allergySet = Stream.of(allergies).map(Allergy::new).collect(Collectors.toSet());
        descriptor.setAllergies(allergySet);
        return this;
    }

    /**
     * Parses the {@code conditions} into a {@code Set<Condition>} and sets it to the
     * {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withConditions(String... conditions) {
        Set<Tag> conditionset = Stream.of(conditions).map(Condition::new).collect(Collectors.toSet());
        descriptor.setConditions(conditionset);
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
