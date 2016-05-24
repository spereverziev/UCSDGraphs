package roadgraph;

import geography.GeographicPoint;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MapNode {

    private GeographicPoint location;
    private Set<MapEdge> edges;

    public MapNode(GeographicPoint location) {
        this.location = location;
        this.edges = new HashSet<>();
    }

    public void addEdge(MapEdge edge) {
        edges.add(edge);
    }

    public GeographicPoint getLocation() {
        return location;
    }

    public void setLocation(GeographicPoint location) {
        this.location = location;
    }

    public Set<MapEdge> getEdges() {
        return edges;
    }

    public void setEdges(Set<MapEdge> edges) {
        this.edges = edges;
    }


}
