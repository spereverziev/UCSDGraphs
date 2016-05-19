package roadgraph;

import geography.GeographicPoint;

public class MapEdge {

    private GeographicPoint start;

    private GeographicPoint end;

    private String roadName;

    private String roadType;

    private double distance;

    public MapEdge(GeographicPoint start, GeographicPoint end, String roadName, String roadType, double distance) {
        this.start = start;
        this.end = end;
        this.roadName = roadName;
        this.roadType = roadType;
        this.distance = distance;
    }

    public MapEdge() {
    }

    public GeographicPoint getStart() {
        return start;
    }

    public void setStart(GeographicPoint start) {
        this.start = start;
    }

    public GeographicPoint getEnd() {
        return end;
    }

    public void setEnd(GeographicPoint end) {
        this.end = end;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


}
