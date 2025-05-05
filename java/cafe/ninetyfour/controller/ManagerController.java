package cafe.ninetyfour.controller;

import cafe.ninetyfour.Cafe94App;
import cafe.ninetyfour.models.Staff;
import cafe.ninetyfour.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ManagerController {
    @FXML private Label welcomeLabel;
    @FXML private Label userDetailsLabel; // Add this line

    private Cafe94App cafe94App;
    private User user;

    public void setUser(User user) {
        this.user = user;
        updateUserDisplay();
    }

    public void setCafe94App(Cafe94App cafe94App) {
        this.cafe94App = cafe94App;
    }

    private void updateUserDisplay() {
        // Welcome message
        welcomeLabel.setText("Welcome, " + user.getFirstName() + " " + user.getLastName());

        // Detailed user information
        StringBuilder details = new StringBuilder();
        details.append("Role: Manager\n");
        details.append("Username: ").append(user.getUsername()).append("\n");
        details.append("Email: ").append(user.getEmail()).append("\n");

        if (user instanceof Staff) {
            Staff staff = (Staff) user;
            details.append("Hours Worked: ").append(staff.getTotalWorkedHours()).append("\n");
            details.append("Hours Scheduled: ").append(staff.getHoursToWork()).append("\n");
            details.append("Remaining Hours: ").append(staff.getHoursToWork() - staff.getTotalWorkedHours());
        }

        userDetailsLabel.setText(details.toString());
    }

    @FXML
    private void handleLogout() {
        try {
            cafe94App.getAuthService().logout(user.getUsername());
            cafe94App.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageStaff() {
        cafe94App.showStaffManagementView(user);
    }

    @FXML
    private void handleViewReports() {
        cafe94App.showReportsView(user);
    }

    @FXML
    private void handleProfiles() {
        // Implementation for profile management
    }
}