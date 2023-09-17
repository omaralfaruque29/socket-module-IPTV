package Receiver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.Socket;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.swing.text.html.parser.AttributeList;

public class ProcessFunction extends Thread {
	String input;
	String ipAddress;
	int port;
	Process process;
    Socket socket;
	public ProcessFunction(Socket socket, String input) {
		this.socket = socket;
		this.input = input;
//		this.ipAddress = ipAddress;
//		this.port = port;
	}

	@Override
	public void run() {
		try {
			String cpuMemUtil = "";
			if (input.startsWith("ffmpeg -i")) {
				String[] cmd = { "/bin/sh", "-c", input};
				String output = processCommand(cmd);
			} else if (input.equalsIgnoreCase("getcpumemory")) {
				Long totalMem = null;
				Long freeMem = null;
				List<String> methodsBucket = new ArrayList<String>();
				methodsBucket.add("getSystemCpuLoad");
				methodsBucket.add("getTotalPhysicalMemorySize");
				methodsBucket.add("getFreePhysicalMemorySize");
				OperatingSystemMXBean operatingSystemMXBean = ManagementFactory
						.getOperatingSystemMXBean();
				for (Method method : operatingSystemMXBean.getClass()
						.getDeclaredMethods()) {
					if (methodsBucket.contains(method.getName())) {
						method.setAccessible(true);
						if (method.getName().startsWith("get")
								&& Modifier.isPublic(method.getModifiers())) {
							Object value;
							try {
								value = method.invoke(operatingSystemMXBean);
							} catch (Exception e) {
								value = e;
							}
							System.out.println("" + method.getName() + ":"
									+ value);
							if (method.getName().equalsIgnoreCase(
									"getSystemCpuLoad")) {
								double idleCpu = (1 - (double) value) * 100;
								cpuMemUtil = cpuMemUtil
										+ Double.toString(idleCpu) + ",";
							} else if (method.getName().equalsIgnoreCase(
									"getTotalPhysicalMemorySize")) {
								totalMem = (Long) value;
							} else if (method.getName().equalsIgnoreCase(
									"getFreePhysicalMemorySize")) {
								freeMem = (Long) value;
							}
						}
					}
				}
				if (totalMem != null && freeMem != null) {
					Long idleMem = (freeMem * 100) / totalMem;
					cpuMemUtil = cpuMemUtil + Long.toString(idleMem);
					System.out.println("output:" + cpuMemUtil);
				}

				sendResponse(cpuMemUtil);

			} else if (input.contains("|")) {
				String[] cmd = { "/bin/sh", "-c", input };
				String output = processCommand(cmd);
			} else {
				String output = processCommand(input);
				sendResponse(output);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String processCommand(String input) {
		String ordinaryOutput = "";
		try {
			System.out.println("input from process block");
			process = Runtime.getRuntime().exec(input);
			process.waitFor();
			System.out.println("ffmpeg command executed");
			BufferedReader processReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			StringBuffer output = new StringBuffer();
			String line = "";
			while ((line = processReader.readLine()) != null) {
				output.append(line + "\n");
			}
			if (output.toString().length() > 0) {
				System.out.println(output.toString());
				ordinaryOutput = output.toString();
			} else {
				ordinaryOutput = "No output Stream";
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return ordinaryOutput;
	}

	private String processCommand(String[] input) {
		String ordinaryOutput = "";
		try {
			
			Process proc = new ProcessBuilder(input).start();

//			process = Runtime.getRuntime().exec(input);
//			process.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ordinaryOutput;
	}

	private void sendResponse(String output) {
		try {
//			Socket ordinarySocket = new Socket(
//					InetAddress.getByName(ipAddress), port);
			//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.println(output);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
