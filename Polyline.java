import java.awt.*;
import java.util.ArrayList;

/**
 * @author Renata Edaes Hoh, Emiko Rohn
 * CS10 Winter 2023
 */

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 */
public class Polyline implements Shape {
	// TODO: YOUR CODE HERE
	Color color;
	ArrayList<Segment> segments;
	int numPoints;

	public Polyline(ArrayList<Segment> segments, Color color) {
		this.color = color;

		this.segments = segments;
	}

	@Override
	public void moveBy(int dx, int dy) {
		segments.forEach((s) -> s.moveBy(dx, dy));
	}

	@Override
	public Color getColor() {return color;}

	@Override
	public void setColor(Color color) {this.color = color;}

	@Override
	public boolean contains(int x, int y) {
		return segments.stream().anyMatch((s) -> s.contains(x, y));
	}

	@Override
	public void draw(Graphics g) {
		segments.forEach((s) -> {
			s.setColor(color);
			s.draw(g);
		});
	}

	@Override
	public String toString() {
		return "polyline  " + segments + " " +color.getRGB();
	}
}

