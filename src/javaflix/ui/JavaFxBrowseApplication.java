package javaflix.ui;

import javaflix.model.Episode;
import javaflix.model.Genres;
import javaflix.model.MaturityRatings;
import javaflix.model.Movie;
import javaflix.model.Program;
import javaflix.model.Review;
import javaflix.model.Season;
import javaflix.model.Series;
import javaflix.model.User;
import javaflix.model.UserCategory;
import javaflix.model.YearRange;
import javaflix.service.Programs;
import javaflix.service.Users;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * JavaFX application interface for JavaFlix.
 */
public class JavaFxBrowseApplication extends Application {
    private static final String[] GENRE_OPTIONS = {"Any genre", "Adventure", "Comedy", "Drama", "Horror", "Sci-Fi"};
    private static final String[] RATING_OPTIONS = {"Any rating", "G", "PG", "PG13", "R", "NC17"};

    private final BorderPane root = new BorderPane();
    private final FlowPane cards = new FlowPane();
    private final List<Season> pendingSeasons = new ArrayList<>();
    private Stage stage;
    private User currentUser;
    private boolean dataLoaded;
    private TextField searchField;
    private TextField actorFilter;
    private ComboBox<String> genreFilter;
    private ComboBox<String> maturityFilter;
    private ComboBox<String> yearFilter;
    private ComboBox<String> ratingFilter;
    private Label resultCount;
    private Label emptyState;
    private Label validationMessage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        root.getStyleClass().add("app-root");
        Scene scene = new Scene(root, 1180, 800);
        scene.getStylesheets().add(getClass().getResource("javaflix-browse.css").toExternalForm());
        stage.setTitle("JavaFlix");
        stage.setMinWidth(820);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            if (!dataLoaded) {
                return;
            }
            try {
                saveData();
            } catch (IOException exception) {
                event.consume();
                showError("Could not save JavaFlix data", exception);
            }
        });

        try {
            loadData();
            showLogin();
            stage.show();
        } catch (IOException exception) {
            showStartupError(exception);
            stage.show();
        }
    }

    private void loadData() throws IOException {
        readObjects("programs.dat", Program.class, Programs::addProgram);
        readObjects("users.dat", User.class, Users::addUser);
        normalizeFavorites();
        dataLoaded = true;
    }

    private void normalizeFavorites() {
        List<Program> catalog = Programs.getAllPrograms();
        for (User user : Users.getAllUsers()) {
            ArrayList<Program> normalized = new ArrayList<>();
            for (Program favorite : user.getFavorites()) {
                for (Program program : catalog) {
                    if (program.getClass() == favorite.getClass()
                            && program.getTitle().equalsIgnoreCase(favorite.getTitle())) {
                        boolean alreadySaved = normalized.stream().anyMatch(saved ->
                                saved.getClass() == program.getClass()
                                        && saved.getTitle().equalsIgnoreCase(program.getTitle()));
                        if (!alreadySaved) {
                            normalized.add(program);
                        }
                        break;
                    }
                }
            }
            user.setFavorites(normalized);
        }
    }

    private <T> void readObjects(String filename, Class<T> type, java.util.function.Consumer<T> consumer)
            throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename))) {
            while (true) {
                try {
                    Object value = input.readObject();
                    if (!type.isInstance(value)) {
                        throw new IOException(filename + " contains an unexpected object type.");
                    }
                    consumer.accept(type.cast(value));
                } catch (EOFException endOfFile) {
                    return;
                } catch (ClassNotFoundException exception) {
                    throw new IOException("Could not read " + filename + ".", exception);
                }
            }
        }
    }

    private void saveData() throws IOException {
        writeObjects("users.dat", Users.getAllUsers());
        writeObjects("programs.dat", Programs.getAllPrograms());
    }

    private void writeObjects(String filename, List<?> values) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename))) {
            for (Object value : values) {
                output.writeObject(value);
            }
        }
    }

    private void showLogin() {
        currentUser = null;
        root.setTop(createTopBar(false));
        root.setCenter(JavaFxAuthView.login(user -> {
            currentUser = user;
            showBrowse();
        }, this::showRegister));
    }

    private void showRegister() {
        root.setTop(createTopBar(false));
        root.setCenter(JavaFxAuthView.register(() -> {
            showLogin();
            showInformation("Account created", "You can now sign in with your new account.");
        }, this::showLogin));
    }

    private void showBrowse() {
        root.setTop(createTopBar(true));
        VBox content = new VBox(24);
        content.getStyleClass().add("browse-content");
        content.setPadding(new Insets(34, 52, 48, 52));

        VBox intro = new VBox(8,
                eyebrow("YOUR NEXT FAVORITE IS HERE"),
                headline("Browse the catalog."),
                subtitle("Explore movies and series by title, cast, genre, or rating.")
        );
        content.getChildren().add(intro);

        VBox discovery = new VBox(12);
        discovery.getStyleClass().add("discovery-panel");
        searchField = new TextField();
        searchField.setPromptText("Search titles...");
        searchField.getStyleClass().add("search-field");
        searchField.setOnAction(event -> updateResults());
        actorFilter = new TextField();
        actorFilter.setPromptText("Actor name...");
        actorFilter.getStyleClass().add("search-field");
        actorFilter.setOnAction(event -> updateResults());

        HBox searchRow = new HBox(10, searchField, actorFilter, primaryButton("Search"));
        Button searchButton = (Button) searchRow.getChildren().get(2);
        searchButton.setOnAction(event -> updateResults());
        HBox.setHgrow(searchField, Priority.ALWAYS);
        HBox.setHgrow(actorFilter, Priority.ALWAYS);

        HBox filters = new HBox(10);
        filters.setAlignment(Pos.CENTER_LEFT);
        genreFilter = filterCombo(GENRE_OPTIONS, "Any genre");
        maturityFilter = filterCombo(RATING_OPTIONS, "Any rating");
        yearFilter = filterCombo(new String[]{"Any year", "Before 2000", "2000–2009", "2010–2019", "2020+"}, "Any year");
        ratingFilter = filterCombo(new String[]{"Any stars", "3+ stars", "4+ stars"}, "Any stars");
        for (ComboBox<String> filter : List.of(genreFilter, maturityFilter, yearFilter, ratingFilter)) {
            filter.setOnAction(event -> updateResults());
        }
        Button reset = secondaryButton("Reset");
        reset.setOnAction(event -> resetFilters());
        filters.getChildren().addAll(genreFilter, maturityFilter, yearFilter, ratingFilter, reset);
        discovery.getChildren().addAll(searchRow, filters);

        HBox section = new HBox(12);
        section.setAlignment(Pos.CENTER_LEFT);
        Label sectionTitle = new Label("Explore the catalog");
        sectionTitle.getStyleClass().add("section-title");
        resultCount = new Label();
        resultCount.getStyleClass().add("result-count");
        section.getChildren().addAll(sectionTitle, resultCount);

        cards.setHgap(16);
        cards.setVgap(16);
        cards.setPrefWrapLength(1060);
        cards.getStyleClass().add("catalog-grid");
        emptyState = new Label();
        emptyState.getStyleClass().add("empty-state");
        VBox catalog = new VBox(16, section, cards, emptyState);
        content.getChildren().addAll(discovery, catalog);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");
        root.setCenter(scrollPane);
        updateResults();
    }

    private void showFavorites() {
        root.setTop(createTopBar(true));
        VBox content = pageContent("YOUR LIST", "Favorites", "Your saved movies and series.");
        List<Program> favorites = currentUser.getFavorites().stream()
                .filter(this::isInCatalog)
                .sorted(Comparator.comparing(Program::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
        if (favorites.isEmpty()) {
            content.getChildren().add(emptyMessage("No favorites yet", "Browse the catalog and save your favorite titles."));
        } else {
            FlowPane favoriteCards = new FlowPane();
            favoriteCards.setHgap(16);
            favoriteCards.setVgap(16);
            for (Program program : favorites) {
                favoriteCards.getChildren().add(createProgramCard(program, true));
            }
            content.getChildren().add(favoriteCards);
        }
        root.setCenter(wrapPage(content));
    }

    private void showProfile() {
        root.setTop(createTopBar(true));
        VBox content = pageContent("ACCOUNT", "Profile", "Manage your JavaFlix account details.");
        VBox form = new VBox(12);
        form.getStyleClass().add("form-card");
        TextField email = new TextField(currentUser.getEmail());
        PasswordField oldPassword = new PasswordField();
        oldPassword.setPromptText("Current password");
        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("New password (optional)");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm new password");
        Label feedback = feedbackLabel();

        Button save = primaryButton("Save changes");
        save.setOnAction(event -> {
            String newEmail = email.getText().trim();
            if (!newEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                setFeedback(feedback, "Enter a valid email address.");
                return;
            }
            String newSecret = newPassword.getText();
            if (!newSecret.isEmpty()) {
                if (!currentUser.getPassword().equals(oldPassword.getText())) {
                    setFeedback(feedback, "Current password is incorrect.");
                    return;
                }
                if (!newSecret.equals(confirmPassword.getText())) {
                    setFeedback(feedback, "New passwords do not match.");
                    return;
                }
                if (newSecret.equals(currentUser.getPassword())) {
                    setFeedback(feedback, "Choose a password different from your current one.");
                    return;
                }
                currentUser.setPassword(newSecret);
            }
            currentUser.setEmail(newEmail);
            setFeedback(feedback, "Profile updated.");
        });

        form.getChildren().addAll(
                fieldGroup("Username", new Label(currentUser.getUsername())),
                fieldGroup("Email", email),
                fieldGroup("Password", oldPassword),
                fieldGroup("New password", newPassword),
                fieldGroup("Confirm password", confirmPassword),
                feedback,
                save
        );
        form.setMaxWidth(540);
        content.getChildren().add(form);
        root.setCenter(wrapPage(content));
    }

    private void showAdminAddProgram() {
        root.setTop(createTopBar(true));
        VBox content = pageContent("CATALOG MANAGEMENT", "Add a title", "Add a movie or series to the JavaFlix catalog.");

        VBox form = new VBox(12);
        form.getStyleClass().add("form-card");
        ComboBox<String> type = filterCombo(new String[]{"Movie", "Series"}, "Movie");
        TextField title = new TextField();
        title.setPromptText("Title");
        TextArea description = new TextArea();
        description.setPromptText("Synopsis");
        description.setPrefRowCount(3);
        ComboBox<Genres> genre = new ComboBox<>(FXCollections.observableArrayList(Genres.values()));
        genre.setPromptText("Choose genre");
        ComboBox<MaturityRatings> maturity = new ComboBox<>(FXCollections.observableArrayList(MaturityRatings.values()));
        maturity.setPromptText("Choose maturity rating");
        TextField actors = new TextField();
        actors.setPromptText("Separate names with commas");
        TextField yearOrYears = new TextField();
        yearOrYears.setPromptText("Release year (movie) or year range(s), e.g. 2019-2023");
        TextField duration = new TextField();
        duration.setPromptText("Duration in minutes");
        Label seasonsStatus = new Label("No seasons added");
        seasonsStatus.getStyleClass().add("result-count");
        Button addSeason = secondaryButton("Add season details");
        addSeason.setOnAction(event -> {
            Season season = showSeasonDialog(pendingSeasons.size() + 1);
            if (season != null) {
                pendingSeasons.add(season);
                seasonsStatus.setText(pendingSeasons.size() + " season(s) added");
            }
        });
        HBox seasonRow = new HBox(12, addSeason, seasonsStatus);
        seasonRow.setAlignment(Pos.CENTER_LEFT);
        Label feedback = feedbackLabel();

        type.setOnAction(event -> {
            boolean series = "Series".equals(type.getValue());
            duration.setManaged(!series);
            duration.setVisible(!series);
            seasonRow.setManaged(series);
            seasonRow.setVisible(series);
            yearOrYears.setPromptText(series
                    ? "Year range(s), e.g. 2019-2023"
                    : "Release year, e.g. 2022");
            pendingSeasons.clear();
            seasonsStatus.setText("No seasons added");
        });
        seasonRow.setVisible(false);
        seasonRow.setManaged(false);

        Button add = primaryButton("Add to catalog");
        add.setOnAction(event -> {
            String name = title.getText().trim();
            if (name.isEmpty()) {
                setFeedback(feedback, "Enter a title.");
                return;
            }
            if (Programs.getAllPrograms().stream().anyMatch(program -> program.getTitle().equalsIgnoreCase(name))) {
                setFeedback(feedback, "A title with that name already exists.");
                return;
            }
            if (genre.getValue() == null || maturity.getValue() == null) {
                setFeedback(feedback, "Choose both a genre and maturity rating.");
                return;
            }
            List<String> cast = splitNames(actors.getText());
            try {
                if ("Movie".equals(type.getValue())) {
                    int year = JavaFxInputValidation.parsePositiveInteger(yearOrYears.getText(), "Release year");
                    int minutes = JavaFxInputValidation.parsePositiveInteger(duration.getText(), "Duration");
                    Programs.addProgram(new Movie(name, description.getText().trim(), maturity.getValue(),
                            minutes, genre.getValue(), year, new ArrayList<>(cast), new ArrayList<>(), new ArrayList<>()));
                } else {
                    ArrayList<YearRange> years = YearRange.fromStringToYearRanges(yearOrYears.getText().trim());
                    Programs.addProgram(new Series(name, description.getText().trim(), maturity.getValue(),
                            genre.getValue(), new ArrayList<>(cast), new ArrayList<>(),
                            new ArrayList<>(pendingSeasons), years, new ArrayList<>()));
                    pendingSeasons.clear();
                    seasonsStatus.setText("No seasons added");
                }
                setFeedback(feedback, "Title added successfully.");
                title.clear();
                description.clear();
                actors.clear();
                yearOrYears.clear();
                duration.clear();
            } catch (IllegalArgumentException exception) {
                setFeedback(feedback, exception.getMessage());
            }
        });
        form.getChildren().addAll(
                fieldGroup("Program type", type),
                fieldGroup("Title", title),
                fieldGroup("Description", description),
                fieldGroup("Genre", genre),
                fieldGroup("Maturity rating", maturity),
                fieldGroup("Main cast", actors),
                fieldGroup("Year / years", yearOrYears),
                fieldGroup("Duration", duration),
                seasonRow,
                feedback,
                add
        );
        form.setMaxWidth(680);
        content.getChildren().add(form);
        root.setCenter(wrapPage(content));
    }

    private Season showSeasonDialog(int seasonNumber) {
        return JavaFxSeasonDialog.show(stage, seasonNumber).orElse(null);
    }

    private void showProgramDetails(Program program) {
        JavaFxProgramDetailsDialog.show(stage, program, currentUser, this::updateResultsIfVisible);
    }

    private boolean isInCatalog(Program candidate) {
        return Programs.getAllPrograms().stream().anyMatch(program ->
                program.getClass() == candidate.getClass()
                        && program.getTitle().equalsIgnoreCase(candidate.getTitle()));
    }

    private void updateResultsIfVisible() {
        if (searchField != null && root.getCenter() != null) {
            updateResults();
        }
    }

    private Node createTopBar(boolean authenticated) {
        HBox topBar = new HBox(18);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("top-bar");
        Label brand = new Label("JAVAFLIX");
        brand.getStyleClass().add("brand");
        topBar.getChildren().add(brand);
        if (!authenticated) {
            return topBar;
        }

        Button browse = navButton("Browse", this::showBrowse);
        Button profile = navButton("Profile", this::showProfile);
        topBar.getChildren().add(browse);
        if (currentUser.getCategory() == UserCategory.SUBSCRIBER) {
            topBar.getChildren().add(navButton("Favorites", this::showFavorites));
        } else {
            topBar.getChildren().add(navButton("Add title", this::showAdminAddProgram));
        }
        topBar.getChildren().add(profile);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label username = new Label(currentUser.getUsername());
        username.getStyleClass().add("section-label");
        Button logout = secondaryButton("Log out");
        logout.setOnAction(event -> {
            javafx.scene.control.ButtonType result = showConfirmation("Log out", "Do you want to log out?");
            if (result == javafx.scene.control.ButtonType.OK) {
                try {
                    saveData();
                    showLogin();
                } catch (IOException exception) {
                    showError("Could not save JavaFlix data", exception);
                }
            }
        });
        topBar.getChildren().addAll(spacer, username, logout);
        return topBar;
    }

    private Button navButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setOnAction(event -> action.run());
        return button;
    }

    private VBox createProgramCard(Program program, boolean favoriteView) {
        VBox card = new VBox(13);
        card.setPrefWidth(248);
        card.setMaxWidth(248);
        card.getStyleClass().add("catalog-card");

        StackPane artwork = new StackPane();
        artwork.setMinHeight(164);
        artwork.setPrefHeight(164);
        artwork.getStyleClass().add("poster-placeholder");
        Label monogram = new Label("JF");
        monogram.getStyleClass().add("poster-monogram");
        Label type = new Label(program instanceof Series ? "SERIES" : "FILM");
        type.getStyleClass().add("poster-type");
        StackPane.setAlignment(type, Pos.BOTTOM_LEFT);
        StackPane.setMargin(type, new Insets(0, 0, 14, 14));
        artwork.getChildren().addAll(monogram, type);

        VBox info = new VBox(6);
        Label title = new Label(program.getTitle());
        title.getStyleClass().add("card-title");
        title.setWrapText(true);
        title.setMinHeight(40);
        Label metadata = new Label(program.getYear() + " · " + formatGenre(program.getGenre())
                + " · " + program.getMaturityRating());
        metadata.getStyleClass().add("card-metadata");
        Label score = new Label(program.getNumberOfReviews() == 0 ? "Not rated"
                : String.format(Locale.ROOT, "★ %.1f", program.getNumberOfStars()));
        score.getStyleClass().add("card-rating");
        info.getChildren().addAll(title, metadata, score);

        Button details = primaryButton("View details");
        details.setMaxWidth(Double.MAX_VALUE);
        details.setOnAction(event -> showProgramDetails(program));
        card.getChildren().addAll(artwork, info, details);

        if (currentUser.getCategory() == UserCategory.SUBSCRIBER) {
            Button favorite = secondaryButton(favoriteView ? "Remove from favorites"
                    : (currentUser.getFavorites().contains(program) ? "Saved" : "Add to favorites"));
            favorite.setOnAction(event -> {
                if (currentUser.getFavorites().contains(program)) {
                    currentUser.removeFavorite(program);
                } else {
                    currentUser.addFavorite(program);
                }
                if (favoriteView) {
                    showFavorites();
                } else {
                    updateResults();
                }
            });
            card.getChildren().add(favorite);
        }
        return card;
    }

    private void updateResults() {
        if (cards == null || searchField == null) {
            return;
        }
        String query = searchField.getText().trim().toLowerCase(Locale.ROOT);
        String actor = actorFilter.getText().trim().toLowerCase(Locale.ROOT);
        String selectedGenre = genreFilter.getValue();
        String selectedMaturity = maturityFilter.getValue();
        String selectedYear = yearFilter.getValue();
        String selectedStars = ratingFilter.getValue();

        List<Program> matches = Programs.getAllPrograms().stream()
                .filter(program -> query.isEmpty()
                        || program.getTitle().toLowerCase(Locale.ROOT).contains(query))
                .filter(program -> actor.isEmpty() || program.getMainCast().stream()
                        .anyMatch(name -> name.toLowerCase(Locale.ROOT).contains(actor)))
                .filter(program -> "Any genre".equals(selectedGenre)
                        || program.getGenre() == Genres.fromString(selectedGenre))
                .filter(program -> "Any rating".equals(selectedMaturity)
                        || program.getMaturityRating().name().equals(selectedMaturity))
                .filter(program -> matchesYear(program, selectedYear))
                .filter(program -> matchesStars(program, selectedStars))
                .sorted(Comparator.comparing(Program::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        cards.getChildren().clear();
        for (Program program : matches) {
            cards.getChildren().add(createProgramCard(program, false));
        }
        resultCount.setText(matches.size() + (matches.size() == 1 ? " title" : " titles"));
        emptyState.setText(Programs.getAllPrograms().isEmpty()
                ? "No catalog data found. Initialize sample data to get started."
                : "No titles match those filters. Try changing your search.");
        emptyState.setVisible(matches.isEmpty());
        emptyState.setManaged(matches.isEmpty());
    }

    private boolean matchesYear(Program program, String selectedYear) {
        if (selectedYear == null || selectedYear.equals("Any year")) {
            return true;
        }
        int year = program.getYear();
        if (program instanceof Series) {
            return ((Series) program).getYears().stream()
                    .anyMatch(range -> matchesYearBucket(range.getStartYear(), range.getEndYear(), selectedYear));
        }
        return matchesYearBucket(year, year, selectedYear);
    }

    private boolean matchesYearBucket(int startYear, int endYear, String selectedYear) {
        switch (selectedYear) {
            case "Before 2000":
                return startYear < 2000;
            case "2000–2009":
                return startYear <= 2009 && endYear >= 2000;
            case "2010–2019":
                return startYear <= 2019 && endYear >= 2010;
            case "2020+":
                return endYear >= 2020;
            default:
                return true;
        }
    }

    private boolean matchesStars(Program program, String selectedStars) {
        if (selectedStars == null || selectedStars.equals("Any stars")) {
            return true;
        }
        double minimum = selectedStars.equals("4+ stars") ? 4.0 : 3.0;
        return program.getNumberOfReviews() > 0 && program.getNumberOfStars() >= minimum;
    }

    private void resetFilters() {
        searchField.clear();
        actorFilter.clear();
        genreFilter.setValue("Any genre");
        maturityFilter.setValue("Any rating");
        yearFilter.setValue("Any year");
        ratingFilter.setValue("Any stars");
        updateResults();
    }

    private ComboBox<String> filterCombo(String[] values, String defaultValue) {
        ComboBox<String> combo = new ComboBox<>(FXCollections.observableArrayList(values));
        combo.setValue(defaultValue);
        combo.getStyleClass().add("filter-combo");
        combo.setPrefWidth(160);
        return combo;
    }

    private List<String> splitNames(String values) {
        List<String> names = new ArrayList<>();
        for (String name : values.split(",")) {
            if (!name.trim().isEmpty()) {
                names.add(name.trim());
            }
        }
        return names;
    }

    private String formatGenre(Genres genre) {
        if (genre == null) {
            return "Genre unavailable";
        }
        if (genre == Genres.SCIFI) {
            return "Sci-Fi";
        }
        String value = genre.name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private VBox pageContent(String eyebrow, String heading, String description) {
        VBox content = new VBox(22,
                new VBox(8, eyebrow(eyebrow), headline(heading), subtitle(description))
        );
        content.setPadding(new Insets(38, 52, 48, 52));
        content.getStyleClass().add("browse-content");
        return content;
    }

    private ScrollPane wrapPage(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");
        return scrollPane;
    }

    private VBox fieldGroup(String label, Node field) {
        VBox group = new VBox(6, new Label(label), field);
        group.getStyleClass().add("field-group");
        return group;
    }

    private VBox emptyMessage(String title, String detail) {
        Label heading = new Label(title);
        heading.getStyleClass().add("section-title");
        Label description = subtitle(detail);
        return new VBox(8, heading, description);
    }

    private Label wrappedLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("subtitle");
        return label;
    }

    private Label eyebrow(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("eyebrow");
        return label;
    }

    private Label headline(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("headline");
        label.setWrapText(true);
        return label;
    }

    private Label subtitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("subtitle");
        label.setWrapText(true);
        return label;
    }

    private Label feedbackLabel() {
        Label label = new Label(" ");
        label.getStyleClass().add("validation-message");
        return label;
    }

    private void setFeedback(Label label, String message) {
        label.setText(message);
    }

    private Button primaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("primary-button");
        return button;
    }

    private Button secondaryButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("secondary-button");
        return button;
    }

    private javafx.scene.control.ButtonType showConfirmation(String title, String message) {
        return JavaFxDialogs.showConfirmation(stage, title, message);
    }

    private void showInformation(String title, String message) {
        JavaFxDialogs.showInformation(stage, title, message);
    }

    private void showWarning(String title, String message) {
        JavaFxDialogs.showWarning(stage, title, message);
    }

    private void showError(String title, Exception exception) {
        JavaFxDialogs.showError(stage, title, exception.getMessage());
    }

    private void showStartupError(IOException exception) {
        root.setTop(createTopBar(false));
        VBox message = new VBox(14,
                eyebrow("DATA FILE ERROR"),
                headline("JavaFlix couldn’t open its data."),
                subtitle(exception.getMessage() == null ? "The data files could not be read." : exception.getMessage()),
                subtitle("Check that programs.dat and users.dat match this version. Back up incompatible files before regenerating sample data.")
        );
        message.setMaxWidth(620);
        message.getStyleClass().add("form-card");
        root.setCenter(new StackPane(message));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
