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

    public double distance() {
    	int absX = Math.abs(x);
    	int absY = Math.abs(y);
    	
    	if(absX > 0 && absY > 0) {
    		//returns highest abs value
    		return absX < absY ? absY : absX;
    	} 
    	return absX + absY;
         
    }

    // y is inverted due to where 0,0 is
    public static Vector delta(Vector v1, Vector v2) {
        return new Vector(v2.x - v1.x, (v2.y - v1.y) * -1);
    }

    public static Vector vector(Location location) {
        return new Vector(location.getX(), location.getY());
    }

    @Override
    public String toString() {
        return "[Vector= " + x + ", " + y  + "]";
    }
    
    public void inverse() {
    	x *= -1;
    	y *= -1;
   	}
}
