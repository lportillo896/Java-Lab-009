import org.apache.commons.codec.digest.Crypt;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 *
 * @author Trevor Hartman
 * @author Logan Portillo
 *
 * @since Version 1.0
 *
 */
public class Crack {
    private final User[] users;
    private final String dictionary;
    public Crack(String shadowFile, String dictionary) throws FileNotFoundException {
        this.dictionary = dictionary;
        this.users = Crack.parseShadow(shadowFile);
    }

    public void crack() throws FileNotFoundException {
        FileInputStream fileStream = new FileInputStream(this.dictionary);
        Scanner console = new Scanner(fileStream);

        while (console.hasNextLine()) {
            String word = console.nextLine();
            for (User user : this.users) {
                String passHash = user.getPassHash();
                if (passHash.contains("$")) {
                    String hash = Crypt.crypt(word, user.getPassHash());
                    if (hash.equals(passHash)) {
                        System.out.println("Found Password "+word+" for User "+user.getUsername());
                    }
                }
            }
        }
        console.close();
    }

    public static int getLineCount(String path) {
        int lineCount = 0;
        try (Stream<String> stream = Files.lines(Path.of(path), StandardCharsets.UTF_8)) {
            lineCount = (int)stream.count();
        } catch(IOException ignored) {}
        return lineCount;
    }

    public static User[] parseShadow(String shadowFile) throws FileNotFoundException {
        FileInputStream fileStream = new FileInputStream(shadowFile);
        Scanner console = new Scanner(fileStream);

        int lines = getLineCount(shadowFile);
        int i = 0;
        User[] users = new User[lines];

        while (console.hasNextLine()) {
            String line = console.nextLine();
            String[] element = line.split(":");
            String username = element[0];
            String passHash = element[1];
            User user = new User(username, passHash);
            users[i] = user;
            i++;
        }
        console.close();
        return users;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Type the path to your shadow file: ");
        String shadowPath = sc.nextLine();
        System.out.print("Type the path to your dictionary file: ");
        String dictPath = sc.nextLine();

        Crack c = new Crack(shadowPath, dictPath);
        c.crack();
    }
}
