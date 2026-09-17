/***
 * Student name: Gadiel Nieto
 * Course and section: COSC 2436-9
 * Assignment: Dijkstra's Algorithm
 * This program uses Dijkstra's Shortest Path algorithm to calculate and print
 * the shortest distance between two vertices on a graph

 * Your assignment is to place code after each comment beginning like // * comment.
 * Look for methods in the Graph and PathVertexInfo classes to use before coding your own.
 * The DijkstraTest can be used to unit test your code before submitting the complete compressed project folder.
 * All of your changes will be in the Dijkstra class.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Dijkstra {

    public static final String EDGES_FILE_NAME = "edges.txt";
    public static final String PATHS_FILE_NAME = "paths.txt";
    public static final String NO_PATH_EXISTS = "No path exists.";
    private final Graph graph;
    private List<Edge> edges;
    private final List<Path> paths;

    public Dijkstra() {
        graph = new Graph();
        edges = processEdges(readLines(EDGES_FILE_NAME));
        graph.addVerticesToGraph(edges);
        graph.addEdgesToGraph(edges);
        paths = processPaths(readLines(PATHS_FILE_NAME));
    }

    public void run() {
        // Find shortest path between each pair of vertices from the paths.txt list.
        paths.forEach(path -> {
            // Split the path into two vertices using the path record.
            var start = path.vertexFrom();
            var end = path.vertexTo();

            // Print the start and end vertices (no new line).
            System.out.printf("%s to %s: ", start.vertex(), end.vertex());

            // Call the dijkstraShortestPath method, returning infoMap.
            // InfoMap is an Adjacency Matrix of distances between all the vertices.
            var infoMap = dijkstraShortestPath(start);

            // Fetch the ending vertex entry from infoMap
            var endVertex = infoMap.get(end);

            // If the endVertex is null OR the endVertex distance is 999999, print "No path exists.".
            if (endVertex == null || endVertex.getDistance() == PathVertexInfo.NO_PATH_FOUND) {
                System.out.println(NO_PATH_EXISTS);
            } else {
                // Format shortest path and distance.
                formatShortestPath(path, infoMap, endVertex.getDistance());
            }
        });
    }

    HashMap<Vertex, PathVertexInfo> dijkstraShortestPath(Vertex startVertex) { // Figure 12.13.2
        // * Create the HashMap for vertex information (Adjacency list)
        HashMap<Vertex, PathVertexInfo> infoMap = new HashMap<>();

        // * Put all graph vertices in both the info HashMap and the PriorityQueue
        // * of unvisited vertices
        PriorityQueue<PathVertexInfo> unvisitedQueue = new PriorityQueue<>();

        for (Vertex v : graph.getVertices()) {
            PathVertexInfo vertexInfo = new PathVertexInfo(v);
            infoMap.put(v, vertexInfo);
        }

        // * startVertex has a distance of 0 from itself
        infoMap.get(startVertex).setDistance(0);

        // Add all vertices to the priority queue after setting start distance
        for (PathVertexInfo info : infoMap.values()) {
            unvisitedQueue.add(info);
        }

        // * Iterate through all vertices in the priority queue
        while (!unvisitedQueue.isEmpty()) {
            // * Get info about the vertex with the shortest distance from startVertex
            PathVertexInfo currentInfo = unvisitedQueue.poll();
            Vertex currentVertex = currentInfo.getVertex();

            // * Check potential path lengths from the current vertex to all neighbors
            for (Edge edge : graph.getEdgesFrom(currentVertex)) {
                Vertex adjacentVertex = edge.toVertex();
                PathVertexInfo adjacentInfo = infoMap.get(adjacentVertex);

                // If a shorter path from startVertex to adjacentVertex is found,
                // update adjacentVertex's distance and predecessor
                int newDistance = currentInfo.getDistance() + edge.distance();

                if (newDistance < adjacentInfo.getDistance()) {
                    unvisitedQueue.remove(adjacentInfo);
                    adjacentInfo.setDistance(newDistance);
                    adjacentInfo.setPredecessor(currentVertex);
                    unvisitedQueue.add(adjacentInfo);
                }
            }
        }

        return infoMap;
    }

    // Retrieve the shortest path from the infoMap.
    List<Vertex> getShortestPath(Path path, HashMap<Vertex, PathVertexInfo> infoMap) { // Figure 12.13.3
        // * Start from endVertex and build the path in reverse.

        // * define shortestPath as an ArrayList of type Vertex
        ArrayList<Vertex> shortestPath = new ArrayList<>();

        // * Set current vertex to path.vertexTo()
        Vertex currentVertex = path.vertexTo();

        // * While currentVertex is not null AND currentVertex not equal to vertexFrom.
        while (currentVertex != null && !currentVertex.equals(path.vertexFrom())) {
            // * Add the currentVertex to the shortestPath
            shortestPath.add(currentVertex);

            // * set the currentVertex to the predecessor
            PathVertexInfo currentInfo = infoMap.get(currentVertex);
            currentVertex = currentInfo.getPredecessor();
        }

        // * Reverse the path to be start to end
        Collections.reverse(shortestPath);

        // * return shortestPath list
        return shortestPath;
    }

    // Format shortest path and distance..
    void formatShortestPath(Path path, HashMap<Vertex,
            PathVertexInfo> infoMap, int distance) {
        // Print the from vertex (no new line).
        System.out.print(path.vertexFrom().vertex());
        // Get the shortest path and print each vertex in the path preceded by " -> " (no new line).
        getShortestPath(path, infoMap)
            .forEach(currentVertex -> System.out.printf(" -> %s", currentVertex.vertex()));

        // Print the distance within parentheses.
        System.out.printf(" (%d)\n", distance);
    }

    public List<Edge> processEdges(List<String> lines) {
        edges = lines.stream()
            .map(line -> line.split(","))
            .map(edge -> new Edge(
                new Vertex(edge[0].trim()),
                new Vertex(edge[1].trim()),
                Integer.parseInt(edge[2].trim())))
            .toList();
        return edges;
    }

    List<Path> processPaths(List<String> lines) {
        return lines.stream()
            .map(line -> line.split(","))
            .map(path -> new Path(
                new Vertex(path[0].trim()),
                new Vertex(path[1].trim())))
            .toList();
    }

    List<String> readLines(String fn) {
        try {
            return Files.readAllLines(Paths.get(fn).toFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
    }
    return null;
}

    // The main method.
    public static void main(String[] args) {
        new Dijkstra().run();
    }
}
