package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class PostCryptAPI {

    public static Key makeDESKey() {
        Key key = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
            keyGen.init(56);
            key = keyGen.generateKey();
        } catch (Exception e) {
            System.out.println(e);
        }
        return key;
    }

    public static void saveKey(Key key, String filename) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(filename)));
            objectOutputStream.writeObject(key);
            objectOutputStream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Key loadKey(String filename) {
        Key key = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(filename)));
            key = (Key) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return key;
    }

    /**
     * This function encryptes and decryptes text using DES, EBC, and PKCS5
     * Padding.
     *
     * @param mode Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
     * @param plainText
     * @param key
     * @return
     */
    public static byte[] encrypt_DES_EBC_PKCS5(int mode, byte[] plainText, Key key) {
        byte[] cipherText = null;
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            if (mode == Cipher.ENCRYPT_MODE) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }

            cipherText = cipher.doFinal(plainText);
        } catch (Exception e) {
            System.out.println(e);
        }
        return cipherText;
    }

}
