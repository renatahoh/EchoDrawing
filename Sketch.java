import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Renata Edaes Hoh, Emiko Rohn
 * CS10 Winter 2023
 */

public class Sketch {
    static Map<Integer, Shape> sketch;
    int id;

    public Sketch() {
        sketch = new TreeMap<Integer, Shape>();
        id = 0;
    }

    /**
     * Adds a new shape to the map
     *
     * @param shape shape to add to map
     */
    public int addShape(Shape shape){
        if (shape != null) {
            if (!sketch.containsKey(id)) {
                System.out.println("Adding new shape: " + id + " " + shape);
                sketch.put(id++, shape);
            } else {
                System.out.println("Replacing shape at: " + id);
                sketch.put(id++, shape);
            }
        }
        return id;
    }

    /**
     * Adds shape given ID
     *
     * @param id integer id of shape
     * @param shape shape class
     */
    public void addShapeId(Integer id, Shape shape){
        sketch.put(id, shape);
    }

    public void setStartId(int id){
        this.id = id;
    }

    public Shape getShape(Integer id){
        return sketch.get(id);
    }

    public Map<Integer, Shape> getSketch() {return sketch;}

    public void deleteShape(Integer id){
        sketch.remove(id);
    }

    /**
     * Given the point (x, y) of the mouse, give me the id of the shape I'm clicking on
     * @param p point (x, y) coordinates of mouse
     * @return id of shape, -1 if shape not found
     */
    public static int getId(Point p){
        for(Map.Entry<Integer, Shape> s : sketch.entrySet()){
            if(s.getValue().contains(p.x, p.y)) return s.getKey();
        }
        return -1;
    }

}
