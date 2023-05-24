import java.awt.*;
import java.util.ArrayList;

/**
 * @author Renata Edaes Hoh, Emiko Rohn
 * CS10 Winter 2023
 */

/**
 * Helps us go from a string to a shape
 *
 * @author Emi Rohn and Renata Hoh, Dartmouth COSC 10, 23W
 */

public class Message {
    String message;
    Sketch sketch;
    public Message(String message, Sketch sketch){
        this.message = message;
        this.sketch = sketch;
    }

    /**
     * Reads message given in constructor
     */
    public void readMessage(){
        // here we are just filtering the string, taking out the command words add, move, recolor, and delete
        if (message.startsWith("add")) {
            drawShape(message.substring(4), sketch);
        } else if (message.startsWith("move")) {
            moveShape(message.substring(5), sketch);
        } else if (message.startsWith("recolor")) {
            recolorShape(message.substring(8), sketch);
        } else if (message.startsWith("delete")) {
            deleteShape(message.substring(7), sketch);
        }
    }

    /**
     * Message format: id shapeType x1 y1 x2 y2 (segment coordinates for polyline)
     * @param message String message after "draw "
     */
    private void drawShape(String message, Sketch sketch) {
        String[] parts = message.split(" ");
        Shape curr = null;
        // storing ID passed from the communicator
        int id = Integer.parseInt(parts[0]);

        // using corresponding string parts for each different shape to create them
        if (parts[1].equals("ellipse")) {
            Color color = new Color(Integer.parseInt(parts[6]));
            curr = new Ellipse(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), color);

        } else if (parts[1].equals("segment")){
            Color color = new Color(Integer.parseInt(parts[6]));
            curr = new Segment(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), color);

        }else if (parts[1].equals("rectangle")){
            curr = new Rectangle(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
            //Need to set color like this because the given rectangle constructor doesn't take color as a parameter
            curr.setColor(new Color(Integer.parseInt(parts[6])));

        }else if (parts[1].equals("polyline")){
            ArrayList<Segment> segments = new ArrayList<Segment>();
            //Need to loop through all - starting on the 4th and ending on the second to last
            //Increment i by 6 because we're creating segments
            for(int i = 4; i < parts.length-1; i = i + 6){
                Segment s = new Segment(Integer.parseInt(parts[i]), Integer.parseInt(parts[i+1]), Integer.parseInt(parts[i+2]), Integer.parseInt(parts[i+3]), new Color(Integer.parseInt(parts[parts.length-1])));
                segments.add(s);
            }
            curr = new Polyline(segments, new Color(Integer.parseInt(parts[parts.length-1])));
        }
        // if shape already exists, replace it with current id
        if (id != -1) sketch.addShapeId(id, curr);
        // otherwise create new shape
        else sketch.addShape(curr);
    }

    /**
     * Message format: id dy dx
     * @param message String message after "move "
     */
    private void moveShape(String message, Sketch sketch) {
        String parts[] = message.split(" ");
        int id = Integer.parseInt(parts[0]);
        Shape curr = sketch.getShape(id);

        //Uses moveBy method from Shape interface
        curr.moveBy(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

        //Replace the old shape
        sketch.addShapeId(id, curr);
    }

    /**
     * Message format: id color
     * @param message String message after "recolor "
     */
    private void recolorShape(String message, Sketch sketch){
        String parts[] = message.split(" ");
        int id = Integer.parseInt(parts[0]);
        Shape curr = sketch.getShape(id);
        curr.setColor(new Color(Integer.parseInt(parts[parts.length-1])));

        //Replace the old shape
        sketch.addShapeId(id, curr);
    }

    /**
     * Message format: id
     * @param message String message after "delete "
     */
    private void deleteShape(String message, Sketch sketch) {
        String parts[] = message.split(" ");
        int id = Integer.parseInt(parts[0]);
        sketch.deleteShape(id);
    }
    public String toString() {
        return message;
    }
}
