package com.zkr.study.java.jdk_concurrent_3.jmh_3_4;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhukerui
 * @date 2019-05-24 9:55
 * @className JmhMapDemo
 * @description JMh测试HashMap, concurrentHashMap和同步控制的HashMap三者在size()和get()上的性能比较
 */
@State(value = Scope.Benchmark)
public class JmhMapDemo {
    private static Map hashMap = new HashMap();
    private static Map concurrentHashMap = new ConcurrentHashMap();
    private static Map synchronizedHashMap = Collections.synchronizedMap(new HashMap<>());
    private static String getValue = "4";

    @Setup
    public void setup() {
        int count = 10000;
        for (int i = 0; i < count; i++) {
            hashMap.put(Integer.toString(i), Integer.toString(i));
            concurrentHashMap.put(Integer.toString(i), Integer.toString(i));
            synchronizedHashMap.put(Integer.toString(i), Integer.toString(i));
        }
    }

    @Benchmark
    public void hashMapGet() {
        hashMap.get(getValue);
    }

    @Benchmark
    public void synchronizedHashMapGet() {
        synchronizedHashMap.get(getValue);
    }

    @Benchmark
    public void concurrentHashMapGet() {
        concurrentHashMap.get(getValue);
    }

    @Benchmark
    public void hashMapSize() {
        hashMap.size();
    }

    @Benchmark
    public void synchronizedHashMapSize() {
        synchronizedHashMap.size();
    }

    @Benchmark
    public void concurrentHashMapSize() {
        concurrentHashMap.size();
    }

    public static void main(String[] args) throws RunnerException {
        Options option = new OptionsBuilder().include(JmhMapDemo.class.getSimpleName())
                .forks(1).build();
        new Runner(option).run();
    }
}
