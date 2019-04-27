package ryan.transformers.model;

import prins.simulator.model.Agent;
import prins.simulator.model.Location;
import ryan.transformers.TransformerConfig;
import ryan.transformers.TransformerSim;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan
 *
 */
public class AutoBot extends Agent {

	private final List<Location> path;
	private boolean alive;
	private ArrayList<Location> flaggedLocations;
	
	
	/**
	 * @param location initial AutoBot location.
	 */
	public AutoBot(final Location location) {
		super(location);
		this.path = new ArrayList<>();
		this.flaggedLocations = new ArrayList<Location>();
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
		setAlive(isLocationFree(planet, location));
		
	}
	
	private void move(Planet planet, Location nextLocation) {
		if (isLocationFree(planet, nextLocation)) {
			planet.setAgent(null, location);
			location = nextLocation;
			if(!path.contains(location)) {
				path.add(location);
			}
			planet.setAgent(this, location);
		} else {
			setAlive(false);
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
		if (!alive) {
			System.out.println("Path[" + path.size() + "] before death");
			flagLocation(location);
			path.remove(location);
		}
	}
	
	public void reset() {
		location = path.get(0);
		path.clear();
		setAlive(true);
	}
	
	private boolean isLocationFree(Planet planet, Location location) {
    	return !planet.locationMatches(location, Block.class);
    }
	
	private void flagLocation(Location location) {
    	if(!isFlaggedLocation(location)) {
    		flaggedLocations.add(location);
    		System.out.println("locations flagged[" + flaggedLocations.size() + "]");
    	}
    }
	
	 private boolean isFlaggedLocation(Location location) {
	    	return flaggedLocations.stream().anyMatch(loc -> loc.matches(location));
	    }

	public boolean hasReachedAllSpark() {
		return !path.isEmpty() && path.get(path.size() - 1).matches(TransformerConfig.ALL_SPARK_LOCATION);
	}
	
	
}
