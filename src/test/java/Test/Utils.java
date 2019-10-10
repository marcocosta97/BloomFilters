package Test;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Marco Costa
 */
public class Utils {
    private static final String regex = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)"
            + "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

    public static void printCSV(String filename, StringBuilder string) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {

            writer.write(string.toString());

            System.out.println("[!!] Stampato su " + filename);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    /* parsing del file di test */
    public static ArrayList<String> parseFile(String filename) throws IOException {
        ArrayList<String> domains = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(regex);

                if(lines.length < 2)
                    continue;
                domains.add(lines[1].trim());
            }

            return domains;
        }
    }

}
