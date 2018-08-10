package sample;

import java.io.File;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Defaults;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zakyalvan
 */
@SpringBootApplication
public class BenchmarkRunner {
  public static void main(String[] args) throws Exception {
    String resultFile = "results".concat(File.separator)
        .concat(InetAddress.getLocalHost().getHostName())
        .concat("-").concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))).concat(".csv");

    Options options = new OptionsBuilder()
        .include(".*")
        .warmupIterations(Defaults.WARMUP_ITERATIONS)
        .measurementIterations(Defaults.MEASUREMENT_ITERATIONS)
        .forks(Defaults.MEASUREMENT_FORKS)
        .threads(Defaults.THREADS)
        .shouldDoGC(true)
        .shouldFailOnError(true)
        .resultFormat(ResultFormatType.CSV)
        .result(resultFile)
        .shouldFailOnError(true)
        .jvmArgs("-server")
        .build();

    new Runner(options).run();
  }
}
