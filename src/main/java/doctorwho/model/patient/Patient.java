package doctorwho.model.patient;

import static doctorwho.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import doctorwho.commons.util.ToStringBuilder;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

/**
 * Represents a Patient in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Patient {

    // Identity fields
    private final Name name;
    private final Nric nric;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Appointment appointment;

    /**
     * Every field must be present and not null.
     */
    public Patient(Name name, Nric nric, Phone phone, Email email, Address address, Set<Tag> tags,
                   Appointment appointment) {
        requireAllNonNull(name, nric, phone, email, address, tags);
        this.name = name;
        this.nric = nric;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.appointment = appointment;
    }

    public Patient(Name name, Nric nric, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, nric, phone, email, address, tags, null);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Nric getNric() {
        return nric;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable set of allergies, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getAllergies() {
        return Collections.unmodifiableSet(tags.stream()
                .filter(t -> t instanceof Allergy)
                .collect(Collectors.toSet()));
    }

    /**
     * Returns an immutable set of conditions, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getConditions() {
        return Collections.unmodifiableSet(tags.stream()
                .filter(t -> t instanceof Condition)
                .collect(Collectors.toSet()));
    }

    public Optional<Appointment> getAppointment() {
        return Optional.ofNullable(appointment);
    }

    public boolean hasOverlappingAppointment(Patient patient) {
        return patient.appointment != null && appointment.isOverlapping(patient.appointment);
    }

    /**
     * Returns true if any of the patients in a list of {@code Patients}, not counting the same patient,
     * has a clashing appointment.
     *
     * @param patients The list of patients to check against
     * @return the boolean
     */
    public boolean hasOverlappingAppointmentInList(List<Patient> patients) {
        return patients.stream()
                .filter(p -> !this.isSamePatient(p))
                .anyMatch(this::hasOverlappingAppointment);
    }

    /**
     * Returns true if both patients have the same NRIC.
     * This defines a weaker notion of equality between two patients.
     */
    public boolean isSamePatient(Patient otherPatient) {
        if (otherPatient == this) {
            return true;
        }

        return otherPatient != null
                && otherPatient.getNric().equals(getNric());
    }

    /**
     * Returns true if both patients have the same identity and data fields.
     * This defines a stronger notion of equality between two patients.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Patient)) {
            return false;
        }

        Patient otherPatient = (Patient) other;
        return name.equals(otherPatient.name)
            && nric.equals(otherPatient.nric)
                && phone.equals(otherPatient.phone)
                && email.equals(otherPatient.email)
                && address.equals(otherPatient.address)
                && tags.equals(otherPatient.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, nric, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("nric", nric)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }
}
