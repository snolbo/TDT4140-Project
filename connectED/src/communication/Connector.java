package communication;

import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Queue;

import T2.ServerRequest;

// Either connect to or sets up connection depending on mode
public class Connector implements Runnable {
	private ServerSocket welcomeSocket = null; // sockets at server
	private Queue<ChatController> controllerQueue;

	private Boolean isHost;
	private int hostPort;

	public Connector() {
		controllerQueue = new ArrayDeque<ChatController>();
		this.hostPort = 9001; // port to connect to if client, port to open at if host
	}

	public Boolean isHost(){
		return this.isHost;
	}
	
	public void setHost() {
		this.isHost = true;
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
	}

	@Override
	public void run() {
		RecieveAndSend connection;
		ChatController tempChatController = controllerQueue.poll();
		Socket socket;
		if (isHost) {
			tempChatController.setHost(true);
			ServerRequest request = new ServerRequest("Helper");
			request.helperRequest();
			try {
				socket = welcomeSocket.accept();
				// TODO -> client should only be able to make one connection at a time
				connection = new RecieveAndSend(socket, tempChatController);
				new Thread(connection).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			tempChatController.setHost(false);
			ServerRequest request = new ServerRequest("Student");
			String helperIP = request.studentRequest();
			try {
				socket = new Socket(helperIP, this.hostPort ); 
				connection = new RecieveAndSend(socket, tempChatController); 
				new Thread(connection).start();
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
