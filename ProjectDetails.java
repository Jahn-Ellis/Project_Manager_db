package task8;

import java.time.LocalDate;

public class ProjectDetails {

    // Attributes
    static int projectNum;
    static String projectName;
    static String buildingType;
    static String address;
    static int ERFNum;
    static double feesCost;
    static double feesPaid;
    static String deadline;
    static String status;

    // Methods
    public ProjectDetails(int projectNum, String projectName, String buildingType, String address,
    int ERFNum, double feesCost, double feesPaid, String deadline, String status) {
        ProjectDetails.projectNum = projectNum;
        ProjectDetails.projectName = projectName;
        ProjectDetails.buildingType = buildingType;
        ProjectDetails.address = address;
        ProjectDetails.ERFNum = ERFNum;
        ProjectDetails.feesCost = feesCost;
        ProjectDetails.feesPaid = feesPaid;
        ProjectDetails.deadline = deadline;
        ProjectDetails.status = status;
    }

    public static int getprojectNum() {
        return projectNum;
    }

    public static String getprojectName() {
        return projectName;
    }

    public static String getBuildingType() {
        return buildingType;
    }

    public static String getAddress() {
        return address;
    }

    public static int getERFNum() {
        return ERFNum;
    }

    public static double getFeesCost() {
        return feesCost;
    }

    public static double getFeesPaid() {
        return feesPaid;
    }

    public static String getDeadline() {
        return deadline;
    }

    public static String getStatus() {
        return status;
    }

    public static double getAmountDue() {
        return feesCost - feesPaid;
    }

    public String toString() {
        String output = "Project Number: " + projectNum;
        output += "\nProject Name: " + projectName;
        output += "\nBuilding Type: " + buildingType;
        output += "\nAddress: " + address;
        output += "\nERF Num: " + ERFNum;
        output += "\nTotal Cost: " + feesCost;
        output += "\nFeed Paid: " + feesPaid;
        output += "\nDeadline: " + deadline;
        output += "\nStatus: " + status;

        return output;
    }
}