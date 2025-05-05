package cafe.ninetyfour.controller;

import cafe.ninetyfour.Cafe94App;
import cafe.ninetyfour.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainController {
    @FXML private Tab bookingsTab;
    @FXML private Tab ordersTab;
    @FXML private Tab staffTab;
    private Cafe94App cafe94App;
    private User loggedInUser;


    public void setCafe94App(Cafe94App cafe94App) {
        this.cafe94App = cafe94App;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        updateTabs();
    }
    public void updateTabs () {
        // Enable/disable tabs based on role
        switch(loggedInUser.getRole()) {
            case CUSTOMER:
                staffTab.setDisable(true);
                // Customers can only view their own bookings/orders
                break;
            case WAITER:
                staffTab.setDisable(true);
                break;
            case CHEF:
                bookingsTab.setDisable(true);
                staffTab.setDisable(true);
                break;
            case MANAGER:
                // Manager can access everything
                break;
        }
    }
}