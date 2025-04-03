package cafe94.models;

import cafe94.enums.UserRole;


public class DeliveryDriver extends Staff {
    public DeliveryDriver(String username, String password, int hoursToWork) {
        super(username, password, UserRole.DRIVER, hoursToWork);
    }



}
