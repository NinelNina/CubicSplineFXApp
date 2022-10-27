package cs.vsu.ru.kravtsova_n_e.cubicsplineapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CubicSplineController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}