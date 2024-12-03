

package bookingsys;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class booking {

   
  public void manageBookings() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();
        String response;
        MAIN ss = new MAIN();

        do {
            System.out.println("");
        System.out.println("===================================");
        System.out.println("|         Booking Services        |");
        System.out.println("===================================");
        System.out.println("| 1. Book a Room                  |");
        System.out.println("| 2. View Booking History         |");
        System.out.println("| 3. Booking Check-Out            |");
        System.out.println("| 4. Cancel a Booking             |");
        System.out.println("| 5. Delete a Booking             |");
        System.out.println("| 6. Return to Main Menu          |");
        System.out.println("===================================");
        System.out.print("Enter your choice: ");

    
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input! Please enter a valid number between 1 and 6.");
            sc.next(); 
            System.out.print("Enter your choice: ");
        }

        int option = sc.nextInt();

        while (option < 1 || option > 6) {
            System.out.println("Invalid choice! Please enter a number between 1 and 6.");
            System.out.print("Enter your choice: ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number between 1 and 6.");
                sc.next(); 
                System.out.print("Enter your choice: ");
            }
            option = sc.nextInt();
        }




            switch (option) {
                case 1:
                    addBooking();
                    break;
                case 2:
                    viewBookingHistory();
                    break;
                case 3:
                    bookingCheckOut();
                    break;
                case 4:
                    cancelBooking();
                    break;
                case 5:
                    viewBookings();
                    deleteBooking();
                    break;
                case 6:
                    System.out.println("");
                    System.out.println("Exiting Manage Booking Information...");
                    System.out.println("");
                    ss.main(new String[] {});
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }

            while (true) {
                System.out.print("Do you want to go back? (yes/no): ");
                response = sc.next();
                if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        } while (response.equalsIgnoreCase("yes"));
        System.out.println("");
        System.out.println("Exiting Manage Booking Information...");
        System.out.println("");
    }
 
private void addBooking() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();
    viewConfig vcon = new viewConfig();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);

    System.out.println("GUEST DETAILS");
    String guestQuery = "SELECT g_id, g_fname, g_lname FROM tbl_guest";
    String[] guestHeaders = {"Guest ID", "First Name", "Last Name"};
    String[] guestColumns = {"g_id", "g_fname", "g_lname"};
    vcon.GuestDetails(guestQuery, guestHeaders, guestColumns);

    // Ensure valid Guest ID input
    int guestId = -1;
    while (guestId == -1) {
        System.out.print("Enter Guest ID: ");
        guestId = sc.nextInt();
        if (conf.getSingleValue("SELECT COUNT(*) FROM tbl_guest WHERE g_id = ?", guestId) == 0) {
            System.out.println("Invalid Guest ID. Please try again.");
            guestId = -1;  // Reset to prompt for input again
        }
    }

    System.out.println("\nROOM DETAILS");
    String roomQuery = "SELECT r_id, r_num, r_type, r_capacity, r_price, r_availability FROM tbl_room WHERE r_availability = 'available'";
    String[] roomHeaders = {"Room ID", "Room Number", "Room Type", "Capacity", "Price", "Availability"};
    String[] roomColumns = {"r_id", "r_num", "r_type", "r_capacity", "r_price", "r_availability"};
    vcon.viewRoom(roomQuery, roomHeaders, roomColumns);

    // Ensure valid Room ID input
    int roomId = -1;
    while (roomId == -1) {
        System.out.print("Enter Room ID: ");
        roomId = sc.nextInt();
        if (conf.getSingleValue("SELECT COUNT(*) FROM tbl_room WHERE r_id = ? AND r_availability = 'available'", roomId) == 0) {
            System.out.println("Invalid or unavailable Room ID. Please try again.");
            roomId = -1;  // Reset to prompt for input again
        }
    }

    Date checkInDate = null;
    while (checkInDate == null) {
        System.out.print("Enter Check-In Date (YYYY-MM-DD): ");
        String checkInDateStr = sc.next();
        checkInDate = validateDate(checkInDateStr, dateFormat);
    }

    Date checkOutDate = null;
    while (checkOutDate == null || checkOutDate.before(checkInDate)) {
        System.out.print("Enter Check-Out Date (YYYY-MM-DD): ");
        String checkOutDateStr = sc.next();
        checkOutDate = validateDate(checkOutDateStr, dateFormat);

        // Check if Check-Out Date is after Check-In Date
        if (checkOutDate != null && checkOutDate.before(checkInDate)) {
            System.out.println("Check-Out Date must be after Check-In Date.");
            checkOutDate = null;  // Reset to prompt again
        }
    }

    // Calculate total nights, check if dates are valid
    long totalNights = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);
    if (totalNights <= 0) {
        System.out.println("Invalid check-in and check-out dates.");
        return;
    }

    double roomPrice = conf.getSingleValue("SELECT r_price FROM tbl_room WHERE r_id = ?", roomId);
    double totalAmount = roomPrice * totalNights;
    System.out.println("---------------------------------------------");
    System.out.println("Total Nights: " + totalNights);
    System.out.println("Total Amount: " + totalAmount);

    System.out.print("Do you want to proceed to payment? (yes/no): ");
    String proceedToPayment = sc.next();

    if (proceedToPayment.equalsIgnoreCase("no")) {
        System.out.println("Booking cancelled.");
        return;
    }

    // Ensure the payment amount is correct
    double paymentAmount;
    while (true) {
        System.out.print("Enter Payment Amount: ");
        paymentAmount = sc.nextDouble();

        if (paymentAmount == totalAmount) {
            break; 
        }

        System.out.println("Payment must be exactly " + totalAmount + ". Please enter the correct amount.");
    }

    System.out.println("---------------------------------------------");
    String bookingStatus = "Booked";
    String paymentStatus = "Paid";

    String sql = "INSERT INTO tbl_booking (g_id, r_id, b_checkin, b_checkout, b_total_nights, b_total_amount, b_payment_status, b_status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    conf.addRecord(sql, String.valueOf(guestId), String.valueOf(roomId), checkInDate.toString(), checkOutDate.toString(),
            String.valueOf(totalNights), String.valueOf(totalAmount), paymentStatus, bookingStatus);

    System.out.println("Booking added successfully!");

    int bookingId = (int) conf.getSingleValue("SELECT b_id FROM tbl_booking WHERE g_id = ? AND r_id = ? AND b_checkin = ?", guestId, roomId, checkInDate.toString());

    String updateAvailabilitySql = "UPDATE tbl_room SET r_availability = 'unavailable' WHERE r_id = ?";
    conf.updateRecord(updateAvailabilitySql, roomId);

    String guestName = conf.getStringValue("SELECT g_fname || ' ' || g_lname FROM tbl_guest WHERE g_id = ?", guestId);
    String roomNumber = conf.getStringValue("SELECT r_num FROM tbl_room WHERE r_id = ?", roomId);
    String roomType = conf.getStringValue("SELECT r_type FROM tbl_room WHERE r_id = ?", roomId);

    System.out.println("");
    System.out.println("==================================================");
    System.out.println("|              Booking Confirmation              |");
    System.out.println("--------------------------------------------------");
    System.out.println("Booking ID: " + bookingId);
    System.out.println("Guest Name: " + guestName);
    System.out.println("Room Number: " + roomNumber);
    System.out.println("Room Type: " + roomType);
    System.out.println("Check-In Date: " + checkInDate);
    System.out.println("Check-Out Date: " + checkOutDate);
    System.out.println("Total Amount Paid: " + paymentAmount);
    System.out.println("Booking Status: " + bookingStatus);
    System.out.println("--------------------------------------------------");
    System.out.println("| We are looking forward to your stay. Have a great day!");
    System.out.println("==================================================");
    System.out.println("");
}


private java.sql.Date validateDate(String dateStr, SimpleDateFormat dateFormat) {
    java.util.Date utilDate = null;
    try {
        utilDate = dateFormat.parse(dateStr);
        java.util.Date currentDate = new java.util.Date(System.currentTimeMillis());

        // Check if the input date is not in the past
        if (utilDate.before(currentDate)) {
            System.out.println("Date cannot be in the past. Please enter a valid date.");
            return null;
        }
    } catch (ParseException e) {
        System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
    }
    return new java.sql.Date(utilDate.getTime());  // Convert to java.sql.Date
}


    

 private void viewBookings() {
        viewConfig vconf = new viewConfig();
        Config conf = new Config();
        int bookingCount = (int) conf.getSingleValue("SELECT COUNT(*) FROM tbl_booking");
        if (bookingCount == 0) {
            System.out.println("");
            System.out.println("No bookings available to view.");
            System.out.println("");
            return;
        }

        String cqry = "SELECT b_id, g.g_fname || ' ' || g.g_lname AS guest_name, "
                + "r.r_num AS room_number, b.b_checkin AS check_in_date, "
                + "b.b_checkout AS check_out_date, b.b_status AS booking_status, "
                + "b.b_payment_status AS payment_status "
                + "FROM tbl_booking b "
                + "JOIN tbl_guest g ON b.g_id = g.g_id "
                + "JOIN tbl_room r ON b.r_id = r.r_id";

        String[] headers = { "Booking ID", "Guest Name", "Room Number", "Check-In Date", "Check-Out Date", "Booking Status", "Payment Status" };
        String[] columns = { "b_id", "guest_name", "room_number", "check_in_date", "check_out_date", "booking_status", "payment_status" };
        vconf.viewBooking(cqry, headers, columns);
    }


    
private void bookingCheckOut() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();

    // Check if there are any bookings with status 'Booked'
    String checkQuery = "SELECT COUNT(*) FROM tbl_booking WHERE b_status = 'Booked'";
    int bookingCount = (int) conf.getSingleValue(checkQuery);
    
    if (bookingCount == 0) {
        System.out.println("No bookings with status 'Booked' available to check out.");
        return;
    }

    // Display bookings with b_status = 'Booked'
    System.out.println("\nBookings with status 'Booked':");
    String query = "SELECT b_id, g.g_fname || ' ' || g.g_lname AS guest_name, "
                 + "r.r_num AS room_number, b.b_checkin AS check_in_date, "
                 + "b.b_checkout AS check_out_date, b.b_status AS booking_status, "
                 + "b.b_payment_status AS payment_status "
                 + "FROM tbl_booking b "
                 + "JOIN tbl_guest g ON b.g_id = g.g_id "
                 + "JOIN tbl_room r ON b.r_id = r.r_id "
                 + "WHERE b.b_status = 'Booked'";

    String[] headers = { "Booking ID", "Guest Name", "Room Number", "Check-In Date", "Check-Out Date", "Booking Status", "Payment Status" };
    String[] columns = { "b_id", "guest_name", "room_number", "check_in_date", "check_out_date", "booking_status", "payment_status" };
    
    viewConfig vconf = new viewConfig();
    vconf.viewBooking(query, headers, columns); // Display bookings with status 'Booked'

    // Ask for Booking IDs to check out (comma separated)
    System.out.print("Enter Booking ID(s) to check out (separate with commas): ");
    String input = sc.nextLine();
    String[] bookingIds = input.split(",");

    for (String bookingIdStr : bookingIds) {
        bookingIdStr = bookingIdStr.trim();

        // Check if the input is a valid number (non-letter, valid integer)
        while (!isValidBookingId(bookingIdStr)) {
            System.out.println("Invalid input! Please enter a valid numeric Booking ID.");
            System.out.print("Enter Booking ID again: ");
            bookingIdStr = sc.nextLine().trim();
        }

        int bookingId = Integer.parseInt(bookingIdStr);

        // Validate if the Booking ID exists and has a status of 'Booked'
        while (conf.getSingleValue("SELECT b_id FROM tbl_booking WHERE b_id = ? AND b_status = 'Booked'", bookingId) == 0) {
            System.out.println("Booking ID " + bookingId + " either doesn't exist or is not in 'Booked' status.");
            System.out.print("Enter Booking ID again: ");
            bookingId = sc.nextInt();
            sc.nextLine(); // Consume the newline character
        }

        // Fetch the Room ID linked to this booking
        String roomQuery = "SELECT r_id FROM tbl_booking WHERE b_id = ?";
        int roomId = (int) conf.getSingleValue(roomQuery, bookingId);

        // Update the booking status to 'Checked Out'
        String updateBookingStatusQuery = "UPDATE tbl_booking SET b_status = 'Checked Out' WHERE b_id = ?";
        conf.updateRecord(updateBookingStatusQuery, bookingId);

        // Make the room available again
        String updateRoomStatusQuery = "UPDATE tbl_room SET r_availability = 'available' WHERE r_id = ?";
        conf.updateRecord(updateRoomStatusQuery, roomId);

        System.out.println("Booking ID " + bookingId + " successfully checked out.");
    }

    System.out.println("All selected bookings have been checked out.");
    System.out.println("Rooms are now available for new bookings.");
    System.out.println("");
}

// Helper method to check if the Booking ID is valid (numeric and non-letter)
private boolean isValidBookingId(String bookingIdStr) {
    if (bookingIdStr.isEmpty()) {
        return false;
    }

    // Check if all characters are digits
    for (int i = 0; i < bookingIdStr.length(); i++) {
        if (!Character.isDigit(bookingIdStr.charAt(i))) {
            return false;
        }
    }

    return true;
}


// Helper method to check if a string is a valid integer
private boolean isValidInteger(String str) {
    try {
        Integer.parseInt(str);  // Try to parse the string as an integer
        return true;
    } catch (NumberFormatException e) {
        return false;  // If it throws an exception, the input is not a valid integer
    }
}







    
  private void deleteBooking() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();

    // Check if there are any bookings available
    int bookingCount = (int) conf.getSingleValue("SELECT COUNT(*) FROM tbl_booking");
    if (bookingCount == 0) {
        System.out.println("No bookings available to delete.");
        return; 
    }
    
    // Ask for Booking ID
    System.out.print("Enter Booking ID to delete: ");
    int bookingId = sc.nextInt();

    // Validate if the Booking ID exists
    while (conf.getSingleValue("SELECT b_id FROM tbl_booking WHERE b_id = ?", bookingId) == 0) {
        System.out.println("Booking ID doesn't exist.");
        System.out.print("Enter Booking ID again: ");
        bookingId = sc.nextInt();
    }

    // Ask the user to confirm before deletion
    System.out.print("Are you sure you want to delete Booking ID " + bookingId + "? (Y/N): ");
    String confirmation = sc.next();

    // Proceed only if the user confirms
    if (confirmation.equalsIgnoreCase("Y")) {
        // Fetch the Room ID linked to this booking
        String roomQuery = "SELECT r_id FROM tbl_booking WHERE b_id = ?";
        int roomId = (int) conf.getSingleValue(roomQuery, bookingId);

        // Update the room availability to 'available' before deleting
        String updateRoomStatusQuery = "UPDATE tbl_room SET r_availability = 'available' WHERE r_id = ?";
        conf.updateRecord(updateRoomStatusQuery, roomId);

        // Delete the booking record
        String deleteBookingQuery = "DELETE FROM tbl_booking WHERE b_id = ?";
        conf.updateRecord(deleteBookingQuery, bookingId);

        System.out.println("");
        System.out.println("Booking deleted successfully.");
        System.out.println("");
    } else {
        System.out.println("Booking deletion cancelled.");
    }
}



private void cancelBooking() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();

    // Check if there are any bookings with status 'Booked'
    String checkBookingsQuery = "SELECT COUNT(*) FROM tbl_booking WHERE b_status = 'Booked'";
    int bookingCount = (int) conf.getSingleValue(checkBookingsQuery);

    if (bookingCount == 0) {
        System.out.println("No bookings with status 'Booked' available to cancel.");
        return;
    }

    // Fetch bookings with status 'Booked'
    String bookingsQuery = "SELECT b_id, g.g_fname || ' ' || g.g_lname AS guest_name, "
            + "r.r_num AS room_number, b.b_checkin AS check_in_date, "
            + "b.b_checkout AS check_out_date, b.b_status AS booking_status, "
            + "b.b_payment_status AS payment_status "
            + "FROM tbl_booking b "
            + "JOIN tbl_guest g ON b.g_id = g.g_id "
            + "JOIN tbl_room r ON b.r_id = r.r_id "
            + "WHERE b.b_status = 'Booked'";

    String[] headers = { "Booking ID", "Guest Name", "Room Number", "Check-In Date", "Check-Out Date", "Booking Status", "Payment Status" };
    String[] columns = { "b_id", "guest_name", "room_number", "check_in_date", "check_out_date", "booking_status", "payment_status" };

    viewConfig vconf = new viewConfig();
    vconf.viewBooking(bookingsQuery, headers, columns);

    // Ask for Booking IDs (comma separated) to cancel
    System.out.print("Enter Booking IDs to cancel (separate with commas): ");
    String input = sc.nextLine();
    String[] bookingIds = input.split(",");

    for (String bookingIdStr : bookingIds) {
        int bookingId;

        // Validate if the entered Booking ID is a valid integer and exists in the database
        while (true) {
            try {
                bookingId = Integer.parseInt(bookingIdStr.trim());

                // Check if the Booking ID exists in the database
                if (conf.getSingleValue("SELECT COUNT(*) FROM tbl_booking WHERE b_id = ?", bookingId) == 0) {
                    System.out.println("Booking ID " + bookingId + " doesn't exist.");
                    System.out.print("Enter a valid Booking ID again: ");
                    bookingIdStr = sc.nextLine(); // Read again if invalid ID is entered
                    continue; // Continue the loop to get a valid ID
                }
                break; // Exit the loop if the ID is valid
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for the Booking ID.");
                System.out.print("Enter Booking ID again: ");
                bookingIdStr = sc.nextLine(); // Read again if input is not a valid integer
            }
        }

        // Fetch the Room ID linked to this booking
        String roomQuery = "SELECT r_id FROM tbl_booking WHERE b_id = ?";
        int roomId = (int) conf.getSingleValue(roomQuery, bookingId);

        // Update the booking status to 'Cancelled'
        String updateBookingStatusQuery = "UPDATE tbl_booking SET b_status = 'Cancelled' WHERE b_id = ?";
        conf.updateRecord(updateBookingStatusQuery, bookingId);

        // Update the room availability to 'available'
        String updateRoomStatusQuery = "UPDATE tbl_room SET r_availability = 'available' WHERE r_id = ?";
        conf.updateRecord(updateRoomStatusQuery, roomId);

        System.out.println("Booking ID " + bookingId + " cancelled successfully.");
    }

    System.out.println("All selected bookings have been cancelled.");
    System.out.println("");
}





    private void viewBookingHistory() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();
    
    System.out.println("");
System.out.println("+------------------------------+");
System.out.println("|   Booking Status Filter      |");
System.out.println("+------------------------------+");
System.out.println("| 1. Booked                    |");
System.out.println("| 2. Cancelled                 |");
System.out.println("| 3. Checked Out               |");
System.out.println("| 4. View All                  |");
System.out.println("+------------------------------+"); 

int statusChoice = 0;
boolean validChoice = false;

// Keep prompting the user until they provide a valid input
while (!validChoice) {
    System.out.print("Enter your choice: ");
    if (sc.hasNextInt()) {
        statusChoice = sc.nextInt();
        // Validate if the choice is within the acceptable range (1-4)
        if (statusChoice >= 1 && statusChoice <= 4) {
            validChoice = true; // Valid input, exit the loop
        } else {
            System.out.println("Invalid choice. Please select a number between 1 and 4.");
        }
    } else {
        System.out.println("Invalid input. Please enter a valid number.");
        sc.next(); // Consume the invalid input to avoid infinite loop
    }
}
    String status = "";
  

    switch (statusChoice) {
        case 1:
            status = "Booked";
            break;
        case 2:
            status = "Cancelled";
            break;
        case 3:
            status = "Checked Out";
            break;
        case 4:
            status = "";
            viewBookings();
            return; // No need to proceed if View All option is selected
        default:
            validChoice = false;
          System.out.println("Invalid choice, please try again.");
            break;
    }

    if (!validChoice) {
        return; // Return to menu if invalid choice
    }

    // Build the query to fetch booking history based on selected status
    String query = "SELECT b_id, g.g_fname || ' ' || g.g_lname AS guest_name, "
            + "r.r_num AS room_number, b.b_checkin AS check_in_date, "
            + "b.b_checkout AS check_out_date, b.b_status AS booking_status, "
            + "b.b_payment_status AS payment_status "
            + "FROM tbl_booking b "
            + "JOIN tbl_guest g ON b.g_id = g.g_id "
            + "JOIN tbl_room r ON b.r_id = r.r_id "
            + "WHERE b.b_status = ?";

    // Fetch records from the database
    Object[][] results = conf.fetchRecords(query, status);

    // Check if no records are found for the selected status
    if (results == null || results.length == 0) {
        System.out.println("No bookings found with status '" + status + "'.");
        return;
    }

    // Display the booking history
    System.out.println("");
    System.out.println("\t\t\t\t===================================");
    System.out.println("\t\t\t\t|       Booking History          |");
    System.out.println("\t\t\t\t===================================");
    System.out.println("-----------------------------------------------------------------------------------------------------------------");
    System.out.printf("| %-10s | %-20s | %-10s | %-12s | %-12s | %-15s | %-12s |\n",
            "Booking ID", "Guest Name", "Room #", "Check-In", "Check-Out", "Status", "Payment");
    System.out.println("------------------------------------------------------------------------------------------------------------------");

    for (Object[] row : results) {
        System.out.printf("| %-10s | %-20s | %-10s | %-12s | %-12s | %-15s | %-12s |\n",
                row[0], row[1], row[2], row[3], row[4], row[5], row[6]);
    }

    System.out.println("------------------------------------------------------------------------------------------------------------------");
    System.out.println("");
}



    
    
}