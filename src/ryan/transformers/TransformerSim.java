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
        planet.clearWorld();
        TransformerConfig.resetRandom();
        populate();
        bot.reset();
        gui.setStep(0);
    }

    @Override
    protected void update() {

        //handle iterations for autobot learning
    	if (bot.isAlive()) {
    		bot.act(planet);
    	} else {
    		//bot isn't alive and sim needs to restart
    		reset();
    	}
    	

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

    private void populateObstacleCourse() {
        for(BlockArea area : BLOCK_AREAS) {
            blocks.addAll(area.getArea());
        }
    }
}
