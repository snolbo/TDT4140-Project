package T2;

import java.io.*;
import java.net.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class ServerRequest {
	
	private int serverPort;
	private String serverIP;
	private String tag;
	
	public ServerRequest(String tag){
		this.tag = tag;
		this.serverPort = 9999; //NTNU server Port
		this.serverIP = "10.22.43.121"; 
	}
	
	//method for sending a tag "student" to server, 
	//such that the server can enqueue its connection socket to the student
	//returning helperAddress with IP and port if it has found a match 
	public String studentRequest() { 
		try{
			Socket clientSocket = new Socket(serverIP, serverPort); // connect to server queuing and distributing IP's
			DataOutputStream sendStream = new DataOutputStream(clientSocket.getOutputStream()); // create outputstream
			BufferedReader recvBuff = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // create receiveing stream
			sendStream.writeBytes(tag + '\n'); // own info to server
			String helperIP = recvBuff.readLine(); // receive helperIP
			recvBuff.close();
			sendStream.close();
			clientSocket.close();
			return helperIP;
		}
		catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void helperRequest() { 
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
