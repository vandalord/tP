package doctorwho.ui;

import doctorwho.model.patient.Patient;
import doctorwho.model.tag.Allergy;
import doctorwho.model.tag.Condition;
import doctorwho.model.tag.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code Patient}.
 */
public class PatientCard extends UiPart<Region> {

    private static final String FXML = "PatientListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Patient patient;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label appointment;

    /**
     * Creates a {@code PatientCode} with the given {@code Patient} and index to display.
     */
    public PatientCard(Patient patient, int displayedIndex) {
        super(FXML);
        this.patient = patient;
        id.setText(displayedIndex + ". ");
        name.setText(patient.getName().fullName);
        phone.setText(patient.getPhone().value);
        address.setText(patient.getAddress().value);
        email.setText(patient.getEmail().value);
        if (patient.getTags().isEmpty()) {
            tags.setVisible(false);
            tags.setManaged(false);
        } else {
            for (Tag tag : patient.getTags().stream()
                    .sorted((t1, t2) -> {
                        int p1 = getPriority(t1);
                        int p2 = getPriority(t2);
                        if (p1 != p2) {
                            return Integer.compare(p1, p2);
                        }
                        return t1.tagName.compareTo(t2.tagName);
                    }).toList()) {

                Label tagLabel = new Label(tag.tagName);
                tagLabel.getStyleClass().add("tag");

                if (tag instanceof Allergy) {
                    tagLabel.getStyleClass().add("allergy-tag");
                } else if (tag instanceof Condition) {
                    tagLabel.getStyleClass().add("condition-tag");
                } else {
                    tagLabel.getStyleClass().add("general-tag");
                }

                tags.getChildren().add(tagLabel);
            }
        }
        patient.getAppointment().ifPresentOrElse(
                appt -> {
                    appointment.setText(appt.toString());
                    appointment.setVisible(true);
                    appointment.setManaged(true);
                }, () -> {
                    appointment.setText("No appointment scheduled");
                }
        );
    }

    private int getPriority(Tag tag) {
        if (tag instanceof Allergy) {
            return 0;
        }
        if (tag instanceof Condition) {
            return 1;
        }
        return 2;
    }
}

