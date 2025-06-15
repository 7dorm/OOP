package ru.nsu.dunaev;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<Integer> find(String filename, String substring) throws IOException {
        List<Integer> result = new ArrayList<>();
        if (substring.isEmpty()) return result;
        int bufferSize = 4096;
        int subLen = substring.length();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {
            StringBuilder buffer = new StringBuilder();
            int index = 0;
            int bytesRead;
            char[] charBuffer = new char[bufferSize];

            while ((bytesRead = reader.read(charBuffer)) != -1) {
                buffer.append(charBuffer, 0, bytesRead);
                int start = 0;

                while ((start = buffer.indexOf(substring, start)) != -1) {
                    result.add(index + start);
                    start += 1;
                }

                index += buffer.length() - subLen + 1;

                buffer = new StringBuilder(buffer.substring(Math.max(0, buffer.length() - subLen + 1)));
            }
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("input.txt"), StandardCharsets.UTF_8))) {
                writer.write("привет мир и как у тебя дела и как настроение я ненавижу пролог и оси");
            }
            List<Integer> result = find("input.txt", "и");
            List<Integer> result1 = find("input.txt", "а");
            List<Integer> result2 = find("input.txt", "у");
            System.out.println(result);
            System.out.println(result1);
            System.out.println(result2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}