package Test;

import bloom.BloomBlocker;
import bloom.BloomSet;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.IntStream;

/**
 * @author Marco Costa
 */
public class SpeedTest {

    private static final int N_ITERATIONS = 10;


    private static void loadStructure(ArrayList<String> arraySet, HashSet<String> hashSet,
                                      ArrayList<String> hosts) throws IOException {

        int n = hosts.size();
        for(int i = 0; i < n; i++)
        {
            String o = hosts.get(i);
            hashSet.add(o);
            arraySet.add(o);
        }
    }

    private static double testFunction(Collection<String> set,
                                       String[] domains, int n) {

        long[] timeData = new long[3];
        long startTime, endTime;

        startTime = System.nanoTime();
        for(int i = 0; i < n; i++) {
            boolean contains = set.contains(domains[i]);
        }
        endTime = System.nanoTime();

        return (endTime - startTime) / 1000000.d; // nanosec -> msec

    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> hosts = BloomBlocker.loadHostfile("hosts");

        ArrayList<String> domainsList = Utils.parseFile("google_host");
        String[] domains = new String[domainsList.size()];
        domains = domainsList.toArray(domains);

        BloomSet<String> bloomSet = new BloomSet<>(hosts.size(), 0.000001);
        bloomSet.addAll(hosts);

        HashSet<String> hashSet = new HashSet<>(hosts.size());
        ArrayList<String> arraySet = new ArrayList<>();

        loadStructure(arraySet, hashSet, hosts);

        StringBuilder hashData = new StringBuilder();
        StringBuilder arrayData = new StringBuilder();
        StringBuilder bloomData = new StringBuilder();

        hashData.append("n, time\n");
        arrayData.append("n, time\n");
        bloomData.append("n, time\n");

        for(int i = 1; i < domains.length; i += 2000)
        {
            double[] times = IntStream.range(0, 3).mapToDouble(k -> 0.d).toArray();

            for(int j = 0; j < N_ITERATIONS; j++)
            {
                times[0] += testFunction(arraySet, domains, i);
                times[1] += testFunction(hashSet, domains, i);
                times[2] += testFunction(bloomSet, domains, i);
            }

            for(int k = 0; k < times.length; k++)
                times[k] /= N_ITERATIONS;

            System.out.println("ARRAY - " + i + ": " + times[0]);
            System.out.println("HASH - " + i + ": " + times[1]);
            System.out.println("BLOOM - " + i + ": " + times[2]);
            System.out.println("*************************************************");

            arrayData.append(i + ", " + times[0] +'\n');
            hashData.append(i + ", " + times[1] +'\n');
            bloomData.append(i + ", " + times[2] +'\n');
        }

        double[] times = IntStream.range(0, 3).mapToDouble(k -> 0.d).toArray();
        int i = domains.length;

        for(int j = 0; j < N_ITERATIONS; j++)
        {
            times[0] += testFunction(arraySet, domains, i);
            times[1] += testFunction(hashSet, domains, i);
            times[2] += testFunction(bloomSet, domains, i);
        }

        for(int k = 0; k < times.length; k++)
            times[k] /= N_ITERATIONS;

        System.out.println("ARRAY - " + i + ": " + times[0]);
        System.out.println("HASH - " + i + ": " + times[1]);
        System.out.println("BLOOM - " + i + ": " + times[2]);
        System.out.println("*************************************************");

        arrayData.append(i + ", " + times[0] +'\n');
        hashData.append(i + ", " + times[1] +'\n');
        bloomData.append(i + ", " + times[2] +'\n');


        Utils.printCSV("speed_test_array.csv", arrayData);
        Utils.printCSV("speed_test_hash.csv", hashData);
        Utils.printCSV("speed_test_bloom.csv", bloomData);
    }
}


