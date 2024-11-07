package bookingsys;

import java.util.Scanner;

public class booking {

    // Method to manage bookings - menu-driven CRUD operations
    public void manageBookings() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();
        String response;
        booking b = new booking();

        do {
            System.out.println("+----------------------------+");
            System.out.println("|  Booking Management Menu   |");
            System.out.println("+----------------------------+");
            System.out.println("| 1. Add Booking             |");
            System.out.println("| 2. View Bookings           |");
            System.out.println("| 3. Update Booking          |");
            System.out.println("| 4. Delete Booking          |");
            System.out.println("| 5. Exit                    |");
            System.out.println("+----------------------------+");
            System.out.print("Enter your choice: ");
            int option = sc.nextInt();

            // Switch-case to handle different booking CRUD operations
            switch (option) {
                case 1:
                    b.addBooking();
                    break;
                case 2:
                    b.viewBookings();
                    break;
                case 3:
                    b.viewBookings();
                    b.updateBooking();
                    break;
                case 4:
                    b.viewBookings();
                    b.deleteBooking();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }

            // Ask if the user wants to continue
            System.out.print("Do you want to continue? (yes/no): ");
            response = sc.next();

        } while (response.equalsIgnoreCase("yes"));  // Continue until user chooses to exit
        System.out.println("");
    }

    // Add a new booking to the system
    private void addBooking() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();

        // Ask for guest ID and room ID
        System.out.print("Enter Guest ID: ");
        int guestId = sc.nextInt();
        System.out.print("Enter Room ID: ");
        int roomId = sc.nextInt();

        // Ask for check-in and check-out dates
        System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
        String checkIn = sc.next();
        System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
        String checkOut = sc.next();

        // Calculate number of nights and total cost
        System.out.print("Enter price per night: ");
        double pricePerNight = sc.nextDouble();
        int numNights = calculateNumberOfNights(checkIn, checkOut);
        double totalCost = numNights * pricePerNight;

        // Ask for payment status
        System.out.print("Enter Payment Status (Paid/Unpaid): ");
        String paymentStatus = sc.next();

        // Insert the booking into the database
        String sql = "INSERT INTO tbl_booking (g_id, r_id, b_check_in, b_check_out, b_num_nights, b_total_cost, b_pstatus) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        conf.addRecord(sql, String.valueOf(guestId), String.valueOf(roomId), checkIn, checkOut, String.valueOf(numNights), String.valueOf(totalCost), paymentStatus);
    }

    // View all bookings
    private void viewBookings() {
        String cqry = "SELECT * FROM tbl_booking";
        String[] Headers = {"Booking ID", "Guest ID", "Room ID", "Check-in Date", "Check-out Date", "No. of Nights", "Total Cost", "Payment Status"};
        String[] Columns = {"b_id", "g_id", "r_id", "b_check_in", "b_check_out", "b_num_nights", "b_total_cost", "b_pstatus"};
        Config conf = new Config();
        conf.viewRecords(cqry, Headers, Columns);
    }

    // Update an existing booking
    private void updateBooking() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();

        System.out.print("Enter Booking ID to update: ");
        int bookingId = sc.nextInt();

        // Check if the booking ID exists
        while (conf.getSingleValue("SELECT b_id FROM tbl_booking WHERE b_id = ?", bookingId) == 0) {
            System.out.println("Booking ID doesn't exist.");
            System.out.print("Enter Booking ID again: ");
            bookingId = sc.nextInt();
        }

        // Ask for new details for the booking
        System.out.print("Enter new check-in date (YYYY-MM-DD): ");
        String checkIn = sc.next();
        System.out.print("Enter new check-out date (YYYY-MM-DD): ");
        String checkOut = sc.next();

        // Calculate new number of nights and total cost
        System.out.print("Enter new price per night: ");
        double pricePerNight = sc.nextDouble();
        int numNights = calculateNumberOfNights(checkIn, checkOut);
        double totalCost = numNights * pricePerNight;

        System.out.print("Enter new payment status (Paid/Unpaid): ");
        String paymentStatus = sc.next();

        // Update the booking record
        String sql = "UPDATE tbl_booking SET b_check_in = ?, b_check_out = ?, b_num_nights = ?, b_total_cost = ?, b_pstatus = ? WHERE b_id = ?";
        conf.updateRecord(sql, checkIn, checkOut, numNights, totalCost, paymentStatus, bookingId);
    }

    // Delete an existing booking
    private void deleteBooking() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();

        System.out.print("Enter Booking ID to delete: ");
        int bookingId = sc.nextInt();

        // Check if the booking ID exists
        while (conf.getSingleValue("SELECT b_id FROM tbl_booking WHERE b_id = ?", bookingId) == 0) {
            System.out.println("Booking ID doesn't exist.");
            System.out.print("Enter Booking ID again: ");
            bookingId = sc.nextInt();
        }

        // Delete the booking record
        String sql = "DELETE FROM tbl_booking WHERE b_id = ?";
        conf.deleteRecord(sql, bookingId);
    }

    // Helper method to calculate number of nights between check-in and check-out
    private int calculateNumberOfNights(String checkIn, String checkOut) {
        // Example of calculating number of nights (this could be enhanced using a date library like java.time)
        String[] checkInParts = checkIn.split("-");
        String[] checkOutParts = checkOut.split("-");

        int checkInYear = Integer.parseInt(checkInParts[0]);
        int checkInMonth = Integer.parseInt(checkInParts[1]);
        int checkInDay = Integer.parseInt(checkInParts[2]);

        int checkOutYear = Integer.parseInt(checkOutParts[0]);
        int checkOutMonth = Integer.parseInt(checkOutParts[1]);
        int checkOutDay = Integer.parseInt(checkOutParts[2]);

        // For simplicity, assume each month has 30 days (this can be enhanced to use actual calendar calculations)
        int checkInDays = checkInYear * 365 + checkInMonth * 30 + checkInDay;
        int checkOutDays = checkOutYear * 365 + checkOutMonth * 30 + checkOutDay;

        return Math.abs(checkOutDays - checkInDays);  // Return the absolute difference in days
    }
}
