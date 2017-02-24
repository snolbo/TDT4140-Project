package server;

import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Queue;


public class Server implements Runnable{
	private ServerSocket serverSocket; // sockets at server
	private Socket socket;
	private Queue<ServerTabController> chatTabControllers;


	// Server is constructed through the constructor of the Controller The server is connected to the Controller as it needs to send changes that needs
	// to be sent to the GUI from the output and input sent from the Server (text in and text out)
	public Server(){		
		try{
			serverSocket = new ServerSocket(7000, 0); // 6789 is the port reserved for this application, 100 is backlog, #of people hat can be in line waiting to join the instant messenger
		} catch(IOException e){
			e.printStackTrace();
		}
		chatTabControllers = new ArrayDeque<ServerTabController>();
	}

	
	@Override
	public void run() {
			// creates an instance to hold a new connection
			ServerConnection serverConnection;
			try{
				// accept connection to the serverSocket
				socket = serverSocket.accept();
				ServerTabController tempChatTabController = chatTabControllers.poll();
				// pops the head of the queue of chatTabControllers and creates a new serverConnection TODO -> one should not be able to make more than one tab waiting for connection
				serverConnection = new ServerConnection(socket, tempChatTabController);
				// the created serverConnection is run as a separate thread
				serverConnection.run();
			}catch(IOException e){
				e.printStackTrace();
		}
	}

	
	public void passChatTabController(ServerTabController controller) {
		this.chatTabControllers.add(controller);
	}


}
