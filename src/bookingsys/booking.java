

package bookingsys;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class booking {

    // Method to handle the booking process (Add, View, Update, Delete Booking)
    public void manageBookings() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();
        String response;

        do {
            System.out.println("+-----------------------------+");
            System.out.println("|   Booking Management Menu   |");
            System.out.println("+-----------------------------+");
            System.out.println("| 1. Add Booking              |");
            System.out.println("| 2. View Bookings            |");
            System.out.println("| 3. Update Booking           |");
            System.out.println("| 4. Delete Booking           |");
            System.out.println("| 5. Exit                     |");
            System.out.println("+-----------------------------+");
            System.out.print("Enter your choice: ");
            int option = sc.nextInt();

            switch (option) {
                case 1:
                    addBooking(); // Call the add booking method
                    break;
                case 2:
                    viewBookings(); // Call the view bookings method
                    break;
                case 3:
                    updateBooking(); // Call the update booking method
                    break;
                case 4:
                    deleteBooking(); // Call the delete booking method
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }

            System.out.print("Do you want to continue? (yes/no): ");
            response = sc.next();

        } while (response.equalsIgnoreCase("yes"));  // Continue until user chooses to exit
    }

  private void addBooking() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();

    // Get input for guest ID, room ID, and dates
    System.out.print("Enter Guest ID: ");
    int guestId = sc.nextInt();
    System.out.print("Enter Room ID: ");
    int roomId = sc.nextInt();
    System.out.print("Enter Check-In Date (YYYY-MM-DD): ");
    String checkInDateStr = sc.next();
    System.out.print("Enter Check-Out Date (YYYY-MM-DD): ");
    String checkOutDateStr = sc.next();

    // Parse the dates
    Date checkInDate = Date.valueOf(checkInDateStr);
    Date checkOutDate = Date.valueOf(checkOutDateStr);

    // Calculate total nights
    long totalNights = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);
    if (totalNights <= 0) {
        System.out.println("Invalid check-in and check-out dates.");
        return;
    }

    // Get the room price from the database
    double roomPrice = (double) conf.getSingleValue("SELECT r_price FROM tbl_room WHERE r_id = ?", roomId);

    // Calculate total amount
    double totalAmount = roomPrice * totalNights;

    // Payment Process
    System.out.println("Total Nights: " + totalNights);
    System.out.println("Total Amount: " + totalAmount);
    System.out.print("Enter Payment Amount: ");
    double paymentAmount = sc.nextDouble();

    // Check if payment is sufficient
    if (paymentAmount < totalAmount) {
        System.out.println("Insufficient payment. Please pay the full amount.");
        return;
    }

    // Insert booking record into database
    String sql = "INSERT INTO tbl_booking (g_id, r_id, b_checkin, b_checkout, b_total_nights, b_total_amount, b_payment_status) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    String paymentStatus = (paymentAmount >= totalAmount) ? "Paid" : "Pending"; // Payment status

    conf.addRecord(sql, String.valueOf(guestId), String.valueOf(roomId), checkInDate.toString(), checkOutDate.toString(),
                   String.valueOf(totalNights), String.valueOf(totalAmount), paymentStatus);

    System.out.println("Booking added successfully!");

    // Retrieve details for booking confirmation
    String guestName = ("SELECT g_fname FROM tbl_guest WHERE g_id = ?", guestId);
    int roomNumber = "SELECT r_num FROM tbl_room WHERE r_id = ?", roomId
    String roomType = (String) conf.getSingleValue("SELECT r_type FROM tbl_room WHERE r_id = ?", roomId);

    // Format dates for confirmation display
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedCheckInDate = dateFormat.format(checkInDate);
    String formattedCheckOutDate = dateFormat.format(checkOutDate);

    // Display booking confirmation
    System.out.println("--------------------------------------------------");
    System.out.println("              Booking Confirmation               ");
    System.out.println("--------------------------------------------------");
    System.out.println("Booking ID: " + guestId);
    System.out.println("Guest Name: " + guestName);
    System.out.println("Room Number: " + roomNumber);
    System.out.println("Room Type: " + roomType);
    System.out.println("Check-In Date: " + formattedCheckInDate);
    System.out.println("Check-Out Date: " + formattedCheckOutDate);
    System.out.println("Total Amount Paid: " + paymentAmount);
    System.out.println("--------------------------------------------------");
}


    // Method to view all bookings
    private void viewBookings() {
        String cqry = "SELECT b_id, g.g_fname || ' ' || g.g_lname AS guest_name, r.r_num AS room_number, b.b_payment_status AS payment_status "
                     + "FROM tbl_booking b "
                     + "JOIN tbl_guest g ON b.g_id = g.g_id "
                     + "JOIN tbl_room r ON b.r_id = r.r_id";
        String[] headers = {"Booking ID", "Guest Name", "Room Number", "Payment Status"};
        String[] columns = {"b_id", "guest_name", "room_number", "payment_status"};
        Config conf = new Config();
        conf.viewRecords(cqry, headers, columns);
    }

    // Method to update an existing booking
    private void updateBooking() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();

        // Ask for the booking ID to update
        System.out.print("Enter Booking ID to update: ");
        int bookingId = sc.nextInt();

        // Check if the booking exists
      while (conf.getSingleValue("SELECT b_id FROM tbl_booking WHERE b_id = ?", bookingId) <= 0) {
            System.out.println("Selection ID doesn't exist.");
            System.out.print("Select ID again: ");
            bookingId = sc.nextInt(); // Reusing the same 'id' variable without redeclaring it
        }

        // Ask for the new check-in and check-out dates
        System.out.print("Enter new Check-In Date (YYYY-MM-DD): ");
        String checkInDateStr = sc.next();
        System.out.print("Enter new Check-Out Date (YYYY-MM-DD): ");
        String checkOutDateStr = sc.next();

        // Parse the dates
        Date checkInDate = Date.valueOf(checkInDateStr);
        Date checkOutDate = Date.valueOf(checkOutDateStr);

        // Calculate total nights
        long totalNights = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);
        if (totalNights <= 0) {
            System.out.println("Invalid check-in and check-out dates.");
            return;
        }

        // Get the room price from the database
        String roomQuery = "SELECT r_price FROM tbl_room WHERE r_id = (SELECT r_id FROM tbl_booking WHERE b_id = ?)";
        double roomPrice = conf.getSingleValue(roomQuery, bookingId);

        // Calculate total amount
        double totalAmount = roomPrice * totalNights;

        // Payment Process
        System.out.println("Total Nights: " + totalNights);
        System.out.println("Total Amount: " + totalAmount);
        System.out.print("Enter Payment Amount: ");
        double paymentAmount = sc.nextDouble();

        // Check if payment is sufficient
        String paymentStatus = (paymentAmount >= totalAmount) ? "Paid" : "Pending"; // Payment status

        // Update the booking record in the database
        String updateSQL = "UPDATE tbl_booking SET b_checkin = ?, b_checkout = ?, b_total_nights = ?, b_total_amount = ?, b_payment_status = ? "
                         + "WHERE b_id = ?";
        conf.addRecord(updateSQL, checkInDate.toString(), checkOutDate.toString(),
                       String.valueOf(totalNights), String.valueOf(totalAmount), paymentStatus, String.valueOf(bookingId));

        System.out.println("Booking updated successfully!");
    }

    // Method to delete a booking
    private void deleteBooking() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();

        // Ask for the booking ID to delete
        System.out.print("Enter Booking ID to delete: ");
        int bookingId = sc.nextInt();

        // Check if the booking exists
         while (conf.getSingleValue("SELECT b_id FROM tbl_booking WHERE b_id = ?", bookingId) <= 0) {
            System.out.println("Selection ID doesn't exist.");
            System.out.print("Select ID again: ");
            bookingId = sc.nextInt(); // Reusing the same 'id' variable without redeclaring it
        }
        
   

        // Delete the booking record from the database
        String deleteSQL = "DELETE FROM tbl_booking WHERE b_id = ?";
        conf.addRecord(deleteSQL, String.valueOf(bookingId));

        System.out.println("Booking deleted successfully!");
    }
}
