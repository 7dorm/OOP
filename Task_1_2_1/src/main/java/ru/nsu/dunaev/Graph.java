package ru.nsu.dunaev;
import java.util.List;

public interface Graph {
    void addVertex(int vertex);
    void removeVertex(int vertex);
    void addEdge(int vertex1, int vertex2);
    void removeEdge(int vertex1, int vertex2);
    List<Integer> getNeighbors(int vertex);
    boolean equals(Object obj);
    String toString();
}