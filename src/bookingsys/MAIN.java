package bookingsys;

import static bookingsys.Config.connectDB;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MAIN {

    public static void main(String[] args) {

        connectDB();
        Scanner sc = new Scanner(System.in);
        String op = null;
        boolean exit = true;

        do {
         
            System.out.println("==================================");
            System.out.println("|      HOTEL BOOKING SYSTEM      |");
            System.out.println("==================================");
            System.out.println("| 1. Manage Guest Information    |");
            System.out.println("| 2. Manage Room Information     |");
            System.out.println("| 3. Manage Booking              |");
            System.out.println("| 4. Records                     |");
            System.out.println("| 5. Exit                        |");
            System.out.println("==================================");
            System.out.print("Enter Action: ");

            int option = -1;  // Default invalid value
            boolean validOption = false;

            // Validate option input
            while (!validOption) {
                try {
                    option = sc.nextInt();

                    // Check if the input is valid (1 to 5)
                    if (option >= 1 && option <= 5) {
                        validOption = true;
                    } else {
                        System.out.print("Invalid option. Please choose between 1 and 5: ");
                    }

                } catch (InputMismatchException e) {
                    System.out.print("Invalid input. Please enter a number between 1 and 5: ");
                    sc.nextLine();  // Clear the invalid input
                }
            }

            switch (option) {
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
                    Record re = new Record();
                    re.displayMenu();
                    break;

                case 5:
                    System.out.print("Exit Selected. Type (yes) to confirm: ");
                    String response = sc.next();
                    System.out.println("");
                    if (response.equalsIgnoreCase("yes")) {
                        exit = false;
                    }
                    break;
            }

        } while (exit);

        System.out.println("Program Close");
        sc.close();
    }
}
