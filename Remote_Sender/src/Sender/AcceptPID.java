package Sender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptPID extends Thread {
	int port;
	public AcceptPID(int port) {
		this.port = port;
	}
	@Override
	public void run() {
		try {
			ServerHandler handler = new ServerHandler();
			while (true) {
				//handler.acceptResult(port);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
