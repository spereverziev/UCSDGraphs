/**
 * @author UCSD MOOC development team and YOU
 * <p>
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between
 */
package roadgraph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import cache.LFUCache;
import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 *
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
    //TODO: Add your member variables here in WEEK 2

    private Map<GeographicPoint, MapNode> nodes;

    private Map<MapNode, Double> distances;

    private LFUCache<SearchDirection, List<GeographicPoint>> cachedDirections;

    private int numberOfEdges;
    /**
     * Create a new empty MapGraph
     */
    public MapGraph() {
        nodes = new HashMap<>();
        distances = new HashMap<>();
        cachedDirections = new LFUCache<>(5, 0.9f);
        numberOfEdges = 0;
        // TODO: Implement in this constructor in WEEK 2
    }

    /**
     * Get the number of vertices (road intersections) in the graph
     * @return The number of vertices in the graph.
     */
    public int getNumVertices() {
        //TODO: Implement this method in WEEK 2
        return nodes.size();
    }

    /**
     * Return the intersections, which are the vertices in this graph.
     * @return The vertices in this graph as GeographicPoints
     */
    public Set<GeographicPoint> getVertices() {
        return nodes.keySet();
        //TODO: Implement this method in WEEK 2
    }

    /**
     * Get the number of road segments in the graph
     * @return The number of edges in the graph.
     */
    public int getNumEdges() {
        //TODO: Implement this method in WEEK 2
        return numberOfEdges;
    }


    /** Add a node corresponding to an intersection at a Geographic Point
     * If the location is already in the graph or null, this method does
     * not change the graph.
     * @param location  The location of the intersection
     * @return true if a node was added, false if it was not (the node
     * was already in the graph, or the parameter is null).
     */
    public boolean addVertex(GeographicPoint location) {
        // TODO: Implement this method in WEEK 2
        if (location == null) {
            return false;
        }

        if(nodes.get(location) == null) {
            MapNode node = new MapNode(location);
            nodes.put(location, node);
            distances.put(node, Double.POSITIVE_INFINITY);
            return true;
        }

        return false;
    }

    /**
     * Adds a directed edge to the graph from pt1 to pt2.
     * Precondition: Both GeographicPoints have already been added to the graph
     * @param from The starting point of the edge
     * @param to The ending point of the edge
     * @param roadName The name of the road
     * @param roadType The type of the road
     * @param length The length of the road, in km
     * @throws IllegalArgumentException If the points have not already been
     *   added as nodes to the graph, if any of the arguments is null,
     *   or if the length is less than 0.
     */
    public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
                        String roadType, double length) throws IllegalArgumentException {

        if (from == null || to == null) {
            throw new NullPointerException("From or to can't be null");
        }

        MapNode nodeFrom = nodes.get(from);
        if (nodeFrom == null || nodes.get(to) == null) {
            throw new IllegalArgumentException("Locations are not in graph");
        }

        MapEdge edge = new MapEdge(from, to, roadName, roadType, length);
        nodeFrom.addEdge(edge);

        numberOfEdges++;
        //TODO: Implement this method in WEEK 2

    }


    /** Find the path from start to goal using breadth first search
     *
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest (unweighted)
     *   path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return bfs(start, goal, temp);
    }

    /** Find the path from start to goal using breadth first search
     *
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest (unweighted)
     *   path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start,
                                     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        // TODO: Implement this method in WEEK 2
        MapNode startNode = nodes.get(start);
        MapNode goalNode = nodes.get(goal);

        if(startNode == null || goalNode == null) {
            System.out.println("Start or goal not found");
            return new LinkedList<>();
        }

        Map<MapNode, MapNode> parentNodes = new HashMap<>();
        boolean found = bfsSearch(start, goal, nodeSearched, parentNodes);
        if (!found) {
            System.out.println("The goal is not found");
            return null;
        }
        // Hook for visualization.  See writeup.
        //nodeSearched.accept(next.getLocation());

        return constructPath(startNode, goalNode, parentNodes);
    }

    private List<GeographicPoint> constructPath(MapNode start, MapNode goal, Map<MapNode, MapNode> parentNodes) {
        LinkedList<GeographicPoint> path = new LinkedList<>();
        MapNode curr = goal;
        while (curr != start) {
            path.addFirst(curr.getLocation());
            curr = parentNodes.get(curr);
        }
        path.addFirst(start.getLocation());
        return path;
    }

    private boolean bfsSearch(GeographicPoint start,
                              GeographicPoint goal, Consumer<GeographicPoint> nodeSearched, Map<MapNode, MapNode> parentNodes ) {
        Queue<MapNode> toExplore = new LinkedList<>();
        Set<MapNode> visitedNodes = new HashSet<>();
        MapNode mapNode = nodes.get(start);
        toExplore.add(mapNode);
        boolean found = false;
        while (!toExplore.isEmpty()) {
            MapNode curr = toExplore.remove();
            nodeSearched.accept(curr.getLocation());
            if(goal.equals(curr.getLocation())) {
                found = true;
                break;
            }
            visitedNodes.add(curr);

            for(MapEdge edge : curr.getEdges()) {
                MapNode next = nodes.get(edge.getEnd());
                if(!visitedNodes.contains(next)) {
                    toExplore.add(next);
                    visitedNodes.add(next);
                    parentNodes.put(next, curr);
                }
            }
        }

        return found;
    }


    /** Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        // You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return dijkstra(start, goal, temp);
    }

    /** Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(GeographicPoint start,
                                          GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        // TODO: Implement this method in WEEK 3

        // Hook for visualization.  See writeup.
        //nodeSearched.accept(next.getLocation());

        MapNode startNode = nodes.get(start);
        MapNode goalNode = nodes.get(goal);

        if(startNode == null || goalNode == null) {
            System.out.println("Start or goal not found");
            return new LinkedList<>();
        }

        List<GeographicPoint> cachedPath = cachedDirections.get(new SearchDirection(start, goal));
        if (cachedPath != null) {
            System.out.println("Found cached path. Immediately return the path.");
            return cachedPath;
        }

        Map<MapNode, MapNode> parentNodes = new HashMap<>();


        boolean found = dijkstraSearch(start, goal, nodeSearched, parentNodes);
        if (!found) {
            System.out.println("The goal is not found");
            return null;
        }

        List<GeographicPoint> path = constructPath(startNode, goalNode, parentNodes);
        cachedDirections.put(new SearchDirection(start, goal), path);

        return path;
    }

    private boolean dijkstraSearch(GeographicPoint start,
                                GeographicPoint goal,
                                Consumer<GeographicPoint> nodeSearched,
                                Map<MapNode, MapNode> parentNodes) {

        Comparator<MapNode> dijkstraComparator = (node1, node2) -> {
            if (distances.get(node1).equals(distances.get(node2))) {
                return 0;
            }
            return distances.get(node1) > distances.get(node2) ? 1 : -1;
        };

        return searchWithComparator(start, goal, nodeSearched, parentNodes, dijkstraComparator);
    }

    private boolean searchWithComparator(GeographicPoint start, GeographicPoint goal,
                                         Consumer<GeographicPoint> nodeSearched, Map<MapNode, MapNode> parentNodes,
                                         Comparator<MapNode> comparator) {
        Queue<MapNode> toExplore = new PriorityQueue<>(comparator);
        Set<MapNode> visitedNodes = new HashSet<>();
        MapNode mapNode = nodes.get(start);
        distances.put(mapNode,0.0);
        toExplore.add(mapNode);
        int vertexesVisited = 0;
        boolean found = false;
        while (!toExplore.isEmpty()) {
            MapNode curr = toExplore.remove();
            nodeSearched.accept(curr.getLocation());
            vertexesVisited++;
            if(!visitedNodes.contains(curr)) {
                visitedNodes.add(curr);

                if(goal.equals(curr.getLocation())) {
                    found = true;
                    break;
                }

                for(MapEdge edge : curr.getEdges()) {
                    MapNode next = nodes.get(edge.getEnd());
                    if(!visitedNodes.contains(next)) {
                        Double currDistance = distances.get(curr);
                        if(currDistance + edge.getDistance() < distances.get(next)) {
                            distances.put(next, currDistance + edge.getDistance());
                            toExplore.add(next);
                            parentNodes.put(next, curr);
                        }

                    }
                }
            }
        }
        resetDistanceToVisitedVertexes(visitedNodes);
        System.out.println("Vertex visited: " + vertexesVisited);
        return found;
    }

    private void resetDistanceToVisitedVertexes(Set<MapNode> visitedNodes) {
        for (MapNode node : visitedNodes) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
    }

    /** Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal The goal location
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return aStarSearch(start, goal, temp);
    }

    /** Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     *   start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(GeographicPoint start,
                                             GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        // TODO: Implement this method in WEEK 3
        MapNode startNode = nodes.get(start);
        MapNode goalNode = nodes.get(goal);

        if(startNode == null || goalNode == null) {
            System.out.println("Start or goal not found");
            return new LinkedList<>();
        }

        List<GeographicPoint> cachedPath = cachedDirections.get(new SearchDirection(start, goal));
        if (cachedPath != null) {
            System.out.println("Found cached path. Immediately return the path.");
            return cachedPath;
        }

        Map<MapNode, MapNode> parentNodes = new HashMap<>();

        boolean found = aStarSearch(start, goal, nodeSearched, parentNodes);
        if (!found) {
            System.out.println("The goal is not found");
            return null;
        }

        List<GeographicPoint> path = constructPath(startNode, goalNode, parentNodes);
        cachedDirections.put(new SearchDirection(start, goal), path);
        return path;
    }

    private boolean aStarSearch(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched, Map<MapNode, MapNode> parentNodes) {
        Comparator<MapNode> aStarComparator = (node1, node2) -> {
            Double distanceForNode1 = distances.get(node1) + goal.distance(node1.getLocation());
            Double distanceForNode2 = distances.get(node2) + goal.distance(node2.getLocation());
            if (distanceForNode1.equals(distanceForNode2)) {
                return 0;
            }
            return distanceForNode1 > distanceForNode2 ? 1 : -1;
        };


        return searchWithComparator(start, goal, nodeSearched, parentNodes, aStarComparator);
    }


    public static void main(String[] args) {
//        System.out.print("Making a new map...");
//        MapGraph theMap = new MapGraph();
//        System.out.print("DONE. \nLoading the map...");
//        GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
//        System.out.println("DONE.");
//
//        GeographicPoint start = new GeographicPoint(1, 1);
//        GeographicPoint end = new GeographicPoint(8, -1);

        // You can use this method for testing.

//		Use this code in Week 3 End of Week Quiz
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);


                    List<GeographicPoint> route3 = theMap.aStarSearch(start,end);
                    List<GeographicPoint> route = theMap.dijkstra(start,end);
                    List<GeographicPoint> route2 = theMap.dijkstra(start,end);
		List<GeographicPoint> route4 = theMap.aStarSearch(start,end);



    }

}
