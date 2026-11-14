package javaflix.ui;

import javaflix.model.Movie;
import javaflix.model.Program;
import javaflix.model.Review;
import javaflix.model.Series;
import javaflix.model.User;
import javaflix.model.UserCategory;
import javaflix.model.YearRange;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.Locale;
import java.util.stream.Collectors;

final class JavaFxProgramDetailsDialog {
    private JavaFxProgramDetailsDialog() {
    }

    static void show(Window owner, Program program, User user, Runnable onChanged) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle(program.getTitle());
        dialog.setHeaderText(program.getTitle());
        JavaFxDialogStyle.apply(dialog.getDialogPane());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox details = new VBox(12);
        details.getStyleClass().add("details-content");
        details.getChildren().addAll(
                new Label(program.getYear() + " · " + formatGenre(program)
                        + " · " + program.getMaturityRating()),
                subtitle(program instanceof Series ? "Series" : "Movie"),
                wrappedLabel(program.getDescription()),
                new Label("Cast: " + String.join(", ", program.getMainCast()))
        );
        if (program instanceof Movie) {
            details.getChildren().add(new Label("Duration: " + ((Movie) program).getDuration() + " min"));
        } else if (program instanceof Series) {
            Series series = (Series) program;
            details.getChildren().add(new Label("Years: " + series.getYears().stream()
                    .map(YearRange::toString).collect(Collectors.joining(", "))));
            details.getChildren().add(new Label("Seasons: " + series.getSeasons().size()));
        }
        String average = program.getNumberOfReviews() == 0 ? "Not rated"
                : String.format(Locale.ROOT, "★ %.1f / 5 (%d reviews)",
                program.getNumberOfStars(), program.getNumberOfReviews());
        Label rating = new Label(average);
        rating.getStyleClass().add("card-rating");
        details.getChildren().add(rating);

        VBox reviews = new VBox(8);
        Label reviewsHeading = new Label("Reviews");
        reviewsHeading.getStyleClass().add("section-title");
        reviews.getChildren().add(reviewsHeading);
        if (program.getUserReviews().isEmpty()) {
            reviews.getChildren().add(subtitle("No reviews yet."));
        } else {
            for (Review review : program.getUserReviews()) {
                reviews.getChildren().add(new Label(String.format(Locale.ROOT, "%s  ·  %d/5%n%s",
                        review.getReviewer().getUsername(), review.getReviewStars(), review.getReviewText())));
            }
        }
        details.getChildren().add(reviews);

        if (user.getCategory() == UserCategory.SUBSCRIBER) {
            addSubscriberActions(dialog, details, program, user, onChanged);
        }

        ScrollPane scroll = new ScrollPane(details);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(500);
        dialog.getDialogPane().setContent(scroll);
        dialog.showAndWait();
    }

    private static void addSubscriberActions(Dialog<Void> dialog, VBox details,
            Program program, User user, Runnable onChanged) {
        Button favorite = secondaryButton(user.getFavorites().contains(program)
                ? "Remove from favorites" : "Add to favorites");
        favorite.setOnAction(event -> {
            if (user.getFavorites().contains(program)) {
                user.removeFavorite(program);
                favorite.setText("Add to favorites");
            } else {
                user.addFavorite(program);
                favorite.setText("Remove from favorites");
            }
            onChanged.run();
        });
        details.getChildren().add(favorite);

        TextField reviewText = new TextField();
        reviewText.setPromptText("Write a review");
        ComboBox<Integer> stars = new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        stars.setValue(5);
        Review existing = findReview(program, user);
        if (existing != null) {
            reviewText.setText(existing.getReviewText());
            stars.setValue(existing.getReviewStars());
        }
        Button submitReview = primaryButton(existing == null ? "Submit review" : "Update review");
        submitReview.setOnAction(event -> {
            if (reviewText.getText().trim().isEmpty()) {
                JavaFxDialogs.showWarning(dialog.getOwner(), "Review",
                        "Write a few words before submitting your review.");
                return;
            }
            program.addReview(new Review(stars.getValue(), reviewText.getText().trim(), user));
            dialog.close();
            onChanged.run();
            show(dialog.getOwner(), program, user, onChanged);
        });
        details.getChildren().addAll(stars, reviewText, submitReview);
        if (existing != null) {
            Button deleteReview = secondaryButton("Delete my review");
            deleteReview.setOnAction(event -> {
                Review currentReview = findReview(program, user);
                if (currentReview != null) {
                    program.deleteReview(currentReview);
                }
                dialog.close();
                onChanged.run();
                show(dialog.getOwner(), program, user, onChanged);
            });
            details.getChildren().add(deleteReview);
        }
    }

    private static Review findReview(Program program, User user) {
        for (Review review : program.getUserReviews()) {
            if (review.getReviewer().equals(user)) {
                return review;
            }
        }
        return null;
    }

    private static Label wrappedLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        return label;
    }

    private static Label subtitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("subtitle");
        return label;
    }

    private static String formatGenre(Program program) {
        String genre = program.getGenre().toString();
        return genre.charAt(0) + genre.substring(1).toLowerCase(Locale.ROOT);
    }

    private static Button primaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("primary-button");
        return button;
    }

    private static Button secondaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("secondary-button");
        return button;
    }
}
