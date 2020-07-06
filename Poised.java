/**
 *
 * <h1>Poised Construction - Project Manager</h1>
 * <p>The application makes use a database for data handling.
 * Add data is read from and written to respective tables by way of objects being created and added to their
 * respective groupings.</p>
 * <p>There are multiple functions to display, edit, and transfer projects to archives.</p>
 * <p>Be sure to generate relevant db and user details to when using the program to ensure your tables are read from
 * and written to properly.</p>
 * <p>Follow the prompts on the main menu.</p>
 *
 * @Auther Jon Ellis
 * @Version 1.0
 * @Since 2020-07
 * */

package task8;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class Poised {

    public static void main(String[] args) {

        Scanner selector = new Scanner(System.in);
        int select = 0;
        boolean quit = false;
        boolean back = false;

        // Driver Code

        try {
            // Connect to the poise_db database, via the jdbc:mysql: channel on localhost (this PC)
            // Use username "otheruser", password "swordfish".
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poise_db?useSSL=false",
                    "otheruser",
                    "swordfish"
            );
            // Create a direct line to the database for running our queries
            Statement statement = connection.createStatement();
            ResultSet results;
            int rowsAffected;

            // Main Menu
            do {
                mainMenu();
                // Input try/catch
                do {
                    try {
                        select = selector.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Please Enter a Valid Integer.");
                        selector.nextLine();
                    }
                } while (select < 0);
                    switch (select) {
                        case 1:
                            // Create a Project & add to ArrayList ---------------------------------------------------- complete
                            createCustomer(statement);
                            createProject(statement);
                            createArchitect(statement);
                            createContractor(statement);
                            addProject(statement);
                            break;
                        case 2:
                            // Display all projects ------------------------------------------------------------------- complete
                            viewAllProjects(statement);
                            break;
                        case 3:
                            // Find project to edit ------------------------------------------------------------------- complete
                            findProject(statement);
                            break;
                        case 4:
                            // Find project to update payment --------------------------------------------------------- complete
                            updatePayment(statement);
                            break;
                        case 5:
                            // View Incomplete ------------------------------------------------------------------------ complete
                            viewIncomplete(statement);
                            break;
                        case 6:
                            // View Overdue --------------------------------------------------------------------------- complete
                            overdueProjects(statement);
                            break;
                        case 7:
                            // Finalise project, display Invoice, update tables --------------------------------------- complete
                            finaliseProject(statement);
                            break;
                        case 8:
                            // View Archive
                            viewArchive(statement);
                            break;
                        case 0:
                            // Exit ----------------------------------------------------------------------------------- complete
                            quit = true;
                            break;
                        default:
                            System.out.println("Invalid selection.");
                    }
            } while (!quit);
            System.out.println("Goodbye.");

            statement.close();
            connection.close();

        } catch (SQLException e) {
            // Catch a SQLException.
            e.printStackTrace();
        }
    }

    // Menus -----------------------------------------------------------------------------------------------------------

    public static void mainMenu() {

        System.out.println("Poised Project Manager" +
                "\n" +
                "\n1 - Create Project" +
                "\n2 - View all Projects" +
                "\n3 - Find Project (View & Edit)" +
                "\n4 - Update Payment" +
                "\n5 - View Incomplete Projects" +
                "\n6 - View Overdue Projects" +
                "\n7 - Finalise Project" +
                "\n8 - View Archive" +
                "\n0 - Exit");

    }

    public static void editMenu() {

        System.out.println("\nSelect Section to Edit" +
                "\n" +
                "\n1 - Project & Financial Details" +
                "\n2 - Customer Details" +
                "\n3 - Architect Details" +
                "\n4 - Contractor Details" +
                "\n0 - Back to Main Menu");

    }

    public static void projectEditMenu() {

        System.out.println("\nSelect item to edit" +
                "\n" +
                "\n1 - Project Number" +
                "\n2 - Project Name" +
                "\n3 - Building Type" +
                "\n4 - Project Address" +
                "\n5 - Project ERF Number" +
                "\n6 - Project Cost" +
                "\n7 - Amount Paid to Date" +
                "\n8 - Deadline" +
                "\n9 - Status (Mark as Complete/Incomplete)" +
                "\n0 - Back to Main Menu");

    }

    public static void contactEditMenu() {

        System.out.println("\nSelect item to edit" +
                "\n" +
                "\n1 - Name" +
                "\n2 - Surname" +
                "\n3 - Contact" +
                "\n4 - Email" +
                "\n5 - Address" +
                "\n0 - Back to Main Menu");

    }

    public static void createCustomer(Statement statement) throws SQLException {

        Scanner Input = new Scanner(System.in);

        System.out.println("\nEnter Customer Name:");
        String name = Input.nextLine();

        System.out.println("\nEnter Customer Surname:");
        String surname = Input.nextLine();

        System.out.println("\nEnter Customer Contact:");
        int contact = 0;

        // Input error try block
        do {
            try {
                contact = Input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please Enter a Valid Contact.");
                Input.nextLine();
            }
        } while (contact <= 0);

        Input.nextLine(); // Fix skip error

        System.out.println("\nEnter Customer Email:");
        String email = Input.nextLine();

        System.out.println("\nEnter Customer Address:");
        String address = Input.nextLine();

        ContactDetailsCustomer Customer = new ContactDetailsCustomer(name, surname, contact, email, address);
    }

    public static void createProject(Statement statement) throws SQLException {

        Scanner Input = new Scanner(System.in);

        System.out.println("\nEnter a Project Number:");
        int projectNum = 0;
        int projectNumCheck;
        int proNum = 0;
        boolean availableNum = false;

        ArrayList<Integer> projectArrNum = new ArrayList<Integer>();
        ArrayList<String> projectArrName = new ArrayList<String>();

        // Statement for PRJ_Num checks
        ResultSet results = statement.executeQuery("SELECT PRJ_Num FROM project");

        while (results.next()) {
            projectArrNum.add(results.getInt("PRJ_Num"));
        }

        results.close();

        // Statement for PRJ_Name checks
        results = statement.executeQuery("SELECT PRJ_Name FROM project");

        while (results.next()) {
            projectArrName.add(results.getString("PRJ_Name"));
        }

        results.close();

        // Check PRJ_Num availability
        while (!availableNum) {
            do {
                try {
                    projectNum = Input.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Please Enter a Valid Integer.");
                    Input.nextLine();
                }
            } while (projectNum <= 0);

            for (int i = 0; i < projectArrNum.size(); i++) {
                projectNumCheck = projectArrNum.get(i);
                if (projectNumCheck == projectNum) {
                    System.out.println("Project already exists, please enter another number.");
                    proNum -= 1;
                } else {
                    proNum += 1;
                }
                if (proNum == projectArrNum.size()) {
                    availableNum = true;
                }
            }
        }

        Input.nextLine(); // fix input skip error

        System.out.println("\nEnter Project Name:");

        String projectName = "";
        String projectNameCheck;
        int proName = 0;
        boolean availableName = false;

        // Check PRJ_Name availability
        while (!availableName) {

            projectName = Input.nextLine();

            for (int i = 0; i < projectArrName.size(); i++) {
                projectNameCheck = projectArrName.get(i);
                if (projectNameCheck.equals(projectName)) {
                    System.out.println("Project already exists, please try again.");
                    proName -= 1;
                } else {
                    proName += 1;
                }
                if (proName == projectArrName.size()) {
                    availableName = true;
                } else if (projectName.isEmpty()) {
                    availableName = true;
                }
            }
        }


        System.out.println("\nEnter Building Type:");
        String buildingType = Input.nextLine();

        // If empty auto-generate & check availability
        if (projectName.isEmpty()) {
            projectName = buildingType + " " + ContactDetailsCustomer.getSurname();
        }

        String projectAutoNameCheck;
        int proAutoName = 0;
        proName = 0;
        boolean availableAutoName = false;
        availableName = false;

        while (!availableAutoName) {
            for (int i = 0; i < projectArrName.size(); i++) {
                projectAutoNameCheck = projectArrName.get(i);
                if (projectAutoNameCheck.equals(projectName)) {
                    System.out.println("Please enter a custom Project Name:");

                    while (!availableName) {

                        projectName = Input.nextLine();

                        for (int j = 0; j < projectArrName.size(); j++) {
                            projectNameCheck = projectArrName.get(j);
                            if (projectNameCheck.equals(projectName)) {
                                System.out.println("Project already exists, please try again.");
                                proName -= 1;
                            } else {
                                proName += 1;
                            }
                            if (proName == projectArrName.size()) {
                                availableName = true;
                            }
                        }
                    }

                    proAutoName -= 1;
                } else {
                    proAutoName += 1;
                }
                if (proAutoName == projectArrName.size()) {
                    availableAutoName = true;
                }
            }
        }

        System.out.println("\nEnter Project Address:");
        String address = Input.nextLine();

        System.out.println("\nEnter ERF Number:");
        int ERFNum = 0;

        // Input error try block
        do {
            try {
                ERFNum = Input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please Enter a Valid Integer.");
                Input.nextLine();
            }
        } while (ERFNum <= 0);

        System.out.println("\nTotal Cost of Project:");
        double feesCost = 0;

        // Input error try block
        do {
            try {
                feesCost = Input.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Please Enter a Valid Amount.");
                Input.nextLine();
            }
        } while (feesCost <= 0);

        System.out.println("\nAmount Paid to Date by Customer:");
        double feesPaid = 0;

        // Input error try block
        do {
            try {
                feesPaid = Input.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Please Enter a Valid Amount.");
                Input.nextLine();
            }
        } while (feesPaid < 0);

        Input.nextLine(); // Fix skip error

        System.out.println("\nProject Deadline (yyyy-mm-dd):");
        boolean valid = false;
        String deadline = "";

        do {
            try{
                deadline = Input.nextLine();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date testDeadline = sdf.parse(deadline);
                if (testDeadline.before(sdf.parse(String.valueOf(LocalDate.now())))) {
                    System.out.println("Date already past, please enter a valid date:");
                } else {
                    valid = true;
                }
            } catch (ParseException e) {
                System.out.println("Please Enter a Valid Date.");
            }
        } while (!valid);

        // Auto-assign incomplete status
        String status = "Incomplete";

        ProjectDetails Project = new ProjectDetails(projectNum, projectName, buildingType, address,
                ERFNum, feesCost, feesPaid, deadline, status);
    }

    public static void createArchitect(Statement statement) throws SQLException {

        Scanner Input = new Scanner(System.in);

        System.out.println("\nEnter Architect Name:");
        String name = Input.nextLine();

        System.out.println("\nEnter Architect Surname:");
        String surname = Input.nextLine();

        System.out.println("\nEnter Architect Contact:");
        int contact = 0;

        // Input error try block
        do {
            try {
                contact = Input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please Enter a Valid Contact.");
                Input.nextLine();
            }
        } while (contact <= 0);

        Input.nextLine(); // Fix skip error

        System.out.println("\nEnter Architect Email:");
        String email = Input.nextLine();

        System.out.println("\nEnter Architect Address:");
        String address = Input.nextLine();

        ContactDetailsArchitect Architect = new ContactDetailsArchitect(name, surname, contact, email, address);
    }

    public static void createContractor(Statement statement) throws SQLException {

        Scanner Input = new Scanner(System.in);

        System.out.println("\nEnter Contractor Name:");
        String name = Input.nextLine();

        System.out.println("\nEnter Contractor Surname:");
        String surname = Input.nextLine();

        System.out.println("\nEnter Contractor Contact:");
        int contact = 0;

        // Input error try block
        do {
            try {
                contact = Input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please Enter a Valid Contact.");
                Input.nextLine();
            }
        } while (contact <= 0);

        Input.nextLine(); // Fix skip error

        System.out.println("\nEnter Contractor Email:");
        String email = Input.nextLine();

        System.out.println("\nEnter Contractor Address:");
        String address = Input.nextLine();

        ContactDetailsContractor Contractor = new ContactDetailsContractor(name, surname, contact, email, address);
    }

    public static void addProject(Statement statement) throws SQLException {

        // Get all values from object to write to database tables
        // Project
        int projectNum = ProjectDetails.getprojectNum();
        String projectName = ProjectDetails.getprojectName();
        String buildingType = ProjectDetails.getBuildingType();
        String projectAddress = ProjectDetails.getAddress();
        int ERFNum = ProjectDetails.getERFNum();
        String deadline = ProjectDetails.getDeadline();
        String status = ProjectDetails.getStatus();

        // Finances
        double feesCost = ProjectDetails.getFeesCost();
        double feesPaid = ProjectDetails.getFeesPaid();

        // Customer
        String custName = ContactDetailsCustomer.getName();
        String custSurname = ContactDetailsCustomer.getSurname();
        int custContact = ContactDetailsCustomer.getContact();
        String custEmail = ContactDetailsCustomer.getEmail();
        String custAddress = ContactDetailsCustomer.getAddress();

        // Architect
        String archName = ContactDetailsArchitect.getName();
        String archSurname = ContactDetailsArchitect.getSurname();
        int archContact = ContactDetailsArchitect.getContact();
        String archEmail = ContactDetailsArchitect.getEmail();
        String archAddress = ContactDetailsArchitect.getAddress();

        // Contractor
        String contName = ContactDetailsContractor.getName();
        String contSurname = ContactDetailsContractor.getSurname();
        int contContact = ContactDetailsContractor.getContact();
        String contEmail = ContactDetailsContractor.getEmail();
        String contAddress = ContactDetailsContractor.getAddress();

        // Add to Project
        int rowsAffected = statement.executeUpdate(
                "INSERT INTO project VALUES ('"+projectNum+"', '"+projectName+"', '"+buildingType+"'," +
                        "'"+projectAddress+"', '"+ERFNum+"', '"+deadline+"', '"+status+"')"
        );

        // Add to Finances
        rowsAffected = statement.executeUpdate(
                "INSERT INTO finances VALUES ('"+projectNum+"', '"+feesCost+"', '"+feesPaid+"')"
        );

        // Add to Customer
        rowsAffected = statement.executeUpdate(
                "INSERT INTO customer VALUES ('"+projectNum+"', '"+custName+"', '"+custSurname+"', '"+custContact+"', '"+custEmail+"', '"+custAddress+"')"
        );

        // Add to Architect
        rowsAffected = statement.executeUpdate(
                "INSERT INTO architect VALUES ('"+projectNum+"', '"+archName+"', '"+archSurname+"', '"+archContact+"', '"+archEmail+"', '"+archAddress+"')"
        );

        // Add to Contractor
        rowsAffected = statement.executeUpdate(
                "INSERT INTO contractor VALUES ('"+projectNum+"', '"+contName+"', '"+contSurname+"', '"+contContact+"', '"+contEmail+"', '"+contAddress+"')"
        );
    }

    public static void viewAllProjects(Statement statement) throws SQLException {

        System.out.println("All Current Projects:\n");

        // Statement
        ResultSet results = statement.executeQuery("SELECT * FROM project");

        // Loop over the results, printing them all.
        while (results.next()) {
            System.out.println("Project Number: " + results.getInt("PRJ_Num") + "\n" +
                    "Project Name: " + results.getString("PRJ_Name") + "\n" +
                    "Building Type: " + results.getString("Build_Type") + "\n" +
                    "Project Address: " + results.getString("PRJ_Add") + "\n" +
                    "ERF Number: " + results.getInt("ERF_Num") + "\n" +
                    "Deadline: " + results.getString("Deadline") + "\n" +
                    "Status: " + results.getString("Status") + "\n");
        }
        results.close();
    }

    public static void findProject(Statement statement) throws SQLException {

        // Find
        String searchValue;
        Scanner Input = new Scanner(System.in);

        int proNum = 0;
        boolean found = false;
        boolean back = false;

        do {

            System.out.println("\nSearch by:" +
                    "\n1 - Project Number" +
                    "\n2 - Project Name" +
                    "\n0 - Back");
            int select = Input.nextInt();
            Input.nextLine(); // Fix input skip error

            switch (select) {
                case 1:
                    System.out.println("\nEnter a Project Number:");
                    int searchNum = 0;

                    do {
                        try {
                            searchNum = Input.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Please Enter a Valid Integer.");
                            Input.nextLine();
                        }
                    } while (searchNum <= 0);

                    // Statement
                    ResultSet results = statement.executeQuery("SELECT * FROM project");

                    while (results.next()) {
                        proNum = results.getInt("PRJ_Num");
                        if (proNum == searchNum) {
                            System.out.println("Project Found");
                            found = true;
                            back = true;
                            break;
                        } else
                            System.out.println("Project not found");
                    }
                    results.close();
                    break;
                case 2:
                    System.out.println("\nEnter a Project Name:");
                    String searchName = Input.nextLine();

                    // Statement
                    results = statement.executeQuery("SELECT * FROM project");

                    while (results.next()) {
                        searchValue = results.getString("PRJ_Name");
                        if (searchValue.equals(searchName)) {
                            System.out.println("Project Found");
                            proNum = results.getInt("PRJ_Num");
                            found = true;
                            back = true;
                            break;
                        } else
                            System.out.println("Project not found");
                    }
                    results.close();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid selection.");
                }
            } while (!back);

        back = false;
        int rowsAffected;
        int select = 0;

        if (found) {

            // Display
            // Project
            System.out.println("Project Details\n");

            // Statement
            ResultSet results = statement.executeQuery("SELECT * FROM project WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Project Number: " + results.getInt("PRJ_Num") + "\n" +
                        "Project Name: " + results.getString("PRJ_Name") + "\n" +
                        "Building Type: " + results.getString("Build_Type") + "\n" +
                        "Project Address: " + results.getString("PRJ_Add") + "\n" +
                        "ERF Number: " + results.getInt("ERF_Num") + "\n" +
                        "Deadline: " + results.getString("Deadline") + "\n" +
                        "Status: " + results.getString("Status") + "\n");
            }
            results.close();

            // Customer
            System.out.println("Customer Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM customer WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Customer Name: " + results.getString("Cust_Name") + " " + results.getString("Cust_Sname") + "\n" +
                        "Customer Contact: " + results.getInt("Cust_Con") + "\n" +
                        "Customer Email: " + results.getString("Cust_Email") + "\n" +
                        "Customer Address: " + results.getString("Cust_Add") + "\n");
            }
            results.close();

            // Fiances
            System.out.println("Financial Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM finances WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Total Cost of Project: R" + results.getDouble("Fees_Cost") + "\n" +
                        "Total Paid by Customer: R" + results.getDouble("Fees_paid") + "\n" +
                        "Amount Due: R" + (results.getDouble("Fees_Cost") - results.getDouble("Fees_paid")) + "\n");
            }
            results.close();

            // Architect
            System.out.println("Architect Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM architect WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Architect Name: " + results.getString("Arch_Name") + " " + results.getString("Arch_Sname") + "\n" +
                        "Architect Contact: " + results.getInt("Arch_Con") + "\n" +
                        "Architect Email: " + results.getString("Arch_Email") + "\n" +
                        "Architect Address: " + results.getString("Arch_Add") + "\n");
            }
            results.close();

            // Contractor
            System.out.println("Contractor Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM contractor WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Contractor Name: " + results.getString("Cont_Name") + " " + results.getString("Cont_Sname") + "\n" +
                        "Contractor Contact: " + results.getInt("Cont_Con") + "\n" +
                        "Contractor Email: " + results.getString("Cont_Email") + "\n" +
                        "Contractor Address: " + results.getString("Cont_Add") + "\n");
            }
            results.close();

            // Edit yes/back
            System.out.println("\nWould you like to edit this Project?" +
                    "\n" +
                    "\n1 - Yes" +
                    "\n0 - Back to Main Menu");
            // Input try/catch
            do {
                try {
                    select = Input.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Please Enter a Valid Integer.");
                    Input.nextLine();
                }
            } while (select < 0);
            do {
                switch (select) {
                    case 1:
                        editMenu();
                        // Input try/catch
                        do {
                            try {
                                select = Input.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Please Enter a Valid Integer.");
                                Input.nextLine();
                            }
                        } while (select < 0);
                        // Select category to edit
                        do {
                            switch (select) {
                                case 1:
                                    // Edit Project Details
                                    projectEditMenu();
                                    // Input try/catch
                                    do {
                                        try {
                                            select = Input.nextInt();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Please Enter a Valid Integer.");
                                            Input.nextLine();
                                        }
                                    } while (select < 0);
                                    // Select item to edit
                                    do {
                                        switch (select) {
                                            case 1:
                                                // Change projectNum
                                                int newProjectNum = 0;

                                                // Statement
                                                results = statement.executeQuery("SELECT PRJ_Num FROM project WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Project Number: " + results.getInt("PRJ_Num"));
                                                System.out.println("Enter new Project Number:");
                                                // Input try/catch
                                                do {
                                                    try {
                                                        newProjectNum = Input.nextInt();
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please Enter a Valid Integer.");
                                                        Input.nextLine();
                                                    }
                                                } while (newProjectNum <= 0);
                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE project SET PRJ_Num='"+newProjectNum+"' WHERE PRJ_Num='"+proNum+"'");
                                                rowsAffected = statement.executeUpdate("UPDATE customer SET PRJ_Num='"+newProjectNum+"' WHERE PRJ_Num='"+proNum+"'");
                                                rowsAffected = statement.executeUpdate("UPDATE architect SET PRJ_Num='"+newProjectNum+"' WHERE PRJ_Num='"+proNum+"'");
                                                rowsAffected = statement.executeUpdate("UPDATE contractor SET PRJ_Num='"+newProjectNum+"' WHERE PRJ_Num='"+proNum+"'");
                                                rowsAffected = statement.executeUpdate("UPDATE finances SET PRJ_Num='"+newProjectNum+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Project Number Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 2:
                                                // Change projectName
                                                String newProjectName;

                                                // Statement
                                                results = statement.executeQuery("SELECT PRJ_Name FROM project WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Project Name: " + results.getString("PRJ_Name"));
                                                System.out.println("Enter new Project Name:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newProjectName = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE project SET PRJ_Name='"+newProjectName+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Project Name Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 3:
                                                // Change buildingType
                                                String newBuildingType;

                                                // Statement
                                                results = statement.executeQuery("SELECT Build_Type FROM project WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Building Type: " + results.getString("Build_Type"));
                                                System.out.println("Enter new Building Type:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newBuildingType = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE project SET Build_Type='"+newBuildingType+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Building Type Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 4:
                                                // Change address
                                                String newAddress;

                                                // Statement
                                                results = statement.executeQuery("SELECT PRJ_Add FROM project WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Project Address: " + results.getString("PRJ_Add"));
                                                System.out.println("Enter new Project Address:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newAddress = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE project SET PRJ_Add='"+newAddress+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Project Address Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 5:
                                                // Change ERFNum
                                                int newERFNum = 0;

                                                // Statement
                                                results = statement.executeQuery("SELECT ERF_Num FROM project WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current ERF Number: " + results.getInt("ERF_Num"));
                                                System.out.println("Enter new ERF Number:");
                                                // Input try/catch
                                                do {
                                                    try {
                                                        newERFNum = Input.nextInt();
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please Enter a Valid Integer.");
                                                        Input.nextLine();
                                                    }
                                                } while (newERFNum <= 0);

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE project SET ERF_Num='"+newERFNum+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Project ERF Number Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 6:
                                                // Change cost
                                                double newFeesCost = 0;

                                                // Statement
                                                results = statement.executeQuery("SELECT Fees_Cost FROM finances WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Project Cost: " + results.getDouble("Fees_Cost"));
                                                System.out.println("Enter new Project Cost:");
                                                // Input try/catch
                                                do {
                                                    try {
                                                        newFeesCost = Input.nextDouble();
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please Enter a Valid Amount.");
                                                        Input.nextLine();
                                                    }
                                                } while (newFeesCost <= 0);
                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE finances SET Fees_Cost='"+newFeesCost+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Project Cost Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 7:
                                                // Change paid
                                                double newFeesPaid = 0;

                                                // Statement
                                                results = statement.executeQuery("SELECT Fees_Paid FROM finances WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Amount Paid to Date: " + results.getDouble("Fees_Paid"));
                                                System.out.println("Enter new Amount Paid to Date:");
                                                // Input try/catch
                                                do {
                                                    try {
                                                        newFeesPaid = Input.nextDouble();
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please Enter a Valid Amount.");
                                                        Input.nextLine();
                                                    }
                                                } while (newFeesPaid <= 0);
                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE finances SET Fees_Paid='"+newFeesPaid+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Project Amount Paid to Date Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 8:
                                                // Change deadline
                                                String newDeadline = "";
                                                boolean valid = false;

                                                // Statement
                                                results = statement.executeQuery("SELECT Deadline FROM project WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Project Deadline: " + results.getString("Deadline"));
                                                System.out.println("Enter new Project Deadline:");

                                                // Input
                                                Input.nextLine(); // Fix skip error

                                                do {
                                                    try{
                                                        newDeadline = Input.nextLine();
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                        System.out.println(sdf.parse(newDeadline));
                                                        valid = true;
                                                    } catch (ParseException e) {
                                                        System.out.println("Please Enter a Valid Date.");
                                                    }
                                                } while (!valid);

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE project SET Deadline='"+newDeadline+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Project Deadline Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 9:
                                                // Change Status
                                                int newStatusSelect = 0;
                                                String complete = "Complete";
                                                String incomplete = "Incomplete";

                                                // Statement
                                                results = statement.executeQuery("SELECT Status FROM project WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Project Status: " + results.getString("Status"));

                                                // Change to Complete
                                                if (results.getString("Status").equals("Incomplete")) {
                                                    System.out.println("Change Project Status to Complete?" +
                                                            "\n" +
                                                            "\n1 - Yes" +
                                                            "\n0 - Back to Main Menu");
                                                    do {
                                                        try {
                                                            select = Input.nextInt();
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Please Enter a Valid Integer.");
                                                            Input.nextLine();
                                                        }
                                                    } while (select < 0);

                                                    switch (select) {
                                                        case 1:
                                                            // Update
                                                            rowsAffected = statement.executeUpdate("UPDATE project SET Status='"+complete+"' WHERE PRJ_Num='"+proNum+"'");
                                                            break;
                                                        case 0:
                                                            break;
                                                    }

                                                    // Change to Incomplete
                                                } else if (results.getString("Status").equals("Complete")) {
                                                    System.out.println("Change Project Status to Incomplete?" +
                                                            "\n" +
                                                            "\n1 - Yes" +
                                                            "\n0 - Back to Main Menu");
                                                    do {
                                                        try {
                                                            select = Input.nextInt();
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Please Enter a Valid Integer.");
                                                            Input.nextLine();
                                                        }
                                                    } while (select < 0);

                                                    switch (select) {
                                                        case 1:
                                                            // Update
                                                            rowsAffected = statement.executeUpdate("UPDATE project SET Status='"+incomplete+"' WHERE PRJ_Num='"+proNum+"'");
                                                            break;
                                                        case 0:
                                                            break;
                                                    }
                                                }

                                                System.out.println("Project Status Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 0:
                                                // back
                                                back = true;
                                                break;
                                            default:
                                                System.out.println("Invalid selection.");
                                        }
                                    } while (!back);

                                    break;
                                case 2:
                                    // Edit Customer Details
                                    contactEditMenu();
                                    // Input try/catch
                                    do {
                                        try {
                                            select = Input.nextInt();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Please Enter a Valid Integer.");
                                            Input.nextLine();
                                        }
                                    } while (select < 0);
                                    do {
                                        switch (select) {
                                            case 1:
                                                // Change Customer Name
                                                String newName;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cust_Name FROM customer WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Customer Name: " + results.getString("Cust_Name"));
                                                System.out.println("Enter new Customer Name:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newName = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE customer SET Cust_Name='"+newName+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Customer Name Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 2:
                                                // Change Customer Surname
                                                String newSurname;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cust_Sname FROM customer WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Customer Surname: " + results.getString("Cust_Sname"));
                                                System.out.println("Enter new Customer Surname:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newSurname = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE customer SET Cust_Sname='"+newSurname+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Customer Surname Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 3:
                                                // Change Customer Contact
                                                int newContact = 0;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cust_Con FROM customer WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Customer Contact: " + results.getInt("Cust_Con"));
                                                System.out.println("Enter new Customer Contact:");
                                                // Input try/catch
                                                do {
                                                    try {
                                                        newContact = Input.nextInt();
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please Enter a Valid Contact.");
                                                        Input.nextLine();
                                                    }
                                                } while (newContact <= 0);

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE customer SET Cust_Con='"+newContact+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Customer Contact Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 4:
                                                // Change Customer Email
                                                String newEmail;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cust_Email FROM customer WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Customer Email: " + results.getString("Cust_Email"));
                                                System.out.println("Enter new Customer Email:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newEmail = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE customer SET Cust_Email='"+newEmail+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Customer Email Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 5:
                                                // Change Customer Address
                                                String newAddress;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cust_Add FROM customer WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Customer Address: " + results.getString("Cust_Add"));
                                                System.out.println("Enter new Customer Address:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newAddress = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE customer SET Cust_Add='"+newAddress+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Customer Address Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 0:
                                                // back
                                                back = true;
                                                break;
                                            default:
                                                System.out.println("Invalid selection.");
                                        }
                                    } while (!back);

                                    break;
                                case 3:
                                    // Edit Architect Details
                                    contactEditMenu();
                                    // Input try/catch
                                    do {
                                        try {
                                            select = Input.nextInt();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Please Enter a Valid Integer.");
                                            Input.nextLine();
                                        }
                                    } while (select < 0);
                                    do {
                                        switch (select) {
                                            case 1:
                                                // Change Architect Name
                                                String newName;

                                                // Statement
                                                results = statement.executeQuery("SELECT Arch_Name FROM architect WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Architect Name: " + results.getString("Arch_Name"));
                                                System.out.println("Enter new Architect Name:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newName = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE architect SET Arch_Name='"+newName+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Architect Name Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 2:
                                                // Change Architect Surname
                                                String newSurname;

                                                // Statement
                                                results = statement.executeQuery("SELECT Arch_Sname FROM architect WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Architect Surname: " + results.getString("Arch_Sname"));
                                                System.out.println("Enter new Architect Surname:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newSurname = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE architect SET Arch_Sname='"+newSurname+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Architect Surname Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 3:
                                                // Change Architect Contact
                                                int newContact = 0;

                                                // Statement
                                                results = statement.executeQuery("SELECT Arch_Con FROM architect WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Architect Contact: " + results.getInt("Arch_Con"));
                                                System.out.println("Enter new Architect Contact:");
                                                // Input try/catch
                                                do {
                                                    try {
                                                        newContact = Input.nextInt();
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please Enter a Valid Contact.");
                                                        Input.nextLine();
                                                    }
                                                } while (newContact <= 0);

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE architect SET Arch_Con='"+newContact+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Architect Contact Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 4:
                                                // Change Architect Email
                                                String newEmail;

                                                // Statement
                                                results = statement.executeQuery("SELECT Arch_Email FROM architect WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Architect Email: " + results.getString("Arch_Email"));
                                                System.out.println("Enter new Architect Email:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newEmail = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE architect SET Arch_Email='"+newEmail+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Architect Email Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 5:
                                                // Change Architect Address
                                                String newAddress;

                                                // Statement
                                                results = statement.executeQuery("SELECT Arch_Add FROM architect WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Architect Address: " + results.getString("Arch_Add"));
                                                System.out.println("Enter new Architect Address:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newAddress = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE architect SET Arch_Add='"+newAddress+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Architect Address Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 0:
                                                // back
                                                back = true;
                                                break;
                                            default:
                                                System.out.println("Invalid selection.");
                                        }
                                    } while (!back);

                                    break;
                                case 4:
                                    // Edit Contractor Details
                                    contactEditMenu();
                                    // Input try/catch
                                    do {
                                        try {
                                            select = Input.nextInt();
                                        } catch (InputMismatchException e) {
                                            System.out.println("Please Enter a Valid Integer.");
                                            Input.nextLine();
                                        }
                                    } while (select < 0);
                                    do {
                                        switch (select) {
                                            case 1:
                                                // Change Contractor Name
                                                String newName;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cont_Name FROM contractor WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Contractor Name: " + results.getString("Cont_Name"));
                                                System.out.println("Enter new Contractor Name:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newName = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE contractor SET Cont_Name='"+newName+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Contractor Name Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 2:
                                                // Change Contractor Surname
                                                String newSurname;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cont_Sname FROM contractor WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Contractor Surname: " + results.getString("Cont_Sname"));
                                                System.out.println("Enter new Contractor Surname:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newSurname = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE contractor SET Cont_Sname='"+newSurname+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Contractor Surname Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 3:
                                                // Change Contractor Contact
                                                int newContact = 0;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cont_Con FROM contractor WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Contractor Contact: " + results.getInt("Cont_Con"));
                                                System.out.println("Enter new Contractor Contact:");
                                                // Input try/catch
                                                do {
                                                    try {
                                                        newContact = Input.nextInt();
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please Enter a Valid Contact.");
                                                        Input.nextLine();
                                                    }
                                                } while (newContact <= 0);

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE contractor SET Cont_Con='"+newContact+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Contractor Contact Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 4:
                                                // Change Contractor Email
                                                String newEmail;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cont_Email FROM contractor WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Contractor Email: " + results.getString("Cont_Email"));
                                                System.out.println("Enter new Contractor Email:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newEmail = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE contractor SET Cont_Email='"+newEmail+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Contractor Email Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 5:
                                                // Change Contractor Address
                                                String newAddress;

                                                // Statement
                                                results = statement.executeQuery("SELECT Cont_Add FROM contractor WHERE PRJ_Num='"+proNum+"'");

                                                results.next(); // fix cursor

                                                System.out.println("Current Contractor Address: " + results.getString("Cont_Add"));
                                                System.out.println("Enter new Contractor Address:");

                                                // Input
                                                Input.nextLine(); // Fix skip error
                                                newAddress = Input.nextLine();

                                                // Update
                                                rowsAffected = statement.executeUpdate("UPDATE contractor SET Cont_Add='"+newAddress+"' WHERE PRJ_Num='"+proNum+"'");
                                                System.out.println("Contractor Address Successfully Updated.");

                                                results.close();
                                                back = true;
                                                break;
                                            case 0:
                                                // back
                                                back = true;
                                                break;
                                            default:
                                                System.out.println("Invalid selection.");
                                        }
                                    } while (!back);

                                    break;
                                case 0:
                                    back = true;
                                    break;
                                default:
                                    System.out.println("Invalid selection.");
                            }
                        } while (!back);

                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid selection.");
                }
            } while (!back);
        }
    }

    public static void updatePayment(Statement statement) throws SQLException {

        // Find
        String searchValue;
        Scanner Input = new Scanner(System.in);

        int proNum = 0;
        boolean found = false;
        boolean back = false;

        do {

            System.out.println("\nSearch by:" +
                    "\n1 - Project Number" +
                    "\n2 - Project Name" +
                    "\n0 - Back");
            int select = Input.nextInt();
            Input.nextLine(); // Fix input skip error

            switch (select) {
                case 1:
                    System.out.println("\nEnter a Project Number:");
                    int searchNum = 0;

                    do {
                        try {
                            searchNum = Input.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Please Enter a Valid Integer.");
                            Input.nextLine();
                        }
                    } while (searchNum <= 0);

                    // Statement
                    ResultSet results = statement.executeQuery("SELECT * FROM project");

                    while (results.next()) {
                        proNum = results.getInt("PRJ_Num");
                        if (proNum == searchNum) {
                            System.out.println("Project Found");
                            found = true;
                            back = true;
                            break;
                        } else
                            System.out.println("Project not found");
                    }
                    results.close();
                    break;
                case 2:
                    System.out.println("\nEnter a Project Name:");
                    String searchName = Input.nextLine();

                    // Statement
                    results = statement.executeQuery("SELECT * FROM project");

                    while (results.next()) {
                        searchValue = results.getString("PRJ_Name");
                        if (searchValue.equals(searchName)) {
                            System.out.println("Project Found");
                            proNum = results.getInt("PRJ_Num");
                            found = true;
                            back = true;
                            break;
                        } else
                            System.out.println("Project not found");
                    }
                    results.close();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        } while (!back);

        int rowsAffected;

        // Display & Input of new payment value
        if (found) {

            // Display
            // Project
            System.out.println("Project Details\n");

            // Statement
            ResultSet results = statement.executeQuery("SELECT * FROM project WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Project Number: " + results.getInt("PRJ_Num") + "\n" +
                        "Project Name: " + results.getString("PRJ_Name") + "\n" +
                        "Building Type: " + results.getString("Build_Type") + "\n" +
                        "Project Address: " + results.getString("PRJ_Add") + "\n" +
                        "ERF Number: " + results.getInt("ERF_Num") + "\n" +
                        "Deadline: " + results.getString("Deadline") + "\n" +
                        "Status: " + results.getString("Status") + "\n");
            }
            results.close();

            // Customer
            System.out.println("Customer Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM customer WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Customer Name: " + results.getString("Cust_Name") + " " + results.getString("Cust_Sname") + "\n" +
                        "Customer Contact: " + results.getInt("Cust_Con") + "\n" +
                        "Customer Email: " + results.getString("Cust_Email") + "\n" +
                        "Customer Address: " + results.getString("Cust_Add") + "\n");
            }
            results.close();

            // Fiances
            System.out.println("Financial Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM finances WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Total Cost of Project: R" + results.getDouble("Fees_Cost") + "\n" +
                        "Total Paid by Customer: R" + results.getDouble("Fees_paid") + "\n" +
                        "Amount Due: R" + (results.getDouble("Fees_Cost") - results.getDouble("Fees_paid")) + "\n");
            }
            results.close();

            // Architect
            System.out.println("Architect Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM architect WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Architect Name: " + results.getString("Arch_Name") + " " + results.getString("Arch_Sname") + "\n" +
                        "Architect Contact: " + results.getInt("Arch_Con") + "\n" +
                        "Architect Email: " + results.getString("Arch_Email") + "\n" +
                        "Architect Address: " + results.getString("Arch_Add") + "\n");
            }
            results.close();

            // Contractor
            System.out.println("Contractor Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM contractor WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Contractor Name: " + results.getString("Cont_Name") + " " + results.getString("Cont_Sname") + "\n" +
                        "Contractor Contact: " + results.getInt("Cont_Con") + "\n" +
                        "Contractor Email: " + results.getString("Cont_Email") + "\n" +
                        "Contractor Address: " + results.getString("Cont_Add") + "\n");
            }
            results.close();

            System.out.println("Enter additional payment amount made by Customer:");

            double amount = 0;
            double addAmount = 0;

            // Input error try block
            do {
                try {
                    amount = Input.nextDouble();
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid amount.");
                    Input.nextLine();
                }
            } while (amount < 0);

            // Statement
            results = statement.executeQuery("SELECT * FROM finances WHERE PRJ_Num='"+proNum+"'");

            results.next(); // fix cursor

            // New full amount paid
            addAmount = results.getDouble("Fees_Paid") + amount;

            // Update
            rowsAffected = statement.executeUpdate("UPDATE finances SET Fees_Paid='"+addAmount+"' WHERE PRJ_Num='"+proNum+"'");

            System.out.println("Amount successfully added to account.");

            results.close();
        }
    }

    public static void viewIncomplete(Statement statement) throws SQLException {

        System.out.println("All Incomplete Projects:\n");

        // Statement
        ResultSet results = statement.executeQuery("SELECT * FROM project WHERE Status='Incomplete'");
        // Loop over the results, printing them all.
        while (results.next()) {
            int proNum = results.getInt("PRJ_Num");
            System.out.println("Project Number: " + results.getInt("PRJ_Num") + "\n" +
                    "Project Name: " + results.getString("PRJ_Name") + "\n" +
                    "Building Type: " + results.getString("Build_Type") + "\n" +
                    "Project Address: " + results.getString("PRJ_Add") + "\n" +
                    "ERF Number: " + results.getInt("ERF_Num") + "\n" +
                    "Deadline: " + results.getString("Deadline") + "\n" +
                    "Status: " + results.getString("Status") + "\n");
        }
        results.close();
    }

    public static void overdueProjects(Statement statement) throws SQLException {

        System.out.println("All Overdue Projects:\n");

        // Statement
        ResultSet results = statement.executeQuery("SELECT * FROM project");

        // Loop over the results, printing them all.
        while (results.next()) {

            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date deadlineCheck = (sdf.parse(results.getString("Deadline")));
                String statusCheck = results.getString("Status");
                // Check overdue
                if (deadlineCheck.before(sdf.parse(String.valueOf(LocalDate.now()))) && statusCheck.equals("Incomplete")) {
                    System.out.println("Project Number: " + results.getInt("PRJ_Num") + "\n" +
                            "Project Name: " + results.getString("PRJ_Name") + "\n" +
                            "Building Type: " + results.getString("Build_Type") + "\n" +
                            "Project Address: " + results.getString("PRJ_Add") + "\n" +
                            "ERF Number: " + results.getInt("ERF_Num") + "\n" +
                            "Deadline: " + results.getString("Deadline") + "\n" +
                            "Status: " + results.getString("Status") + "\n");
                    }
                // Overdue, but marked as complete - prompt that the project needs to be looked at
                else if (deadlineCheck.before(sdf.parse(String.valueOf(LocalDate.now()))) && statusCheck.equals("Complete")){
                    System.out.println("Project Number: " + results.getInt("PRJ_Num") + "\n" +
                            "Project Name: " + results.getString("PRJ_Name") + "\n" +
                            "Building Type: " + results.getString("Build_Type") + "\n" +
                            "Project Address: " + results.getString("PRJ_Add") + "\n" +
                            "ERF Number: " + results.getInt("ERF_Num") + "\n" +
                            "Deadline: " + results.getString("Deadline") + "\n" +
                            "Status: " + results.getString("Status") + "\n" +
                            "\n===Project Number " + results.getString(1) + " needs to be finalized or invoiced===\n");
                }
            } catch (ParseException e) {
                System.out.println("Error.");
            }
        }
        results.close();
    }

    public static void finaliseProject(Statement statement) throws SQLException {

        // Find
        String searchValue;
        Scanner Input = new Scanner(System.in);

        int proNum = 0;
        boolean found = false;
        boolean back = false;

        do {

            System.out.println("\nSearch by:" +
                    "\n1 - Project Number" +
                    "\n2 - Project Name" +
                    "\n0 - Back");
            int select = Input.nextInt();
            Input.nextLine(); // Fix input skip error

            switch (select) {
                case 1:
                    System.out.println("\nEnter a Project Number:");
                    int searchNum = 0;

                    do {
                        try {
                            searchNum = Input.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Please Enter a Valid Integer.");
                            Input.nextLine();
                        }
                    } while (searchNum <= 0);

                    // Statement
                    ResultSet results = statement.executeQuery("SELECT * FROM project");

                    while (results.next()) {
                        proNum = results.getInt("PRJ_Num");
                        if (proNum == searchNum) {
                            System.out.println("Project Found");
                            found = true;
                            back = true;
                            break;
                        } else
                            System.out.println("Project not found");
                    }
                    results.close();
                    break;
                case 2:
                    System.out.println("\nEnter a Project Name:");
                    String searchName = Input.nextLine();

                    // Statement
                    results = statement.executeQuery("SELECT * FROM project");

                    while (results.next()) {
                        searchValue = results.getString("PRJ_Name");
                        if (searchValue.equals(searchName)) {
                            System.out.println("Project Found");
                            proNum = results.getInt("PRJ_Num");
                            found = true;
                            back = true;
                            break;
                        } else
                            System.out.println("Project not found");
                    }
                    results.close();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        } while (!back);

        back = false;
        int rowsAffected;
        int select = 0;

        if (found) {

            // Display
            // Project
            System.out.println("Project to be Finalised OR Invoiced\n");

            // Statement
            ResultSet results = statement.executeQuery("SELECT * FROM project WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Project Number: " + results.getInt("PRJ_Num") + "\n" +
                        "Project Name: " + results.getString("PRJ_Name") + "\n" +
                        "Building Type: " + results.getString("Build_Type") + "\n" +
                        "Project Address: " + results.getString("PRJ_Add") + "\n" +
                        "ERF Number: " + results.getInt("ERF_Num") + "\n" +
                        "Deadline: " + results.getString("Deadline") + "\n" +
                        "Status: " + results.getString("Status") + "\n");
            }
            results.close();

            // Customer
            System.out.println("Customer Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM customer WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Customer Name: " + results.getString("Cust_Name") + " " + results.getString("Cust_Sname") + "\n" +
                        "Customer Contact: " + results.getInt("Cust_Con") + "\n" +
                        "Customer Email: " + results.getString("Cust_Email") + "\n" +
                        "Customer Address: " + results.getString("Cust_Add") + "\n");
            }
            results.close();

            // Fiances
            System.out.println("Financial Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM finances WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Total Cost of Project: R" + results.getDouble("Fees_Cost") + "\n" +
                        "Total Paid by Customer: R" + results.getDouble("Fees_paid") + "\n" +
                        "Amount Due: R" + (results.getDouble("Fees_Cost") - results.getDouble("Fees_paid")) + "\n");
            }
            results.close();

            // Architect
            System.out.println("Architect Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM architect WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Architect Name: " + results.getString("Arch_Name") + " " + results.getString("Arch_Sname") + "\n" +
                        "Architect Contact: " + results.getInt("Arch_Con") + "\n" +
                        "Architect Email: " + results.getString("Arch_Email") + "\n" +
                        "Architect Address: " + results.getString("Arch_Add") + "\n");
            }
            results.close();

            // Contractor
            System.out.println("Contractor Details\n");

            // Statement
            results = statement.executeQuery("SELECT * FROM contractor WHERE PRJ_Num='"+proNum+"'");

            // Loop over the results, printing them all.
            while (results.next()) {
                System.out.println("Contractor Name: " + results.getString("Cont_Name") + " " + results.getString("Cont_Sname") + "\n" +
                        "Contractor Contact: " + results.getInt("Cont_Con") + "\n" +
                        "Contractor Email: " + results.getString("Cont_Email") + "\n" +
                        "Contractor Address: " + results.getString("Cont_Add") + "\n");
            }

            results.close();

            do {

                System.out.println("\nContinue:" +
                        "\n1 - Yes" +
                        "\n0 - No");
                select = Input.nextInt();
                Input.nextLine(); // Fix input skip error

                switch (select) {
                    case 1:

                        // Statement
                        results = statement.executeQuery("SELECT * FROM finances WHERE PRJ_Num='"+proNum+"'");

                        results.next(); // fix cursor

                        if (results.getDouble("Fees_Paid") < results.getDouble("Fees_Cost")) {

                            results.close();

                            // Customer Details
                            // Statement
                            results = statement.executeQuery("SELECT * FROM customer WHERE PRJ_Num='"+proNum+"'");

                            results.next(); // fix cursor

                            System.out.println("***Invoice***\n" +
                                    "\nCustomer Details:" +
                                    "\nCustomer Name: " + results.getString("Cust_Name") +
                                    " " + results.getString("Cust_Sname") +
                                    "\nCustomer Contact: " + results.getInt("Cust_Con") +
                                    "\nCustomer Email: " + results.getString("Cust_Email") +
                                    "\nCustomer Address: " + results.getString("Cust_Add"));

                            results.close();
                            // Amount Due
                            // Statement
                            results = statement.executeQuery("SELECT * FROM finances WHERE PRJ_Num='"+proNum+"'");

                            results.next(); // fix cursor

                            System.out.println("Amount Due: R" + (results.getDouble("Fees_Cost") - results.getDouble("Fees_paid")) + "\n");

                            results.close();
                            back = true;
                            break;
                        } else if (results.getDouble("Fees_Paid") == results.getDouble("Fees_Cost")) {

                            results.close();

                            // Statement
                            // Project
                            results = statement.executeQuery("SELECT * FROM project WHERE PRJ_Num='" + proNum + "'");

                            results.next(); // fix cursor

                            LocalDate finalisedDate = LocalDate.now();

                            String archive = ("Archived: " + finalisedDate.toString() + "\n" +
                                    "Project Details:" + "\n" +
                                    "Project Number: " + results.getInt("PRJ_Num") + "\n" +
                                    "Project Name: " + results.getString("PRJ_Name") + "\n" +
                                    "Building Type: " + results.getString("Build_Type") + "\n" +
                                    "Project Address: " + results.getString("PRJ_Add") + "\n" +
                                    "ERF Number: " + results.getInt("ERF_Num") + "\n" +
                                    "Deadline: " + results.getString("Deadline") + "\n" +
                                    "Status: Complete" + "\n");

                            results.close();

                            // Statement
                            // Finances
                            results = statement.executeQuery("SELECT * FROM finances WHERE PRJ_Num='" + proNum + "'");

                            results.next(); // fix cursor

                            archive += ("Financial Details:\n" +
                                    "Project Cost: " + results.getDouble("Fees_Cost") + "\n" +
                                    "Amount Paid by Customer: " + results.getDouble("Fees_Paid") + "\n");

                            results.close();

                            // Statement
                            // Customer
                            results = statement.executeQuery("SELECT * FROM customer WHERE PRJ_Num='" + proNum + "'");

                            results.next(); // fix cursor

                            archive += ("Customer Details:\n" +
                                    "Customer Name: " + results.getString("Cust_Name") + " " + results.getString("Cust_Sname") + "\n" +
                                    "Customer Contact: " + results.getInt("Cust_Con") + "\n" +
                                    "Customer Email: " + results.getString("Cust_Email") + "\n" +
                                    "Customer Address: " + results.getString("Cust_Add") + "\n");

                            results.close();

                            // Statement
                            // Architect
                            results = statement.executeQuery("SELECT * FROM architect WHERE PRJ_Num='" + proNum + "'");

                            results.next(); // fix cursor

                            archive += ("Architect Details:\n" +
                                    "Architect Name: " + results.getString("Arch_Name") + " " + results.getString("Arch_Sname") + "\n" +
                                    "Architect Contact: " + results.getInt("Arch_Con") + "\n" +
                                    "Architect Email: " + results.getString("Arch_Email") + "\n" +
                                    "Architect Address: " + results.getString("Arch_Add") + "\n");

                            results.close();

                            // Statement
                            // Contractor
                            results = statement.executeQuery("SELECT * FROM contractor WHERE PRJ_Num='" + proNum + "'");

                            results.next(); // fix cursor

                            archive += ("Contractor Details:\n" +
                                    "Contractor Name: " + results.getString("Cont_Name") + " " + results.getString("Cont_Sname") + "\n" +
                                    "Contractor Contact: " + results.getInt("Cont_Con") + "\n" +
                                    "Contractor Email: " + results.getString("Cont_Email") + "\n" +
                                    "Contractor Address: " + results.getString("Cont_Add") + "\n");

                            results.close();

                            // Add to archive
                            rowsAffected = statement.executeUpdate("INSERT INTO archive VALUES ('"+archive+"')");

                            // Delete from tables
                            rowsAffected = statement.executeUpdate("DELETE FROM project WHERE PRJ_Num='"+proNum+"'");
                            rowsAffected = statement.executeUpdate("DELETE FROM finances WHERE PRJ_Num='"+proNum+"'");
                            rowsAffected = statement.executeUpdate("DELETE FROM customer WHERE PRJ_Num='"+proNum+"'");
                            rowsAffected = statement.executeUpdate("DELETE FROM architect WHERE PRJ_Num='"+proNum+"'");
                            rowsAffected = statement.executeUpdate("DELETE FROM contractor WHERE PRJ_Num='"+proNum+"'");

                            System.out.println("Project Successfully Archived.");

                        } else if (results.getDouble("Fees_Paid") > results.getDouble("Fees_Cost")) {
                            System.out.println("Customer has a credit balance of: R" +
                                    (results.getDouble("Fees_Paid") - results.getDouble("Fees_Cost")) +
                                    "\nPlease review.\n");
                        }

                        results.close();
                        back = true;
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid selection.");
                }
            } while (!back);
        }
    }

    public static void viewArchive(Statement statement) throws SQLException {

        System.out.println("All Archived Projects:\n");

        // Statement
        ResultSet results = statement.executeQuery("SELECT * FROM archive");

        // Loop over the results, printing them all.
        while (results.next()) {
            System.out.println(results.getString("Finalized") + "\n");
        }
        results.close();
    }
}