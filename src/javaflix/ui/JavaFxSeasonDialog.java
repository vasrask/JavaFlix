package javaflix.ui;

import javaflix.model.Episode;
import javaflix.model.Season;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.Optional;

final class JavaFxSeasonDialog {
    private JavaFxSeasonDialog() {
    }

    static Optional<Season> show(Window owner, int seasonNumber) {
        Dialog<Season> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Season " + seasonNumber);
        dialog.setHeaderText("Add a season and its episodes");
        JavaFxDialogStyle.apply(dialog.getDialogPane());
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType addType = new ButtonType("Add season", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(cancel);
        dialog.getDialogPane().getButtonTypes().add(addType);

        TextField year = new TextField();
        year.setPromptText("Season year");
        TextArea episodes = new TextArea();
        episodes.setPromptText("One episode per line: Episode title | duration in minutes");
        episodes.setPrefRowCount(7);
        VBox content = new VBox(10, new Label("Year"), year, new Label("Episodes"), episodes);
        content.setPadding(new Insets(12));
        dialog.getDialogPane().setContent(content);

        Button add = (Button) dialog.getDialogPane().lookupButton(addType);
        add.getStyleClass().add("primary-button");
        add.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            try {
                int seasonYear = JavaFxInputValidation.parsePositiveInteger(year.getText(), "Season year");
                ArrayList<Episode> parsedEpisodes = parseEpisodes(episodes.getText());
                if (parsedEpisodes.isEmpty()) {
                    throw new IllegalArgumentException("Add at least one episode.");
                }
                dialog.setResult(new Season(seasonNumber, seasonYear, parsedEpisodes));
            } catch (IllegalArgumentException exception) {
                event.consume();
                JavaFxDialogs.showWarning(owner, "Season details", exception.getMessage());
            }
        });
        dialog.setResultConverter(button -> button == addType
                ? dialog.getResult()
                : null);
        return dialog.showAndWait();
    }

    private static ArrayList<Episode> parseEpisodes(String text) {
        ArrayList<Episode> episodes = new ArrayList<>();
        for (String line : text.split("\\R")) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|", 2);
            if (parts.length != 2 || parts[0].trim().isEmpty()) {
                throw new IllegalArgumentException("Use one episode per line: title | duration.");
            }
            int duration = JavaFxInputValidation.parsePositiveInteger(parts[1], "Episode duration");
            episodes.add(new Episode(parts[0].trim(), episodes.size() + 1, duration));
        }
        return episodes;
    }
}
