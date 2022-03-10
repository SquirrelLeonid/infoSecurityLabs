import java.io.*;

public class FileWorker {
    public static File openFile(String filename) throws FileNotFoundException {
        var file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        try (var inputStream = new BufferedInputStream(new FileInputStream(file))) {
            return inputStream.readAllBytes();
        }
    }

    public static void writeBytesToFile(File file, byte[] bytes) throws IOException {
        try (var outputStream = new BufferedOutputStream(new FileOutputStream(file, false))) {
            outputStream.write(bytes);
        }
    }
}
