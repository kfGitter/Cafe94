package cafe.ninetyfour.exceptions;

public class ServiceException extends Exception {
    // For business logic errors
    public ServiceException(String message) {
        super(message);
    }

    // For technical errors with root cause
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    // Optional: Add error codes if needed
    private String errorCode;

    public ServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

// In your main application class
//public class CafeApplication extends Application {
//    public static void handleException(Throwable e) {
//        String message;
//        if (e instanceof ServiceException) {
//            message = e.getMessage();
//        } else {
//            message = "An unexpected error occurred";
//            Logger.getLogger(CafeApplication.class.getName())
//                    .log(Level.SEVERE, "Unexpected error", e);
//        }
//
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}