package doctorwho.model.util;

import java.util.Set;

import doctorwho.model.AddressBook;
import doctorwho.model.ReadOnlyAddressBook;
import doctorwho.model.patient.Address;
import doctorwho.model.patient.Appointment;
import doctorwho.model.patient.Email;
import doctorwho.model.patient.Name;
import doctorwho.model.patient.Nric;
import doctorwho.model.patient.Patient;
import doctorwho.model.patient.Phone;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Patient[] getSamplePatients() {
        return new Patient[]{
            new Patient(new Name("Alex Yeoh"), new Nric("S1234567D"), new Phone("87438807"),
                        new Email("alexyeoh@example.com"),
                        new Address("Blk 30 Geylang Street 29, #06-40"),
                        Set.of(new Allergy("Penicillin"), new Condition("Diabetes")),
                        new Appointment("12-03-2026 14:00", 30, "Initial Consultation")),
            new Patient(new Name("Bernice Yu"), new Nric("S2345678H"), new Phone("99272758"),
                        new Email("berniceyu@example.com"),
                        new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                        Set.of(new Allergy("Aspirin"), new Condition("Hypertension"))),
            new Patient(new Name("Charlotte Oliveiro"), new Nric("S3456789A"), new Phone("93210283"),
                        new Email("charlotte@example.com"),
                        new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                        Set.of(new Condition("Asthma"))),
            new Patient(new Name("David Li"), new Nric("S4567890C"), new Phone("91031282"),
                        new Email("lidavid@example.com"),
                        new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                        Set.of(new Allergy("Ibuprofen"), new Allergy("Sulfonamides"))),
            new Patient(new Name("Irfan Ibrahim"), new Nric("S5678901D"), new Phone("92492021"),
                        new Email("irfan@example.com"),
                        new Address("Blk 47 Tampines Street 20, #17-35"),
                        Set.of(new Allergy("Aspirin"))),
            new Patient(new Name("Roy Balakrishnan"), new Nric("S6789012D"), new Phone("92624417"),
                        new Email("royb@example.com"),
                        new Address("Blk 45 Aljunied Street 85, #11-31"),
                        Set.of())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Patient samplePatient : getSamplePatients()) {
            sampleAb.addPatient(samplePatient);
        }
        return sampleAb;
    }
}
