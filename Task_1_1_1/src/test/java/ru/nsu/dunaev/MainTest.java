package ru.nsu.dunaev;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void mainTest() {
        Main.main(new String[]{});
        assertTrue(true);
    }
}