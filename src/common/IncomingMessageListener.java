package common;

import java.io.BufferedReader;

/**
 *
 * @author arshiannafi
 */
public class IncomingMessageListener implements Runnable {

    /**
     * Controls the lifecycle of the thread
     */
    private boolean alive;
    /**
     * Name of the sender. E.g. Server, Client, Alice, Bob etc.
     */
    private String sender;
    private BufferedReader inStream;

    /**
     * Constructor
     *
     * @param sender - Name of the message sender. E.g. Server or Client
     * @param inStream - valid BufferedReader to read from
     */
    public IncomingMessageListener(String sender, BufferedReader inStream) {
        this.alive = true;
        this.sender = sender;
        this.inStream = inStream;
    }
    
    @Override
    public void run() {
        String receivedMessage;
        try {
            while (this.alive && (receivedMessage = inStream.readLine()) != null) {
                System.out.println(sender + ": " + receivedMessage);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Use this function to treminate the thread.
     */
    public void terminate() {
        this.alive = false;
    }

}
