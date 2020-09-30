package pathfinder;

import graph.Edge;
import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;

/**
 * Dijkstra represents a class that implements Dijkstra algorithm to find
 * the minimum-cost path between two vertices in the given graph.
 */
public class Dijkstra {

    // No AF, RI, or checkRep because this class only contains static methods and
    // it does not represent any data abstraction

    /**
     * @param graph the given graph that will be used for Dijkstra
     * @param start the type of T starting vertex
     * @param dest  the type of T ending vertex
     * @return the minimum-cost path segments from the start
     * vertex to the ending vertex, null if no path is found
     * @throws IllegalArgumentException if start or dest is null
     * @spec.requires both start and dest are in the given graph
     */
    public static <T> Path<T> dijkstraAlgo(Graph<T, Double> graph, T start, T dest) {
        if (start == null || dest == null) {
            throw new IllegalArgumentException();
        }
        Queue<Path<T>> active = new PriorityQueue<>(new Comparator<Path<T>>() {
            @Override
            public int compare(Path<T> o1, Path<T> o2) {
                return Double.compare(o1.getCost(), o2.getCost());
            }
        });
        Set<T> finished = new HashSet<>();
        // Add a path from start to itself to active
        Path<T> startPath = new Path<>(start);
        active.add(startPath);
        while (!active.isEmpty()) {
            Path<T> minPath = active.remove();
            T minDestNode = minPath.getEnd();
            if (minDestNode.equals(dest)) { // equals method?? for generic??
                return minPath;
            }
            if (!finished.contains(minDestNode)) {
                for (Edge<T, Double> e : graph.getChildrenOf(minDestNode)) {
                    T child = e.getToVertex();
                    if (!finished.contains(child)) {
                        Path<T> newPath = minPath.extend(child, e.getLabel());
                        active.add(newPath);
                    }
                }
                finished.add(minDestNode);
            }
        }
        return null;
    }
}