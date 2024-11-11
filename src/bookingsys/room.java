
package bookingsys;

import java.util.Scanner;


public class room {
   
   
    public void manageRooms(){
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();
        int choice;
        String response;
         room r = new room();
        
        do {
          
            System.out.println("+----------------------------+");
            System.out.println("|   Room Management Menu     |");
            System.out.println("+----------------------------+");
            System.out.println("| 1. Add Room                |");
            System.out.println("| 2. View Rooms              |");
            System.out.println("| 3. Update Room             |");
            System.out.println("| 4. Delete Room             |");
            System.out.println("| 5. Exit                    |");
            System.out.println("+----------------------------+");
            System.out.print("Enter your choice: ");
            int option = sc.nextInt();

            
            switch (option) {
                case 1:
                    r.addRoom();
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
                 
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
     System.out.print("Do you want to continue? (yes/no):  ");
            response = sc.next();
        } while (response.equalsIgnoreCase("yes"));  // Continue until user chooses to exit
        System.out.println("");
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

   
    public void viewRooms() {
        String cqry = "SELECT * FROM tbl_room";
        String[] Headers = {"ID","Room Number", "Room Type", "Capacity", "Price", "Status"};
        String[] Columns = {"r_id" ,"r_num", "r_type", "r_capacity", "r_price", "r_availability"};
        Config conf = new Config();
        conf.viewRecords(cqry, Headers, Columns);
    }

  
private void updateRoom() {
    Scanner sc = new Scanner(System.in);
    Config conf = new Config();
    
  
    System.out.print("Enter Room ID to update: ");
    int id = sc.nextInt();
    
  
    while (conf.getSingleValue("SELECT r_id FROM tbl_room WHERE r_id = ?", id) == 0) {
        System.out.println("Selection ID doesn't exist.");
        System.out.print("Select Room ID again: ");
        id = sc.nextInt(); 
    }
    
    
    System.out.print("Enter the new room number: ");
    String roomNumber = sc.next();
    System.out.print("Enter the new room type: ");
    String roomType = sc.next();
    System.out.print("Enter the new room capacity: ");
    int roomCapacity = sc.nextInt();
    System.out.print("Enter the new room price: ");
    double roomPrice = sc.nextDouble();
    System.out.print("Enter the new room availability: ");
    String roomAvailability = sc.next();
    
    String qry = "UPDATE tbl_room SET r_num = ?, r_type = ?, r_capacity = ?, r_price = ?, r_availability = ? WHERE r_id = ?";
    
    conf.updateRecord(qry, roomNumber, roomType, roomCapacity, roomPrice, roomAvailability, id); 
}


    // Delete an existing room
    private void deleteRoom() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Room ID to delete: ");
        int id = sc.nextInt();

        String sqlDelete = "DELETE FROM tbl_room WHERE r_id = ?";
        Config conf = new Config();
        conf.deleteRecord(sqlDelete, id);
    }
}

    

