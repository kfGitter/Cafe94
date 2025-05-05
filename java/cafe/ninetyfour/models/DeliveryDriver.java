package cafe.ninetyfour.models;

import cafe.ninetyfour.enums.UserRole;

import java.io.Serializable;

/**
 * Represents a delivery driver in the cafe system responsible
 * for delivering orders to customers.
 */
public class DeliveryDriver extends Staff implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DeliveryDriver with the specified details.
     *
     * @param username the driver's username
     * @param password the driver's password
     * @param hoursToWork the driver's contracted work hours
     */
    public DeliveryDriver(String username, String password, int hoursToWork, String firstName, String lastName) {
        super(username, password, UserRole.DRIVER, hoursToWork, firstName, lastName);
    }



}
