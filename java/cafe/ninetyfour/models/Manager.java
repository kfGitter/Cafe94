package cafe.ninetyfour.models;

import cafe.ninetyfour.enums.ReportType;
import cafe.ninetyfour.enums.UserRole;
import cafe.ninetyfour.exceptions.ServiceException;
import cafe.ninetyfour.services.StaffService;

import java.io.Serializable;
import java.util.Objects;

//import java.util.Objects;

/**
 * Represents a manager in the cafe system with staff management
 * and reporting capabilities.
 */
public class Manager extends Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    private StaffService staffService;
    private  ReportGenerator reportGenerator;

    /**
     * Constructs a new Manager with the mentioned details.
     *
     * @param username the manager's username
     * @param password the manager's password
     * @param hoursToWork the manager's contracted work hours
     * @param firstName the manager's first name
     * @param lastName the manager's last name
     */
    public Manager(String username, String password, int hoursToWork,
                   String firstName, String lastName) {
        super(username, password, UserRole.MANAGER, hoursToWork, firstName,
                lastName);
    }

    /**
     * Sets the StaffService dependency for staff management operations.
     *
     * @param staffService the StaffService implementation to use
     * @throws NullPointerException if staffService is null
     */
    public void setStaffService(StaffService staffService) {
        this.staffService = Objects.requireNonNull(staffService);
    }

    /**
     * Sets the ReportGenerator dependency for report generation.
     *
     * @param reportGenerator the ReportGenerator implementation to use
     * @throws NullPointerException if reportGenerator is null
     */
    public void setReportGenerator(ReportGenerator reportGenerator) {
        this.reportGenerator = Objects.requireNonNull(reportGenerator);
    }

    /**
     * Adds a new staff member to the system.
     *
     * @param staff the Staff object to add
     * @return true if the staff member was added successfully, false otherwise
     * @throws ServiceException if an error occurs during the operation
     */
    public boolean addStaff(Staff staff) throws ServiceException {
        return staffService.addStaff(staff);
    }

    /**
     * Removes a staff member from the system.
     *
     * @param staff the Staff object to remove
     * @return true if the staff member was removed successfully, false otherwise
     * @throws ServiceException if an error occurs during the operation
     */
    public boolean removeStaff(Staff staff) throws ServiceException {
        return staffService.removeStaff(staff.getUsername());
    }

    /**
     * Generates a report of the specified type.
     *
     * @param type the type of report to generate
     * @throws IllegalStateException if the ReportGenerator is not set
     */
    public void generateReport(ReportType type) {
        if (reportGenerator == null) {
            throw new IllegalStateException("ReportGenerator not initialized");
        }
            reportGenerator.generateReport(type);
    }
}