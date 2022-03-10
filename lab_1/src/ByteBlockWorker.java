import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ByteBlockWorker {
    private static final int DEFAULT_BLOCK_SIZE = 16;

    public Block[] addPadToBlocks(byte[] bytes) {
        var blocks = new Block[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            var blockBytes = new byte[DEFAULT_BLOCK_SIZE];
            blockBytes[0] = bytes[i];
            blocks[i] = new Block(blockBytes);
        }

        return blocks;
    }

    public byte[] removePadding(byte[] padBytes) {
        var bytes = new byte[padBytes.length / DEFAULT_BLOCK_SIZE];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = padBytes[i * DEFAULT_BLOCK_SIZE];
        }

        return bytes;
    }

    public Block[] getBlocksFromFile(File file) throws IOException {
        return bytesToBlocks(FileWorker.getBytesFromFile(file));
    }

    public byte[] blocksToBytes(Block[] blocks) {
        var bytes = new byte[blocks.length * DEFAULT_BLOCK_SIZE];

        for (int i = 0; i < blocks.length; i++) {
            var start = i * DEFAULT_BLOCK_SIZE;
            System.arraycopy(blocks[i].m_bytes, 0, bytes, start, DEFAULT_BLOCK_SIZE);
        }

        return bytes;
    }

    public Block[] bytesToBlocks(byte[] bytes) throws IOException {
        if (bytes.length % DEFAULT_BLOCK_SIZE != 0) {
            throw new IOException();
        }

        var numBlocks = bytes.length / DEFAULT_BLOCK_SIZE;
        var blocks = new Block[numBlocks];

        for (int i = 0; i < numBlocks; i++) {
            var start = i * DEFAULT_BLOCK_SIZE;
            var end = start + DEFAULT_BLOCK_SIZE;
            var range = Arrays.copyOfRange(bytes, start, end);
            blocks[i] = new Block(range);
        }

        return blocks;
    }
}
