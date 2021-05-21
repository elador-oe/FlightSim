package interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class VarReader {
	int port;
	int latency;
	volatile static boolean stop;
	Object notifier;

	public VarReader(int port, int latency) {
		this.port = port;
		this.latency = latency;
		stop = false;
		notifier = OpenServerCommand.notifier;
	}

	public void runServer() {
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("Avi&OR Server is Running. waiting for flight simulator app...");
			Socket client = server.accept();
			System.out.println("flight simulator app is connected to the server");
			String cLine;
			String[] varNames = { "airspeed", "alt", "Pressure", "pitch", "roll", "Internal-Pitch", "Internal-Roll",
					"Encoder-Altitude", "Encoder-Pressure", "GPS-Altitude", "Ground-Speed", "Vertical-Speed", "heading",
					"Compass-Heading", "Slip", "Turn", "Fpm-Speed", "aileron", "elevator", "rudder", "Flaps",
					"throttle", "Rpm" };
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while (!stop) {
				cLine = in.readLine();
				try {
					Thread.sleep(1000 / latency);
				} catch (InterruptedException e) {
				}
				if (cLine != null) {
					String[] varValues = cLine.split(",");
					for (int i = 0; i < varNames.length; i++) {
						Interpreter.symTable.put(varNames[i], Double.parseDouble(varValues[i]));
						synchronized (notifier) {
							notifier.notify();
						}
					}
				}
			}
			if (stop)
				server.close();

		} catch (IOException e) {
		}
	}

	public static void stop() {
		stop = true;
	}

}
