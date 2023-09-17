package Sender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.print.DocFlavor.STRING;
import javax.swing.JOptionPane;

public class ServerHandler {

	public int sendCommand(String command, String ipAddress, int port)
			throws UnknownHostException, IOException {
		Socket socket = new Socket(InetAddress.getByName(ipAddress), port);
		int localPort = socket.getLocalPort();
		System.out.println("localport:" +localPort);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		writer.write(command);
		// writer.write("ping -c 3 192.168.1.181");
		writer.flush();
		writer.close();
		socket.close();
		ServerSocket serverSocket = new ServerSocket(localPort);
		Socket socketAcceptResult = serverSocket.accept();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				socketAcceptResult.getInputStream()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			result.append(line + "\n");
		}
		System.out.println(" " + result.toString());
		
		return localPort;
	}
}
