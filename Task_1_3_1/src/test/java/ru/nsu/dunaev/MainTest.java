package ru.nsu.dunaev;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private static final String TEST_FILE = "test_input.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Set up test file before each test
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TEST_FILE), StandardCharsets.UTF_8))) {
            writer.write("абракадабра"); // Test content
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up test file after each test
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testFindSubstringMultipleOccurrences() throws IOException {
        List<Integer> result = Main.find(TEST_FILE, "бра");
        assertEquals(List.of(1, 8), result);
    }

    @Test
    void testFindSubstringSingleOccurrence() throws IOException {
        // Overwrite test file with different content
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TEST_FILE), StandardCharsets.UTF_8))) {
            writer.write("hello world");
        }
        List<Integer> result = Main.find(TEST_FILE, "world");
        assertEquals(List.of(6), result);
    }

    @Test
    void testFindSubstringAtBoundaries() throws IOException {
        // Overwrite test file with content where substring is at boundaries
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TEST_FILE), StandardCharsets.UTF_8))) {
            writer.write("abraabraabra"); // "abra" at start, middle, and end
        }
        List<Integer> result = Main.find(TEST_FILE, "abra");
        assertEquals(List.of(0, 4, 8), result);
    }

    @Test
    void testFindSubstringNotPresent() throws IOException {
        List<Integer> result = Main.find(TEST_FILE, "xyz");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindEmptySubstring() throws IOException {
        List<Integer> result = Main.find(TEST_FILE, "");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindInLargeFile() throws IOException {
        // Create a large file for testing
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TEST_FILE), StandardCharsets.UTF_8))) {
            for (int i = 0; i < 10000; i++) {
                writer.write("abra");
            }
        }
        List<Integer> result = Main.find(TEST_FILE, "bra");
        assertEquals(10000, result.size());
        assertEquals(1, result.get(0));
        assertEquals(39997, result.get(9999));
    }

    @Test
    void testMain() {
        Main.main(new String[]{"Test"});
        assertTrue(true);
    }
}