package common;

import common.PostCryptAPI;

import java.io.BufferedReader;
import java.security.Key;
import javax.crypto.Cipher;

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
    private boolean enableConfidentiality = false;
    private Key key;

    /**
     * Constructor
     *
     * @param sender - Name of the message sender. E.g. Server or Client
     * @param inStream - valid BufferedReader to read from
     */
    public IncomingMessageListener(String sender, BufferedReader inStream, boolean enableConfidentiality, Key key) {
        this.alive = true;
        this.sender = sender;
        this.inStream = inStream;
        this.enableConfidentiality = enableConfidentiality;
        this.key = key;
    }

    @Override
    public void run() {
        String receivedMessage;
        try {
            while (this.alive && (receivedMessage = inStream.readLine()) != null) {
                if (enableConfidentiality) {
                    byte[] dec = PostCryptAPI.encrypt_DES_EBC_PKCS5(Cipher.DECRYPT_MODE, receivedMessage.getBytes(), key);
                    System.out.println(sender + ": " + new String(dec));
                } else {
                    System.out.println(sender + ": " + receivedMessage);
                }
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
