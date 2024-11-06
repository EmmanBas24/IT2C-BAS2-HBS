
package bookingsys;

import static bookingsys.Config.connectDB;
import java.util.Scanner;


public class MAIN {

   
        public static void main(String[] args) {
        
            
        connectDB();   
        Scanner sc = new Scanner(System.in); 
        String op = null;
      
       do{
        System.out.println("----- HOTEL BOOKING SYSTEM -----");
        System.out.println("-------------------------------------");
        System.out.println("1. Manage Guest");
        System.out.println("2. Manage Room");
        System.out.println("3. View Available Rooms");
        System.out.println("4. Book a Room");
        System.out.println("5. Exit");
        System.out.println("-------------------------------------");

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
            break;
           
            case 4:
            break;
            
            case 5:
                System.exit(0);
                break;
            
    }     System.out.print("Do you want to continue?(yes/no): ");
            op=sc.next();
        }while(op.equals("yes")|| op.equals("no"));
         System.out.println("THANK YOU ");
    }     
    }
    

