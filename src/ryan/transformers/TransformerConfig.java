package ryan.transformers;

import prins.simulator.Config;
import prins.simulator.model.Agent;
import prins.simulator.model.Location;
import ryan.transformers.model.BlockArea;
import ryan.transformers.model.Decepticon;

import java.util.Random;

public class TransformerConfig {

	private static final int RANDOM_SEED = 28;

	public static Random RANDOM = new Random(RANDOM_SEED);
	public static Random RANDOM_DECEPTICON = new Random(RANDOM_SEED);

	public static final Location AUTOBOT_START_LOCATION = new Location(5, 15);
	public static final Location ALL_SPARK_LOCATION = new Location(24, 12);
	public static final Location[] DECEPTICON_LOCATIONS = { new Location(15, 7), new Location(19, 15),
			new Location(17, 24) };

	public static final BlockArea[] BLOCK_AREAS = { new BlockArea(15, 6, 1, 11), new BlockArea(9, 2, 1, 7),
			new BlockArea(9, 22, 1, 7), new BlockArea(24, 21, 1, 7), new BlockArea(18, 5, 7, 1), };

	public static void resetRandom() {
		RANDOM = new Random(RANDOM_SEED);
		RANDOM_DECEPTICON = new Random(RANDOM_SEED);
	}

	public static int randomInt(Class<? extends Agent> agent, int min, int max) {
		Random random = RANDOM;
		if (agent == Decepticon.class) {
			random = RANDOM_DECEPTICON;
		}
		return random.nextInt(max - min + 1) + min;
	}

	static {
		Config.max_simulation_speed = 5;
	}
}
