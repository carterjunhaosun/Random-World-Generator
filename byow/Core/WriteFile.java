package byow.Core;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.*;

/**
 * @source https://www.w3schools.com/java/java_files_create.asp
 */

public class WriteFile {
    private String seed;
    private String moves;
    public WriteFile(String stringSeed, ArrayList<Integer> moves) {

    }
    public static void main(String[] args) {
        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write("Files in Java might be tricky, but it is fun enough!");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
