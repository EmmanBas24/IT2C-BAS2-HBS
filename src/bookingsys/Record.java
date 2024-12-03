package bookingsys;

import static bookingsys.Config.connectDB;
import java.sql.*;
import java.util.Scanner;

public class Record {

    public void displayMenu() {
        Config conf = new Config();
        Scanner sc = new Scanner(System.in);
        int choice;
        String response = null;

        do {
            System.out.println("");
            System.out.println("+--------------------------+");
            System.out.println("|      Booking Records     |");
            System.out.println("+--------------------------+");
            System.out.println("| 1. View General Records  |");
            System.out.println("| 2. View Specific Record  |");
            System.out.println("| 3. Return to Main menu   |");
            System.out.println("+--------------------------+");
            System.out.print("Enter your choice: ");
            
            // Validate if input is an integer for the menu choice
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number between 1 and 3.");
                sc.next(); // clear the invalid input
                System.out.print("Enter your choice: ");
            }
            choice = sc.nextInt();
            
            // Validate choice range
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice! Please enter a number between 1 and 3.");
                continue; // Prompt user again
            }

            switch (choice) {
                case 1:
                    viewGeneralRecords();
                    break;
                case 2:
                    viewSpecificRecord();
                    break;
                case 3:
                    System.out.println("");
                    System.out.println("Exiting Booking Records...");
                    System.out.println("");
                    return; 
                default:
                    System.out.println("Invalid choice, please try again.");
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
        System.out.println("Exiting Booking Records...");
        System.out.println("");
    }

    private void viewGeneralRecords() {
    viewConfig conf = new viewConfig();
    
    // Modify the query to only select bookings that are 'Booked'
    String query = "SELECT b.b_id, g.g_fname, g.g_lname, b.b_status " +
                   "FROM tbl_booking b " +
                   "JOIN tbl_guest g ON b.g_id = g.g_id " +
                   "WHERE b.b_status = 'Booked'";  // Filter bookings that are 'Booked'

    String[] headers = {"Booking ID", "First Name", "Last Name", "Booking Status"};
    String[] columns = {"b_id", "g_fname", "g_lname", "b_status"};

    conf.viewGeneralRecord(query, headers, columns);
}

    

    private void viewSpecificRecord() {
        Config conf = new Config();
        Scanner sc = new Scanner(System.in); 
        
        // Validate if input is an integer for Booking ID
        System.out.print("Enter Booking ID: ");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input! Please enter a valid integer for Booking ID.");
            sc.next();  // clear the invalid input
            System.out.print("Enter Booking ID: ");
        }
        int bookingId = sc.nextInt();
        
        // Validate Booking ID to ensure it's positive
        if (bookingId <= 0) {
            System.out.println("Booking ID must be a positive integer.");
            return; // Exit method if the booking ID is invalid
        }

        // Try to connect to the database and fetch the record
        try (Connection conn = conf.connectDB()) {
            String query = "SELECT b.b_id, b.b_checkin, b.b_checkout, b.b_status, b.b_payment_status, "
                         + "r.r_num, r.r_type, g.g_fname, g.g_lname, g.g_cnum, g.g_email, b.b_total_amount "
                         + "FROM tbl_booking b "
                         + "JOIN tbl_room r ON b.r_id = r.r_id "
                         + "JOIN tbl_guest g ON b.g_id = g.g_id "
                         + "WHERE b.b_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, bookingId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String guestName = rs.getString("g_fname") + " " + rs.getString("g_lname");
                        int roomNumber = rs.getInt("r_num");
                        String roomType = rs.getString("r_type");
                        String checkInDate = rs.getString("b_checkin");
                        String checkOutDate = rs.getString("b_checkout");
                        String bookingStatus = rs.getString("b_status");
                        String paymentStatus = rs.getString("b_payment_status");
                        double paymentAmount = rs.getDouble("b_total_amount");
                        int contactNumber = rs.getInt("g_cnum");  
                        String email = rs.getString("g_email");       

                        System.out.println("");
                        System.out.println("==================================================");
                        System.out.println("|                Booking Record                  |");
                        System.out.println("--------------------------------------------------");
                        System.out.println("Booking ID: " + bookingId);
                        System.out.println("Guest Name: " + guestName);
                        System.out.println("Room Number: " + roomNumber);
                        System.out.println("Room Type: " + roomType);
                        System.out.println("Check-In Date: " + checkInDate);
                        System.out.println("Check-Out Date: " + checkOutDate);
                        System.out.println("Total Amount Paid: " + paymentAmount);
                        System.out.println("Booking Status: " + bookingStatus);
                        System.out.println("Contact Number: " + contactNumber);  
                        System.out.println("Guest Email: " + email);           
                        System.out.println("==================================================");
                        System.out.println("");
                    } else {
                        System.out.println("No record found for the given Booking ID.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching booking details: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
