package communication;

import java.io.*;
import java.net.*;


/**
 * @author snorr
 * Handles the connection setup betweenhelpers and students
 */
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
		this.hostPort = 9005;
	}

	public Boolean isAssistantHost(){
		return this.isAssistantHost;
	}
	
	public Boolean isHelperHost(){
		return this.isHelperHost;
	}
	
	/**
	 * Handles closerequest
	 */
	public void close(){
		closeWelcomeSocket();
	}
	
	
	
	/**
	 * Sets mode as helperhost and creates a new welcomesocket
	 */
	public void setHelperHost() {
		this.isHelperHost = true;
		this.isAssistantHost = false;	
		try {
			welcomeSocket = new ServerSocket(hostPort, 20);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * sets mode as assistant and creates a new welcomesocket
	 */
	public void setAssistantHost() {
		this.isAssistantHost = true;
		this.isHelperHost = false;
		try {
			welcomeSocket = new ServerSocket(hostPort, 20);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Seets mode as client and closes open welcomesockets if there are any
	 */
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
	
	/**
	 * @param tag
	 * Sends a serverrequest telling distributing IP server that this person is a halper
	 */
	public void sendHelperRequest(String tag){
		ServerRequest request = new ServerRequest(tag);
		request.helperRequest();
	}
	
	/**
	 * helpers  wait for someone to connect to their open socket. Students wait for response from server distributing ip, and then connect to helpers
	 */
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
		return socket;
	}
	
	public String getHelperIP(){
		return helperIP;
	}

}
