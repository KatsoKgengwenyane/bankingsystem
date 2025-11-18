package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

        // --- Setup Model and Controller ---
Bank bank = new Bank();
Customer customer = new Customer("John", "Doe", "Gaborone");

// Create some default accounts for demo
customer.addAccount(new SavingsAccount("S001", "Main Branch", customer));
customer.addAccount(new InvestmentAccount("I001", "Main Branch", customer, 1000));
customer.addAccount(new ChequeAccount("C001", "Main Branch", customer, "Tech Corp", "Gaborone"));

bank.addCustomer(customer);

BankController controller = new BankController(bank, this);

        // === COLORS ===
        String bacBlue = "#0A1E59";   // deep BAC blue
        String bacGold = "#F2C14E";   // warm BAC gold
        String lightGray = "#F7F8FA"; // background gray

        // === TITLE ===
        Label lblTitle = new Label("🏦 BAC Banking System");
        lblTitle.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + bacBlue + ";");

        // === FORM INPUTS ===
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
        
        // === BUTTONS ===
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
        "-fx-background-color: " + bacGold + "; -fx-text-fill: " + bacBlue + "; -fx-font-weight: bold; -fx-background-radius: 8;"
    ));
    b.setOnMouseExited(e -> b.setStyle(
        "-fx-background-color: " + bacBlue + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;"
    ));
}

// === FEEDBACK AREA ===
TextArea txtFeedback = new TextArea();
txtFeedback.setEditable(false);
txtFeedback.setWrapText(true);
txtFeedback.setPromptText("System feedback will appear here...");
txtFeedback.setPrefHeight(100);
txtFeedback.setStyle("-fx-border-color: " + bacBlue + "; -fx-border-radius: 8;");

// === LAYOUTS ===
GridPane inputGrid = new GridPane();
inputGrid.setHgap(10);
inputGrid.setVgap(10);
inputGrid.add(lblName, 0, 0);
inputGrid.add(txtName, 1, 0);
inputGrid.add(lblAccountType, 0, 1);
inputGrid.add(cmbAccountType, 1, 1);
inputGrid.add(lblAmount, 0, 2);
inputGrid.add(txtAmount, 1, 2);
inputGrid.add(lblBalance, 0, 3, 2, 1); // ✅ Correct placement

HBox buttonBox = new HBox(15, btnDeposit, btnWithdraw, btnClear, btnExit);
buttonBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(25, lblTitle, inputGrid, buttonBox, txtFeedback);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setStyle("-fx-background-color: " + lightGray + ";");

        // === EVENT HANDLERS ===
   btnDeposit.setOnAction(e -> {
    try {
        double amount = Double.parseDouble(txtAmount.getText());
        String result = controller.handleDeposit(txtName.getText(), cmbAccountType.getValue(), amount);
        txtFeedback.setText(result);

        Customer foundCustomer = bank.findCustomerByName(txtName.getText());
        if (foundCustomer != null) {
            Account account = bank.findAccountByType(foundCustomer, cmbAccountType.getValue());
            if (account != null) {
                lblBalance.setText("Balance: " + account.getBalance());
            }
        }
    } catch (NumberFormatException ex) {
        txtFeedback.setText("⚠️ Please enter a valid numeric amount.");
    }
});
btnWithdraw.setOnAction(e -> {
    try {
        double amount = Double.parseDouble(txtAmount.getText());
        String result = controller.handleWithdraw(txtName.getText(), cmbAccountType.getValue(), amount);
        txtFeedback.setText(result);

        Customer foundCustomer = bank.findCustomerByName(txtName.getText());
        if (foundCustomer != null) {
            Account account = bank.findAccountByType(foundCustomer, cmbAccountType.getValue());
            if (account != null) {
                lblBalance.setText("Balance: " + account.getBalance());
            }
        }
    } catch (NumberFormatException ex) {
        txtFeedback.setText("⚠️ Please enter a valid numeric amount.");
    }
});
        // === SCENE ===
        Scene scene = new Scene(mainLayout, 520, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
