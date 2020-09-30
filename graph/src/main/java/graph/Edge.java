package graph;

/**
 * Represents a weighted, directed edge between two vertices.
 */
public class Edge<T, E> {
    private T fromVertex;
    private T toVertex;
    private E label;

    public Edge(T fromVertex, T toVertex, E label) {
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        this.label = label;
    }

    public T getFromVertex() {
        return this.fromVertex;
    }

    public T getToVertex() {
        return this.toVertex;
    }

    public E getLabel() {
        return this.label;
    }
}
