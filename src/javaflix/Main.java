package javaflix;

import javaflix.ui.JavaFxBrowseApplication;
import javafx.application.Application;

/**
 * Application entry point.
 */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Application.launch(JavaFxBrowseApplication.class, args);
    }
}
