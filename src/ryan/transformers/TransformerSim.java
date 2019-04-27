package ryan.transformers;

import prins.simulator.Simulator;
import prins.simulator.model.Location;
import prins.simulator.view.Gui;
import ryan.transformers.model.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static ryan.transformers.TransformerConfig.*;

public class TransformerSim extends Simulator {

    private Planet planet;
    private Gui gui;
    private List<Block> blocks;
    private AutoBot bot;
    private AllSpark allSpark;

    public TransformerSim() {
        this.planet = new Planet();
        this.gui = new Gui(planet);
        this.blocks = new ArrayList<>();
        this.bot = new AutoBot(AUTOBOT_START_LOCATION);
        this.allSpark = new AllSpark(ALL_SPARK_LOCATION);

        gui.registerAgentColors(AutoBot.class, Color.GREEN);
        gui.registerAgentColors(AllSpark.class, Color.RED);
        gui.registerAgentColors(Block.class, Color.BLACK);

        populate();
    }

    @Override
    protected void prepare() {
        gui.addPropertyChangeListener(this);
    }

    @Override
    protected void render() {
        gui.setStep(step);
        gui.render();
    }

    @Override
    protected void reset() {
        planet.clear();
        TransformerConfig.resetRandom();
        populate();
    }

    @Override
    protected void update() {

        //handle iterations for autobot learning
    	 bot.act(planet);

        //set allSpark location - ensure it remains on the sim
        planet.setAgent(allSpark, allSpark.getLocation());

        //set blocks locations - so they are displayed on the sim
        blocks.forEach(block -> planet.setAgent(block, block.getLocation()));
    }

    /**
     * Populate the planet with AutoBots and single AllSpark
     */
    private void populate() {

        //populate autobot
        bot.setLocation(AUTOBOT_START_LOCATION);
        planet.setAgent(bot, bot.getLocation());

        //populate all spark
        planet.setAgent(allSpark, allSpark.getLocation());

        //populate blocks
        populateObstacleCourse();
    }

    /**
     * Clears down the bots list and planet so the simulation can repeat
     */
    private void clear() {
        planet.clearWorld();
    }

    private void populateObstacleCourse() {
        for(BlockArea area : BLOCK_AREAS) {
            blocks.addAll(area.getArea());
        }
    }
}
