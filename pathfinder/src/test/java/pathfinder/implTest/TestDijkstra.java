package pathfinder.implTest;

import graph.Graph;

import org.junit.Test;
import pathfinder.Dijkstra;
import pathfinder.datastructures.Path;

import static org.junit.Assert.*;

public class TestDijkstra {
    @Test
    public void testSmallGraph() {
        Graph<String, Double> graph = new Graph<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "C", 1.0);
        graph.addEdge("C", "D", 1.0);
        graph.addEdge("D", "B", 1.0);
        Path<String> path = Dijkstra.dijkstraAlgo(graph, "A", "B");
    }
}
