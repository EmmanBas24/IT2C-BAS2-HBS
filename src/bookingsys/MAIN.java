
package bookingsys;

import static bookingsys.Config.connectDB;
import java.util.Scanner;


public class MAIN {

   
        public static void main(String[] args) {
        
            
        connectDB();   
        Scanner sc = new Scanner(System.in); 
        String op = null;
        boolean exit = true;
      
       do{
                // Display the Hotel Booking System menu in a box
            System.out.println("+----------------------------+");
            System.out.println("|   HOTEL BOOKING SYSTEM     |");
            System.out.println("+----------------------------+");
            System.out.println("| 1. Manage Guest            |");
            System.out.println("| 2. Manage Room             |");
            System.out.println("| 3. Book a Room             |");
            System.out.println("| 4. Records                 |");
            System.out.println("| 5. Exit                    |");
            System.out.println("+----------------------------+");
            System.out.print("Enter Action: ");
            int option = sc.nextInt();


        while(option > 5){
            System.out.print("Invalid Action. Try Again: ");
            option = sc.nextInt();
         
        } switch(option){

            case 1: 
                guest guest = new guest();
                guest.manageGuests();
            break;
          
            case 2:
                room r = new room();
                r.manageRooms();
            break;
          
            case 3:
                booking b = new booking();
                b.manageBookings();
            break;
           
            case 4:
            break;
            
            case 5:
                System.out.print("Exit Selected. Type (yes) to continue: ");
                String response = sc.next();
                if(response.equalsIgnoreCase("yes")){
                exit = false;
                }
                break;
          
                
    }     
        }while(exit);
       
            System.out.println("Program Close");
        sc.close(); 
    }     

} 

