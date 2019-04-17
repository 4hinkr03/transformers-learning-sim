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
package prins.simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import prins.simulator.Config;
import prins.simulator.model.Agent;
import prins.simulator.model.Environment;
import prins.simulator.model.Location;

/**
 * A graphical user interface for controlling a simulation.
 *
 * @author Prins Butt
 */
public class Gui extends JFrame {

    private final Map<Class, Color> agentTypesMap;
    private final PropertyChangeSupport changes;
    private JPanel legendPanel;
    private final Environment model;
    private JButton resetButton;
    private JSlider speedSlider;
    private JButton startButton;
    private JButton stepButton;
    private JTextField stepTextField;
    private JButton stopButton;
    private JTable world;

    /**
     * Sets up a graphical user interface to control a simulation.
     *
     * @param model The environment to be visualised by the graphical user
     * interface.
     */
    public Gui(Environment model) {
        this.model = model;
        agentTypesMap = new HashMap<>();

        changes = new PropertyChangeSupport(this);

        initGui();
        initComponents();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    /**
     * Registers an agent class and its associated colour with the graphical
     * user interface.
     *
     * @param agent The agent class to be registered.
     * @param color The colour associated with the agent.
     */
    public void registerAgentColors(Class agent, Color color) {
        if (!agentTypesMap.containsKey(agent)) {
            agentTypesMap.put(agent, color);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    /**
     * Renders the graphical user interface.
     */
    public void render() {
        if (!isVisible()) {
            pack();
            setVisible(true);
        }

        updateLegend();

        this.revalidate();
        this.repaint();
    }

    /**
     * Set the value of the current simulation step in the graphical user
     * interface.
     *
     * @param step The current simulation step.
     */
    public void setStep(int step) {
        stepTextField.setText("" + step);
    }

    private void initComponents() {
        initInfo();
        initWorld();
        initControls();
    }

    private void initControls() {
        JPanel controlsPanel = new JPanel();

        startButton = new JButton("Start");
        startButton.addActionListener((ActionEvent e) -> {
            start();
        });
        controlsPanel.add(startButton);

        stepButton = new JButton("Step");
        stepButton.addActionListener((ActionEvent e) -> {
            step();
        });
        controlsPanel.add(stepButton);

        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener((ActionEvent e) -> {
            stop();
        });
        controlsPanel.add(stopButton);

        resetButton = new JButton("Reset");
        resetButton.setEnabled(false);
        resetButton.addActionListener((ActionEvent e) -> {
            reset();
        });
        controlsPanel.add(resetButton);

        controlsPanel.add(new JSeparator(SwingConstants.VERTICAL));

        controlsPanel.add(new JLabel("Step"));

        stepTextField = new JTextField();
        stepTextField.setEnabled(false);
        stepTextField.setColumns(5);
        controlsPanel.add(stepTextField);

        controlsPanel.add(new JSeparator(SwingConstants.VERTICAL));

        controlsPanel.add(new JLabel("Speed"));

        speedSlider = new JSlider(JSlider.HORIZONTAL,
                Config.min_simulation_speed,
                Config.max_simulation_speed,
                Config.initial_simulation_speed);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.addChangeListener((ChangeEvent e) -> {
            slide();
        });

        controlsPanel.add(speedSlider);

        add(controlsPanel, BorderLayout.SOUTH);
    }

    private void initGui() {
        setTitle(Config.simulation_name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());
    }

    private void initInfo() {
        legendPanel = new JPanel();
        add(legendPanel, BorderLayout.NORTH);
    }

    private void initWorld() {
        DefaultTableModel tableModel = new DefaultTableModel(
                (model.getWidth() > 0)
                ? model.getWidth() : Config.world_width,
                (model.getHeight() > 0)
                ? model.getHeight() : Config.world_width
        );

        world = new JTable(tableModel);
        world.setEnabled(false);

        for (int col = 0; col < model.getWidth(); col++) {
            TableColumn column = world.getColumnModel().getColumn(col);
            column.setMinWidth(Config.world_cell_size);
            column.setMaxWidth(Config.world_cell_size);
            world.setDefaultRenderer(world.getColumnClass(col), new CellRenderer());
        }

        JPanel worldPanel = new JPanel();
        worldPanel.add(world);
        add(worldPanel, BorderLayout.CENTER);
    }

    private void reset() {
        resetButton.setEnabled(false);

        changes.firePropertyChange("reset", false, true);
    }

    private void slide() {
        changes.firePropertyChange("speed", 0, speedSlider.getValue());
    }

    private void start() {
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        stepButton.setEnabled(false);
        resetButton.setEnabled(false);

        changes.firePropertyChange("start", false, true);
    }

    private void step() {
        resetButton.setEnabled(true);

        changes.firePropertyChange("step", false, true);
    }

    private void stop() {
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        stepButton.setEnabled(true);

        if (Integer.parseInt(stepTextField.getText()) > 0) {
            resetButton.setEnabled(true);
        }

        changes.firePropertyChange("stop", true, false);
    }

    private void updateLegend() {
        Map<Class, Integer> agentCounts = new HashMap<>();

        for (int row = 0; row < model.getHeight(); row++) {
            for (int col = 0; col < model.getWidth(); col++) {
                Agent agent = model.getAgent(new Location(col, row));

                if (agent != null) {
                    if (agentCounts.containsKey(agent.getClass())) {
                        int count = agentCounts.get(agent.getClass()) + 1;
                        agentCounts.put(agent.getClass(), count);
                    } else {
                        agentCounts.put(agent.getClass(), 1);
                    }
                }
            }
        }

        legendPanel.removeAll();

        for (Class agent : agentCounts.keySet()) {
            JLabel colorLabel = new JLabel();
            colorLabel.setBackground(agentTypesMap.get(agent));
            colorLabel.setOpaque(true);
            colorLabel.setPreferredSize(new Dimension(15, 15));
            legendPanel.add(colorLabel);

            StringBuilder data = new StringBuilder(agent.getSimpleName());
            data.append(" (").append(agentCounts.get(agent)).append(") ");
            legendPanel.add(new JLabel(data.toString()));
        }
    }

    private class CellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            Agent agent = model.getAgent(new Location(column, row));

            if (agent != null && agentTypesMap.containsKey(agent.getClass())) {
                component.setBackground(agentTypesMap.get(agent.getClass()));
            } else {
                component.setBackground(null);
            }

            return component;
        }
    }
}
