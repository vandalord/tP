package doctorwho.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import doctorwho.commons.exceptions.IllegalValueException;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.Appointment;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Patient;
import doctorwho.model.patient.Phone;
import doctorwho.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Patient}.
 */
class JsonAdaptedPatient {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Patient's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String appointmentStart;
    private final Integer appointmentDuration;
    private final String appointmentNote;

    /**
     * Constructs a {@code JsonAdaptedPatient} with the given patient details.
     */
    @JsonCreator
    public JsonAdaptedPatient(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                              @JsonProperty("email") String email, @JsonProperty("address") String address,
                              @JsonProperty("tags") List<JsonAdaptedTag> tags,
                              @JsonProperty("appointmentStart") String appointmentStart,
                              @JsonProperty("appointmentDuration") Integer appointmentDuration,
                              @JsonProperty("appointmentNote") String appointmentNote) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.appointmentStart = appointmentStart;
        this.appointmentDuration = appointmentDuration;
        this.appointmentNote = appointmentNote;
    }

    /**
     * Converts a given {@code Patient} into this class for Jackson use.
     */
    public JsonAdaptedPatient(Patient source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));

        appointmentStart = source.getAppointment()
                .map(a -> a.getStartTime().format(Appointment.FORMATTER)).orElse(null);
        appointmentDuration = source.getAppointment()
                .map(Appointment::getDuration).orElse(null);
        appointmentNote = source.getAppointment()
                .map(Appointment::getNote).orElse(null);
    }

    /**
     * Converts this Jackson-friendly adapted patient object into the model's {@code Patient} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted patient.
     */
    public Patient toModelType() throws IllegalValueException {
        final List<Tag> patientTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            patientTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(patientTags);

        Appointment modelAppointment = null;

        if (appointmentStart != null && appointmentDuration != null && appointmentNote != null) {
            if (!Appointment.isValidDateTime(appointmentStart)) {
                throw new IllegalValueException(Appointment.STARTTIME_CONSTRAINTS);
            }
            if (!Appointment.isValidDuration(appointmentDuration)) {
                throw new IllegalValueException(Appointment.DURATION_CONSTRAINTS);
            }

            if (!Appointment.isValidNote(appointmentNote)) {
                throw new IllegalValueException(Appointment.NOTE_CONSTRAINTS);
            }

            modelAppointment = new Appointment(appointmentStart, appointmentDuration, appointmentNote);
        }

        return new Patient(modelName, modelPhone, modelEmail, modelAddress, modelTags, modelAppointment);
    }

}
