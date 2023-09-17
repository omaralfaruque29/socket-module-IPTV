package Receiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class RemoteReceiver {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(5000);
		Socket socket = null;
		BufferedReader socketReader;
		InetAddress ip = null;

		// Timer timer = new Timer();
		// timer.scheduleAtFixedRate(new TimerTask() {
		// @Override
		// public void run() {
		// // if (Availability.checkPort("192.168.100.25", 7001)) {
		// String getPID = "pgrep -fl ffmpeg";
		// new ProcessFunction(getPID, "192.168.100.173", 7000).start();
		// // }
		// }
		// }, 1 * 60 * 1000, 5 * 60 * 1000);

		while (true) {
			try {
				socket = serverSocket.accept();
				String ipAddress = socket.getInetAddress().getHostAddress();
				int port = socket.getPort();
				System.out.println("command from ip address:" + ipAddress
						+ " & port" + port);
				socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String input = socketReader.readLine();
				System.out.println("INPUT..............   " + input);
				if (input.length() > 0) {
					new ProcessFunction(socket, input).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
