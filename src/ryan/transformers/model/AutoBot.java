package ryan.transformers.model;

import prins.simulator.model.Agent;
import prins.simulator.model.Location;
import ryan.transformers.TransformerConfig;
import ryan.transformers.TransformerSim;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            Location nextLocation = nextLocation(planet);
            if(nextLocation != null) {
                move(planet, nextLocation);
            } else {
                //next location doesn't exist, flag this location
                System.out.println("No potential neighbours, flagged current location");
                setAlive(false);
            }
		}
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
			System.out.println("Path[" + path.size() + "] before death " + path.get(path.size() - 1));
			flagLocation(location);
			path.remove(location);
		}
	}
	
	public void reset() {
		location = path.get(0);
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
		return !path.isEmpty() && location.matches(TransformerConfig.ALL_SPARK_LOCATION);
	}
	
	private Location nextLocation(Planet planet) {
		List<Location> locations = planet.getAdjacentLocations(location);
		locations = locations.stream().filter(loc -> !isFlaggedLocation(loc)).collect(Collectors.toList());
		if(!locations.isEmpty()) {
            return locations.get(TransformerConfig.randomInt(null, 0, locations.size()-1));
        }
		return null;
	}

	public int getPathSize() {
        return path.size();
    }

    public void optimisePath(Planet planet) {
        System.out.println("Optimising Path [" + getPathSize() + "]");
        int currentIndex = 0;
        int nextIndex = 0;
        int difference = 1;
        for (Location location : path) {
            List<Location> neighbours = planet.getAdjacentLocations(location);
            for (Location nextLocation : path) {
                for (Location neighbour : neighbours) {
                    if (nextLocation.matches(neighbour)) {
                        int currentIndexTemp = path.indexOf(location);
                        int nextIndexTemp = path.indexOf(nextLocation);
                        int differenceTemp = nextIndexTemp - currentIndexTemp;
                        if (differenceTemp > difference) {
                            currentIndex = currentIndexTemp;
                            nextIndex = nextIndexTemp;
                            difference = differenceTemp;
                        }
                    }
                }
            }
        }

        if (difference > 1) {
            System.out.println("From [" + currentIndex + "] => [" + nextIndex + "]");
            for(int i = nextIndex - 1; i > currentIndex; i--) {
                //System.out.println("Remove [" + i + "] from path");
                path.remove(i);
            }
            System.out.println("Optimised Path [" + getPathSize() + "]");
        } else {
            System.out.println("No Optimisations found. No changes made");
        }


    }
	
}
