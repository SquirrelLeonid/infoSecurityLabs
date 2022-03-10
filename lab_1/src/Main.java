import java.io.*;
import java.security.*;

public class Main {
    public static void main(String[] args) {
        try {
            executeCommand(args);
        } catch (IOException ex) {
            System.out.println("I/O exception");
            ex.printStackTrace();
        } catch (GeneralSecurityException ex) {
            System.out.println("bad at security");
            ex.printStackTrace();
        }
    }

    private static void executeCommand(String[] args) throws
            IOException,
            GeneralSecurityException{
        var cryptographer = new Cryptographer();

        switch (args[0]) {
            case "prepare" -> cryptographer.prepare(args[1]);
            case "encode" -> cryptographer.encode(args[1]);
            case "translate" -> cryptographer.translate();
            case "decode" -> cryptographer.decode(args[1]);
            default -> System.out.println("unknown command");
        }
    }
}
