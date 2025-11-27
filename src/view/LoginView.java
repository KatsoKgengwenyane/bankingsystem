package view;

import database.DBInit;
import database.UserDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView extends Application {

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void start(Stage stage) {
        stage.setTitle("BAC Digital Banking – Login");

        // COLORS
        String bacBlue = "#0A1E59";
        String bg = "#F5F7FA";

        // ================= HEADER BAR =================
        Label topTitle = new Label("BAC Digital Banking System");
        topTitle.setStyle("-fx-font-size:24px; -fx-text-fill:white; -fx-font-weight:bold;");

        HBox header = new HBox(topTitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(18));
        header.setStyle("-fx-background-color:" + bacBlue + ";");

        // ================= LOGIN FORM =================
        Label lblLoginTitle = new Label("User Login");
        lblLoginTitle.setStyle("-fx-font-size:20px; -fx-font-weight:bold;");

        TextField txtUser = new TextField();
        txtUser.setPromptText("Username");
        txtUser.setStyle("-fx-background-radius:8; -fx-padding:10;");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password");
        txtPass.setStyle("-fx-background-radius:8; -fx-padding:10;");

        Button btnLogin = new Button("Login");
        btnLogin.setStyle(
                "-fx-background-color:" + bacBlue + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-padding:8 18;"
        );

        Button btnForgot = new Button("Forgot Password?");
        btnForgot.setStyle(
                "-fx-background-color:#D4AF37;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-padding:6 14;"
        );

        Label message = new Label();
        message.setStyle("-fx-text-fill:red; -fx-font-weight:bold;");

        // ================= BUTTON EVENTS =================
        btnLogin.setOnAction(e -> {
            try {
                boolean ok = userDAO.authenticate(txtUser.getText(), txtPass.getText());
                if (ok) {
                    message.setStyle("-fx-text-fill:green;");
                    message.setText("Login successful.");

                    new MainView().start(new Stage()); 
                    stage.close();
                } else {
                    message.setStyle("-fx-text-fill:red;");
                    message.setText("Invalid username or password.");
                }
            } catch (Exception ex) {
                message.setStyle("-fx-text-fill:red;");
                message.setText("Error: " + ex.getMessage());
            }
        });

        btnForgot.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter new password for " + txtUser.getText());
            dialog.showAndWait().ifPresent(newPass -> {
                try {
                    userDAO.updatePassword(txtUser.getText(), newPass);
                    message.setStyle("-fx-text-fill:green;");
                    message.setText("Password updated.");
                } catch (Exception ex) {
                    message.setStyle("-fx-text-fill:red;");
                    message.setText("Error: " + ex.getMessage());
                }
            });
        });

        // ================= LOGIN CARD =================
        VBox card = new VBox(15, lblLoginTitle, txtUser, txtPass, btnLogin, btnForgot, message);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:12;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 4);"
        );
        card.setPrefWidth(350);

        // CENTER LAYOUT
        VBox center = new VBox(card);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(30));
        center.setStyle("-fx-background-color:" + bg + ";");

        // ROOT
        VBox root = new VBox(header, center);

        Scene scene = new Scene(root, 450, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        DBInit.initialize(); // recreate tables

        try {
            new UserDAO().createUser("admin", "admin123");
            System.out.println("Admin user created.");
        } catch (Exception e) {
            System.out.println("Admin already exists.");
        }

        launch(args);
    }
}
