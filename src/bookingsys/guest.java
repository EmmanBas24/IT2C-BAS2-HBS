package bookingsys;

import java.util.Scanner;
import java.util.regex.Pattern;

public class guest {

      // Method to handle menu-driven CRUD operations
    public void manageGuests() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();
        String response;
        guest g = new guest();
        MAIN ss = new MAIN();
        int option = -1; // Initialize option to an invalid value

        do {
            System.out.println("");
            System.out.println("==============================");
            System.out.println("|     Guest Records Menu     |");
            System.out.println("==============================");
            System.out.println("| 1. Create Guest Profile     |");
            System.out.println("| 2. View Guest Profiles      |");
            System.out.println("| 3. Update Guest Profile     |");
            System.out.println("| 4. Delete Guest Profile     |");
            System.out.println("| 5. Return to Main Menu      |");
            System.out.println("==============================");

            // Input validation for option
            while (true) {
                System.out.print("Enter your choice: ");
                if (sc.hasNextInt()) {
                    option = sc.nextInt();
                    if (option >= 1 && option <= 5) {
                        break; // Valid option
                    } else {
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    sc.next(); // Clear the invalid input
                   
                }
            }

            // Switch-case to handle different CRUD operations
            switch (option) {
                case 1:
                    g.addGuest();
                    g.viewGuest();
                    break;
                case 2:
                    g.viewGuest();
                    break;
                case 3:
                    g.viewGuest();
                    g.updateGuest();
                    break;
                case 4:
                    g.viewGuest();
                    g.deleteGuest();
                    break;
                case 5:
                    System.out.println("");
                    System.out.println("Exiting Manage Guest Information...");
                    System.out.println("");
                    ss.main(new String[]{}); // Main menu
                    break;
                default:
                    // This default case won't be reached because of the validation above
                    break;
            }

            // Validating the response for going back
            while (true) {
                System.out.print("Do you want to go back? (yes/no): ");
                response = sc.next();
                if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {
                    break; // Exit loop if valid response
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        } while (response.equalsIgnoreCase("yes"));  // Continue until user chooses "no"
        System.out.println("");
        System.out.println("Exiting Manage Guest information...");
        System.out.println("");
    }

    // Add a new guest to the system with input validation
    private void addGuest() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();

        String fname = "";
        String lname = "";
        String cnum = "";
        String email = "";

        // Name validation
        while (true) {
            System.out.print("Guest first name: ");
            fname = sc.next();
            if (isValidName(fname)) break;
            else System.out.println("Invalid first name. Only letters are allowed.");
        }

        while (true) {
            System.out.print("Guest last name: ");
            lname = sc.next();
            if (isValidName(lname)) break;
            else System.out.println("Invalid last name. Only letters are allowed.");
        }

        // Contact number validation (only numbers, 10 digits)
        while (true) {
            System.out.print("Enter contact number: ");
            cnum = sc.next();
            if (isValidContactNumber(cnum)) break;
            else System.out.println("Invalid contact number. It should contain 11 digits.");
        }

        // Email validation
        while (true) {
            System.out.print("Enter Email: ");
            email = sc.next();
            if (isValidEmail(email)) break;
            else System.out.println("Invalid email format.");
        }

        String sql = "INSERT INTO tbl_guest(g_fname, g_lname, g_cnum, g_email) "
                + "VALUES (?, ?, ?, ?)";

        conf.addRecord(sql, fname, lname, cnum, email);
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    private boolean isValidContactNumber(String contact) {
        return contact.matches("\\d{11}");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    // View all guests
    public void viewGuest() {
        System.out.println("");
        String cqry = "SELECT * FROM tbl_guest";
        String[] Headers = {"ID", "First Name", "Last Name", "Contact Number", "Email"};
        String[] Columns = {"g_id", "g_fname", "g_lname", "g_cnum", "g_email"};
        viewConfig conf = new viewConfig();
        conf.viewGuest(cqry, Headers, Columns);
    }

  // Update guest information with ID validation
private void updateGuest() {
    Config conf = new Config();
    Scanner sc = new Scanner(System.in);

    int id = -1;

    // Guest ID validation: Ensure the ID is numeric and exists in the database
    while (id <= 0) {
        System.out.print("Enter guest ID to update: ");

        // Check if the input is an integer (rejects letters and special characters)
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid numeric ID.");
            sc.next(); // Consume the invalid input to avoid infinite loop
            continue;
        }

        // Read the valid integer input
        id = sc.nextInt();

        // Check if the ID exists in the database
        if (id <= 0 || conf.getSingleValue("SELECT COUNT(*) FROM tbl_guest WHERE g_id = ?", id) == 0) {
            System.out.println("Invalid ID or ID doesn't exist. Please enter a valid ID.");
            id = -1; // Reset id to force re-entry
        }
    }

    // Variables for new guest details
    String fname = "";
    String lname = "";
    String cnum = "";
    String email = "";

    // Name validation
    while (true) {
        System.out.print("Enter the new first name: ");
        fname = sc.next();
        if (isValidName(fname)) break;
        else System.out.println("Invalid first name. Only letters are allowed.");
    }

    while (true) {
        System.out.print("Enter the new last name: ");
        lname = sc.next();
        if (isValidName(lname)) break;
        else System.out.println("Invalid last name. Only letters are allowed.");
    }

    // Contact number validation
    while (true) {
        System.out.print("Enter the new contact number: ");
        cnum = sc.next();
        if (isValidContactNumber(cnum)) break;
        else System.out.println("Invalid contact number. It should contain 11 digits.");
    }

    // Email validation
    while (true) {
        System.out.print("Enter the new Email: ");
        email = sc.next();
        if (isValidEmail(email)) break;
        else System.out.println("Invalid email format.");
    }

    // SQL query to update the guest record
    String qry = "UPDATE tbl_guest SET g_fname = ?, g_lname = ?, g_cnum = ?, g_email = ? WHERE g_id = ?";
    conf.updateRecord(qry, fname, lname, cnum, email, id);

    System.out.println("Guest record updated successfully!");
}


   // Delete an existing guest with ID validation
private void deleteGuest() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();

    int id = -1;

    // Guest ID validation: Ensure the ID is numeric and exists in the database
    while (id <= 0) {
        System.out.print("Enter guest ID to delete: ");

        // Check if the input is an integer (rejects letters and special characters)
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid numeric ID.");
            sc.next(); // Consume the invalid input to avoid infinite loop
            continue;
        }

        // Read the valid integer input
        id = sc.nextInt();

        // Check if the ID exists in the database
        if (id <= 0 || conf.getSingleValue("SELECT g_id FROM tbl_guest WHERE g_id = ?", id) <= 0) {
            System.out.println("Invalid ID or ID doesn't exist. Please enter a valid ID.");
        }
    }

    String sqlDelete = "DELETE FROM tbl_guest WHERE g_id = ?";
    conf.deleteRecord(sqlDelete, id);
    System.out.println("Guest record deleted successfully!");
}

}