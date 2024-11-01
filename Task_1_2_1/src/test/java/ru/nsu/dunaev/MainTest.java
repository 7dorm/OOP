package ru.nsu.dunaev;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MainTest {
    private Graph adjacencyMatrixGraph;
    private Graph adjacencyListGraph;

    @BeforeEach
    public void setup() {
        adjacencyMatrixGraph = new AdjacencyMatrixGraph(5);
        adjacencyListGraph = new AdjacencyListGraph();
        for (int i = 0; i < 5; i++) {
            adjacencyListGraph.addVertex(i);
        }
    }

    @Test
    public void testMain(){
        Main.main(new String[]{"Test"});
        assertTrue(true);
    }

    @Test
    public void testCreateGraph(){
        adjacencyMatrixGraph = new AdjacencyMatrixGraph(5);
        adjacencyListGraph = new AdjacencyListGraph();
        assertNotNull(adjacencyListGraph);
        assertNotNull(adjacencyMatrixGraph);
    }

    @Test
    public void testAddAndRemoveVertex() {
        adjacencyListGraph.addVertex(5);
        assertTrue(adjacencyListGraph.getNeighbors(5).isEmpty());

        adjacencyListGraph.removeVertex(5);
        assertNull(adjacencyListGraph.getNeighbors(5));
    }

    @Test
    public void testAddAndRemoveEdge() {
        adjacencyMatrixGraph.addEdge(0, 1);
        adjacencyListGraph.addEdge(0, 1);

        assertTrue(adjacencyMatrixGraph.getNeighbors(0).contains(1));
        assertTrue(adjacencyListGraph.getNeighbors(0).contains(1));

        adjacencyMatrixGraph.removeEdge(0, 1);
        adjacencyListGraph.removeEdge(0, 1);

        assertFalse(adjacencyMatrixGraph.getNeighbors(0).contains(1));
        assertFalse(adjacencyListGraph.getNeighbors(0).contains(1));
    }

    @Test
    public void testGetNeighbors() {
        adjacencyMatrixGraph.addEdge(1, 2);
        adjacencyListGraph.addEdge(1, 2);

        List<Integer> matrixNeighbors = adjacencyMatrixGraph.getNeighbors(1);
        List<Integer> listNeighbors = adjacencyListGraph.getNeighbors(1);

        assertEquals(1, matrixNeighbors.size());
        assertEquals(1, listNeighbors.size());
        assertTrue(matrixNeighbors.contains(2));
        assertTrue(listNeighbors.contains(2));
    }

    @Test
    public void testEquals() {
        Graph matrixGraph1 = new AdjacencyMatrixGraph(5);
        Graph matrixGraph2 = new AdjacencyMatrixGraph(5);

        matrixGraph1.addEdge(0, 1);
        matrixGraph2.addEdge(0, 1);

        assertEquals(matrixGraph1, matrixGraph2);

        matrixGraph2.removeEdge(0, 1);
        assertNotEquals(matrixGraph1, matrixGraph2);
    }

    @Test
    public void testToString() {
        adjacencyListGraph.addEdge(0, 1);
        assertNotNull(adjacencyListGraph.toString());

        adjacencyMatrixGraph.addEdge(0, 1);
        assertNotNull(adjacencyMatrixGraph.toString());
    }
}