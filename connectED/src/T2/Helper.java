package T2;

import java.io.*;
import java.net.*;

// replaced with ServerRequest
public class Helper {
	
	private String tag; 
	private int serverPort;
	private String serverIP;
	
	public Helper(String tag){
		this.tag = tag;
		this.serverPort = 6000; //NTNU server Port
		this.serverIP = ""; //Akkurat nå er Camilla server, ellers NTNU server IP - Christine
	}
	
	//method for sending tag "Helper" to server, 
	//such that the server can enqueue the helpers IP and port
	//no returnvalue, as the helper is only interested in 
	//giving away its address to a student for later connection to this student
	public void giveHelperAddress() { 
		try{
			Socket clientSocket = new Socket(serverIP, serverPort); // socket to server queuing requests
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // output stream
			outToServer.writeBytes(tag + '\n'); // write tag to server. server take IP from message. Port to connect is decided from T1
			outToServer.close();
			clientSocket.close(); // close socket, next wait for someone to connect to the open port that we have chosen in Connector
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
		
	public void removeAdressFromQueue(){
		try{
			Socket clientSocket = new Socket(serverIP, serverPort); // socket to server queuing requests
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); // output stream
			outToServer.writeBytes(tag + '\n'); // write tag to server. server take IP from message. Port to connect is decided from T1
			outToServer.close();
			clientSocket.close(); // close socket, next wait for someone to connect to the open port that we have chosen in Connector
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

}
