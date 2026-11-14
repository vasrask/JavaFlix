package javaflix.ui;

import javaflix.model.User;
import javaflix.model.UserCategory;
import javaflix.service.Users;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.function.Consumer;

final class JavaFxAuthView {
    private JavaFxAuthView() {
    }

    static StackPane login(Consumer<User> onLogin, Runnable onRegister) {
        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        Label feedback = feedbackLabel();

        Button signIn = primaryButton("Sign in");
        signIn.setMaxWidth(Double.MAX_VALUE);
        signIn.setOnAction(event -> {
            if (username.getText().trim().isEmpty() || password.getText().isEmpty()) {
                feedback.setText("Enter your username and password.");
                return;
            }
            User user = Users.loginUser(username.getText().trim(), password.getText());
            if (user == null) {
                feedback.setText("Username or password is incorrect.");
                password.requestFocus();
                password.selectAll();
                return;
            }
            onLogin.accept(user);
        });
        password.setOnAction(event -> signIn.fire());

        Button register = secondaryButton("Create an account");
        register.setMaxWidth(Double.MAX_VALUE);
        register.setOnAction(event -> onRegister.run());

        VBox form = new VBox(14,
                eyebrow("WELCOME BACK"),
                headline("Your next favorite awaits."),
                subtitle("Sign in to browse the JavaFlix catalog."),
                new Region(),
                username,
                password,
                feedback,
                signIn,
                register
        );
        form.setMaxWidth(420);
        form.getStyleClass().add("auth-card");
        VBox.setVgrow(form.getChildren().get(3), Priority.ALWAYS);

        StackPane container = new StackPane(form);
        container.getStyleClass().add("auth-background");
        return container;
    }

    static StackPane register(Runnable onRegistered, Runnable onBack) {
        TextField username = new TextField();
        username.setPromptText("Username");
        TextField email = new TextField();
        email.setPromptText("Email address");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        Label feedback = feedbackLabel();

        Button createAccount = primaryButton("Create account");
        createAccount.setMaxWidth(Double.MAX_VALUE);
        createAccount.setOnAction(event -> {
            String name = username.getText().trim();
            String emailAddress = email.getText().trim();
            String secret = password.getText();
            if (name.isEmpty()) {
                feedback.setText("Enter a username.");
            } else if (emailAddress.isEmpty()
                    || !emailAddress.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                feedback.setText("Enter a valid email address.");
            } else if (secret.isEmpty()) {
                feedback.setText("Enter a password.");
            } else if (Users.existingUsername(name)) {
                feedback.setText("That username is already in use.");
            } else {
                Users.addUser(new User(name, emailAddress, secret, UserCategory.SUBSCRIBER, new ArrayList<>()));
                onRegistered.run();
            }
        });

        Button back = secondaryButton("Back to sign in");
        back.setMaxWidth(Double.MAX_VALUE);
        back.setOnAction(event -> onBack.run());

        VBox form = new VBox(14,
                eyebrow("CREATE YOUR ACCOUNT"),
                headline("Create your account."),
                subtitle("Your watchlist is waiting."),
                username,
                email,
                password,
                feedback,
                createAccount,
                back
        );
        form.setMaxWidth(420);
        form.getStyleClass().add("auth-card");
        StackPane container = new StackPane(form);
        container.setPadding(new Insets(36));
        container.getStyleClass().add("auth-background");
        return container;
    }

    private static Label feedbackLabel() {
        Label label = new Label(" ");
        label.getStyleClass().add("validation-message");
        return label;
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

    private static Label eyebrow(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("eyebrow");
        return label;
    }

    private static Label headline(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("headline");
        label.setWrapText(true);
        return label;
    }

    private static Label subtitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("subtitle");
        label.setWrapText(true);
        return label;
    }
}
