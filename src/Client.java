
import common.IncomingMessageListener;
import common.PostCryptAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.Cipher;

/**
 *
 * @author arshiannafi
 */
public class Client {

    public static String SYSTEM = "PostCrypt: ";
    private static final int PORT = 50000;

    private static Scanner keyb = new Scanner(System.in);

    private static boolean enableConfidentiality = false;
    private static boolean enableIntegrity = false;
    private static boolean enableAuthentication = false;

    public static void main(String[] args) {
        // Welcome message
        System.out.println("Welcome to PostCrypt Client");

        // Let user config the server
        clientConfig();

        // Show user selected config
        printClientConfig();

        // Start the server
        startClient();
    }

    public static void clientConfig() {

        int userInput;

        while (true) {
            System.out.println("");
            System.out.println(SYSTEM + "Enter the number to enable/disable feature");
            System.out.println("");
            System.out.println((enableConfidentiality ? "[Enabled ]" : "[Disabled]") + " 1. Confidentiality");
            System.out.println((enableIntegrity ? "[Enabled ]" : "[Disabled]") + " 2. Integrity");
            System.out.println((enableAuthentication ? "[Enabled ]" : "[Disabled]") + " 3. Authentication");
//            System.out.println("[        ]");
            System.out.println("[        ] 9. Done");
            System.out.println("");

            System.out.print("> ");
            userInput = Integer.parseInt(keyb.nextLine());
            if (userInput == 1) { // Confidentiality
                enableConfidentiality = !enableConfidentiality;
            } else if (userInput == 2) { // Integrity
                enableIntegrity = !enableIntegrity;
            } else if (userInput == 3) { // Authentication
                enableAuthentication = !enableAuthentication;
            } else if (userInput == 9) { // exit
                break;
            }

        } // End of inf while

    }

    public static void printClientConfig() {
        System.out.println("\n\n\n\n\n");
        System.out.println(SYSTEM + "Server will start with the following configuration:");
        System.out.println("");
        System.out.println("\t" + (enableConfidentiality ? "[Enabled ]" : "[Disabled]") + " Confidentiality");
        System.out.println("\t" + (enableIntegrity ? "[Enabled ]" : "[Disabled]") + " Integrity");
        System.out.println("\t" + (enableAuthentication ? "[Enabled ]" : "[Disabled]") + " Authentication");
        System.out.println("");
        System.out.println("\tPort: " + PORT);
        System.out.println("");
    }

    public static void startClient() {

        String hostName = "127.0.0.1";
        int portNumber = 50000;

        System.out.println(SYSTEM + "< Connecting to: " + hostName + ":" + portNumber + " >");

        try {

            // Socket
            Socket mySocket = new Socket(hostName, portNumber);

            // Streams
            PrintWriter outStream = new PrintWriter(mySocket.getOutputStream(), true);
            BufferedReader inStream = new BufferedReader(
                    new InputStreamReader(mySocket.getInputStream()));

            // Exchange keys
            Key key = null;
            if (enableConfidentiality) {
                key = PostCryptAPI.loadKey("key");
            }

            System.out.println(SYSTEM + "< Connection established >");
            System.out.println(SYSTEM + "< Type your message then press enter to send >");

            // User input
            String userInput;

            // Start thread to listen to incomming messages
            IncomingMessageListener incommingMessageListener = new IncomingMessageListener("Server", inStream, enableConfidentiality, key);
            Thread incommingMessageListenerThread = new Thread(incommingMessageListener);
            incommingMessageListenerThread.start();

            while (true) {
                userInput = keyb.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                // Send message
                System.out.println("Client: " + userInput);
                if (enableConfidentiality) {
                    outStream.println(PostCryptAPI.encrypt_DES_EBC_PKCS5(Cipher.ENCRYPT_MODE, userInput.getBytes(), key));
                } else {
                    outStream.println(userInput);
                }
            }

            // Stop the thread
            incommingMessageListener.terminate();

        } catch (Exception e) {
            System.err.println(e);
            System.out.println(SYSTEM + "< Ensure that the server on " + hostName + " is running on port " + portNumber + " >");
        }

    }

}
