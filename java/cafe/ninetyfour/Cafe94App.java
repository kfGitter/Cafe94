package cafe.ninetyfour;

import cafe.ninetyfour.controller.*;
//import cafe.ninetyfour.models.TestData;
import cafe.ninetyfour.models.User;
import cafe.ninetyfour.services.*;
import cafe.ninetyfour.utils.TestData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;


public class Cafe94App extends Application {
    private Stage primaryStage;
    private MainController mainController;
    private CustomerService customerService;
    private StaffService staffService;
    private BookingService bookingService;
    private OrderService orderService;
    private AuthService authService;
    private TableManager tableManager;


    /**
     * Constructor for Cafe94App.
     * Initializes the service classes and sets up the application.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Cafe94");

        // Set application icon
        try {
            Image coffee = new Image(getClass().getResourceAsStream("/coffee.png"));
            primaryStage.getIcons().add(coffee);
        } catch (Exception e) {
            System.err.println("Could not load application icon: " + e.getMessage());
        }

        initializeServices();

        // Start with login screen
        showLoginView();
    }

    /**
     * Initialize the services and load data.
     */
    private void initializeServices() {
        // Initialize services with proper file paths
        this.customerService = new CustomerService();
        this.staffService = new StaffService();
        this.bookingService = new BookingService(tableManager);
        this.orderService = new OrderService();
        this.authService = new AuthService(customerService, staffService);

        //Test data for development phase
        if (isDeveloping()) {
            TestData.initializeTestUsers(customerService, staffService);
        }

        // Verifying data loaded
        System.out.println("Loaded customers: " + customerService.getAllCustomers().size());
        System.out.println("Loaded staff: " + staffService.getAllStaff().size());
    }

    /**
     * Check if the application is in development mode.
     * This can be based on a system property or configuration.
     *
     * @return true if in development mode, false otherwise
     */
    private boolean isDeveloping() {
        // You can check a system property or configuration
        return true;
    }

    /**
     * Show the login view.
     */
    public void showLoginView() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            Parent loginView = loader.load();

            // Create scene
            Scene scene = new Scene(loginView);

            // Set up controller
            LoginController controller = loader.getController();
            controller.setCafe94App(this);

            // Show the stage
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load LoginView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Show an error alert and return to the login view.
     *
     * @param title   The title of the alert.
     * @param message The message to display in the alert.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        showLoginView();
    }

    // Getters for services
    public AuthService getAuthService() {
        return authService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public MainController getMainController() {
        return mainController;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public OrderService getOrderService() {
        return orderService;
    }


    /**
     * Show the main view after successful login.
     *
     * @param user The user who is logged in.
     */
    public void showManagerView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerView.fxml"));
            Parent managerView = loader.load();

            Scene scene = new Scene(managerView);

            ManagerController controller = loader.getController();
            controller.setCafe94App(this);
            controller.setUser(user);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load ManagerView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Show the chef view.
     *
     * @param user The user who is logged in.
     */
    public void showChefView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChefView.fxml"));
            Parent chefView = loader.load();

            Scene scene = new Scene(chefView);

            ChefController controller = loader.getController();
            controller.setCafe94App(this);
            controller.setUser(user);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load ChefView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show the waiter view.
     *
     * @param user The user who is logged in.
     */

    public void showWaiterView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WaiterView.fxml"));
            Parent waiterView = loader.load();

            Scene scene = new Scene(waiterView);

            WaiterController controller = loader.getController();
            controller.setCafe94App(this);
            controller.setUser(user);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load WaiterView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Show the driver view.
     *
     * @param user The user who is logged in.
     */
    public void showDriverView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DriverView.fxml"));
            Parent driverView = loader.load();

            Scene scene = new Scene(driverView);

            DriverController controller = loader.getController();
            controller.setCafe94App(this);
            controller.setUser(user);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load DriverView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show the customer view.
     *
     * @param user The user who is logged in.
     */
    public void showCustomerView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerView.fxml"));
            Parent customerView = loader.load();

            Scene scene = new Scene(customerView);

            CustomerController controller = loader.getController();
            controller.setCafe94App(this);
            controller.setUser(user);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load CustomerView.fxml: " + e.getMessage());
            e.printStackTrace();
        }

    }
    /**
     * Show the reports view.
     *
     * @param user The user who is logged in.
     */
    public void showReportsView(User user) {

    }

    /**
     * Show the staff management view.
     *
     * @param user The user who is logged in.
     */
    public void showStaffManagementView(User user) {

    }

    public static void main(String[] args) {
        launch(args);
    }


}

