package communication;

import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Queue;

import T2.ServerRequest;

// Either connect to or sets up connection depending on mode
public class Connector implements Runnable {
	private ServerSocket welcomeSocket = null; // sockets at server

	private Boolean isAssistantHost = null;
	private Boolean isHelperHost = null;
	private int hostPort;
	private ChatTabController chatTabController;

	public Connector(ChatTabController chatTabController) {
		this.chatTabController = chatTabController;
		this.hostPort = 9001; // port to connect to if client, port to open at if host
	}

	public Boolean isAssistantHost(){
		return this.isAssistantHost;
	}
	
	public Boolean isHelperHost(){
		return this.isHelperHost;
	}
	
	public void setHelperHost() {
		this.isHelperHost = true;
		if(this.welcomeSocket == null){
			try {
				welcomeSocket = new ServerSocket(hostPort, 20);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setAssistantHost() {
		this.isHelperHost = true;
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
	
	@Override
	public void run() {
		Socket socket = null;
		if (isHelperHost || isAssistantHost) {
			try {
				socket = welcomeSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			ServerRequest request = new ServerRequest("Student");
			String helperIP = request.studentRequest();
			try {
				socket = new Socket(helperIP,hostPort);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

}
