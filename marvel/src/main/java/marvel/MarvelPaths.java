package marvel;

import graph.Edge;
import graph.Graph;

import java.util.*;

/**
 * MarvelPaths represents a graph that contains Marvel characters, using
 * the given input .tsv file. Two characters are connected if they appear
 * in the same comic book. It also contains a breadth first search
 * algorithm to find the shortest path between two characters.
 */
public class MarvelPaths {

    // No AF, RI, or checkRep because this class only contains static methods and
    // it does not represent any data abstraction

    /**
     * An interactive console that allows the users to type in two heroes
     * that exists in the graph and prints out the path between them if
     * it exists
     *
     * @param args
     */
    public static void main(String[] args) {
        Graph<String, String> graph = buildGraph("biggerData.tsv");
        System.out.println("Welcome to the interactive realm of Marvel social network");
        boolean tryAgain = false;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("What is your first Marvel character? ");
            String startHero = input.next();
            System.out.println();
            System.out.println("What is the other Marvel character? ");
            String endHero = input.next();
            if (!graph.containVertex(startHero) || !graph.containVertex(endHero)) {
                tryAgain = true;
                if (!graph.containVertex(startHero)) {
                    System.out.println("unknown character " + startHero + ". Please try again");
                }
                if (!graph.containVertex(endHero)) {
                    System.out.println("unknown character " + endHero + ". Please try again");
                }
            } else {
                System.out.println("path from " + startHero + " to " + endHero + ":");
                List<Edge<String, String>> paths = bFSFindPath(graph, startHero, endHero);
                String result = "";
                if (paths == null) {
                    result = "Oops! There is no connection between these two heroes";
                } else {
                    for (Edge<String, String> e : paths) {
                        result += e.getFromVertex() + " to " + e.getToVertex() + " via " +
                                e.getLabel() + "\n";
                    }
                }
                System.out.println(result);
            }
        } while (tryAgain);
        System.out.println("This is the end of the program. Thank you for using our algorithm!");
        input.close();
    }

    /**
     * @param filename the input file that is used to build the graph
     * @return the graph that is built from the given input file
     */
    public static Graph<String, String> buildGraph(String filename) {
        Graph<String, String> graph = new Graph<>();
        Iterator<BeanReader> marvelIR = MarvelParser.parseData(filename);
        Map<String, List<String>> bookToChar = new HashMap<>();
        while (marvelIR.hasNext()) {
            BeanReader line = marvelIR.next();
            String character = line.getHero();
            String book = line.getBook();
            graph.addVertex(character); // add current character to the graph
            if (!bookToChar.containsKey(book)) {
                bookToChar.put(book, new LinkedList<>());
            }
            bookToChar.get(book).add(character);
        }
        // add an edge if two characters appear in the same book
        for (String title : bookToChar.keySet()) {
            List<String> connectedChars = bookToChar.get(title);
            for (int i = 0; i < connectedChars.size(); i++) {
                for (int j = i + 1; j < connectedChars.size(); j++) {
                    graph.addEdge(connectedChars.get(i), connectedChars.get(j), title);
                    graph.addEdge(connectedChars.get(j), connectedChars.get(i), title);
                }
            }
        }
        return graph;
    }

    /**
     * @param graph the given graph that will be used for BFS
     * @param start the starting vertex
     * @param dest  the ending vertex
     * @return a list of edges that represents the path between the starting
     * vertex and the ending vertex
     * @throws IllegalArgumentException if start or dest is null
     * @spec.requires both start and dest are in the given graph
     */
    public static List<Edge<String, String>> bFSFindPath(Graph<String, String> graph, String start, String dest) {
        if (start == null || dest == null) {
            throw new IllegalArgumentException();
        }
        Queue<String> visited = new LinkedList<>();
        Map<String, List<Edge<String, String>>> noteToPath = new HashMap<>();
        visited.add(start);
        noteToPath.put(start, new LinkedList<>());
        while (!visited.isEmpty()) {
            String currNode = visited.remove();
            if (currNode.equals(dest)) {
                return noteToPath.get(currNode);
            }
            List<Edge<String, String>> neighbors = graph.getChildrenOf(currNode);
            neighbors.sort(new Comparator<Edge<String, String>>() {
                public int compare(Edge<String, String> thisOne, Edge<String, String> other) {
                    int compareName = thisOne.getToVertex().compareTo(other.getToVertex());
                    if (compareName == 0) {
                        return thisOne.getLabel().compareTo(other.getLabel());
                    }
                    return compareName;
                }
            });
            for (Edge<String, String> e : neighbors) {
                String neighborNode = e.getToVertex();
                if (!noteToPath.containsKey(neighborNode)) {
                    List<Edge<String, String>> copyEdge = new ArrayList<>(noteToPath.get(currNode));
                    copyEdge.add(e);
                    noteToPath.put(neighborNode, copyEdge);
                    visited.add(neighborNode);
                }
            }
        }
        return null;
    }
}