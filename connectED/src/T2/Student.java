package T2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Student {
	
	private int serverPort;
	private String serverIP;
	private String tag;
	private String helperIP;
	private String helperPort;
	
	public Student(){
		this.tag = "Student";
		this.helperIP = null;
		this.helperPort = null;
		this.serverPort = 4890; //NTNU server Port
		this.serverIP = "10.22.4.202"; //Akkurat nå er Camilla server, ellers NTNU server IP - Christine
	}
	//method for formating the helper address, such that we can divide helperIP and helperPort
	public ArrayList<String> formatHelperAddress(String helperAddress){
		String[] IPAndPort = helperAddress.split("-");
		String helperIP = IPAndPort[0];
		String helperPort = IPAndPort[1];
		ArrayList<String> address = new ArrayList<String>();
		address.add(helperIP);
		address.add(helperPort);
		return address;
	}
	//method for sending a tag "student" to server, 
	//such that the server can enqueue its connection socket to the student
	//returning helperAddress with IP and port if it has found a match 
	public ReturningAddressValues getHelperAddress(String tag) { 
		
		try{
			String helperAddress;
			//lager clientSocket med tilknyttet serverIP og portnummer
			Socket clientSocket = new Socket(serverIP, serverPort);
			//Lager en outputstream for tagen som skal til server
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			//Lager en inputstream som skal plukke opp IP adressen sendr fra serveren
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			//Omgjør tag-setningen til bytes
			outToServer.writeBytes(tag + '\n');
			//Venter på at serveren skal returnere med en hjelper-IP
			helperAddress = inFromServer.readLine();
			clientSocket.close();
			ArrayList<String> formatedHelperAddress = formatHelperAddress(helperAddress);
			helperIP = formatedHelperAddress.get(0);
			helperPort = formatedHelperAddress.get(1);
			int HelperPort = Integer.parseInt(helperPort);
			return new ReturningAddressValues(helperIP, HelperPort);
		}
		catch (IOException IOe){
			IOe.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Student stud = new Student();
		ReturningAddressValues helperAddress = stud.getHelperAddress(stud.tag);
		System.out.println(helperAddress.toString());
		
	}
		
}
