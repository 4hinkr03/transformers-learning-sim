package ryan.transformers.model;

import prins.simulator.model.Agent;
import prins.simulator.model.Location;
import java.util.ArrayList;
import java.util.List;

public class AutoBot extends Agent {

    private List<Location> path;

    public AutoBot(Location location) {
        super(location);
        this.path = new ArrayList<>();
    }

    /**
     * Procedure for the AutoBot to act within said environment
     *
     * @param planet - AutoBot environment
     */
    public void act(Planet planet) {

    }
}
