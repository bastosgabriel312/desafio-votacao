package br.com.db.desafio_votacao.v1.performance;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@State(Scope.Benchmark)
public class JMHConfig {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AssociadoServicePerformanceTest.class.getSimpleName())
                .resultFormat(ResultFormatType.TEXT)
                .include(PautaServicePerformanceTest.class.getSimpleName())
                .resultFormat(ResultFormatType.TEXT)
                .include(SessaoVotacaoServicePerformanceTest.class.getSimpleName())
                .resultFormat(ResultFormatType.TEXT)
                .include(VotoServicePerformanceTest.class.getSimpleName())
                .result("results/results.txt")
                .resultFormat(ResultFormatType.TEXT)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
