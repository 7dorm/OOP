package ru.nsu.dunaev;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final int[] testData = {20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053};

    @Test
    public void MainTest() {
        Main.main(new String[]{});
    }

    @Test
    public void SeqTest() {
        long startTime = System.currentTimeMillis();
        boolean resultSequential = PrimeCheckerSequential.hasNonPrime(this.testData);
        long endTime = System.currentTimeMillis();
        System.out.println("Sequential: " + resultSequential + " Time: " + (endTime - startTime) + " ms");
    }

    @Test
    public void ThreadTest() {
        try {
            long startTime = System.currentTimeMillis();
            boolean resultParallelThread = PrimeCheckerParallelThread.hasNonPrime(this.testData, 4);
            long endTime = System.currentTimeMillis();
            System.out.println("Parallel Thread: " + resultParallelThread + " Time: " + (endTime - startTime) + " ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ParallelTest() {
        long startTime = System.currentTimeMillis();
        boolean resultParallelStream = PrimeCheckerParallelStream.hasNonPrime(this.testData);
        long endTime = System.currentTimeMillis();
        System.out.println("Parallel Stream: " + resultParallelStream + " Time: " + (endTime - startTime) + " ms");
    }
}