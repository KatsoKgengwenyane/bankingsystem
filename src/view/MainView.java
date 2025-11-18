package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import controller.BankController;
import model.*;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🏦 BAC Banking System Interface");

        // ===== MODEL SETUP =====
        Bank bank = new Bank();
        Customer customer = new Customer("John", "Doe", "Gaborone");

        // Default accounts for demonstration
        customer.addAccount(new SavingsAccount("S001", "Main Branch", customer));
        customer.addAccount(new InvestmentAccount("I001", "Main Branch", customer, 1000));
        customer.addAccount(new ChequeAccount("C001", "Main Branch", customer, "Tech Corp", "Gaborone"));

        bank.addCustomer(customer);

        // ===== CONTROLLER =====
        BankController controller = new BankController(bank, this);

        // ===== COLORS =====
        String bacBlue = "#0A1E59";
        String bacGold = "#F2C14E";
        String lightGray = "#F7F8FA";

        // ===== TITLE =====
        Label lblTitle = new Label("🏦 BAC Banking System");
        lblTitle.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + bacBlue + ";");

        // ===== INPUT FIELDS =====
        Label lblName = new Label("Customer Name:");
        TextField txtName = new TextField();
        txtName.setPromptText("Enter full name");

        Label lblAccountType = new Label("Account Type:");
        ComboBox<String> cmbAccountType = new ComboBox<>();
        cmbAccountType.getItems().addAll("Savings Account", "Investment Account", "Cheque Account");
        cmbAccountType.setPromptText("Select type");

        Label lblAmount = new Label("Amount:");
        TextField txtAmount = new TextField();
        txtAmount.setPromptText("Enter amount (e.g. 500)");

        Label lblBalance = new Label("Balance: -");
        lblBalance.setStyle("-fx-font-weight: bold; -fx-text-fill: darkgreen;");

        // ===== BUTTONS =====
        Button btnDeposit = new Button("Deposit");
        Button btnWithdraw = new Button("Withdraw");
        Button btnClear = new Button("Clear");
        Button btnExit = new Button("Exit");

        Button[] buttons = {btnDeposit, btnWithdraw, btnClear, btnExit};
        for (Button b : buttons) {
            b.setPrefWidth(120);
            b.setStyle(
                "-fx-background-color: " + bacBlue + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8;"
            );

            b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: " + bacGold + "; " +
                "-fx-text-fill: " + bacBlue + "; " +
                "-fx-font-weight: bold; -fx-background-radius: 8;"
            ));

            b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: " + bacBlue + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 8;"
            ));
        }

        // ===== FEEDBACK BOX =====
        TextArea txtFeedback = new TextArea();
        txtFeedback.setEditable(false);
        txtFeedback.setWrapText(true);
        txtFeedback.setPromptText("System feedback will appear here...");
        txtFeedback.setPrefHeight(110);
        txtFeedback.setStyle("-fx-border-color: " + bacBlue + "; -fx-border-radius: 8;");

        // ===== LAYOUT =====
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(lblName, 0, 0);
        inputGrid.add(txtName, 1, 0);
        inputGrid.add(lblAccountType, 0, 1);
        inputGrid.add(cmbAccountType, 1, 1);
        inputGrid.add(lblAmount, 0, 2);
        inputGrid.add(txtAmount, 1, 2);
        inputGrid.add(lblBalance, 0, 3, 2, 1);

        HBox buttonBox = new HBox(15, btnDeposit, btnWithdraw, btnClear, btnExit);
        buttonBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(25, lblTitle, inputGrid, buttonBox, txtFeedback);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: " + lightGray + ";");

        // ===== EVENT HANDLERS =====

        btnDeposit.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(txtAmount.getText());
                String result = controller.handleDeposit(
                        txtName.getText(),
                        cmbAccountType.getValue(),
                        amount
                );
                txtFeedback.setText(result);

                updateBalance(bank, txtName.getText(), cmbAccountType.getValue(), lblBalance);

            } catch (NumberFormatException ex) {
                txtFeedback.setText("⚠️ Please enter a valid numeric amount.");
            }
        });

        btnWithdraw.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(txtAmount.getText());
                String result = controller.handleWithdraw(
                        txtName.getText(),
                        cmbAccountType.getValue(),
                        amount
                );
                txtFeedback.setText(result);

                updateBalance(bank, txtName.getText(), cmbAccountType.getValue(), lblBalance);

            } catch (NumberFormatException ex) {
                txtFeedback.setText("⚠️ Please enter a valid numeric amount.");
            }
        });

        btnClear.setOnAction(e -> {
            txtName.clear();
            txtAmount.clear();
            cmbAccountType.setValue(null);
            lblBalance.setText("Balance: -");
            txtFeedback.clear();
        });

        btnExit.setOnAction(e -> primaryStage.close());

        // ===== SCENE =====
        Scene scene = new Scene(mainLayout, 520, 470);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateBalance(Bank bank, String customerName, String type, Label lblBalance) {
        Customer c = bank.findCustomerByName(customerName);
        if (c != null) {
            Account a = bank.findAccountByType(c, mapType(type));
            if (a != null) {
                lblBalance.setText("Balance: " + a.getBalance());
            }
        }
    }

    private String mapType(String guiName) {
        if (guiName == null) return null;
        switch (guiName) {
            case "Savings Account": return "SavingsAccount";
            case "Investment Account": return "InvestmentAccount";
            case "Cheque Account": return "ChequeAccount";
            default: return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
