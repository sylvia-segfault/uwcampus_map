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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CampusMap represents an ADT that constructs a campus map based on campus_buildings.tsv
 * and campus_paths.tsv files. It implements the model of a user interface that prompts
 * the user to input two names of the buildings on campus and it finds the minimum-cost
 * path between those two buildings.
 */
public class CampusMap implements ModelAPI {
    private Graph<Point, Double> campusGraph;
    private Map<String, String> nameSToL;
    private Map<String, Point> nameToPoint;
    private static final boolean DEBUG = false;

    // RI: campusGraph != null && nameSToL != null && nameToPoint != null
    //     && nameSToL does not contain any null entry && nameToPoint does
    //     not contain any null entry

    // AF(this): an empty graph {} if nameSToL and nameToPoint are empty.
    //           this.campusGraph has the coordinates as its vertices and
    //           each edge label between two existing coordinates in this
    //           graph represents the cost going from one location to another.
    //           this.nameSToL maps from the short name of the campus buildings
    //           to their full name.
    //           this.nameToPoint maps the name of a location to its actual
    //           Cartesian coordinate

    /**
     * Constructs a campus map based on campus_buildings.tsv and campus_paths.tsv
     * files. It keeps track of each building's short name to its corresponding
     * full name. Updates a string representation for each location in the map
     * and its corresponding Cartesian coordinates.
     *
     * @spec.effects this.campusGraph is updated to be a graph that represents
     * the campus map.
     * this.nameSToL is updated to contain each building's short
     * name to its corresponding full name
     * this.nameToPoint is updated to have a Cartesian coordinate
     * for each string representation of the location on campus
     */
    public CampusMap() {
        nameSToL = new HashMap<>();
        nameToPoint = new HashMap<>();
        campusGraph = new Graph<>();
        List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings("campus_buildings.tsv");
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths("campus_paths.tsv");
        // loop through each campus buildings
        for (CampusBuilding b : buildings) {
            String shortName = b.getShortName(); // get the short name of this building
            Point p = new Point(b.getX(), b.getY()); // get this building's coordinate
            campusGraph.addVertex(p); // add the coordinate of the building to the graph
            nameSToL.put(shortName, b.getLongName()); // updates its full name
            nameToPoint.put(shortName, p); // update its corresponding coordinate
        }
        for (CampusPath path : paths) {
            Point pStart = new Point(path.getX1(), path.getY1()); // the starting point of this path segment
            Point pEnd = new Point(path.getX2(), path.getY2()); // the ending point of this path segment
            // if this campus graph does not contain the given coordinates, add them into the graph
            if (!(campusGraph.containVertex(pStart) && campusGraph.containVertex(pEnd))) {
                if (!campusGraph.containVertex(pStart)) {
                    campusGraph.addVertex(pStart);
                    nameToPoint.put("start point of a side way", pStart);
                }
                if (!campusGraph.containVertex(pEnd)) {
                    campusGraph.addVertex(pEnd);
                    nameToPoint.put("end point of a side way", pEnd);
                }
            }
            // add an edge/path between these two locations and label the edge
            // as the distance between them
            campusGraph.addEdge(pStart, pEnd, path.getDistance());
        }
        checkRep();
    }

    /**
     * @param shortName The short name of a building to query.
     * @return true if the map contains this building, false
     * otherwise.
     */
    @Override
    public boolean shortNameExists(String shortName) {
        checkRep();
        return nameSToL.containsKey(shortName);
    }

    /**
     * @param shortName The short name of a building to look up.
     * @return the string representation of the given building's
     * corresponding full name
     * @throws IllegalArgumentException if the building does not
     *                                  exist in this campus map
     */
    @Override
    public String longNameForShort(String shortName) {
        checkRep();
        if (!nameSToL.containsKey(shortName)) {
            throw new IllegalArgumentException();
        }
        checkRep();
        return nameSToL.get(shortName);
    }

    /**
     * @return A mapping from all the buildings' short names to their long names in this campus map.
     */
    @Override
    public Map<String, String> buildingNames() {
        checkRep();
        return new HashMap<>(nameSToL);
    }

    /**
     * Finds the shortest path, by distance, between the two provided buildings.
     *
     * @param startShortName The short name of the building at the beginning of this path.
     * @param endShortName   The short name of the building at the end of this path.
     * @return A path between {@code startBuilding} and {@code endBuilding}, or {@literal null}
     * if none exists.
     * @throws IllegalArgumentException if {@code startBuilding} or {@code endBuilding} are
     *                                  {@literal null}, or not valid short names of buildings in
     *                                  this campus map.
     */
    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        checkRep();
        return Dijkstra.dijkstraAlgo(campusGraph, nameToPoint.get(startShortName), nameToPoint.get(endShortName));
    }

    /**
     * A method to check if any operation in this class would potentially
     * violates the representation invariant.
     */
    private void checkRep() {
        assert campusGraph != null;
        assert nameSToL != null;
        assert nameToPoint != null;
        if (DEBUG) {
            for (String s : nameSToL.keySet()) {
                assert s != null;
                assert nameSToL.get(s) != null;
            }
            for (String s : nameToPoint.keySet()) {
                assert s != null;
                assert nameToPoint.get(s) != null;
            }
        }
    }
}