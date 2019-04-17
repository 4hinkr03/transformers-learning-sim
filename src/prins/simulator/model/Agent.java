/*
 * Copyright (C) 2019 Prins Butt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package prins.simulator.model;

/**
 * Agent is abstract class for all implementations of an agent in the
 * simulation.
 *
 * @author Prins Butt
 */
public abstract class Agent {

    /**
     * The current location of the agent.
     */
    protected Location location;

    /**
     * Constructs an agent with the specified location.
     *
     * @param location The specified location.
     */
    public Agent(Location location) {
        this.location = location;
    }

    /**
     * Retrieves the current location of the agent.
     *
     * @return A location object containing the current location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of the agent.
     *
     * @param location The current location of the agent.
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}
