package view;

import controller.BankController;
import database.SampleData;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {

    private final BankController controller = new BankController();

    private Label lblBalance;
    private TextField txtName;
    private ComboBox<String> cmbType;

    @Override
    public void start(Stage stage) {
        stage.setTitle("🏦 BAC Banking System");

        // Colors
        String bacBlue = "#0A1E59";
        String bg = "#F7F8FA";

        // Title
        Label lblTitle = new Label("🏦 BAC Banking System");
        lblTitle.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + bacBlue);

        // Inputs
        Label lblName = new Label("Customer Name:");
        txtName = new TextField();

        Label lblAccType = new Label("Account Type:");
        cmbType = new ComboBox<>();
        cmbType.getItems().addAll("SavingsAccount", "InvestmentAccount", "ChequeAccount");

        Label lblAmount = new Label("Amount:");
        TextField txtAmount = new TextField();

        lblBalance = new Label("Balance: -");
        lblBalance.setStyle("-fx-font-weight: bold; -fx-text-fill: darkgreen;");

        // Buttons
        Button btnDeposit = new Button("Deposit");
        Button btnWithdraw = new Button("Withdraw");
        Button btnClear = new Button("Clear");
        Button btnExit = new Button("Exit");

        Button[] btns = {btnDeposit, btnWithdraw, btnClear, btnExit};
        for (Button b : btns) {
            b.setPrefWidth(120);
            b.setStyle("-fx-background-color:" + bacBlue + "; -fx-text-fill:white; -fx-font-weight:bold;");
        }

        // Feedback
        TextArea txtFeedback = new TextArea();
        txtFeedback.setEditable(false);
        txtFeedback.setPrefHeight(120);

        // GRID
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(lblName, 0, 0);
        grid.add(txtName, 1, 0);

        grid.add(lblAccType, 0, 1);
        grid.add(cmbType, 1, 1);

        grid.add(lblAmount, 0, 2);
        grid.add(txtAmount, 1, 2);

        grid.add(lblBalance, 0, 3, 2, 1);

        // BUTTON ROW
        HBox hbox = new HBox(15, btnDeposit, btnWithdraw, btnClear, btnExit);
        hbox.setAlignment(Pos.CENTER);

        // MAIN LAYOUT
        VBox layout = new VBox(20, lblTitle, grid, hbox, txtFeedback);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color:" + bg);

        // =======================================================
        // EVENT HANDLERS
        // =======================================================

        // DEPOSIT
        btnDeposit.setOnAction(e -> {
            String name = txtName.getText().trim();
            String type = cmbType.getValue();

            double amount;
            try {
                amount = Double.parseDouble(txtAmount.getText());
            } catch (Exception ex) {
                txtFeedback.setText("⚠ Enter valid amount.");
                return;
            }

            String result = controller.deposit(name, type, amount);
            txtFeedback.setText(result);
            refreshBalance();
        });

        // WITHDRAW
        btnWithdraw.setOnAction(e -> {
            String name = txtName.getText().trim();
            String type = cmbType.getValue();

            double amount;
            try {
                amount = Double.parseDouble(txtAmount.getText());
            } catch (Exception ex) {
                txtFeedback.setText("⚠ Enter valid amount.");
                return;
            }

            String result = controller.withdraw(name, type, amount);
            txtFeedback.setText(result);
            refreshBalance();
        });

        // CLEAR
        btnClear.setOnAction(e -> {
            txtName.clear();
            txtAmount.clear();
            cmbType.setValue(null);
            txtFeedback.clear();
            lblBalance.setText("Balance: -");
        });

        // EXIT
        btnExit.setOnAction(e -> System.exit(0));

        // Auto-refresh balance when typing name or choosing account type
        txtName.setOnKeyReleased(e -> refreshBalance());
        cmbType.setOnAction(e -> refreshBalance());

        stage.setScene(new Scene(layout, 540, 520));
        stage.show();
    }

    // ======================================================
    // REFRESH BALANCE
    // ======================================================
    private void refreshBalance() {
        String name = txtName.getText().trim();
        String type = cmbType.getValue();

        if (name.isEmpty() || type == null) {
            lblBalance.setText("Balance: -");
            return;
        }

        Double bal = controller.getBalance(name, type);
        if (bal == null) {
            lblBalance.setText("Balance: -");
        } else {
            lblBalance.setText("Balance: " + bal);
        }
    }

    public static void main(String[] args) {
        SampleData.load();
        launch(args);
    }
}
