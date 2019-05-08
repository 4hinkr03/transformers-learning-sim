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
		this.step = 0;
	}

	/**
	 * Procedure for the AutoBot to act within said environment.
	 *
	 * @param planet - AutoBot environment
	 */
	public void act(Planet planet, int step) {
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
                die(planet);
            }
		}
        this.step = step;

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
			die(planet);
		}
	}

	/**
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}
	
	public void reset() {
		location = path.get(0);
		alive = true;
		step = 0;
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
			//corner case when all neighbours have been flagged, so no move can de done. We can assume that
            //the current location must also be avoided next iteration of the simulation
            System.out.println("No neighbours available - must die");
            die(planet);
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
                path.remove(i);
            }
            System.out.println("Optimised Path [" + getPathSize() + "]");
        } else {
            System.out.println("No Optimisations found. No changes made");
        }
    }

    public void smoothPath(Planet planet) {
        int grid = 5;
        for (int i = 0; i < getPathSize() - grid; i+=grid) {
            //every grid do something
        	System.out.println("Smoothing path [" + getPathSize() + "]");
            Vector v = getLocationVector(i, i + grid);
            System.out.println("vector=" + v);
            System.out.println("vector D=" + v.distance());
            double dist = getDistanceVector(i, i + grid);
            System.out.println("distance=" + dist);

        }
        System.out.println("Smoothed path + [" + getPathSize() + "]");

    }

    private double getDistanceVector(int start, int finish) {
        double distance = 0;
        for(int index = start; index < finish; index++) {
            Location startLoc = path.get(index);
            Location finishLoc = path.get(index + 1);
            int deltax = Math.abs(finishLoc.getX() - startLoc.getX());
            int deltay = Math.abs(finishLoc.getY() - startLoc.getY());
            //double tempDistance = distance;
            if(deltax != 0 && deltay != 0) {
                distance += Math.hypot(deltax, deltay);
            } else {
                distance += deltax + deltay;
            }
            //System.out.println("start=" + startLoc + ", finish=" + finishLoc + ", distance=" + (distance - tempDistance));
        }
        return distance;
    }

    private Vector getLocationVector(int start, int finish) {
	    Vector vector = new Vector();
        for(int index = start; index < finish; index++) {
            Vector delta = Vector.delta(Vector.vector(path.get(index)), Vector.vector(path.get(index + 1)));
            System.out.println("delta=" + delta);
            vector.add(delta);
        }
	    return vector;
    }

	public void die(Planet planet) {
	    if(alive) {
	        alive = false;
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
    }
	
}
