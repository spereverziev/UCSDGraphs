package roadgraph;

import geography.GeographicPoint;

import java.util.Objects;

public class SearchDirection {

    private GeographicPoint start;

    private GeographicPoint goal;

    public SearchDirection() {
    }

    public SearchDirection(GeographicPoint start, GeographicPoint goal) {
        this.start = start;
        this.goal = goal;
    }

    public GeographicPoint getStart() {
        return start;
    }

    public void setStart(GeographicPoint start) {
        this.start = start;
    }

    public GeographicPoint getGoal() {
        return goal;
    }

    public void setGoal(GeographicPoint goal) {
        this.goal = goal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchDirection that = (SearchDirection) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(goal, that.goal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, goal);
    }
}
