/*
 * Copyright (C) 2020 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.specTest;

import graph.Edge;
import graph.Graph;
import pathfinder.Dijkstra;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    public static void main(String[] args) {
        // You only need a main() method if you choose to implement
        // the 'interactive' test driver, as seen with GraphTestDriver's sample
        // code. You may also delete this method entirely and just
    }

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, Graph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests()
            throws IOException {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();
                    List<String> arguments = new ArrayList<>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }
                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch (command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> graph = graphs.get(graphName);
        graph.addVertex(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        Graph<String, Double> graph = graphs.get(graphName);
        graph.addEdge(parentName, childName, Double.parseDouble(edgeLabel));
        edgeLabel = String.format("%.3f", Double.parseDouble(edgeLabel));
        output.println("added edge " + edgeLabel + " from " + parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String, Double> graph = graphs.get(graphName);
        String result = graphName + " contains:";
        Set<String> nodes = new TreeSet<>(graph.getVertices());
        for (String vertex : nodes) {
            result += " " + vertex;
        }
        output.println(result);
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String, Double> graph = graphs.get(graphName);
        parentName = parentName.replace("_", " ");
        String result = "the children of " + parentName + " in " + graphName + " are:";
        List<Edge<String, Double>> children = graph.getChildrenOf(parentName);
        children.sort(new EdgeSort());
        for (Edge<String, Double> child : children) {
            result += " " + child.getToVertex() + "(" + child.getLabel() + ")";
        }
        output.println(result);
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to LoadGraph: " + arguments);
        }
        String graphName = arguments.get(0);
        String nodeA = arguments.get(1);
        String nodeB = arguments.get(2);
        findPath(graphName, nodeA, nodeB);
    }

    private void findPath(String graphName, String nodeA, String nodeB) {
        Graph<String, Double> graph = graphs.get(graphName);
        nodeA = nodeA.replace("_", " ");
        nodeB = nodeB.replace("_", " ");
        double totalW = 0.000;
        if (!graph.containVertex(nodeA) || !graph.containVertex(nodeB)) {
            if (!graph.containVertex(nodeA)) {
                output.println("unknown node " + nodeA);
            }
            if (!graph.containVertex(nodeB)) {
                output.println("unknown node " + nodeB);
            }
        } else {
            Path<String> paths = Dijkstra.dijkstraAlgo(graph, nodeA, nodeB);
            output.println("path from " + nodeA + " to " + nodeB + ":");
            if (paths == null) {
                output.println("no path found");
            } else {
                for (Path<String>.Segment segment : paths) {
                    double cost = segment.getCost();
                    totalW += cost;
                    // rounding the printed cost of path
                    String roundedCost = String.format("%.3f", cost);
                    output.println(segment.getStart() + " to " + segment.getEnd() + " with weight " + roundedCost);
                }
                // rounding the printed cost of path
                output.println("total cost: " + String.format("%.3f", totalW));
            }
        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }

    private class EdgeSort implements Comparator<Edge<String, Double>> {
        public int compare(Edge<String, Double> thisOne, Edge<String, Double> other) {
            int compareHero = thisOne.getToVertex().compareTo(other.getToVertex());
            if (compareHero == 0) {
                return thisOne.getLabel().compareTo(other.getLabel());
            }
            return compareHero;
        }
    }
}
