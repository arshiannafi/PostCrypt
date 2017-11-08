
import common.IncomingMessageListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author arshiannafi
 */
public class Server {

    public static void main(String[] args) {
        // // Get port from args
        //
        // if (args.length != 1) {
        //     System.err.println("Usage: java Server <port number>");
        //     System.exit(1);
        // }
        int portNumber = 50000;

        System.out.println("[Info] Server listening on port: " + portNumber);
        System.out.println("[Info] Waiting for client");

        try {

            // Sockets
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();

            // Streams
            PrintWriter outStream
                    = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader inStream = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            // Client connected
            System.out.println("[Info] Client connected");

            // User
            Scanner keyb = new Scanner(System.in);
            String userInput;

            // Start thread to listen to incomming messages
            IncomingMessageListener incommingMessageListener = new IncomingMessageListener("Client", inStream);
            Thread imlThread = new Thread(incommingMessageListener);
            imlThread.start();

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
        }

    }

}
