// Stores information for a vertex that is relevant to a shortest path algorithm
public class PathVertexInfo implements Comparable<PathVertexInfo> {
    private final Vertex vertex;
    private int distance;
    private Vertex predecessor;

    public final static int NO_PATH_FOUND = 999999;

    PathVertexInfo(Vertex vertex) {
        this.vertex = vertex;
        predecessor = null;
        distance = NO_PATH_FOUND;
    }

    Vertex getVertex() { return vertex; }
    Vertex getPredecessor() { return predecessor; }
    int getDistance() { return distance; }

    void setPredecessor(Vertex predecessor) {this.predecessor = predecessor; }
    void setDistance(int distance) { this.distance = distance; }

    public int compareTo(PathVertexInfo other) {
        return Integer.compare(distance, other.distance);
    }

    @Override
    public String toString() {
        return "PathVertexInfo{vertex=%s, distance=%s, predecessor=%s}"
            .formatted(vertex, distance, predecessor);
    }
}
