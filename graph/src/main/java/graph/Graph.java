package graph;

import java.util.*;

/**
 * Represents a mutable, directed labeled graph that contains unique vertices.
 */

public class Graph<T, E> {
    private Map<T, List<Edge<T, E>>> graph;
    private static final boolean DEBUG = false;

    // RI: graph != null && graph does not contain null vertices nor null edges
    //     && graph must contain the vertex v if v appears in any edge in this
    //     graph

    // AF(this): an empty graph {} if this.size() = 0
    //           {v1 = [], ...} where v1 is an vertex in this graph
    //           and it has no outgoing edges.
    //           {v1 = [c1(e1), c2(e2), ... c_n(e_n)], v2 = [...], ... , v_n = [...]}
    //           where c1, c2, ... c_n are the children vertices of v1 and e1, e2, ...
    //           e_n are the labels of the edges which connect v1 with c1, v1 with c2,
    //           and so on.


    /**
     * Creates an empty, directed labeled graph
     *
     * @spec.effects sets this to empty
     */
    public Graph() {
        graph = new HashMap<>();
        checkRep();
    }

    /**
     * Adds the given vertex to this graph
     *
     * @param vertex The type of T vertex that is to be added
     * @return true if the vertex is added successfully, otherwise false
     * @spec.requires vertex != null
     * @spec.modifies this
     * @spec.effects Adds the given vertex to this graph
     */
    public boolean addVertex(T vertex) {
        checkRep();
        if (containVertex(vertex)) {
            return false;
        }
        graph.put(vertex, new LinkedList<>());
        checkRep();
        return true;
    }

    /**
     * Returns true if this graph contains the specified vertex
     *
     * @param vertex The type of T vertex whose presence in this graph is to be tested
     * @return true if this graph contains the specified vertex
     * @spec.requires vertex != null
     */
    public boolean containVertex(T vertex) {
        checkRep();
        return graph.containsKey(vertex);
    }

    /**
     * Returns a list of vertices contained in this graph
     *
     * @return a list of vertices contained in this graph
     */
    public List<T> getVertices() {
        checkRep();
        return new LinkedList<>(graph.keySet());
    }

    /**
     * Adds a labeled edge that connects the given two vertices to this graph,
     * if those vertices exist in this graph, and are not already connected
     * by the same labeled edge.
     *
     * @param fromVertex The type of T beginning vertex of this edge
     * @param toVertex   The type of T ending vertex of this edge
     * @param edgeLabel  The type of E label of the edge that is to be added
     * @return true is the edge is added successfully, otherwise return false
     * @spec.requires edge != null, beginVertex != null, endVertex != null
     * @spec.modifies this
     * @spec.effects Adds a labeled edge that connects the given two vertices
     * to this graph, if those vertices exist in this graph, and are not already
     * connected by the same labeled edge.
     */
    public boolean addEdge(T fromVertex, T toVertex, E edgeLabel) {
        checkRep();
        if (!containVertex(fromVertex) || !containVertex(toVertex)) {
            return false;
        }
        Edge<T, E> currEdge = new Edge<>(fromVertex, toVertex, edgeLabel);
        graph.get(fromVertex).add(currEdge);
        checkRep();
        return true;
    }

    /**
     * Returns a list of all the outgoing edges that are connected with the
     * given vertex
     *
     * @param vertex The type of T parent vertex
     * @return a list of all the outgoing edges that are connected with the
     * given vertex
     * @throws IllegalArgumentException if the given vertex is not in this graph
     * @spec.requires vertex != null
     */
    public List<Edge<T, E>> getChildrenOf(T vertex) {
        checkRep();
        if (!containVertex(vertex)) {
            throw new IllegalArgumentException();
        }
        checkRep();
        return graph.get(vertex);
    }

    /**
     * Returns the number of vertices in this graph
     *
     * @return the number of vertices in this graph
     */
    public int size() {
        checkRep();
        return graph.size();
    }

    private void checkRep() {
        assert graph != null;
        if (DEBUG) {
            for (T vertex : graph.keySet()) {
                assert vertex != null;
                List<Edge<T, E>> edges = graph.get(vertex);
                for (Edge<T, E> edge : edges) {
                    assert edge != null && edge.getLabel() != null
                            && edge.getFromVertex() != null &&
                            edge.getToVertex() != null;
                }
            }
        }
    }
}