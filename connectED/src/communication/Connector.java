package communication;

import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Queue;

// Either connect to or sets up connection depending on mode
public class Connector implements Runnable {
	private ServerSocket welcomeSocket = null; // sockets at server
	private Queue<ChatController> controllerQueue;

	private Boolean isHost;
	private String serverIP;
	private int hostPort;

	public Connector() {
		controllerQueue = new ArrayDeque<ChatController>();
		this.hostPort = 8012; // this is to be recieves from other moduøe
	}

	public Boolean isHost(){
		return this.isHost;
	}
	
	public void setHost() {
		this.isHost = true;
		System.out.println("isHost is set to true?: " + Boolean.toString(this.isHost == true));
		if(this.welcomeSocket == null){
			try {
				welcomeSocket = new ServerSocket(hostPort, 2);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		Socket socket;
		if (isHost) {		// I am host
			try {
				socket = welcomeSocket.accept();
				// TODO -> client should only be able to make one connection at a time
				connection = new RecieveAndSend(socket, tempChatTabController); // create new thred operating on connection with controller
				connection.run();		// connection implements Runnable
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// I am client
		else {
			try {
				socket = new Socket(serverIP, hostPort); 
				connection = new RecieveAndSend(socket, tempChatTabController);  // create new thred operating on connection with controller
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
