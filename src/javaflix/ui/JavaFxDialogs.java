package javaflix.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

final class JavaFxDialogs {
    private JavaFxDialogs() {
    }

    static ButtonType showConfirmation(Window owner, String title, String message) {
        Alert alert = createAlert(owner, Alert.AlertType.CONFIRMATION, title, message);
        return alert.showAndWait().orElse(ButtonType.CANCEL);
    }

    static void showInformation(Window owner, String title, String message) {
        createAlert(owner, Alert.AlertType.INFORMATION, title, message).showAndWait();
    }

    static void showWarning(Window owner, String title, String message) {
        createAlert(owner, Alert.AlertType.WARNING, title, message).showAndWait();
    }

    static void showError(Window owner, String title, String message) {
        createAlert(owner, Alert.AlertType.ERROR, title, message).showAndWait();
    }

    private static Alert createAlert(Window owner, Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        JavaFxDialogStyle.apply(alert.getDialogPane());
        return alert;
    }
}
