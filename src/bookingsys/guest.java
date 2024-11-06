package bookingsys;

import java.util.Scanner;

public class guest {

    // Method to handle menu-driven CRUD operations
    public void manageGuests() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();
        int choice;
        guest g = new guest();
        // Loop to display the menu and perform actions based on user choice
        do {
            // Display menu options
            System.out.println("\nGuest Management Menu");
            System.out.println("1. Add Guest");
            System.out.println("2. View Guests");
            System.out.println("3. Update Guest");
            System.out.println("4. Delete Guest");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            // Switch-case to handle different CRUD operations
            switch (choice) {
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
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 5);  // Continue until user chooses to exit
    }

    // Add a new guest to the system
    private void addGuest() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();

        System.out.print("Guest first name: ");
        String fname = sc.next();
        System.out.print("Guest last name: ");
        String lname = sc.next();
        System.out.print("Enter contact number: ");
        String cnum = sc.next();
        System.out.print("Enter Email: ");
        String email = sc.next();

        String sql = "INSERT INTO tbl_guest(g_fname, g_lname, g_cnum, g_email) "
                + "VALUES (?, ?, ?, ?)";
        
        conf.addRecord(sql, fname, lname, cnum, email);
    }

    // View all guests
    private void viewGuest() {
        String cqry = "SELECT * FROM tbl_guest";
        String[] Headers = {"ID", "First Name", "Last Name", "Contact Number", "Email"};
        String[] Columns = {"g_id", "g_fname", "g_lname", "g_cnum", "g_email"};
        Config conf = new Config();
        conf.viewRecords(cqry, Headers, Columns);
    }

  private void updateGuest() {
    Config conf = new Config();
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter guest ID to update: ");
    int id = sc.nextInt();
    
    // Check if the ID exists (Assuming getSingleValue returns a double)
    while (conf.getSingleValue("SELECT g_id FROM tbl_guest WHERE g_id = ?", id) <= 0) {
        System.out.println("Selection ID doesn't exist.");
        System.out.print("Select Guest ID again: ");
        id = sc.nextInt(); // Reusing the same 'id' variable without redeclaring it
    }

    System.out.print("Enter the new first name: ");
    String fname = sc.next();
    System.out.print("Enter the new last name: ");
    String lname = sc.next();
    System.out.print("Enter the new contact number: ");
    String cnum = sc.next();
    System.out.print("Enter the new Email: ");
    String email = sc.next();

    String qry = "UPDATE tbl_guest SET g_fname = ?, g_lname = ?, g_cnum = ?, g_email = ? WHERE g_id = ?";
    conf.updateRecord(qry, fname, lname, cnum, email, id);
}

    // Delete an existing guest
    private void deleteGuest() {
        Scanner sc = new Scanner(System.in);
       Config conf = new Config();

        System.out.print("Enter guest ID to delete: ");
        int id = sc.nextInt();

          // Check if the ID exists (Assuming getSingleValue returns a double)
        while (conf.getSingleValue("SELECT g_id FROM tbl_guest WHERE g_id = ?", id) <= 0) {
            System.out.println("Selection ID doesn't exist.");
            System.out.print("Select Guest ID again: ");
            id = sc.nextInt(); // Reusing the same 'id' variable without redeclaring it
        }
        String sqlDelete = "DELETE FROM tbl_guest WHERE g_id = ?";
       
        conf.deleteRecord(sqlDelete, id);
    }
}
