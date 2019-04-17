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
 * Environment is the abstract class for all implementations of
 *
 * @author Prins Butt
 */
public abstract class Environment {

    /**
     * Clears the environment.
     */
    public abstract void clear();

    /**
     * Retrieves the agent at the specified location in the environment.
     *
     * @param location The specified location of the agent
     * @return The agent at the specified location.
     */
    public abstract Agent getAgent(Location location);

    /**
     * Retrieve the height of the environment.
     *
     * @return The height of the environment.
     */
    public abstract int getHeight();

    /**
     * Retrieve the width of the environment.
     *
     * @return The width of the environment.
     */
    public abstract int getWidth();

    /**
     * Set the agent at the specified location in the environment.
     *
     * @param agent The agent to be added to the environment.
     * @param location The specified location of the agent.
     */
    public abstract void setAgent(Agent agent, Location location);
}
