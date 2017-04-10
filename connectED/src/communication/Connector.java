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
		this.hostPort = 9000; // port to connect to if client, port to open at if host
	}

	public Boolean isAssistantHost(){
		return this.isAssistantHost;
	}
	
	public Boolean isHelperHost(){
		return this.isHelperHost;
	}
	
	
	
	public void setHelperHost() {
		this.isHelperHost = true;
		this.isAssistantHost = false;
		if(this.welcomeSocket == null){
			try {
				welcomeSocket = new ServerSocket(hostPort, 20);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void setAssistantHost() {
		this.isAssistantHost = true;
		this.isHelperHost = false;
		if(this.welcomeSocket == null){
			try {
				welcomeSocket = new ServerSocket(hostPort, 20);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setClient() {
		this.isHelperHost = false;
		this.isAssistantHost = false;
	}
	
	public void sendHelperRequest(String tag){
		ServerRequest request = new ServerRequest(tag);
		request.helperRequest();
	}
	
	public void connect(){
		if (isHelperHost || isAssistantHost) {
			try {
				socket = welcomeSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
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
			if(this.welcomeSocket != null)
				this.welcomeSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
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
