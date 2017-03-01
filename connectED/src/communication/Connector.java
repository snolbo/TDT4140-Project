package communication;

import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Queue;

// Either connect to or sets up connection depending on mode
public class Connector implements Runnable {
	private ServerSocket welcomeSocket = null; // sockets at server
	private Socket socket;
	private Queue<ChatController> controllerQueue;

	private boolean isHost;
	private String serverIP;
	private int hostPort;

	public Connector() {
		controllerQueue = new ArrayDeque<ChatController>();
		this.hostPort = 8012; // this is to be recieves from other moduøe
	}

	public void setHost() {
		this.isHost = true;
		try {
			welcomeSocket = new ServerSocket(hostPort, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setClient() {
		this.isHost = false;
		this.serverIP = ""; // to be recieved from other module
		// also recieve hostport
//		this.hostPort= 
	}

	@Override
	public void run() {
		RecieveAndSend connection;	// creates an object to hold a new connection
		ChatController tempChatTabController = controllerQueue.poll();
		if (isHost) {		// I am host
			try {
				socket = welcomeSocket.accept();
				// TODO -> client should only be able to make one connection at a time
				connection = new RecieveAndSend(socket, tempChatTabController);
				connection.run();		// connection implements Runnable
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// I am client
		else {
			try {
				this.socket = new Socket(serverIP, hostPort); // connect to the
																// server
				connection = new RecieveAndSend(socket, tempChatTabController);
				connection.run();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}

	public void passChatTabController(ChatController controller) {
		this.controllerQueue.add(controller);
	}
	
	public void onCloseRequest(){
		try {
			if(this.welcomeSocket != null)
				this.welcomeSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
