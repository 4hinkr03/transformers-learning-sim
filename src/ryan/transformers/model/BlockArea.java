package ryan.transformers.model;

import prins.simulator.model.Location;

import java.util.ArrayList;
import java.util.List;

public class BlockArea {

    private final List<Block> blocks;
    private int x, y, width, height;

    public BlockArea(int x, int y, int width, int height) {
        this.blocks = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        generateArea();
    }

    private void generateArea() {
        blocks.clear();
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                Block fillBlock = new Block(new Location(x + w, y + h));
                blocks.add(fillBlock);
            }
        }
    }


    public List<Block> getArea() {
        return blocks;
    }
}
