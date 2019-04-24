package ryan.transformers;

import prins.simulator.Config;
import prins.simulator.model.Location;
import ryan.transformers.model.BlockArea;

import java.util.Random;

public class TransformerConfig {

    private static final int RANDOM_SEED = 28;

     public static Random RANDOM = new Random(RANDOM_SEED);

    public static final Location AUTOBOT_START_LOCATION = new Location(4, 15);
    public static final Location ALL_SPARK_LOCATION = new Location(24, 12);

    public static final BlockArea[] BLOCK_AREAS = {
            new BlockArea(15, 12, 1, 7),
    };

    public static void resetRandom() {
        RANDOM = new Random(RANDOM_SEED);
    }

    static {
        Config.max_simulation_speed = 2;
    }
}
