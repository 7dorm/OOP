package ru.nsu.dunaev;

public class PrimeCheckerParallelThread {
    private static boolean hasNonPrime;
    private static final Object lock = new Object();

    public static boolean hasNonPrime(int[] numbers, int numThreads) throws InterruptedException {
        hasNonPrime = false;
        Thread[] threads = new Thread[numThreads];
        int chunkSize = numbers.length / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? numbers.length : start + chunkSize;
            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (!isPrime(numbers[j])) {
                        synchronized (lock) {
                            hasNonPrime = true;
                        }
                        for (Thread thread : threads) {
                            thread.interrupt();
                        }
                        return;
                    }
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return hasNonPrime;
    }

    private static boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        int sqrt = (int) Math.sqrt(num);
        for (int i = 3; i <= sqrt; i += 2) {
            if (num % i == 0) return false;
        }
        return true;
    }
}