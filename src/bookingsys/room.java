
package bookingsys;

import java.util.Scanner;


public class room {
   
   
    public void manageRooms(){
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();
        int choice;
        String response;
         room r = new room();
           MAIN ss = new MAIN();
           int option = -1;
        do {
            System.out.println("");
            System.out.println("===============================");
            System.out.println("|       Room Management       |");
            System.out.println("===============================");
            System.out.println("| 1. Add New Room             |");
            System.out.println("| 2. View Room Information    |");
            System.out.println("| 3. Edit Room Information    |");
            System.out.println("| 4. Remove Room              |");
            System.out.println("| 5. Return to Main Menu      |");
            System.out.println("===============================");
            
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
            
            switch (option) {
                case 1:
                    r.addRoom();
                    r.viewRooms();
                    break;
                case 2:
                    r.viewRooms();
                    break;
                case 3:
                    viewRooms();  
                    r.updateRoom();
                    break;
                case 4:
                    r.viewRooms();  
                    r.deleteRoom();
                    break;
                case 5:
                    System.out.println("");
                    System.out.println("Exiting Manage Room Information...");
                    System.out.println("");
                    ss.main(new String[]{}); 
                    break;
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
        System.out.println("Exiting Manage Room information...");
        System.out.println("");
    }

  
    private void addRoom() {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();

        System.out.print("Enter room number: ");
        String roomNumber = sc.next();

        System.out.println("");
        System.out.println("===========================================");
        System.out.println("| Room Type      |      Capacity          |");
        System.out.println("===========================================");
        System.out.println("| 1. Single      |     1 Person           |");
        System.out.println("| 2. Double      |     2 People           |");
        System.out.println("| 3. Suite       |     4 People           |");
        System.out.println("===========================================");

        int roomTypeOption;
        String roomType = "";
        int roomCapacity = 0;

        do {
            System.out.print("Enter choice (1-3): ");
            roomTypeOption = sc.nextInt();
            switch (roomTypeOption) {
                case 1:
                    roomType = "Single";
                    roomCapacity = 1;
                    break;
                case 2:
                    roomType = "Double";
                    roomCapacity = 2;
                    break;
                case 3:
                    roomType = "Suite";
                    roomCapacity = 4;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid room type.");
            }
        } while (roomType.isEmpty());

        System.out.print("Enter room price per night: ");
        double roomPrice = sc.nextDouble();

       
        String roomAvailability = "available";

        String sql = "INSERT INTO tbl_room(r_num, r_type, r_capacity, r_price, r_availability) "
                   + "VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, roomNumber, roomType, String.valueOf(roomCapacity), String.valueOf(roomPrice), roomAvailability);

        System.out.println("Room added successfully with availability set to 'available'.");
    }


   
    public void viewRooms() {
        String cqry = "SELECT * FROM tbl_room";
        String[] Headers = {"ID","Room Number", "Room Type", "Capacity", "Price", "Status"};
        String[] Columns = {"r_id" ,"r_num", "r_type", "r_capacity", "r_price", "r_availability"};
        viewConfig conf = new viewConfig();
        conf.viewRoom(cqry, Headers, Columns);
    }

  
private void updateRoom() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();

    int id = -1;

    // Room ID validation: Ensure the ID exists in the database
    while (id <= 0 || conf.getSingleValue("SELECT r_id FROM tbl_room WHERE r_id = ?", id) == 0) {
        System.out.print("Enter Room ID to update: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid numeric Room ID.");
            sc.next(); // Consume the invalid input
            continue;
        }

        id = sc.nextInt();
        if (id <= 0 || conf.getSingleValue("SELECT r_id FROM tbl_room WHERE r_id = ?", id) == 0) {
            System.out.println("Room ID doesn't exist. Please enter a valid ID.");
        }
    }

    // Validate and gather new room details
    System.out.print("Enter the new room number: ");
    String roomNumber = sc.next();

    System.out.println("");
    System.out.println("===========================================");
    System.out.println("| Room Type      |      Capacity          |");
    System.out.println("===========================================");
    System.out.println("| 1. Single      |     1 Person           |");
    System.out.println("| 2. Double      |     2 People           |");
    System.out.println("| 3. Suite       |     4 People           |");
    System.out.println("===========================================");
  
    int roomTypeOption = 0;
    String roomType = "";
    int roomCapacity = 0;

    // Room type validation (ensure valid option is selected)
    while (roomTypeOption < 1 || roomTypeOption > 3) {
        System.out.print("Enter choice (1-3): ");
        roomTypeOption = sc.nextInt();
        switch (roomTypeOption) {
            case 1:
                roomType = "Single";
                roomCapacity = 1;
                break;
            case 2:
                roomType = "Double";
                roomCapacity = 2;
                break;
            case 3:
                roomType = "Suite";
                roomCapacity = 4;
                break;
            default:
                System.out.println("Invalid choice. Please select a valid room type.");
        }
    }

    System.out.print("Enter the new room price per night: ");
    double roomPrice = sc.nextDouble();

   // Availability is set to "available" automatically
        String roomAvailability = "available";

    // Update room details in the database
    String qry = "UPDATE tbl_room SET r_num = ?, r_type = ?, r_capacity = ?, r_price = ?, r_availability = ? WHERE r_id = ?";
    conf.updateRecord(qry, roomNumber, roomType, roomCapacity, roomPrice, roomAvailability, id);
    System.out.println("-------------------------------------------");
    System.out.println("Room details updated successfully!");
    System.out.println("");
}



   private void deleteRoom() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();
    
    int id = -1;

    // Room ID validation: Ensure the ID exists in the database
    while (id <= 0 || conf.getSingleValue("SELECT r_id FROM tbl_room WHERE r_id = ?", id) == 0) {
        System.out.print("Enter Room ID to delete: ");
        
        // Validate if input is a valid integer
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid numeric Room ID.");
            sc.next(); // Consume the invalid input
            continue;
        }

        id = sc.nextInt();

        // Check if the room ID exists in the database
        if (id <= 0 || conf.getSingleValue("SELECT r_id FROM tbl_room WHERE r_id = ?", id) == 0) {
            System.out.println("Room ID doesn't exist. Please enter a valid Room ID.");
        }
    }

    // SQL query to delete the room record
    String sqlDelete = "DELETE FROM tbl_room WHERE r_id = ?";
    conf.deleteRecord(sqlDelete, id);

    System.out.println("");
    System.out.println("Room successfully deleted.");
    System.out.println("");
}

}

    

