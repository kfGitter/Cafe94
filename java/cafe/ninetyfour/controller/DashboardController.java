package cafe.ninetyfour.controller;

import cafe.ninetyfour.Cafe94App;
import cafe.ninetyfour.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    @FXML
    private Label welcomeLabel;
    private Cafe94App cafe94App;
    private User loggedInUser; // Store user directly

    public void setCafe94App(Cafe94App cafe94App) {
        this.cafe94App = cafe94App;
    }

    // Add this method to set user directly
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        updateWelcomeMessage();
    }

    private void updateWelcomeMessage() {
        welcomeLabel.setText("Welcome, " + loggedInUser.getFirstName() +
                " (" + loggedInUser.getRole() + ")");
    }

    @FXML
    private void handleNewBooking() {
        if (cafe94App.getBookingService() != null) {
            BookingDialog dialog = new BookingDialog(cafe94App);
            dialog.showAndWait();
        }
    }

    @FXML
    private void handleNewOrder() {
        if (cafe94App.getOrderService() != null) {
            OrderDialog dialog = new OrderDialog(cafe94App);
            dialog.showAndWait();
        }
    }
}