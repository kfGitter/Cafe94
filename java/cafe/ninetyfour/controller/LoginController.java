package cafe.ninetyfour.controller;

import cafe.ninetyfour.models.Staff;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import cafe.ninetyfour.Cafe94App;
import cafe.ninetyfour.models.Customer;
import cafe.ninetyfour.models.User;
import cafe.ninetyfour.exceptions.ServiceException;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private Cafe94App cafe94App;

    public void setCafe94App(Cafe94App cafe94App) {
        this.cafe94App = cafe94App;
    }

    @FXML
    private void handleLogin() {
        errorLabel.setText("");

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            return;
        }

        try {
            User user = cafe94App.getAuthService().login(username, password);

            System.out.println("Logged in as: " + user.getUsername());
            System.out.println("Role: " + user.getRole());


            // Redirect based on role
            if (user instanceof Staff) {
                Staff staff = (Staff) user;
                switch(staff.getRole()) {
                    case MANAGER:
                        cafe94App.showManagerView(user);
                        break;
                    case CHEF:
                        cafe94App.showChefView(user);
                        break;
                    case WAITER:
                        cafe94App.showWaiterView(user);
                        break;
                    case DRIVER:
                        cafe94App.showDriverView(user);
                        break;
                }
            } else {
                // Customer
                cafe94App.showCustomerView(user);
            }
        } catch (ServiceException e) {
            errorLabel.setText(e.getMessage());
            passwordField.clear();
        }
    }
    @FXML
    private void handleRegister() {
        errorLabel.setText("");

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Register New Customer");
        dialog.setHeaderText("Enter your details");
        dialog.setContentText("Username:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        TextField addressField = new TextField();


        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);

        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);

        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(_ -> {
            try {
                // Validate registration fields
                if (firstNameField.getText().trim().isEmpty() ||
                        lastNameField.getText().trim().isEmpty() ||
                        emailField.getText().trim().isEmpty() ||
                addressField.getText().trim().isEmpty()) {
                    errorLabel.setText("All fields are required");
                    return;
                }

                Customer newCustomer = cafe94App.getCustomerService().registerCustomer(
                        usernameField.getText().trim(),
                        passwordField.getText().trim(),
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                       addressField.getText().trim(),
                        emailField.getText().trim());

                errorLabel.setText("Registration successful! Please login.");
                passwordField.clear();
            } catch (ServiceException e) {
                errorLabel.setText(e.getMessage());
            }
        });
    }
}