package doctorwho.ui;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * A UI for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private Label saveLocationStatus;

    @FXML
    private Label totalPatientCount;

    /**
     * Creates a {@code StatusBarFooter} with the given {@code Path}.
     */
    public StatusBarFooter(Path saveLocation, int initialPatientCount) {
        super(FXML);
        saveLocationStatus.setText(Paths.get(".").resolve(saveLocation).toString());
        setTotalPatients(initialPatientCount);
    }

    /**
     * Updates the total patient count displayed in the status bar.
     */
    public void setTotalPatients(int totalPatients) {
        totalPatientCount.setText("Displayed Patient Count: " + totalPatients);
    }
}
