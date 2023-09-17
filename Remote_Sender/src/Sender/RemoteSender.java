package Sender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;

import javax.swing.JOptionPane;

public class RemoteSender {
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		//new AcceptPID(7000).start();
		ServerHandler serverHandler = new ServerHandler();
		String command;
		while (true) {
			command = JOptionPane.showInputDialog("Give next command");
			if (command.length() > 0) {
				Socket socket = new Socket(InetAddress.getByName("38.108.92.178"), 5201);
				PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
				out.println(command);

		
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null) {
					result.append(line + "\n");
				}
				System.out.println(" " + result.toString());
			}
		}
	}

}
