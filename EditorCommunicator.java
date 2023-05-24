import java.io.*;
import java.net.Socket;

/**
 * @author Renata Edaes Hoh, Emiko Rohn
 * CS10 Winter 2023
 */

/**
 * Handles communication to/from the server for the editor
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Chris Bailey-Kellogg; overall structure substantially revised Winter 2014
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			String line;
			while((line = in.readLine()) != null) {
				// this SETID was the easiest way to communicate if the starting ID of a new canvas should not be 0 (e.g. if there are
				// already shapes in the canvas
				if (line.startsWith("SETID")) editor.getSketch().setStartId(Integer.parseInt(line.substring(6)));
				Message mes = new Message(line, editor.getSketch());
				mes.readMessage();
				editor.repaint();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}
	public void sendAdd(Shape shape) {
		send("add -1 " + shape.toString());
	}
	public void sendDelete(Shape shape, int id) {
		send("delete "+ id + " " + shape.toString());
	}
	public void sendRecolor(Shape shape, int id) {
		send("recolor "+ id + " " + shape.toString());
	}
	public void sendMove(int dx, int dy, int id) {
		send("move "+ id + " " + dx + " " + dy);
	}
}
