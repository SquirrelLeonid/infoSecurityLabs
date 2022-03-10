import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Cryptographer {

    private final ByteBlockWorker blockWorker;
    private static final int DEFAULT_DICTIONARY_SIZE = 256;
    private static final File DICTIONARY_FILE_NAME = new File("dictionary.txt");
    private static final File TRANSLATION_FILE_NAME = new File("translate.txt");

    public Cryptographer()
    {
        blockWorker = new ByteBlockWorker();
    }

    public void prepare(String filename) throws IOException {
        var file = FileWorker.openFile(filename);
        var bytes = FileWorker.getBytesFromFile(file);
        var paddedBlocks = blockWorker.addPadToBlocks(bytes);

        FileWorker.writeBytesToFile(file, blockWorker.blocksToBytes(paddedBlocks));
        createBlockDictionary();
    }

    public void encode(String filename) throws
            IOException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {
        var file = FileWorker.openFile(filename);
        var cipher = EncryptionAES.initCipher();
        EncryptionAES.encodeFile(cipher, file, DICTIONARY_FILE_NAME);
    }

    public void translate() throws IOException {
        var encodedDictBlocks = blockWorker.getBlocksFromFile(DICTIONARY_FILE_NAME);
        var originalDictBlocks = getDictionaryBlocks();
        var translationBlocks = new Block[originalDictBlocks.length + encodedDictBlocks.length];

        for (int i = 0; i < encodedDictBlocks.length; i++) {
            translationBlocks[i * 2] = encodedDictBlocks[i];
            translationBlocks[i * 2 + 1] = originalDictBlocks[i];
        }

        FileWorker.writeBytesToFile(TRANSLATION_FILE_NAME, blockWorker.blocksToBytes(translationBlocks));
    }

    public void decode(String filename) throws IOException {
        var file = FileWorker.openFile(filename);
        var encodedBlocks = blockWorker.getBlocksFromFile(file);
        var decodedBlocks = new Block[encodedBlocks.length];

        var translationBlocks = blockWorker.getBlocksFromFile(TRANSLATION_FILE_NAME);
        var translationMap = createTranslationMap(translationBlocks);

        for (int i = 0; i < encodedBlocks.length; i++) {
            decodedBlocks[i] = translationMap.get(encodedBlocks[i]);
        }

        var decodedBytes = blockWorker.blocksToBytes(decodedBlocks);
        var originalBytes = blockWorker.removePadding(decodedBytes);
        FileWorker.writeBytesToFile(file, originalBytes);
    }

    private void createBlockDictionary() throws IOException {
        FileWorker.writeBytesToFile(DICTIONARY_FILE_NAME, blockWorker.blocksToBytes(getDictionaryBlocks()));
    }

    private Block[] getDictionaryBlocks() {
        var bytes = new byte[DEFAULT_DICTIONARY_SIZE];

        for (int i = 0; i < DEFAULT_DICTIONARY_SIZE; i++) {
            bytes[i] = (byte)i;
        }

        return blockWorker.addPadToBlocks(bytes);
    }

    private HashMap<Block, Block> createTranslationMap(Block[] translationBlocks) {
        var translationMap = new HashMap<Block,Block>();

        for (int i = 0; i < translationBlocks.length; i += 2) {
            translationMap.put(translationBlocks[i], translationBlocks[i + 1]);
        }

        return translationMap;
    }
}
