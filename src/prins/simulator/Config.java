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
package prins.simulator;

/**
 * Config contains the configuration variables to control the simulation.
 *
 * @author Prins Butt
 */
public class Config {

    /**
     * The name of the simulator.
     */
    public static String simulation_name = "Simulator";

    /**
     * The initial speed of simulation.
     */
    public static int initial_simulation_speed = 1;

    /**
     * The minimum speed of simulation.
     */
    public static int min_simulation_speed = 1;

    /**
     * The maximum speed of simulation.
     */
    public static int max_simulation_speed = 10;

    /**
     * The width of the world to be simulated.
     */
    public static int world_width = 30;

    /**
     * The height of the world to be simulated.
     */
    public static int world_height = 30;

    /**
     * The size of a cell in the world to simulated.
     */
    public static int world_cell_size = 20;
}
