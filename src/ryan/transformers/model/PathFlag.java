package ryan.transformers.model;

import prins.simulator.model.Location;

public class PathFlag {

    private Location location;
    private boolean allSteps;
    private int step;

    //step can be set to where a decepticon was intercepted, so a location is flagged only for that step
    public PathFlag(Location location, int step) {
        this.location = location;
        this.step = step;
        this.allSteps = (step == -1);
    }

    //set step to -1 meaning it is flagged for all steps e.g. a Block
    public PathFlag(Location location) {
        this(location, -1);
    }

    public boolean matches(Location location, int step) {
        if(allSteps || this.step == step) {
            return this.location.matches(location);
        }
        return false;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isAllSteps() {
        return allSteps;
    }

    public int getStep() {
        return step;
    }

    @Override
    public String toString() {
        return "[Location=" + location.toString() + ", step=" + step + "]";
    }
}
