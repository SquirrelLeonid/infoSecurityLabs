import javax.crypto.*;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptionAES {
    private static final int DEFAULT_KEY_SIZE = 256;

    public static void encodeFile(Cipher cipher, File... files) throws
            IOException,
            BadPaddingException,
            IllegalBlockSizeException {
        for (File file : files) {
            var encodedBytes = cipher.doFinal(FileWorker.getBytesFromFile(file));
            FileWorker.writeBytesToFile(file, encodedBytes);
        }
    }

    public static Cipher initCipher() throws
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException {
        var cipher = Cipher.getInstance("AES/ECB/NoPadding");
        var keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(DEFAULT_KEY_SIZE, new SecureRandom());
        var secretKey = keyGenerator.generateKey();

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher;
    }
}
