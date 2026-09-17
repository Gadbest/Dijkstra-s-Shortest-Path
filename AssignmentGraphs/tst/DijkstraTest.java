import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DijkstraTest {
    Dijkstra dijkstra;
    Graph graph;
    List<Edge> edges;
    List<Path> paths;
    public static final int San_Antonio_Chicago_Path = 9; // Index updated for paths.txt list
    public static final int Chicago_To_Dallas_Path = 4;
    public static final int New_York_To_Kuala_Lumpur_Path = 7;

    @BeforeEach
    void setUp() {
        dijkstra = new Dijkstra();
        graph = new Graph();
        edges = dijkstra.processEdges(dijkstra.readLines(Dijkstra.EDGES_FILE_NAME));
        graph.addVerticesToGraph(edges);
        graph.addEdgesToGraph(edges);
        paths = dijkstra.processPaths(dijkstra.readLines(Dijkstra.PATHS_FILE_NAME));
    }

    @Test
    @Order(1)
    void addVerticesToGraphTest() {
        assertEquals(10, graph.getVertices().size(), "Number of vertices");
    }

    @Test
    @Order(2)
    void addEdgesToGraphTest() {
        assertEquals(13, graph.getEdges().size(), "Number of edges.txt");
    }

    @Test
    @Order(3)
    void getPathTest() {
        var path = paths.get(Chicago_To_Dallas_Path);
        assertEquals("Chicago", path.vertexFrom().vertex(),"From vertex");
        assertEquals("Dallas", path.vertexTo().vertex(),"To vertex");
    }

    @Test
    @Order(4)
    void dijkstraShortestPathTest() {
        var path = paths.get(Chicago_To_Dallas_Path);
        var infoMap = dijkstra.dijkstraShortestPath(path.vertexFrom());
        assertEquals(10, infoMap.size(), "Number of vertices in infoMap");
        System.out.println();
    }

    @Test
    @Order(5)
    void testNoPath() {
        var path = paths.get(San_Antonio_Chicago_Path);
        var infoMap = dijkstra.dijkstraShortestPath(path.vertexFrom());
        assertEquals(999999, infoMap.get(path.vertexTo()).getDistance(), "No path");
    }

    @Test
    @Order(6)
    void getShortestPathTest() {
        var path = paths.get(Chicago_To_Dallas_Path);
        var infoMap = dijkstra.dijkstraShortestPath(path.vertexFrom());
        var distance = infoMap.get(path.vertexTo()).getDistance();
        dijkstra.formatShortestPath(path, infoMap, distance);
        assertEquals(255, distance, "shortest path");
        System.out.println();
    }

    @Test
    @Order(7)
    void multiStopTest() {
        var path = paths.get(New_York_To_Kuala_Lumpur_Path);
        var infoMap = dijkstra.dijkstraShortestPath(path.vertexFrom());
        var distance = infoMap.get(path.vertexTo()).getDistance();
        dijkstra.formatShortestPath(path, infoMap, distance);
        assertEquals(1985, distance);
        var cityList = Stream.of(
                "Dallas", "Honolulu", "Fiji", "Sydney", "Kuala Lumpur")
                .map(Vertex::new)
                .toList();
        assertIterableEquals(cityList, dijkstra.getShortestPath(path, infoMap));
    }
}