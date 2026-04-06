package doctorwho.ui;

import java.util.Comparator;

import doctorwho.model.patient.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * A UI component that displays a detailed, comprehensive view of a {@code Patient}.
 */
public class PatientDetailPanel extends UiPart<Region> {
    private static final String FXML = "PatientDetailPanel.fxml";

    private Patient patient;

    @FXML
    private VBox detailPane;
    @FXML
    private Label name;
    @FXML
    private Label nric;
    @FXML
    private Label sex;
    @FXML
    private Label age;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label address;
    @FXML
    private FlowPane conditions;
    @FXML
    private FlowPane allergies;
    @FXML
    private Label appointment;

    /**
     * Creates a {@code PatientDetailPanel} with no patient.
     */
    public PatientDetailPanel() {
        super(FXML);
        detailPane.setVisible(false);
    }

    /**
     * Sets the patient to be displayed and updates the view.
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
        updateView();
        detailPane.setVisible(true); // Show panel
    }

    /**
     * Updates the UI elements based on the currently set patient.
     */
    private void updateView() {
        if (patient == null) {
            return;
        }

        name.setText(patient.getName().fullName);
        nric.setText(patient.getNric().value);
        String sexDisplay = patient.getSex().value.equals("F") ? "Female" : "Male";
        sex.setText(sexDisplay);
        age.setText(patient.getDateOfBirth() + " (" + patient.getDateOfBirth().getAge() + " old)");
        phone.setText(patient.getPhone().value);
        email.setText(patient.getEmail().value);
        address.setText(patient.getAddress().value);

        populateConditionsFlowPane();
        populateAllergiesFlowPane();

        patient.getAppointment().ifPresentOrElse(
                appt -> appointment.setText(appt.toString()), ()
                        -> appointment.setText("No appointment scheduled")
        );
    }

    // --- Helper methods for populating specific areas ---

    private void populateConditionsFlowPane() {
        conditions.getChildren().clear();

        if (patient.getConditions().isEmpty()) {
            Label naLabel = new Label("N/A");
            naLabel.getStyleClass().add("cell_small_label");
            conditions.getChildren().add(naLabel);
        } else {
            patient.getConditions().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .forEach(tag -> {
                        Label tagLabel = new Label(tag.tagName);
                        tagLabel.getStyleClass().add("tag");
                        tagLabel.getStyleClass().add("condition-tag");
                        conditions.getChildren().add(tagLabel);
                    });
        }
    }

    private void populateAllergiesFlowPane() {
        allergies.getChildren().clear();

        if (patient.getAllergies().isEmpty()) {
            Label naLabel = new Label("N/A");
            naLabel.getStyleClass().add("cell_small_label");
            allergies.getChildren().add(naLabel);
        } else {
            patient.getAllergies().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .forEach(tag -> {
                        Label tagLabel = new Label(tag.tagName);
                        tagLabel.getStyleClass().add("tag");
                        tagLabel.getStyleClass().add("allergy-tag");
                        allergies.getChildren().add(tagLabel);
                    });
        }
    }
}
