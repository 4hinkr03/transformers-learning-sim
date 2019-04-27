package ryan.transformers.model;

import prins.simulator.model.Agent;
import prins.simulator.model.Location;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan
 *
 */
public class AutoBot extends Agent {

	private final List<Location> path;

	/**
	 * @param location initial AutoBot location.
	 */
	public AutoBot(final Location location) {
		super(location);
		this.path = new ArrayList<>();
		path.add(location);
	}

	/**
	 * Procedure for the AutoBot to act within said environment.
	 *
	 * @param planet - AutoBot environment
	 */
	public void act(Planet planet) {
		System.out.println("pathsize=" + path.size());
		int locationIndex = path.indexOf(location);
		if (locationIndex < path.size() - 1) {
			//move normally
			move(planet, path.get(locationIndex + 1));
		} else {
			//move to a random location
			move(planet, planet.getAdjacentLocation(location));
		}
	}
	
	private void move(Planet planet, Location nextLocation) {
		planet.setAgent(null, location);
		location = nextLocation;
		path.add(location);
		planet.setAgent(this, location);
	}
}
