package Test;

import bloom.BloomBlocker;
import bloom.BloomSet;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Marco Costa
 */
public class GuavaSpeedTestJMH {
    @State(Scope.Benchmark)
    public static class MyState {
        public String[] domains;
        public BloomFilter<String> set;

        @Param({"1","2001","4001","6001","8001","10001","12001","14001","16001","16331"})
        public int n;

        @Setup(Level.Trial)
        public void doSetup() throws IOException {
            ArrayList<String> hosts = BloomBlocker.loadHostfile("hosts");
            ArrayList<String> domainsList = Utils.parseFile("google_host");
            domains = new String[domainsList.size()];
            domains = domainsList.toArray(domains);

            set = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), hosts.size(), 0.0000001);
            for(String s : hosts)
                set.put(s);
        }

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean[] testFunction(MyState s) {

        boolean[] res = new boolean[s.domains.length];

        for(int i = 0; i < s.n; i++) {
            res[i] = s.set.mightContain(s.domains[i]);
        }

        return res; /* evita dead code */
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(GuavaSpeedTestJMH.class.getSimpleName())
                .shouldDoGC(true)
                .resultFormat(ResultFormatType.CSV)
                .result("speed_test_guava.csv")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
