package graph.implTest;

import graph.Edge;
import graph.Graph;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;


import java.util.LinkedList;
import java.util.List;


import static org.junit.Assert.*;

/**
 * This class contains a set of test cases that can be used to test the implementation of the
 * Graph class.
 */

public class GraphTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);

    @Test
    public void testEmptyGraph() {
        Graph<String, String> graph = new Graph<>();
        assertEquals(0, graph.size());
    }

    @Test
    public void testAddOneVertexSizeAndContain() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("v1");
        assertEquals(1, graph.size());
        assertTrue(graph.containVertex("v1"));
    }

    @Test
    public void testSizeAndAddTwoVertices() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("v1");
        graph.addVertex("v2");
        assertEquals(2, graph.size());
    }

    @Test
    public void testAddThreeVerticesAndContainVertex() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        assertTrue(graph.containVertex("v2"));
        assertFalse(graph.containVertex("hello"));
    }

    @Test
    public void testGetVerticesOneVertex() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("v1");
        List<String> expected = new LinkedList<>();
        expected.add("v1");
        assertEquals(expected, graph.getVertices());
    }

    @Test
    public void testGetVerticesTwoVertices() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("v1");
        graph.addVertex("v2");
        List<String> expected = new LinkedList<>();
        expected.add("v1");
        expected.add("v2");
        assertEquals(expected, graph.getVertices());
    }

    @Test
    public void testGetVerticesThreeVertices() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        List<String> expected = new LinkedList<>();
        expected.add("v1");
        expected.add("v2");
        expected.add("v3");
        assertEquals(expected, graph.getVertices());
    }

    @Test
    public void testAddEdgesAndGetChildrenOfGeneral() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v4");
        graph.addEdge("v1", "v2", "edge1");
        graph.addEdge("v1", "v3", "edge2");
        graph.addEdge("v1", "v4", "edge3");
        List<String> expected = new LinkedList<>();
        expected.add("v2");
        expected.add("v3");
        expected.add("v4");
        List<Edge<String, String>> children = graph.getChildrenOf("v1");
        List<String> actual = new LinkedList<>();
        for (Edge<String, String> e : children) {
            actual.add(e.getToVertex());
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testGetVerticesGeneral() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v4");
        graph.addVertex("v5");
        graph.addVertex("v6");
        graph.addVertex("v7");
        graph.addVertex("v8");
        graph.addVertex("v9");
        graph.addVertex("v10");
        List<String> expected = new LinkedList<>();
        expected.add("v6");
        expected.add("v7");
        expected.add("v8");
        expected.add("v9");
        expected.add("v10");
        expected.add("v1");
        expected.add("v2");
        expected.add("v3");
        expected.add("v4");
        expected.add("v5");
        assertEquals(expected, graph.getVertices());
    }

    @Test
    public void testContainVertexGeneral() {
        Graph<String, String> graph = new Graph<>();
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v4");
        graph.addVertex("v5");
        graph.addVertex("v6");
        graph.addVertex("v7");
        graph.addVertex("v8");
        graph.addVertex("v9");
        graph.addVertex("v10");
        assertTrue(graph.containVertex("v6"));
        assertFalse(graph.containVertex("cat"));
    }
}