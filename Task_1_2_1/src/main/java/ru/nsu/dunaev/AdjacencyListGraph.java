package ru.nsu.dunaev;

import java.util.*;

public class AdjacencyListGraph implements Graph {
    private Map<Integer, List<Integer>> adjacencyList;

    public AdjacencyListGraph() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public void addVertex(int vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(int vertex) {
        adjacencyList.values().forEach(e -> e.remove(vertex));
        adjacencyList.remove(vertex);
    }

    @Override
    public void addEdge(int vertex1, int vertex2) {
        adjacencyList.get(vertex1).add(vertex2);
        adjacencyList.get(vertex2).add(vertex1);
    }

    @Override
    public void removeEdge(int vertex1, int vertex2) {
        adjacencyList.get(vertex1).remove(vertex2);
        adjacencyList.get(vertex2).remove(vertex1);
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        return adjacencyList.get(vertex);
    }

    @Override
    public void readFromFile(String filePath) {
        // Реализуйте чтение из файла
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AdjacencyListGraph other = (AdjacencyListGraph) obj;
        if (adjacencyList.size() != other.adjacencyList.size()) return false;
        int size = adjacencyList.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!Objects.equals(adjacencyList.get(i).get(j), other.adjacencyList.get(i).get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return adjacencyList.toString();
    }
}