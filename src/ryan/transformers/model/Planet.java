package ryan.transformers.model;

import prins.simulator.Config;
import prins.simulator.model.Agent;
import prins.simulator.model.Environment;
import prins.simulator.model.Location;
import ryan.transformers.TransformerConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Planet extends Environment {

    private Agent[][] planet;
    
    public Planet() {
    	clear();
    }

    @Override
    public void clear() {
        planet = new Agent[getHeight()][getWidth()];
    }

    @Override
    public Agent getAgent(Location location) {
        return planet[location.getY()][location.getX()];
    }

    @Override
    public int getHeight() {
        return Config.world_height;
    }

    @Override
    public int getWidth() {
        return Config.world_width;
    }

    @Override
    public void setAgent(Agent agent, Location location) {
        planet[location.getY()][location.getX()] = agent;
    }
    
    public boolean locationMatches(Location location, Class<? extends Agent> matchClass) {
        Agent agent = getAgent(location);
        return agent != null && agent.getClass() == matchClass;
    }

    public Optional<Location> getAdjacentLocationMatches(Location location, Class<? extends Agent> matchClass) {
        Stream<Location> stream = getAdjacentLocations(location).stream();
        return stream.filter(loc -> locationMatches(loc, matchClass)).findAny();
    }

    public Location getAdjacentLocation(Location location) {
        return getAdjacentLocation(location, null);
    }
    
    public Location getAdjacentLocation(Class agent, Location location, Class<? extends Agent> classIgnore) {
    	 List<Location> locations = getAdjacentLocations(location);
    	 if(classIgnore != null) {
    		 locations = locations.stream().filter(loc -> !locationMatches(loc, classIgnore)).collect(Collectors.toList());
    	 }
         if(!locations.isEmpty()) {
             return locations.get(TransformerConfig.randomInt(agent, 0, locations.size() - 1));
         }

         return null;
    }

    public Location getAdjacentLocation(Location location, Class<? extends Agent> classIgnore) {
        return getAdjacentLocation(null, location, classIgnore);
    }

    public void clearWorld() {
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                Location location = new Location(x, y);
                if(getAgent(location) instanceof AutoBot) {
                    setAgent(null, new Location(x, y));
                }
            }
        }
    }

    private boolean withinBounds(int x, int y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    public List<Location> getAdjacentLocations(Location location) {
        List<Location> adjacentLocations = new ArrayList<>();
        int currentX = location.getX();
        int currentY = location.getY();

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                int x = currentX + xOffset;
                int y = currentY + yOffset;
                Location adjacentLocation = new Location(x, y);
                if (withinBounds(x, y)) {
                    if (!location.matches(adjacentLocation)) {
                    	adjacentLocations.add(adjacentLocation);
                    }
                }
            }
        }
        return adjacentLocations;
    }
}
