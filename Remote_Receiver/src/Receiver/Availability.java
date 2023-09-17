package Receiver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Availability {
	public static boolean checkPort(String ip, int port) {
		boolean available = false;
		try {
			Socket Skt = new Socket(ip, port);
			System.out.println("There is a Server on port " + port + " of "
					+ ip);
			available = true;

		} catch (UnknownHostException e) {
			System.out.println("Exception occured" + e);

		} catch (IOException e) {
			System.out.println("port is not used");
		}

		if (available) {
			System.out.println("boolean available true");
		}
		return available;
	}
}
