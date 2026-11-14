package javaflix.ui;

import java.util.Locale;

final class JavaFxInputValidation {
    private JavaFxInputValidation() {
    }

    static int parsePositiveInteger(String value, String label) {
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed <= 0) {
                throw new IllegalArgumentException(label + " must be greater than zero.");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Enter a valid " + label.toLowerCase(Locale.ROOT) + ".");
        }
    }
}
