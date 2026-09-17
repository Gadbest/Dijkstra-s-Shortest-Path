import java.util.*;

class Graph {

    // Vertex Adjacency List
    // Maps a vertex to an ArrayList of all edges.txt that start from that vertex
    private final HashMap<Vertex, ArrayList<Edge>> fromEdges;

    // Constructs an empty graph
    Graph() {
        fromEdges = new HashMap<>();
    }

    // For each vertex in the edges.txt list, add a new directed edge to the graph.
    public void addVerticesToGraph(List<Edge> edges) {
        var vertices = new HashSet<Vertex>();
        edges.forEach(edge -> {
            initMap(vertices, edge.fromVertex());
            initMap(vertices, edge.toVertex());
        });
    }

    // For each edge in the edges.txt list, add a new directed edge to the graph.
    public void addEdgesToGraph(List<Edge> edges) {
        edges.forEach(this::addDirectedEdge);
    }


    // Initialize vertices and edges.txt in the InfoMap
    private void initMap(Set<Vertex> vertices, Vertex edge) {
        // don't add duplicates
        if (!vertices.contains(edge)) {
            vertices.add(edge);
            // Every vertex must exist as a key in fromEdges maps
            fromEdges.put(edge, new ArrayList<>());
        }
    }

    public void addDirectedEdge(Edge edge) {
        // Don't add the same edge twice
        if (hasEdge(edge.fromVertex(), edge.toVertex())) return;

        // Add the edge to the adjacency list of from edges.txt
        fromEdges.getOrDefault(edge.fromVertex(), new ArrayList<>()).add(edge);
    }

    // Returns the collection of edges.txt with the specified fromVertex
    public Collection<Edge> getEdgesFrom(Vertex fromVertex) {
        return fromEdges.get(fromVertex);
    }

    // Returns the collection of all of this graph's vertices
    public Collection<Vertex> getVertices() {
        return fromEdges.keySet();
    }

    // Return the collection of all of this graph's edges.txt
    public Collection<Edge> getEdges() {
        return fromEdges.values()
            .stream()
            .flatMap(Collection::stream)
            .toList();
    }

    // Returns true if this graph has an edge from fromVertex to toVertex
    public boolean hasEdge(Vertex fromVertex, Vertex toVertex) {
        // Search the list of edges.txt for an edge that goes to toVertex
        return fromEdges.get(fromVertex).stream()
            .map(v -> v.toVertex().vertex())
            .anyMatch(label -> label.equals(toVertex.vertex()));
    }

}
