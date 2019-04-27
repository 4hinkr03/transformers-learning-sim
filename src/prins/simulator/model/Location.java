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
 * Location models the horizontal and vertical distance in a 2D environment.
 *
 * @author Prins Butt
 */
public class Location {

    private int x;
    private int y;

    /**
     * Sets the horizontal and vertical distance in a 2D environment.
     *
     * @param x The horizontal distance
     * @param y The vertical distance
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the horizontal distance.
     *
     * @return The horizontal distance.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the vertical distance.
     *
     * @return The vertical distance.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the horizontal distance.
     *
     * @param x The horizontal distance.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the vertical distance.
     *
     * @param y The vertical distance.
     */
    public void setY(int y) {
        this.y = y;
    }

    public boolean matches(Location location) {
        return this.x == location.x && this.y == location.y;
    }
    
    @Override
    public String toString() {
    	return "[x=" + x + ", y=" + y + "]";
    }
}
