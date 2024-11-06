
package bookingsys;

import java.util.Scanner;


public class room {
    



    // Method to handle menu-driven CRUD operations for rooms
    public void manageRooms() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();
        int choice;
         room r = new room();
        // Loop to display the menu and perform actions based on user choice
        do {
            // Display menu options
            System.out.println("\nRoom Management Menu");
            System.out.println("1. Add Room");
            System.out.println("2. View Rooms");
            System.out.println("3. Update Room");
            System.out.println("4. Delete Room");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            // Switch-case to handle different CRUD operations for rooms
            switch (choice) {
                case 1:
                    r.addRoom();
                    break;
                case 2:
                    r.viewRooms();
                    break;
                case 3:
                    viewRooms();  // Optional: View room before updating
                    r.updateRoom();
                    break;
                case 4:
                    r.viewRooms();  // Optional: View room before deleting
                    r.deleteRoom();
                    break;
                case 5:
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 5);  // Continue until user chooses to exit
    }

    // Add a new room to the system
    private void addRoom() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();

        System.out.print("Enter room number: ");
        String roomNumber = sc.next();
        System.out.print("Enter room type (Single, Double, Suite): ");
        String roomType = sc.next();
        System.out.print("Enter room capacity ( 1, 2, 4): ");
        int roomCapacity = sc.nextInt();
        System.out.print("Enter room price per night: ");
        double roomPrice = sc.nextDouble();
        System.out.print("Enter room availability (available or not): ");
        String roomAvailability = sc.next();

        String sql = "INSERT INTO tbl_room(r_num, r_type, r_capacity, r_price, r_availability) "
             + "VALUES (?, ?, ?, ?, ?)";
       conf.addRecord(sql, roomNumber, roomType, String.valueOf(roomCapacity), String.valueOf(roomPrice), roomAvailability);

    }

    // View all rooms in the system
    private void viewRooms() {
        String cqry = "SELECT * FROM tbl_room";
        String[] Headers = {"Room Number", "Room Type", "Capacity", "Price", "Availability"};
        String[] Columns = {"r_num", "r_type", "r_capacity", "r_price", "r_availability"};
        Config conf = new Config();
        conf.viewRecords(cqry, Headers, Columns);
    }

    // Update an existing room
    private void updateRoom() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter room number to update: ");
        String roomNumber = sc.next();

        System.out.print("Enter the new room type: ");
        String roomType = sc.next();
        System.out.print("Enter the new room capacity: ");
        int roomCapacity = sc.nextInt();
        System.out.print("Enter the new room price: ");
        double roomPrice = sc.nextDouble();
        System.out.print("Enter the new room availability: ");
        String roomAvailability = sc.next();

        String qry = "UPDATE tbl_room SET r_type = ?, r_capacity = ?, r_price = ?, r_availability = ? WHERE r_num = ?";
        Config conf = new Config();
        conf.updateRecord(qry, roomType, roomCapacity, roomPrice, roomAvailability, roomNumber);
    }

    // Delete an existing room
    private void deleteRoom() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter room number to delete: ");
        String roomNumber = sc.next();

        String sqlDelete = "DELETE FROM tbl_room WHERE r_num = ?";
        Config conf = new Config();
        conf.deleteRecord(sqlDelete, roomNumber);
    }
}

    

