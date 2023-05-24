import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * @author Renata Edaes Hoh, Emiko Rohn
 * CS10 Winter 2023
 */

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");

			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			// loop over all shapes of server skecth, then send each to the communicator
			Map<Integer, Shape> sketch = server.getSketch().getSketch();
			for (Integer id : sketch.keySet()) {
				out.println("add " + id + " "+ sketch.get(id));
			}

			// send last id
			out.println("SETID " + server.getSketch().id);

			// Keep getting and handling messages from the client
			String line;
			while ((line = in.readLine()) != null) {
				Message msg = new Message(line, server.getSketch());
				msg.readMessage();
				server.broadcast(msg.toString());
			}

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
