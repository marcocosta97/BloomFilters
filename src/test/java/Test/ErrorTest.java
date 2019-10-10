package Test;

import bloom.BloomBlocker;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Test di falso positivo.
 * @author Marco Costa
 */
public class ErrorTest {

    private static final double bitToKB = (8 * 1024);

    /* funzione da iterare */
    private static int[] testFunction(ArrayList<String> hosts, ArrayList<String> domains, float pf) throws IOException {
        BloomBlocker blocker = new BloomBlocker(hosts, pf);
        int[] stats = new int[2];
        stats[0] = 0;

        for(String s : domains)
            if(blocker.checkDomain(s)) stats[0]++;

        stats[1] = blocker.dimension();
        return stats;
    }


    public static void main(String[] args) throws IOException {
        ArrayList<String> hosts = BloomBlocker.loadHostfile("hosts");
        ArrayList<String> safe_domains = Utils.parseFile("google_host");
        StringBuilder data = new StringBuilder();

        data.append("pf, errors, dim");
        data.append('\n');

        System.out.println("Elementi nel set: " + hosts.size());
        System.out.println("Elementi di test: " + safe_domains.size());
        System.out.println("************************************");
        for(int i = 6; i >= 2; i--)
        {
            float pf = new Double(Math.pow(10, -i)).floatValue();
            int[] result = testFunction(hosts, safe_domains, pf);
            double KBdimension = result[1] / bitToKB;

            System.out.println("PF: " + pf);
            System.out.println("Dimensione: " + KBdimension + " KB");
            System.out.println("Falsi positivi: " + result[0]);
            System.out.println("************************************");

            data.append((pf * 100) + ", " + result[0] + ", " + KBdimension + "\n");
        }

        Utils.printCSV("error_test_1hash_partial.csv", data);

        for(float pf = 0.1f; pf <= 0.9f; pf += 0.1f)
        {
            int[] result = testFunction(hosts, safe_domains, pf);
            double KBdimension = result[1] / bitToKB;

            System.out.println("PF: " + pf);
            System.out.println("Dimensione: " + KBdimension + " KB");
            System.out.println("Falsi positivi: " + result[0]);
            System.out.println("************************************");

            data.append((pf * 100) + ", " + result[0] + ", " + KBdimension + "\n");
        }

        Utils.printCSV("error_test_1hash_full.csv", data);

    }

}
