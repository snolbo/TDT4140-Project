package T2;

import java.io.*;
import java.net.*;

public class Helper {
	
	private String tag; 
	private int serverPort;
	private String serverIP;
	
	public Helper(){
		this.tag = "Helper";
		this.serverPort = 4890; //NTNU server Port
		this.serverIP = "10.22.4.202"; //Akkurat nå er Camilla server, ellers NTNU server IP - Christine
	}
	
	//method for sending tag "Helper" to server, 
	//such that the server can enqueue the helpers IP and port
	//no returnvalue, as the helper is only interested in 
	//giving away its address to a student for later connection to this student
	public void giveHelperAddress() { 
		
		try{
			//lager clientSocket med tilknyttet serverIP og portnummer
			Socket clientSocket = new Socket(serverIP, serverPort);
			//Lager en outputstream for tagen som skal til server
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			//omgjør tags til bytes
			outToServer.writeBytes(tag + '\n');
			//Venter på at serveren skal returnere med en hjelper-IP
			clientSocket.close();
		}
		
		catch (IOException IOe){
			IOe.printStackTrace();
		}
	}
		
		
	public static void main(String[] args) throws Exception {
		Helper help = new Helper();
		help.giveHelperAddress();
	}
	
}
