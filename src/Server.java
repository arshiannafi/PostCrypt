
import common.IncomingMessageListener;
import common.PostCryptAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
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
public class Server {

    public static String SYSTEM = "PostCrypt: ";
    private static final int PORT = 50000;

    private static Scanner keyb = new Scanner(System.in);

    private static boolean enableConfidentiality = false;
    private static boolean enableIntegrity = false;
    private static boolean enableAuthentication = false;

    public static void main(String[] args) {

        // Welcome message
        System.out.println("Welcome to PostCrypt Server");

        // Let user config the server
        serverConfig();

        // Show user selected config
        printServerConfig();

        // Start the server
        startServer();

    }

    public static void serverConfig() {

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

    public static void printServerConfig() {
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

    public static void startServer() {

        Key key = null;
        if (enableConfidentiality) {
            key = PostCryptAPI.makeDESKey();
            PostCryptAPI.saveKey(key, "key");
        }

        // TODO if server key pair DNE, make them
        System.out.println(SYSTEM + "< Server listening on port: " + PORT + " >");
        System.out.println(SYSTEM + "< Waiting for client >");

        try {

            // Sockets
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();

            // Streams
            PrintWriter outStream
                    = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader inStream = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            // Client connected
            System.out.println(SYSTEM + "< Client connected >");
            System.out.println(SYSTEM + "< Type your message then press enter to send >");

            // User
            String userInput;

            // Start thread to listen to incomming messages
            IncomingMessageListener incommingMessageListener = new IncomingMessageListener("Client", inStream, enableConfidentiality, key);
            Thread imlThread = new Thread(incommingMessageListener);
            imlThread.start();

            while (true) {
                userInput = keyb.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                // Send message
                System.out.println("Server: " + userInput);
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
        }

    }

}
