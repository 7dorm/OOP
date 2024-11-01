package ru.nsu.dunaev;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrixGraph implements Graph {
    private int[][] matrix;
    private int size;

    public AdjacencyMatrixGraph(int vertices) {
        size = vertices;
        matrix = new int[size][size];
    }

    @Override
    public void addVertex(int vertex) {
        if (vertex >= size) {
            // Увеличиваем размер матрицы
            int newSize = Math.max(vertex + 1, size * 2);
            int[][] newMatrix = new int[newSize][newSize];

            // Копируем данные из старой матрицы в новую
            for (int i = 0; i < size; i++) {
                System.arraycopy(matrix[i], 0, newMatrix[i], 0, size);
            }

            // Обновляем ссылку на новую матрицу и размер
            matrix = newMatrix;
            size = newSize;
        }
    }

    @Override
    public void removeVertex(int vertex) {
        if (vertex >= size) {
            throw new IllegalArgumentException("Вершина вне диапазона.");
        }

        int newSize = size - 1;
        int[][] newMatrix = new int[newSize][newSize];

        for (int i = 0, newI = 0; i < size; i++) {
            if (i == vertex) continue;  // Пропускаем строку удаляемой вершины

            for (int j = 0, newJ = 0; j < size; j++) {
                if (j == vertex) continue;  // Пропускаем столбец удаляемой вершины

                newMatrix[newI][newJ] = matrix[i][j];
                newJ++;
            }
            newI++;
        }

        matrix = newMatrix;
        size = newSize;
    }

    @Override
    public void addEdge(int vertex1, int vertex2) {
        matrix[vertex1][vertex2] = 1;
        matrix[vertex2][vertex1] = 1;
    }

    @Override
    public void removeEdge(int vertex1, int vertex2) {
        matrix[vertex1][vertex2] = 0;
        matrix[vertex2][vertex1] = 0;
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (matrix[vertex][i] == 1) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AdjacencyMatrixGraph other = (AdjacencyMatrixGraph) obj;
        if (this.size != other.size) return false;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.matrix[i][j] != other.matrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                builder.append(matrix[i][j]).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}