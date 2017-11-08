
import common.IncomingMessageListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author arshiannafi
 */
public class Client {

    public static void main(String[] args) {
        // // Get host name and port from args
        //
        // if (args.length != 2) {
        //     System.err.println(
        //             "Usage: java Cleint <host name> <port number>");
        //     System.exit(1);
        // }
        // String hostName = args[0];
        // int portNumber = Integer.parseInt(args[1]);
        //
        // // Manually setting host and port
        String hostName = "127.0.0.1";
        int portNumber = 50000;

        System.out.println("[Info] Connecting to: " + hostName + ":" + portNumber);

        try {

            // Socket
            Socket mySocket = new Socket(hostName, portNumber);

            System.out.println("[Info] Connection established");

            // Streams
            PrintWriter outStream = new PrintWriter(mySocket.getOutputStream(), true);
            BufferedReader inStream = new BufferedReader(
                    new InputStreamReader(mySocket.getInputStream()));

            // User input scanner
            Scanner keyb = new Scanner(System.in);
            String userInput;

            // Start thread to listen to incomming messages
            IncomingMessageListener incommingMessageListener = new IncomingMessageListener("Server", inStream);
            Thread incommingMessageListenerThread = new Thread(incommingMessageListener);
            incommingMessageListenerThread.start();

            while (true) {
                userInput = keyb.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                // Send message
                System.out.println("[Sent] " + userInput);
                outStream.println(userInput);
            }

            // Stop the thread
            incommingMessageListener.terminate();

        } catch (Exception e) {
            System.err.println(e);
            System.out.println("[Info] Ensure that the server on " + hostName + " is running on port " + portNumber);
        }

    }

}
