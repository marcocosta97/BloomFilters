package Test;

import bloom.BloomBlocker;
import bloom.BloomSet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark JMH per BloomBlocker.
 * @author Marco Costa
 */
public class BloomSpeedTestJMH {
    private static final double pf = 0.0000001; // K = 24

    @State(Scope.Benchmark)
    public static class MyState {
        public String[] domains;
        public BloomSet<String> set;

        @Param({"1","2001","4001","6001","8001","10001","12001","14001","16331"})
        public int n;

        @Setup(Level.Trial) /* setup effettuato una volta */
        public void doSetup() throws IOException {
            ArrayList<String> hosts = BloomBlocker.loadHostfile("hosts");
            ArrayList<String> domainsList = Utils.parseFile("google_host");
            domains = new String[domainsList.size()];
            domains = domainsList.toArray(domains);

            set = new BloomSet<>(hosts.size(), pf);
            set.addAll(hosts);
        }

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean[] testFunction(MyState s) {

        boolean[] res = new boolean[s.domains.length];

        for(int i = 0; i < s.n; i++) {
            res[i] = s.set.contains(s.domains[i]);
        }

        return res; /* evita dead code */
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BloomSpeedTestJMH.class.getSimpleName())
                .shouldDoGC(true)
                .resultFormat(ResultFormatType.CSV)
                .result("speed_test_bloom.csv")
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}


