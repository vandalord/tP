package doctorwho.logic.commands;

import static doctorwho.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static doctorwho.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static doctorwho.logic.parser.CliSyntax.PREFIX_CONDITION;
import static doctorwho.logic.parser.CliSyntax.PREFIX_DOB;
import static doctorwho.logic.parser.CliSyntax.PREFIX_EMAIL;
import static doctorwho.logic.parser.CliSyntax.PREFIX_NAME;
import static doctorwho.logic.parser.CliSyntax.PREFIX_NRIC;
import static doctorwho.logic.parser.CliSyntax.PREFIX_PHONE;
import static doctorwho.logic.parser.CliSyntax.PREFIX_SEX;
import static doctorwho.model.Model.PREDICATE_SHOW_ALL_PATIENTS;
import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import doctorwho.commons.core.index.Index;
import doctorwho.commons.util.CollectionUtil;
import doctorwho.commons.util.ToStringBuilder;
import doctorwho.logic.Messages;
import doctorwho.logic.commands.exceptions.CommandException;
import doctorwho.model.Model;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.DateOfBirth;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Nric;
import doctorwho.model.patient.Patient;
import doctorwho.model.patient.Phone;
import doctorwho.model.patient.Sex;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;

/**
 * Edits the details of an existing patient in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the patient identified "
            + "by the index number used in the displayed patient list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_NRIC + "NRIC] "
            + "[" + PREFIX_SEX + "SEX] "
            + "[" + PREFIX_DOB + "DOB] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_ALLERGY + "ALLERGY]..."
            + "[" + PREFIX_CONDITION + "CONDITION]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PATIENT_SUCCESS = "Edited Patient: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one modified field must be provided.";
    public static final String MESSAGE_DUPLICATE_PATIENT = "This patient already exists in the patient book.";

    private final Index index;
    private final EditPatientDescriptor editPatientDescriptor;

    /**
     * @param index                 of the patient in the filtered patient list to edit
     * @param editPatientDescriptor details to edit the patient with
     */
    public EditCommand(Index index, EditPatientDescriptor editPatientDescriptor) {
        requireNonNull(index);
        requireNonNull(editPatientDescriptor);

        this.index = index;
        this.editPatientDescriptor = new EditPatientDescriptor(editPatientDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Patient> lastShownList = model.getFilteredPatientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX);
        }

        Patient patientToEdit = lastShownList.get(index.getZeroBased());
        Patient editedPatient = createEditedPatient(patientToEdit, editPatientDescriptor);

        if (patientToEdit.equals(editedPatient)) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        if (!patientToEdit.isSamePatient(editedPatient) && model.hasPatient(editedPatient)) {
            throw new CommandException(MESSAGE_DUPLICATE_PATIENT);
        }

        model.setPatient(patientToEdit, editedPatient);
        model.updateFilteredPatientList(PREDICATE_SHOW_ALL_PATIENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_PATIENT_SUCCESS, Messages.format(editedPatient)));
    }

    /**
     * Creates and returns a {@code Patient} with the details of {@code PatientToEdit}
     * edited with {@code editPatientDescriptor}.
     */
    private static Patient createEditedPatient(Patient patientToEdit, EditPatientDescriptor editPatientDescriptor) {
        assert patientToEdit != null;

        Name updatedName = editPatientDescriptor.getName().orElse(patientToEdit.getName());
        Nric updatedNric = editPatientDescriptor.getNric().orElse(patientToEdit.getNric());
        Sex updatedSex = editPatientDescriptor.getSex().orElse(patientToEdit.getSex());
        DateOfBirth updatedDob = editPatientDescriptor.getDateOfBirth().orElse(patientToEdit.getDateOfBirth());
        Phone updatedPhone = editPatientDescriptor.getPhone().orElse(patientToEdit.getPhone());
        Email updatedEmail = editPatientDescriptor.getEmail().orElse(patientToEdit.getEmail());
        Address updatedAddress = editPatientDescriptor.getAddress().orElse(patientToEdit.getAddress());

        Set<Tag> existingTags = patientToEdit.getTags();

        // Keep the existing tags of each type unless that specific type is being replaced.
        Set<Tag> existingAllergies = existingTags.stream()
                .filter(t -> t instanceof Allergy)
                .collect(Collectors.toSet());
        Set<Tag> existingConditions = existingTags.stream()
                .filter(t -> t instanceof Condition)
                .collect(Collectors.toSet());

        Set<Tag> finalAllergies = editPatientDescriptor.getAllergies().orElse(existingAllergies);
        Set<Tag> finalConditions = editPatientDescriptor.getConditions().orElse(existingConditions);

        Set<Tag> updatedTags = new HashSet<>();
        updatedTags.addAll(finalAllergies);
        updatedTags.addAll(finalConditions);

        return new Patient(updatedName, updatedNric, updatedSex, updatedDob, updatedPhone, updatedEmail, updatedAddress,
                updatedTags, patientToEdit.getAppointment().orElse(null));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPatientDescriptor.equals(otherEditCommand.editPatientDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPatientDescriptor", editPatientDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the patient with. Each non-empty field value will replace the
     * corresponding field value of the patient.
     */
    public static class EditPatientDescriptor {
        private Name name;
        private Nric nric;
        private Sex sex;
        private DateOfBirth dob;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> allergies;
        private Set<Tag> conditions;

        public EditPatientDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPatientDescriptor(EditPatientDescriptor toCopy) {
            setName(toCopy.name);
            setNric(toCopy.nric);
            setSex(toCopy.sex);
            setDateOfBirth(toCopy.dob);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setAllergies(toCopy.allergies);
            setConditions(toCopy.conditions);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, nric, sex, dob, phone, email, address, allergies, conditions);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setNric(Nric nric) {
            this.nric = nric;
        }

        public Optional<Nric> getNric() {
            return Optional.ofNullable(nric);
        }

        public void setSex(Sex sex) {
            this.sex = sex;
        }

        public Optional<Sex> getSex() {
            return Optional.ofNullable(sex);
        }

        public void setDateOfBirth(DateOfBirth dob) {
            this.dob = dob;
        }

        public Optional<DateOfBirth> getDateOfBirth() {
            return Optional.ofNullable(dob);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code allergies} to this object's {@code allergies}.
         * A defensive copy of {@code allergies} is used internally.
         */
        public void setAllergies(Set<Tag> allergies) {
            this.allergies = (allergies != null) ? new HashSet<>(allergies) : null;
        }

        /**
         * Returns an unmodifiable allergy set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code allergies} is null.
         */
        public Optional<Set<Tag>> getAllergies() {
            return (allergies != null) ? Optional.of(Collections.unmodifiableSet(allergies)) : Optional.empty();
        }

        /**
         * Sets {@code conditions} to this object's {@code conditions}.
         * A defensive copy of {@code conditions} is used internally.
         */
        public void setConditions(Set<Tag> conditions) {
            this.conditions = (conditions != null) ? new HashSet<>(conditions) : null;
        }

        /**
         * Returns an unmodifiable medical condition set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code conditions} is null.
         */
        public Optional<Set<Tag>> getConditions() {
            return (conditions != null)
                    ? Optional.of(Collections.unmodifiableSet(conditions)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPatientDescriptor)) {
                return false;
            }

            EditPatientDescriptor otherEditPatientDescriptor = (EditPatientDescriptor) other;
            return Objects.equals(name, otherEditPatientDescriptor.name)
                    && Objects.equals(nric, otherEditPatientDescriptor.nric)
                    && Objects.equals(sex, otherEditPatientDescriptor.sex)
                    && Objects.equals(dob, otherEditPatientDescriptor.dob)
                    && Objects.equals(phone, otherEditPatientDescriptor.phone)
                    && Objects.equals(email, otherEditPatientDescriptor.email)
                    && Objects.equals(address, otherEditPatientDescriptor.address)
                    && Objects.equals(allergies, otherEditPatientDescriptor.allergies)
                    && Objects.equals(conditions, otherEditPatientDescriptor.conditions);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("nric", nric)
                    .add("sex", sex)
                    .add("dob", dob)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("allergies", allergies)
                    .add("conditions", conditions)
                    .toString();
        }
    }
}
