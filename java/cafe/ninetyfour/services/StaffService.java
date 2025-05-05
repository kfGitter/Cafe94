package cafe.ninetyfour.services;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import cafe.ninetyfour.exceptions.ServiceException;
import cafe.ninetyfour.enums.UserRole;
import cafe.ninetyfour.models.Staff;
import cafe.ninetyfour.models.User;

/**
 * StaffService class is responsible for managing staff data,
 * including adding, removing, and retrieving staff members.
 */
public class StaffService {
    private static final Logger logger = Logger.getLogger(StaffService.class.getName());
    private static final String STAFF_FILE = "data/staff.dat";
    private final Map<String, Staff> staffMembers = new HashMap<>();
    private int nextStaffId = 1;

    /**
     * Constructor for StaffService.
     * Initializes the staff collection and loads existing staff from file.
     */
    public StaffService() {
        ensureDataDirectoryExists();
        loadStaff();
    }

    /**
     * Ensures the data directory exists.
     */
    private void ensureDataDirectoryExists() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    //persistence methods
    /**
     * Saves the current staff members to a file.
     *
     * @throws ServiceException if an error occurs during saving
     */
    public synchronized void saveStaff() throws ServiceException {
        try {
            new File("data").mkdirs();
            File tempFile = new File(STAFF_FILE + ".tmp");

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(tempFile))) {
                oos.writeObject(new HashMap<>(staffMembers));
            }

            File dataFile = new File(STAFF_FILE);
            if (dataFile.exists() && !dataFile.delete()) {
                throw new ServiceException("Could not delete old staff file");
            }
            if (!tempFile.renameTo(dataFile)) {
                throw new ServiceException("Could not rename temp staff file");
            }
        } catch (IOException e) {
            throw new ServiceException("Failed to save staff data", e);
        }
    }


    /**
     * Loads staff members from a file into the service.
     */
    private void loadStaff() {
        File file = new File(STAFF_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Map<String, Staff> loaded = (Map<String, Staff>) ois.readObject();

                // Reset ID counter
                this.nextStaffId = loaded.values().stream()
                        .mapToInt(Staff::getUserId)
                        .max()
                        .orElse(0) + 1;

                // Reload data
                staffMembers.clear();
                staffMembers.putAll(loaded);
            } catch (Exception e) {
                System.err.println("Error loading staff: " + e.getMessage());
            }
        }
    }



    /**
     * Reloads staff members from the file.
     * This method clears the current staff collection and reloads it from the file.
     *
     * @throws ServiceException if an error occurs during saving
     */
    public synchronized void saveAndReload() throws ServiceException {
        saveStaff();
        staffMembers.clear();
        loadStaff();
    }

    /**
     * Adds a new staff member to the service.
     *
     * @param staff the staff member to add
     * @return true if the staff member was added successfully, false otherwise
     * @throws ServiceException if an error occurs during adding
     */
    public boolean addStaff(Staff staff) throws ServiceException {
        try {
            if (staff == null) {
                throw new ServiceException("Staff cannot be null");
            }
            if (staffMembers.containsKey(staff.getUsername())) {
                throw new ServiceException("Staff member already exists");
            }
            staffMembers.put(staff.getUsername(), staff);
            saveStaff();
            return true;
        } catch (Exception e) {
            logger.severe("Failed to add staff: " + e.getMessage());
            throw new ServiceException("Failed to add staff", e);
        }
    }


    /**
     * Gets a list of all staff with a specific role.
     *
     * @param role The role to filter by
     * @return List of staff with the given role
     */

    public List<Staff> getStaffByRole(UserRole role) {
        return staffMembers.values().stream()
                .filter(staff -> staff.getRole() == role)
                .toList();
    }

    /**
     * Retrieves all staff members.
     *
     * @return a list of all staff members
     */
    public List<Staff> getAllStaff() {
        return List.copyOf(staffMembers.values());
    }

    /**
     * Gets a staff member by their username.
     *
     * @param username Username of the staff member
     * @return Staff object, or null if not found
     */
    public Staff getStaffByUsername(String username) {
        return staffMembers.get(username); // Direct map lookup
    }

    /**
     * Removes a staff member from the system.
     *
     * @param username The username of the staff member to remove
     * @return true if removed successfully; false otherwise
     */
    public boolean removeStaff(String username) throws ServiceException {
        if (!staffMembers.containsKey(username)) {
            logger.warning("Staff member not found: " + username);
            return false;
        }
        staffMembers.remove(username);
        saveStaff();
        return true;
    }
}