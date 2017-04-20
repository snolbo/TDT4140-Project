package communication;

import java.io.*;
import java.net.*;

import T2.ServerRequest;


// Either connect to or sets up connection depending on mode
public class Connector implements Runnable {
	private ServerSocket welcomeSocket = null; // sockets at server
	private Socket socket = null;
	
	private String helperIP;

	private Boolean isAssistantHost = null;
	private Boolean isHelperHost = null;
	private int hostPort;
	private ChatTabController chatTabController;

	public Connector(ChatTabController chatTabController) {
		this.chatTabController = chatTabController;
		this.hostPort = 9005; // port to connect to if client, port to open at if host
//		try {
//			welcomeSocket = new ServerSocket(hostPort, 20);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public Boolean isAssistantHost(){
		return this.isAssistantHost;
	}
	
	public Boolean isHelperHost(){
		return this.isHelperHost;
	}
	
	public void close(){
		closeWelcomeSocket();
	}
	
	
	
	public void setHelperHost() {
		this.isHelperHost = true;
		this.isAssistantHost = false;	
		try {
			welcomeSocket = new ServerSocket(hostPort, 20);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setAssistantHost() {
		this.isAssistantHost = true;
		this.isHelperHost = false;
		try {
			welcomeSocket = new ServerSocket(hostPort, 20);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setClient() {
		this.isHelperHost = false;
		this.isAssistantHost = false;
		
		try {
			if(welcomeSocket != null){
				welcomeSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendHelperRequest(String tag){
		ServerRequest request = new ServerRequest(tag);
		request.helperRequest();
	}
	
	public void connect(){
		System.out.println("Connect in connector: helperhost:" + isHelperHost + ", assistantHost:" + isAssistantHost);
		if (isHelperHost || isAssistantHost) {
			try {
				socket = welcomeSocket.accept();
			} catch (IOException e) {
//				e.printStackTrace();
				closeWelcomeSocket();
			}
		}
		else {
			ServerRequest request = new ServerRequest(chatTabController.getTag());
			helperIP = request.studentRequest();
			if(helperIP != null){
				try {
					socket = new Socket(helperIP,hostPort);
				} catch (IOException e) {
					e.printStackTrace();
					socket = null;
				}
			}
		}
	}
	
	@Override
	public void run() {
		connect();
		if(socket != null)
			this.chatTabController.startChatSession(socket);
	}
	
	public void closeWelcomeSocket(){
		try {
			if(welcomeSocket != null){
				welcomeSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			welcomeSocket = null;
		}
	}
	
	public ServerSocket getWelcomeSocket(){
		return this.welcomeSocket;
	}
	
	
	public Socket getSocket(){
		return this.socket;
	}
	
	public String getHelperIP(){
		return this.helperIP;
	}

}
