package Test;

import bloom.BloomBlocker;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Test di calcolo dimensioni in memoria.
 * @author Marco Costa
 */
public class SpaceTest {

    private static double[] testFunction(ArrayList<String> hosts, int n) throws IOException {
        HashSet<String> hashSet = new HashSet<>(n);
        LinkedList<String> arraySet = new LinkedList<>();

        double[] data = new double[2];

        for(int i = 0; i < n; i++)
        {
            String o = hosts.get(i);
            hashSet.add(o);
            arraySet.add(o);
        }

        data[0] = ObjectSizeCalculator.getObjectSize(hashSet) / 1024.f; // Byte -> KB
        data[1] = ObjectSizeCalculator.getObjectSize(arraySet) / 1024.f;

         return data;
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> hosts = BloomBlocker.loadHostfile("hosts");

        StringBuilder hashData = new StringBuilder();
        StringBuilder arrayData = new StringBuilder();

        hashData.append("n, dim\n");
        arrayData.append("n, dim\n");

        for(int i = 1; i < hosts.size(); i += 1000)
        {
            double[] KBsize = testFunction(hosts, i);

            hashData.append(i + ", " + KBsize[0] + "\n");
            arrayData.append(i + "," + KBsize[1] + "\n");
        }

        int size = hosts.size();
        double[] KBsize = testFunction(hosts, size);

        hashData.append(size + ", " + KBsize[0] + "\n");
        arrayData.append(size + ", " + KBsize[1] + "\n");

        Utils.printCSV("size_test_hash.csv", hashData);
        Utils.printCSV("size_test_array.csv", arrayData);
    }
}