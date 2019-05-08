package ryan.transformers.model;

import prins.simulator.model.Location;

public class Vector {

    protected int x, y;

    public Vector() {
        this(0,0);
    }

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    public int distance() {
        return Math.abs(x) + Math.abs(y);
    }

    public static Vector delta(Vector v1, Vector v2) {
        return new Vector(v2.x - v1.x, v2.y - v1.y);
    }

    public static Vector vector(Location location) {
        return new Vector(location.getX(), location.getY());
    }

    @Override
    public String toString() {
        return "[Vector= " + x + ", " + y  + "]";
    }
}
