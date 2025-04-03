package cafe94.services;

import java.util.*;

import cafe94.models.*;

public class StaffService {
    private final List<Staff> staffMembers = new ArrayList<>();

    public List<Staff> getAllStaff() {
        return Collections.unmodifiableList(staffMembers);
    }

    public boolean addStaff(Staff staff) {
        if (staffMembers.contains(staff)) {
            System.out.println("Staff member already exists.");
            return false;
        }
        staffMembers.add(staff);
        return true;
    }

    public boolean removeStaff(Staff staff) {
        if (!staffMembers.contains(staff)) {
            System.out.println("Staff member not found.");
            return false;
        }
        staffMembers.remove(staff);
        return true;
    }
}