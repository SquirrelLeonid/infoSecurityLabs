import java.util.Arrays;

public class Block {
    public byte[] m_bytes;

    Block(byte[] bytes) {
        m_bytes = bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Arrays.equals(m_bytes, block.m_bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(m_bytes);
    }
}