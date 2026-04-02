package doctorwho.testutil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import doctorwho.model.patient.Address;
import doctorwho.model.patient.Appointment;
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
 * A utility class to help with building Patient objects.
 */
public class PatientBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_NRIC = "S7654321F";
    public static final String DEFAULT_DOB = "20-04-2003";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Nric nric;
    private DateOfBirth dob;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private Appointment appointment;

    /**
     * Creates a {@code PatientBuilder} with the default details.
     */
    public PatientBuilder() {
        name = new Name(DEFAULT_NAME);
        nric = new Nric(DEFAULT_NRIC);
        dob = new DateOfBirth(DEFAULT_DOB);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PatientBuilder with the data of {@code PatientToCopy}.
     */
    public PatientBuilder(Patient patientToCopy) {
        name = patientToCopy.getName();
        nric = patientToCopy.getNric();
        dob = patientToCopy.getDateOfBirth();
        phone = patientToCopy.getPhone();
        email = patientToCopy.getEmail();
        address = patientToCopy.getAddress();
        tags = new HashSet<>(patientToCopy.getTags());
        appointment = patientToCopy.getAppointment().orElse(null);
    }

    /**
     * Sets the {@code Name} of the {@code Patient} that we are building.
     */
    public PatientBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Patient} that we are building.
     */
    public PatientBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Nric} of the {@code Patient} that we are building.
     */
    public PatientBuilder withNric(String nric) {
        this.nric = new Nric(nric);
        return this;
    }

    /**
     * Sets the {@code DateOfBirth} of the {@code Patient} that we are building.
     */
    public PatientBuilder withDateOfBirth(String dob) {
        this.dob = new DateOfBirth(dob);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Patient} that we are building.
     */
    public PatientBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Patient} that we are building.
     */
    public PatientBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Appointment} of the {@code Patient} that we are building.
     */
    public PatientBuilder withAppointment(Appointment appointment) {
        this.appointment = appointment;
        return this;
    }

    /**
     * Parses the {@code allergies} into a {@code Set<Allergy>} and sets it to the {@code Patient}
     * that we are building. Replaces any existing allergies.
     */
    public PatientBuilder withAllergies(String... allergies) {
        this.tags.removeIf(t -> t instanceof Allergy);
        Set<Tag> allergySet = Arrays.stream(allergies)
                .map(Allergy::new)
                .collect(Collectors.toSet());
        this.tags.addAll(allergySet);
        return this;
    }

    /**
     * Parses the {@code conditions} into a {@code Set<Condition>} and sets it to the {@code Patient}
     * that we are building. Replaces any existing conditions.
     */
    public PatientBuilder withConditions(String... conditions) {
        this.tags.removeIf(t -> t instanceof Condition);
        Set<Tag> conditionSet = Arrays.stream(conditions)
                .map(Condition::new)
                .collect(Collectors.toSet());
        this.tags.addAll(conditionSet);
        return this;
    }

    public Patient build() {
        return new Patient(name, nric, dob, phone, email, address, tags, appointment);
    }

}
