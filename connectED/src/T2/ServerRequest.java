package T2;

import java.io.*;
import java.net.*;


public class ServerRequest {
	
	private int serverPort;
	private String serverIP;
	private String tag;
	
	/**
	 * @param tag
	 * Constructor. Sets the serverPort and serverIP for the server handling distribution
	 */
	public ServerRequest(String tag){
		this.tag = tag;
		this.serverPort = 9050; //NTNU server Port
		this.serverIP = "hv-6221.idi.ntnu.no"; 

	}
	
	
	/**
	 * @return
	 * Sends a request to server handling distribution and waits for returns the IP of the available assistant when a match is made
	 */
	public String studentRequest() { 
		try{
			System.out.println("sending studentrequest with " + tag);
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
	
	
	/**
	 * Sends a request to the server handling distribution of IP adresses, and queues the IP from the device the request is sent from in the correct queue depending on the tag set in ServerRequest
	 */
	public void helperRequest() { 
		try{
			System.out.println("sending helperrequest with " + tag);
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
	
	
		
	/**
	 * Removes the backmost instance of the IP address queued by this device in the queue corresponding to the tag set in ServerRequest
	 */
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
