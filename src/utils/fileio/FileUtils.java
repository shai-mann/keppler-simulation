package utils.fileio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {

    public static String loadAsString(String path) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(FileUtils.class.getResourceAsStream(path)));
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Invalid file path provided.");
        }
        return result.toString();
    }

}
