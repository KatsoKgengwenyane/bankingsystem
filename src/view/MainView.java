package view;

import controller.BankController;
import database.DBInit;
import database.SampleData;
import database.UserDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Account;
import model.Customer;
import model.Transaction;

import java.util.List;

public class MainView extends Application {

    private final BankController controller = new BankController();

    // Smooth balance flash color
    private void flashColor(Label label, String color) {
        if (label == null) return;
        label.setStyle("-fx-text-fill:" + color + "; -fx-font-weight:bold; -fx-font-size:16px;");
        new Thread(() -> {
            try { Thread.sleep(700); } catch (Exception ignored) {}
            javafx.application.Platform.runLater(() ->
                    label.setStyle("-fx-text-fill:black; -fx-font-weight:bold; -fx-font-size:16px;")
            );
        }).start();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("🏦 BAC Banking System");

        // Colors
        String bacBlue = "#0A1E59";
        String bg = "#F5F7FA";

        // ================= HEADER =================
        Label topTitle = new Label("BAC Digital Banking System");
        topTitle.setStyle("-fx-font-size:24px; -fx-text-fill:white; -fx-font-weight:bold;");
        HBox header = new HBox(topTitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color:" + bacBlue + ";");

        // ================= FEEDBACK =================
        TextArea feedback = new TextArea();
        feedback.setEditable(false);
        feedback.setPrefHeight(120);

        // ================= FORM FIELDS =================
        // Register fields
        TextField txtFirst = new TextField(); txtFirst.setPromptText("First name");
        TextField txtLast = new TextField(); txtLast.setPromptText("Surname");
        TextField txtAddress = new TextField(); txtAddress.setPromptText("Address");
        TextField txtCell = new TextField(); txtCell.setPromptText("Cellphone");
        TextField txtEmployer = new TextField(); txtEmployer.setPromptText("Employer");
        TextField txtCompany = new TextField(); txtCompany.setPromptText("Company name");
        TextField txtCompanyAddress = new TextField(); txtCompanyAddress.setPromptText("Company address");
        CheckBox chkIsCompany = new CheckBox("Is Company?");

        // Create account fields
        TextField txtCustomerFull = new TextField(); txtCustomerFull.setPromptText("Customer full name");
        TextField txtAccNumber = new TextField(); txtAccNumber.setPromptText("Account number");
        TextField txtBranch = new TextField(); txtBranch.setPromptText("Branch");
        TextField txtInitial = new TextField(); txtInitial.setPromptText("Initial deposit");
        ComboBox<String> cmbAccTypeCreate = new ComboBox<>();
        cmbAccTypeCreate.getItems().addAll("SavingsAccount", "InvestmentAccount", "ChequeAccount");

        // Transaction fields
        TextField txtName = new TextField(); txtName.setPromptText("Customer full name");
        ComboBox<String> cmbAccountList = new ComboBox<>();
        TextField txtAmount = new TextField(); txtAmount.setPromptText("Amount");
        Label lblBalance = new Label("Balance: -");
        lblBalance.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");

        // ================= BUTTONS =================
        Button btnRegister = styled(bacBlue, "Register Customer");
        Button btnUpdate = styled(bacBlue, "Update Customer");
        Button btnDelete = styled("#B00020", "Delete Customer");
        Button btnCreateAcc = styled(bacBlue, "Create Account");
        Button btnDeposit = styled(bacBlue, "Deposit");
        Button btnWithdraw = styled(bacBlue, "Withdraw");
        Button btnHistory = styled(bacBlue, "View Transactions");
        Button btnClear = styled(bacBlue, "Clear Fields");

        // ================= LISTENER =================
        // Live-load accounts when typing customer name in transactions area
        txtName.textProperty().addListener((obs, oldVal, newVal) -> {
            cmbAccountList.getItems().clear();
            lblBalance.setText("Balance: -");
            if (newVal == null || newVal.trim().isEmpty()) return;

            Customer c = controller.loadCustomer(newVal.trim());
            if (c == null) return;

            // display "accountNumber (AccountType)"
            for (Account a : c.getAccounts()) {
                cmbAccountList.getItems().add(a.getAccountNumber() + " (" + a.getType() + ")");
            }

            if (!cmbAccountList.getItems().isEmpty()) {
                cmbAccountList.getSelectionModel().select(0);
                String sel = cmbAccountList.getValue();
                String type = extractTypeFromDisplay(sel);
                lblBalance.setText("Balance: " + controller.getBalance(newVal.trim(), type));
            }
        });

        // When user changes selected account, update balance
        cmbAccountList.valueProperty().addListener((o, oldV, newV) -> {
            if (newV == null || txtName.getText().trim().isEmpty()) {
                lblBalance.setText("Balance: -");
                return;
            }
            String type = extractTypeFromDisplay(newV);
            lblBalance.setText("Balance: " + controller.getBalance(txtName.getText().trim(), type));
        });

        // ================= CUSTOMER ACTIONS =================
        btnRegister.setOnAction(e -> {
            try {
                Customer c = new Customer(txtFirst.getText().trim(), txtLast.getText().trim(), txtAddress.getText().trim());
                c.setCellphone(txtCell.getText().trim());
                c.setEmployer(txtEmployer.getText().trim());
                c.setCompany(chkIsCompany.isSelected());
                c.setCompanyName(txtCompany.getText().trim());
                c.setCompanyAddress(txtCompanyAddress.getText().trim());

                feedback.setText(controller.createCustomer(c));
            } catch (Exception ex) {
                feedback.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnUpdate.setOnAction(e -> {
            Customer c = controller.loadCustomer(txtFirst.getText().trim() + " " + txtLast.getText().trim());
            if (c == null) { feedback.setText("⚠ Customer not found."); return; }

            c.setAddress(txtAddress.getText().trim());
            c.setCellphone(txtCell.getText().trim());
            c.setEmployer(txtEmployer.getText().trim());
            c.setCompany(chkIsCompany.isSelected());
            c.setCompanyName(txtCompany.getText().trim());
            c.setCompanyAddress(txtCompanyAddress.getText().trim());

            feedback.setText(controller.updateCustomer(c));
        });

       btnDelete.setOnAction(e -> {
    Customer c = controller.loadCustomer(txtFirst.getText().trim() + " " + txtLast.getText().trim());
    if (c == null) { 
        feedback.setText("⚠ Cannot delete — customer does not exist."); 
        return; 
    }

    feedback.setText(controller.deleteCustomer(c.getId()));  // <-- FIX
});

        // ================= CREATE ACCOUNT =================
        btnCreateAcc.setOnAction(e -> {
            try {
                Customer c = controller.loadCustomer(txtCustomerFull.getText().trim());
                if (c == null) { feedback.setText("⚠ Customer not found."); return; }

                double init = Double.parseDouble(txtInitial.getText().trim());
                feedback.setText(controller.createAccount(
                        c,
                        cmbAccTypeCreate.getValue(),
                        txtAccNumber.getText().trim(),
                        txtBranch.getText().trim(),
                        init
                ));
            } catch (NumberFormatException nfe) {
                feedback.setText("⚠ Invalid initial amount.");
            } catch (Exception ex) {
                feedback.setText("⚠ Error: " + ex.getMessage());
            }
        });

        // ================= TRANSACTIONS =================
        btnDeposit.setOnAction(e -> {
            try {
                String sel = cmbAccountList.getValue();
                if (sel == null) { feedback.setText("⚠ Choose an account."); return; }
                String type = extractTypeFromDisplay(sel);

                double amt = Double.parseDouble(txtAmount.getText().trim());
                String result = controller.deposit(txtName.getText().trim(), type, amt);

                lblBalance.setText("Balance: " + controller.getBalance(txtName.getText().trim(), type));
                flashColor(lblBalance, "green");
                feedback.setText(result);
            } catch (NumberFormatException nfe) {
                feedback.setText("⚠ Enter valid amount.");
            } catch (Exception ex) {
                feedback.setText("⚠ " + ex.getMessage());
            }
        });

        btnWithdraw.setOnAction(e -> {
            try {
                String sel = cmbAccountList.getValue();
                if (sel == null) { feedback.setText("⚠ Choose an account."); return; }
                String type = extractTypeFromDisplay(sel);

                double amt = Double.parseDouble(txtAmount.getText().trim());
                String result = controller.withdraw(txtName.getText().trim(), type, amt);

                lblBalance.setText("Balance: " + controller.getBalance(txtName.getText().trim(), type));
                flashColor(lblBalance, "red");
                feedback.setText(result);
            } catch (NumberFormatException nfe) {
                feedback.setText("⚠ Enter valid amount.");
            } catch (Exception ex) {
                feedback.setText("⚠ " + ex.getMessage());
            }
        });

        btnHistory.setOnAction(e -> {
            Customer c = controller.loadCustomer(txtName.getText().trim());
            if (c == null) { feedback.setText("⚠ Customer not found."); return; }

            String sel = cmbAccountList.getValue();
            if (sel == null) { feedback.setText("⚠ Choose an account."); return; }
            String type = extractTypeFromDisplay(sel);

            // find matching account by type
            Account acc = c.getAccounts().stream()
                    .filter(a -> a.getType().equalsIgnoreCase(type))
                    .findFirst().orElse(null);

            if (acc == null) { feedback.setText("⚠ Account not found."); return; }

            List<Transaction> list = controller.getAccountTransactions(acc);
            if (list == null || list.isEmpty()) {
                feedback.setText("No transactions.");
                return;
            }

            StringBuilder sb = new StringBuilder("=== Transaction History ===\n");
            list.forEach(t -> sb.append(t).append("\n"));
            feedback.setText(sb.toString());
        });

        // ================= CLEAR =================
        btnClear.setOnAction(e -> {
            // Register inputs
            txtFirst.clear(); txtLast.clear(); txtAddress.clear();
            txtCell.clear(); txtEmployer.clear(); txtCompany.clear(); txtCompanyAddress.clear(); chkIsCompany.setSelected(false);

            // Create account inputs
            txtCustomerFull.clear(); txtAccNumber.clear(); txtBranch.clear(); txtInitial.clear(); cmbAccTypeCreate.setValue(null);

            // Transaction inputs
            txtName.clear(); cmbAccountList.getItems().clear(); txtAmount.clear(); lblBalance.setText("Balance: -");

            // Feedback
            feedback.clear();
        });

        // ================= CARDS =================
        VBox cardRegister = createCard("👤 Register Customer",
                new VBox(10, txtFirst, txtLast, txtAddress, txtCell,
                        txtEmployer, txtCompany, txtCompanyAddress,
                        chkIsCompany, new HBox(10, btnRegister, btnUpdate), btnDelete));

        cardRegister.setPrefWidth(360);

        VBox cardCreate = createCard("🧾 Create Account",
                new VBox(10, txtCustomerFull, txtAccNumber, txtBranch,
                        cmbAccTypeCreate, txtInitial, btnCreateAcc));
        cardCreate.setPrefWidth(340);

        VBox cardTrans = createCard("💰 Transactions",
                new VBox(10, txtName, cmbAccountList, txtAmount, lblBalance,
                        new HBox(10, btnDeposit, btnWithdraw, btnHistory, btnClear)));
        cardTrans.setPrefWidth(400);

        VBox cardFeedback = createCard("📄 Feedback", feedback);
        cardFeedback.setPrefWidth(400);

        // ================= 3-COLUMN LAYOUT =================
        HBox body = new HBox(20,
                cardRegister,   // LEFT
                cardCreate,     // CENTER
                new VBox(20, cardTrans, cardFeedback) // RIGHT (stacked)
        );

        body.setPadding(new Insets(20));
        body.setStyle("-fx-background-color:" + bg + ";");

        Scene scene = new Scene(new VBox(header, body), 1200, 680);
        stage.setScene(scene);
        stage.show();
    }

    // helper: create styled button
    private Button styled(String color, String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + color + "; -fx-text-fill:white; -fx-font-weight:bold; -fx-background-radius:10; -fx-padding:8 16;");
        return b;
    }

    // helper: card component
    private VBox createCard(String title, javafx.scene.Node content) {
        Label lbl = new Label(title);
        lbl.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");

        VBox box = new VBox(12, lbl, content);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color:white; -fx-background-radius:12; -fx-effect:dropshadow(three-pass-box, rgba(0,0,0,0.08), 10,0,0,4);");
        return box;
    }

    // helper: parse "accountNumber (AccountType)" -> returns AccountType (inside parentheses)
    private String extractTypeFromDisplay(String display) {
        if (display == null) return null;
        int open = display.indexOf('(');
        int close = display.indexOf(')');
        if (open >= 0 && close > open) {
            return display.substring(open + 1, close).trim();
        }
        // fallback: maybe user selected just the type or just the account number
        return display.trim();
    }

    public static void main(String[] args) {
        DBInit.initialize();
        SampleData.load();
        try { new UserDAO().createUser("admin", "admin123"); } catch (Exception ignored) {}
        launch(args);
    }
}
