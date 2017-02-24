package T2;
import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class TCPServer {
	
	private LinkedList<Socket> studentQueue;
	private LinkedList<String> helperQueue;
	
	public TCPServer(){
		studentQueue = new LinkedList<Socket>();
		helperQueue = new LinkedList<String>();
	}
	
	
	public LinkedList<Socket> getStudentQueue(){
		return this.studentQueue;
	}
	
	public LinkedList<String> getHelperQueue(){
		return this.helperQueue;
	}
	
       
	public void addStudentQueue(Socket connectionSocket){
		//adds an element at the end of the queue
		studentQueue.addLast(connectionSocket);
       }
	
	public boolean match(){
		return !studentQueue.isEmpty() && !helperQueue.isEmpty();
	}
	
	public Socket studentDequeue() {
		return studentQueue.poll();
	}
	
	//adds an helper at the end of the queue
	public void addHelperQueue(String helperIP, int returnPort) {
		helperQueue.addLast(helperIP + "-" + returnPort);
	}
	
	//removes first helper from Queue
	public String helperDequeue() {
		return helperQueue.poll();
		}
	
	
	//FÂr formatet dhcp-10-22-11-63.wlan.ntnu.no, endrer til 10.22.11.63
	public String formatIP(String returnIP){
		String[] parts = returnIP.split("-");
		String part1 = parts[1];
		String part2 = parts[2];
		String part3 = parts[3];
		String oldPart4 = parts[4];
		int index = oldPart4.indexOf(".");
		String newPart4 = oldPart4.substring(0, index);
		return part1 + "." + part2 + "." + part3 + "." + newPart4;	
	}

	
	public void connectToClient(){
		TCPServer server = new TCPServer();
		String clientTag;
		String returnIP; //helperIP
		int returnPort;
		ServerSocket welcomeSocket;
		String clientIP;
		
		try {
			welcomeSocket = new ServerSocket(4890);
	
			while(true) {
				
				//aksepteret tilkobling
				Socket connectionSocket;
				connectionSocket = welcomeSocket.accept();
    	  
				Socket studentSocket = null;
				String helperAddress = null;	
    	  
				//lagrer IP til client i returnIP
				returnIP = connectionSocket.getInetAddress().getHostName();
				returnPort = connectionSocket.getPort();
				System.out.println(returnPort);
				
				clientIP = formatIP(returnIP);
				
          
				//Gj¯r det mulig Â lese inn det client sender gjennom connectionSocket 
				BufferedReader inFromClient =
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
          
				clientTag = inFromClient.readLine();
				System.out.println("Received: " + clientTag);
  
       
				if (clientTag.equals("Student")){
					server.addStudentQueue(connectionSocket);
				}
       
				else if (clientTag.equals("Helper")){
					server.addHelperQueue(clientIP,returnPort);
				}
         
				if (server.match()){
					studentSocket = server.studentDequeue(); //connectionSocket
					helperAddress = server.helperDequeue(); //helperIP + returnPort
					System.out.println("Hurra!");
					
				}
  		
				//Lager en outputstream sÂ det kommer tilbake til client 
       
				if (clientTag.equals("Student")){
					DataOutputStream outToClient = new DataOutputStream(studentSocket.getOutputStream());
					outToClient.writeBytes(helperAddress);
				} 
				
				if (studentSocket != null){
					studentSocket.close();
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
			
	}
       
     
	public static void main(String[] args) throws Exception{
		TCPServer server = new TCPServer();
		server.connectToClient();
	}
}	
	
	
