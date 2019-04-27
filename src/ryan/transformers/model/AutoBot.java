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
	private boolean alive;
	
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
		int locationIndex = path.indexOf(location);
		if (locationIndex < path.size() - 1) {
			//move normally
			move(planet, path.get(locationIndex + 1));
		} else {
			//move to a random location
			move(planet, planet.getAdjacentLocation(location));
		}
	}
	
	private boolean move(Planet planet, Location nextLocation) {
		if(planet.isLocationFree(nextLocation)) {
			planet.setAgent(null, location);
			location = nextLocation;
			if(!path.contains(location)) {
				path.add(location);
			}
			planet.setAgent(this, location);
			return true;
		} else {
			setAlive(false);
			//flag location somehow.....
			return false;
		}
	}

	/**
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public void reset() {
		location = path.get(0);
		setAlive(true);
	}
	
	
}
