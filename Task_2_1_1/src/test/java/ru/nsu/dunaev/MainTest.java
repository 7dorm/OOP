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
    @Test
    public void testParallelThreadEmptyArray() throws InterruptedException {
        int[] numbers = {};
        assertFalse(PrimeCheckerParallelThread.hasNonPrime(numbers, 2));
    }

    @Test
    public void testParallelThreadAllPrimes() throws InterruptedException {
        int[] numbers = {2, 3, 5, 7, 11};
        assertFalse(PrimeCheckerParallelThread.hasNonPrime(numbers, 2));
    }

    @Test
    public void testParallelThreadWithNonPrime() throws InterruptedException {
        int[] numbers = {2, 3, 4, 7, 11};
        assertTrue(PrimeCheckerParallelThread.hasNonPrime(numbers, 2));
    }

    @Test
    public void testParallelThreadSingleNumber() throws InterruptedException {
        int[] numbers = {9};
        assertTrue(PrimeCheckerParallelThread.hasNonPrime(numbers, 1));
    }

    @Test
    public void testSequentialEmptyArray() {
        int[] numbers = {};
        assertFalse(PrimeCheckerSequential.hasNonPrime(numbers));
    }

    @Test
    public void testSequentialAllPrimes() {
        int[] numbers = {2, 3, 5, 7, 11};
        assertFalse(PrimeCheckerSequential.hasNonPrime(numbers));
    }

    @Test
    public void testSequentialWithNonPrime() {
        int[] numbers = {2, 3, 4, 7, 11};
        assertTrue(PrimeCheckerSequential.hasNonPrime(numbers));
    }

    @Test
    public void testSequentialSingleNumber() {
        int[] numbers = {9};
        assertTrue(PrimeCheckerSequential.hasNonPrime(numbers));
    }

    @Test
    public void testParallelStreamEmptyArray() {
        int[] numbers = {};
        assertFalse(PrimeCheckerParallelStream.hasNonPrime(numbers));
    }

    @Test
    public void testParallelStreamAllPrimes() {
        int[] numbers = {2, 3, 5, 7, 11};
        assertFalse(PrimeCheckerParallelStream.hasNonPrime(numbers));
    }

    @Test
    public void testParallelStreamWithNonPrime() {
        int[] numbers = {2, 3, 4, 7, 11};
        assertTrue(PrimeCheckerParallelStream.hasNonPrime(numbers));
    }

    @Test
    public void testParallelStreamSingleNumber() {
        int[] numbers = {9};
        assertTrue(PrimeCheckerParallelStream.hasNonPrime(numbers));
    }

    @Test
    public void testNegativeNumbers() throws InterruptedException {
        int[] numbers = {-1, -2, -3};
        assertTrue(PrimeCheckerParallelThread.hasNonPrime(numbers, 2));
        assertTrue(PrimeCheckerSequential.hasNonPrime(numbers));
        assertTrue(PrimeCheckerParallelStream.hasNonPrime(numbers));
    }

    @Test
    public void testZeroAndOne() throws InterruptedException {
        int[] numbers = {0, 1};
        assertTrue(PrimeCheckerParallelThread.hasNonPrime(numbers, 2));
        assertTrue(PrimeCheckerSequential.hasNonPrime(numbers));
        assertTrue(PrimeCheckerParallelStream.hasNonPrime(numbers));
    }
}