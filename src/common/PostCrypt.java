
import java.security.*;
import javax.crypto.*;


public class PostCrypt {

  public static void main(String[] args) throws Exception {
    messageDigest(args);
    messageAuthentication(args);
    privateKey(args);
    publicKey(args);
    digitalSignature(args);
  }


  /* Generate a Message Digest */
  public static void messageDigest(String[] args) throws Exception{

    System.out.println("\n---------------- Message Digest --------------------\n");

    // check args and get plaintext
    if (args.length !=1) {
      System.err.println("Usage: java PostCrypt message");
      System.exit(1);
    }
    byte[] plainText = args[0].getBytes("UTF8");

    // get a message digest object using the MD5 algorithm
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");

    // print out the provider used
    System.out.println("\n" + messageDigest.getProvider().getInfo());

    // calculate the digest and print it out
    messageDigest.update(plainText);
    System.out.println("\nDigest: ");
    System.out.println(new String( messageDigest.digest(), "UTF8"));
  }


  /* Generate a Message Authentication Code */
  public static void messageAuthentication(String[] args) throws Exception{

    System.out.println("\n---------------- Message Authentication --------------------\n");

    byte[] plainText = args[0].getBytes("UTF8");

    // get a key for the HmacMD5 algorithm
    System.out.println("\nStart generating key");
    KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
    SecretKey MD5key = keyGen.generateKey();
    System.out.println("Finish generating key");

    // get a MAC object and update it with the plaintext
    Mac mac = Mac.getInstance("HmacMD5");
    mac.init(MD5key);
    mac.update(plainText);

    // print out the provider used and the MAC
    System.out.println("\n" + mac.getProvider().getInfo());
    System.out.println("\nMAC: ");
    System.out.println(new String(mac.doFinal(), "UTF8"));
  }


  /* Encrypt and decrypt using the DES private key algorithm */
  public static void privateKey(String[] args) throws Exception{

    System.out.println("\n---------------- Generating Private Key --------------------\n");

    byte[] plainText = args[0].getBytes("UTF8");

    // get a DES private key
    System.out.println("\nStart generating DES key");
    KeyGenerator keyGen = KeyGenerator.getInstance("DES");
    keyGen.init(56);
    Key key = keyGen.generateKey();
    System.out.println("Finish generating DES key");

    // get a DES cipher object and print the provider
    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    System.out.println("\n" + cipher.getProvider().getInfo());

    // encrypt using the key and the plaintext
    System.out.println("\nStart encryption");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(plainText);
    System.out.println("Finish encryption: ");
    System.out.println(new String(cipherText, "UTF8"));

    // decrypt the ciphertext using the same key
    System.out.println("\nStart decryption");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] newPlainText = cipher.doFinal(cipherText);
    System.out.println("Finish decryption: " );

    System.out.println(new String(newPlainText, "UTF8"));
  }


  public static void publicKey(String[] args) throws Exception{

    System.out.println("\n---------------- Generating Public Key --------------------\n");

    byte[] plainText = args[0].getBytes("UTF8");

    // generate an RSA key
    System.out.println("\nStart generating RSA key");
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(1024);
    KeyPair key = keyGen.generateKeyPair();
    System.out.println("Finish generating RSA key");

    // get an RSA cipher object and print the provider
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    System.out.println( "\n" + cipher.getProvider().getInfo());

    // encrypt the plaintext using the public key
    System.out.println("\nStart encryption");
    cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
    byte[] cipherText = cipher.doFinal(plainText);
    System.out.println("Finish encryption: ");
    System.out.println(new String(cipherText, "UTF8"));

    // decrypt the ciphertext using the private key
    System.out.println("\nStart decryption");
    cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
    byte[] newPlainText = cipher.doFinal(cipherText);
    System.out.println("Finish decryption: ");
    System.out.println(new String(newPlainText, "UTF8"));
  }

  public static void digitalSignature(String[] args) throws Exception{

    System.out.println("\n---------------- Checking Digital Signature --------------------\n");

    // check args and get plaintext
    if (args.length !=1) {
      System.err.println("Usage: java DigitalSignature1Example text");
      System.exit(1);
    }
    byte[] plainText = args[0].getBytes("UTF8");

    // generate an RSA keypair
    System.out.println( "\nStart generating RSA key" );
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(1024);

    KeyPair key = keyGen.generateKeyPair();
    System.out.println( "Finish generating RSA key" );
    //
    // get a signature object using the MD5 and RSA combo
    // and sign the plaintext with the private key,
    // listing the provider along the way

    Signature sig = Signature.getInstance("MD5WithRSA");
    sig.initSign(key.getPrivate());
    sig.update(plainText);
    byte[] signature = sig.sign();
    System.out.println( sig.getProvider().getInfo() );
    System.out.println( "\nSignature:" );
    System.out.println( new String(signature, "UTF8") );
    //
    // verify the signature with the public key
    System.out.println( "\nStart signature verification" );
    sig.initVerify(key.getPublic());
    sig.update(plainText);
    try {
      if (sig.verify(signature)) {
        System.out.println( "Signature verified" );
      } else System.out.println( "Signature failed" );
    } catch (SignatureException se) {
      System.out.println( "Signature failed" );
    }
  }
}
