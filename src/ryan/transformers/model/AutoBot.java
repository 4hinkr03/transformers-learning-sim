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
	private final List<Location> previousPath;
	private boolean optimised, smoothed;
	private boolean alive;
	private List<PathFlag> pathFlags;
	private int step;
	
	
	/**
	 * @param location initial AutoBot location.
	 */
	public AutoBot(final Location location) {
		super(location);
		this.path = new ArrayList<>();
		this.previousPath = new ArrayList<>();
		this.pathFlags = new ArrayList<>();
		path.add(location);
		this.step = 0;
		this.optimised = false;
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
			// this means the autobot has reached the end of the path, meaning the optimised path is safe
			// and it was a success, so it can be reset
			optimised = false;
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
		
		if(isLocationBlock(planet, nextLocation) ) {
			if(!path.contains(nextLocation)) {
				path.add(nextLocation);
				location = nextLocation;
				die(planet);
			}
		} else {
			planet.setAgent(null, location);
			location = nextLocation;
			planet.setAgent(this, location);
			if(!path.contains(location)) {
				path.add(location);
			}
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
	}
	
	private boolean isLocationBlock(Planet planet, Location location) {
    	return planet.locationMatches(location, Block.class);
    }
	
	private void flagLocation(Planet planet, Location location) {
		int locStep = path.indexOf(location);
    	if(!isFlaggedLocation(location, locStep)) {
    		//if location is block, set step to -1
			locStep = isLocationBlock(planet, location) ? locStep : -1;
			PathFlag flag = new PathFlag(location, locStep);
    		pathFlags.add(flag);
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

    public boolean optimisePath(Planet planet) {
        System.out.println("Optimising Path [" + getPathSize() + "]");
        int currentIndex = 0;
        int nextIndex = 0;
        int difference = 1;
        int optimisationFailed = 0;
		for (Location location : path) {
            List<Location> neighbours = planet.getAdjacentLocations(location);
            for (Location nextLocation : path) {
                for (Location neighbour : neighbours) {
                    if (nextLocation.matches(neighbour)) {
                        int currentIndexTemp = path.indexOf(location);
                        int nextIndexTemp = path.indexOf(nextLocation);
                        int differenceTemp = nextIndexTemp - currentIndexTemp;
                        if (differenceTemp > difference) {
                        	//check the potential optimisation found is not flagged
                        	int stepOffset = differenceTemp - 1;
                        	int nextStep = nextIndexTemp;
                        	boolean optimisationSuccess = true;
                        	for(int step = nextIndexTemp - stepOffset; step < path.size() - stepOffset; step++, nextStep++) {
                        		//System.out.println("Checking [" + nextStep + "] against step [" + step + "]");
                        		if(isFlaggedLocation(path.get(nextStep), step)) {
                        			//System.out.println("Potential Optimisation failed");
                        			optimisationSuccess = false;
                        			optimisationFailed++;
                        		}
                        	}
                        	
                        	if(optimisationSuccess) {
                        		//System.out.println("Potential Optimisation success");
                        		currentIndex = currentIndexTemp;
	                            nextIndex = nextIndexTemp;
	                            difference = differenceTemp;
                        	}
                        }
                    }
                }
            }
        }

        if (difference > 1) {
        	//store previous path before optimisation
        	previousPath.clear();
        	previousPath.addAll(path);
        	
            System.out.println("From [" + currentIndex + "] => [" + nextIndex + "]");
            for(int i = nextIndex - 1; i > currentIndex; i--) {
            	path.remove(i);
            }
            System.out.println("Optimised Path [" + getPathSize() + "]");
            
            //check tempPath to ensure optimisation occur
            optimised = true;
            return true;
        } else {
            System.out.println("No Safe Optimisations found. No changes made.");
            System.out.println("Potential but failed optimisations [" + optimisationFailed + "] due to flagged locations");
            return false;
        }
    }

    public boolean smoothPath(Planet planet) {
    	//grids from min to max to smooth out issues, hopefully...
        int gridMin = 3;
        int gridMax = 10;
        int failed = 0;
        System.out.println("Smoothing path [distance=" + getDistanceBetween(0, getPathSize() - 1) + "]");
        System.out.print("Path=");
        path.forEach(System.out::print);
        System.out.println();
        for(int grid = gridMin; grid < gridMax; grid++) {
        	for(int i = 0; i < getPathSize() - grid; i++) {
                //every grid do something
        		int iEnd = i + grid;
        		Vector v = getLocationVector(i, iEnd);
                double vectorDist = v.distance();
                double dist = iEnd - i;
                if(vectorDist < dist) {
                	//smooth locations out.....
                	if(Math.abs(v.x) > 1 || Math.abs(v.y) > 1) {
                		List<Location> tempPath = new ArrayList<>(path);
                        for(int index = i + grid - 1; index > i; index--) {
                    		tempPath.remove(index);
                    	}
                		//this means the start & finish locations are not next to each other,
                        //so the gap removed must be filled with the smoothed solution
                		//add better locations - backwards to ensure they are in th eight order in the array
                    	//inverse vector so we can work from finish to start
                    	v.inverse();
                    	Location currentLoc = tempPath.get(i + 1);
                    	//remove start location because it will be added again once the vector
                        // smooothing is calculated
                    	tempPath.remove(i);
                    	Location tempLocation = new Location(currentLoc.getX(), currentLoc.getY());
                    	
                    	if(v.x != 0 || v.y != 0) {
                    		//we should be able to diagonally fill instead
                    		for(int x = v.x, y = v.y; Math.abs(x) > 0 || Math.abs(y) > 0; ) {
                    			if(x != 0) {
                    				if(v.x >= 0) {
	                        			tempLocation.setX(tempLocation.getX() + 1);
	                        			x--;
	                        		} else {
	                        			tempLocation.setX(tempLocation.getX() - 1);
	                            		x++;
	                        		}
                    			}
                    			if(y != 0) {
                    				if(v.y >= 0) {
                            			tempLocation.setY(tempLocation.getY() + 1);
                            			y--;
                            		} else {
                            			tempLocation.setY(tempLocation.getY() - 1);
                                		y++;
                            		}
                        			
                    			}
                    			Location tempLoc = new Location(tempLocation.getX(), tempLocation.getY());
                        		tempPath.add(i, tempLoc);
                    		}
                    	}
                    	//check temp path isn't flagged
                    	boolean smoothSuccess = true;
                    	for(int tempIndex = i; tempIndex < tempPath.size(); tempIndex++) {
                    		if(isFlaggedLocation(tempPath.get(tempIndex), tempIndex)) {
                    			smoothSuccess = false;
                    			System.out.println("Smooth failed");
                    			failed++;
                    		}
                    	}
                    	
                    	if(smoothSuccess) {
                    		//store previous path before applying smooth to actual path
                        	previousPath.clear();
                        	previousPath.addAll(path);
                        	
                    		System.out.println("Smooth success");
                            System.out.println("Grid size= " + grid);
                    		path.clear();
                    		path.addAll(tempPath);
                    	}
                    	
                	}
                	
                	//we only want to do this once and see if there is an improvement and sure it doesn't get caught
                	System.out.println("Smoothed path [distance=" + getDistanceBetween(0, getPathSize() - 1) + "]");
                	System.out.println("Failed potential smoothed path attempts [" + failed + "]");
                	
                	return true;
                }   
            }
           
        }
        return false;
        
    }

    private double getDistanceBetween(int start, int finish) {
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
            //System.out.println("delta=" + delta);
            vector.add(delta);
        }
	    return vector;
    }

	public void die(Planet planet) {
	    if(alive) {
	        alive = false;
	        //System.out.println("died...");
            //System.out.println("Path[" + path.size() + "] before death " + path.get(path.size() - 1));
            //System.out.print("Path=");
            //path.forEach(System.out::print);
            //System.out.println();
            flagLocation(planet, location);
            //remove all location at and above current location in path - prevent jumping
            int locationIndex = path.indexOf(location);
            //System.out.println("location=" + location + "|" + locationIndex);
            for(int i = path.size() - 1; i >= locationIndex; i--) {
                path.remove(i);
            }
            
            if(!previousPath.isEmpty()) {
            	if(optimised || smoothed) {
                	//revert back to old path, if it exists since the optimisation failed as we died
                	path.clear();
                	path.addAll(previousPath);
                	System.out.println("died after optimisation, reverting back to old path");
                } else {
                	previousPath.clear();
                    previousPath.addAll(path);
                }
            }
            
            
            
           
        }
    }
	
}
