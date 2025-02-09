package ru.nsu.dunaev;

public class PrimeCheckerParallelStream {
    public static boolean hasNonPrime(int[] numbers) {
        return java.util.Arrays.stream(numbers).parallel().anyMatch(num -> !isPrime(num));
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
