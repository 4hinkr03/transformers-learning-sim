package ryan.transformers.model;

import prins.simulator.model.Agent;
import prins.simulator.model.Location;
import ryan.transformers.TransformerConfig;

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
	private List<PathFlag> pathFlags;
	private int step;
	
	
	/**
	 * @param location initial AutoBot location.
	 */
	public AutoBot(final Location location) {
		super(location);
		this.path = new ArrayList<>();
		this.pathFlags = new ArrayList<PathFlag>();
		path.add(location);
		resetStep();
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
                setAlive(planet, false);
            }
		}
		step++;
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
			setAlive(planet, false);
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
	public void setAlive(Planet planet, boolean alive) {
		if (this.alive != alive && !alive) {
			System.out.println("Path[" + path.size() + "] before death " + path.get(path.size() - 1));
			System.out.print("Path=");
			path.forEach(System.out::print);
			System.out.println();
			flagLocation(planet, location);
			//remove all location at and above current location in path - prevent jumping
            int locationIndex = path.indexOf(location);
			System.out.println("location=" + location + "|" + locationIndex);
            for(int i = path.size() - 1; i >= locationIndex; i--) {
				path.remove(i);
			}
		}
		this.alive = alive;
	}
	
	public void reset(Planet planet) {
		location = path.get(0);
		setAlive(planet, true);
		resetStep();
		System.out.println("RESET");
		System.out.println("--------------------------------------");
	}
	
	private boolean isLocationFree(Planet planet, Location location) {
    	return !planet.locationMatches(location, Block.class);
    }
	
	private void flagLocation(Planet planet, Location location) {
		int locStep = path.indexOf(location);
    	if(!isFlaggedLocation(location, locStep)) {
    		//if location is block, set step to -1
			locStep = isLocationFree(planet, location) ? locStep : -1;
			System.out.println("step=" + locStep);
    		PathFlag flag = new PathFlag(location, locStep);
    		pathFlags.add(flag);
			System.out.println("Path Flag=" + flag);
    		System.out.println("locations flagged[" + pathFlags.size() + "]");
    	} else {
			System.out.println("Location already flagged=" + location + ", step=" + locStep);
		}
    }
	
	 private boolean isFlaggedLocation(Location location, int step) {
		return pathFlags.stream().anyMatch(flag -> flag.matches(location, step));
	}

	public boolean hasReachedAllSpark() {
		return !path.isEmpty() && location.matches(TransformerConfig.ALL_SPARK_LOCATION);
	}
	
	private Location nextLocation(Planet planet) {
		List<Location> locations = planet.getAdjacentLocations(location);
		locations = locations.stream().filter(loc -> !isFlaggedLocation(loc, step + 1)).collect(Collectors.toList());
		if(!locations.isEmpty()) {
            return locations.get(TransformerConfig.randomInt(null, 0, locations.size()-1));
        } else {
			System.out.println("no neighbours.....");
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

	private void resetStep() {
		this.step = 0;
	}
	
}
