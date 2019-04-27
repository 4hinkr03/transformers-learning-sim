package ryan.transformers;

import prins.simulator.Config;
import prins.simulator.model.Location;
import ryan.transformers.model.BlockArea;

import java.util.Random;

public class TransformerConfig {

    private static final int RANDOM_SEED = 28;

     public static Random RANDOM = new Random(RANDOM_SEED);

    public static final Location AUTOBOT_START_LOCATION = new Location(5, 15);
    public static final Location ALL_SPARK_LOCATION = new Location(24, 12);

    public static final BlockArea[] BLOCK_AREAS = {
            
    		new BlockArea(15, 12, 1, 7),
    		new BlockArea(9, 2, 1, 7),
    		new BlockArea(9, 22, 1, 7),
    		new BlockArea(24, 21, 1, 7),
    		new BlockArea(18, 5, 7, 1),
    		 
            
            
            
            
            /*new BlockArea(5, 0, 25, 8),
            new BlockArea(2, 5, 3, 2),
            new BlockArea(5, 8, 4, 6),
            new BlockArea(9, 11, 3, 3),
            new BlockArea(0, 11, 2, 3),
            new BlockArea(0, 17, 16, 13),
            new BlockArea(16, 20, 5, 10),
            new BlockArea(15, 11, 11, 1),
            new BlockArea(25, 12, 1, 10),*/
            
    };

    public static void resetRandom() {
        RANDOM = new Random(RANDOM_SEED);
    }

    static {
        Config.max_simulation_speed = 2;
    }
}
