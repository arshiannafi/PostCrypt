
import common.PostCryptAPI;

import java.security.Key;
import java.util.Scanner;

public class Utils {

    private static Scanner keyb = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to PostCrypt Utils");
        mainMenu();
    }

    public static void mainMenu() {
        System.out.println("");
        System.out.println("1. Make key");
        System.out.println("");

        // Get user input
        System.out.print("> ");
        int userInput = Integer.parseInt(keyb.nextLine());
        
        // Execute
        if (userInput == 1) { // Make key
            try {
                // Get filename
                System.out.print("Enter key name: ");
                String filename = keyb.nextLine();
                
                // Make key
                Key key = PostCryptAPI.makeDESKey();
                System.out.println("Key generated");

                // Save key
                PostCryptAPI.saveKey(key, filename);
                System.out.println("Key stored in: " + filename);
                
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

}
