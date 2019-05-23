package com.zkr.study.java.jdk_concurrent_3.jmh_3_4;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author zhukerui
 * @date 2019-05-23 17:30
 * @className JMHSample
 * @description java微基准测试框架JMH
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class JMHSample {
    @Benchmark
    public void wellHelloThere() {

    }

    public static void main(String[] args) throws RunnerException {
        Options option = new OptionsBuilder().include(JMHSample.class.getSimpleName())
                .forks(1).build();
        new Runner(option).run();
    }
}
