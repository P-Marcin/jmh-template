package pl.javowiec.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Warmup(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class Main {

    @Param({"10", "100"})
    private int n;

    @Param({"64", "128"})
    private int bitLength;

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Main.class.getName())
                .build();
        new Runner(options).run();
    }

    @Benchmark
    public List<BigInteger> sumOfPrimes() {
        List<BigInteger> pps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            pps.add(probablePrime());
        }
        return pps;
    }

    @Benchmark
    public List<BigInteger> sumOfPrimesNoResize() {
        List<BigInteger> pps = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            pps.add(probablePrime());
        }
        return pps;
    }

    @Benchmark
    public List<BigInteger> generatePrimes() {
        return IntStream.range(0, n)
                .mapToObj(i -> probablePrime())
                .collect(toList());
    }

    @Benchmark
    public List<BigInteger> generatePrimesParallel() {
        return IntStream.range(0, n)
                .parallel()
                .mapToObj(i -> probablePrime())
                .collect(toList());
    }

    @Benchmark
    public List<BigInteger> generatePrimesParallelUnordered() {
        return IntStream.range(0, n)
                .unordered()
                .parallel()
                .mapToObj(i -> probablePrime())
                .collect(toList());
    }

    @Benchmark
    public List<BigInteger> generatePrimesLimit() {
        return Stream.generate(this::probablePrime)
                .limit(n)
                .collect(toList());
    }

    @Benchmark
    public List<BigInteger> generatePrimesParallelLimit() {
        return Stream.generate(this::probablePrime)
                .parallel()
                .limit(n)
                .collect(toList());
    }

    private BigInteger probablePrime() {
        return BigInteger.probablePrime(bitLength,
                ThreadLocalRandom.current());
    }

}
