package ryan.transformers.model;

import prins.simulator.model.Agent;
import prins.simulator.model.Location;

import java.util.Optional;

public class Decepticon extends Agent {

    public Decepticon(Location location) {
        super(location);
    }

    public void act(Planet planet) {
        Optional<Location> neighbour = planet.getAdjacentLocationMatches(location, AutoBot.class);
        if (neighbour.isPresent()) {
            attack(planet, neighbour.get());
        } else {
        	move(planet, planet.getAdjacentLocation(location, Block.class));
        }
    }

    private void attack(Planet planet, Location location) {
        AutoBot bot = (AutoBot) planet.getAgent(location);
        System.out.println("Attacked AutoBot");
        bot.setAlive(false);
        move(planet, location);
    }

    private void move(Planet planet, Location moveLocation) {
    	System.out.println("movelocation=" + moveLocation);
        if (moveLocation != null) {
            planet.setAgent(null, this.location);
            this.location = moveLocation;
            planet.setAgent(this, this.location);
        }
    }
}
