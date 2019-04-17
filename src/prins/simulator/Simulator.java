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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Simulator is the abstract class for all simulation implementations.
 *
 * @author Prins Butt
 */
public abstract class Simulator implements PropertyChangeListener {

    /**
     * State of the simulation execution.
     */
    protected boolean isRunning;

    /**
     * The speed of simulation.
     */
    protected int speed;

    /**
     * The current simulation step.
     */
    protected int step;

    /**
     * Default constructor to initialise a simulator.
     */
    public Simulator() {
        step = 0;
        speed = Config.initial_simulation_speed;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {

            case "reset":
                isRunning = false;
                step = 0;
                reset();
                render();
                break;

            case "speed":
                speed = (int) event.getNewValue();
                break;

            case "start":
                isRunning = true;
                break;

            case "step":
                step++;
                update();
                render();
                break;

            case "stop":
                isRunning = false;
                break;
        }
    }

    /**
     * Executes the simulation.
     */
    public void run() {

        prepare();

        render();

        while (true) {

            try {
                Thread.sleep((Config.max_simulation_speed / speed) * 100);

                if (isRunning) {
                    step++;

                    update();
                    render();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Prepares the simulation prior to execution.
     */
    protected abstract void prepare();

    /**
     * Renders the current simulation state.
     */
    protected abstract void render();

    /**
     * Resets the simulation to its initial state.
     */
    protected abstract void reset();

    /**
     * Updates the simulation to its next state.
     */
    protected abstract void update();
}
