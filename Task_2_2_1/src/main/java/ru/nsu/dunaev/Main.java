package ru.nsu.dunaev;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Simulation simulation = new Simulation("src/main/resources/config.json");
            simulation.run();
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }
    }
}