package cafe94.models;

import cafe94.enums.ReportType;
import cafe94.enums.UserRole;
import cafe94.services.StaffService;

import java.util.Objects;

//import java.util.Objects;

public class Manager extends Staff {
    private StaffService staffService;
    private  ReportGenerator reportGenerator;

    public Manager(String username, String password, int hoursToWork) {
        super(username, password, UserRole.MANAGER, hoursToWork);
    }

    // Dependency injection via setters
    public void setStaffService(StaffService staffService) {
        this.staffService = Objects.requireNonNull(staffService);
    }

    public void setReportGenerator(ReportGenerator reportGenerator) {
        this.reportGenerator = Objects.requireNonNull(reportGenerator);
    }

    public boolean addStaff(Staff staff) {
        return staffService.addStaff(staff);
    }

    public boolean removeStaff(Staff staff) {
        return staffService.removeStaff(staff);
    }

    public void generateReport(ReportType type) {
        reportGenerator.generateReport(type);
    }
}