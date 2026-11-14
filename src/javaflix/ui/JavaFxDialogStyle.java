package javaflix.ui;

import javafx.scene.control.DialogPane;

final class JavaFxDialogStyle {
    private JavaFxDialogStyle() {
    }

    static void apply(DialogPane dialogPane) {
        dialogPane.getStyleClass().add("javaflix-dialog");
        dialogPane.getStylesheets().add(
                JavaFxDialogStyle.class.getResource("javaflix-browse.css").toExternalForm()
        );
    }
}
