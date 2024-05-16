package serversocketsproj1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private static final int PORT = 6666;
	private static List<ClientHandler> clients = new ArrayList<>();
	static ArrayList<String> al = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT);
		InetAddress ipAddress = InetAddress.getLocalHost();
		System.out.println("Server IP address: " + ipAddress.getHostAddress());
		System.out.println("Server started. Waiting for clients...");

		while (true) {
			Socket clientSocket = serverSocket.accept();
			System.out.println("Client connected: " + clientSocket);

			// Create a new thread to handle client communication
			ClientHandler handler = new ClientHandler(clientSocket);
			clients.add(handler);
			handler.start();
		}
	}

	public static void broadcastMessage(String message, String clientName, ClientHandler excludeClient) {
		for (ClientHandler client : clients) {
			if (client != excludeClient) {
				client.sendMessage(clientName + ":" + message);
			}
		}
	}

	public static void storename(String clientName, ClientHandler clientHandler) {
		al.add(clientName);
		for (ClientHandler client : clients) {
			client.sendMessage(al.toString() + "1");
		}

	}

}

class ClientHandler extends Thread {

	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private String clientName;

	public ClientHandler(Socket socket) throws IOException {
		this.clientSocket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
	}

	public void run() {
		try {

			clientName = in.readLine();

			out.println("welcome:" + clientName);
			Server.storename(clientName, this);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {

				Server.broadcastMessage(inputLine, clientName, this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}
}