package ru.nsu.dunaev;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class HeapSortTest{

    @Test
    void heapsortTest1() {
        HeapSort hp = new HeapSort();
        assert Arrays.equals(hp.heapsort(new int[] {5, 4, 3, 2, 1}),
                new int[]{1, 2, 3, 4, 5});
    }

    @Test
    void heapsortTest2() {
        HeapSort hp = new HeapSort();
        assert Arrays.equals(hp.heapsort(new int[] {-3, 0, 4, -3, -10}),
                new int[]{-10, -3, -3, 0, 4});
    }

    @Test
    void heapsortTest3() {
        HeapSort hp = new HeapSort();
        assert Arrays.equals(hp.heapsort(new int[] {-1, -20, -30, -4, -3}),
                new int[]{-30, -20, -4, -3, -1});
    }
}