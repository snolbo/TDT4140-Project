package T2;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class TCPServer {
	
		private LinkedList<Socket> studentQueueJava;
		private LinkedList<String> studentIPJava;
		
		private LinkedList<String> studentIPITGK;
		private LinkedList<Socket> studentQueueITGK;
		
		private LinkedList<String> studentHelperQueueJava;
		private LinkedList<String> studentAssistantQueueJava;
		
		private LinkedList<String> studentHelperQueueITGK;
		private LinkedList<String> studentAssistantQueueITGK;
		
		private String tag; // to hold received tag
		private ServerSocket welcomeSocket; // welcomeSocket accepting connections
		private String IP; // hold IP of sende
		private Socket connectionSocket;
		private String helperAddress = null;
		
		public TCPServer(){
			
			studentIPJava = new LinkedList<String>(); // queue to hold student's IPs
			studentIPITGK = new LinkedList<String>();
			//need to have different queues for student IPs too, due to mismatch in time when matching
			
			//queues for Java
			studentQueueJava = new LinkedList<Socket>(); // queue to hold student's connectionSocket
			studentHelperQueueJava = new LinkedList<String>();  // queue to hold student helper's IPs
			studentAssistantQueueJava = new LinkedList<String>(); //queue to hold student assistants' IPs
			
			//queues for ITGK
			studentQueueITGK = new LinkedList<Socket>();
			studentHelperQueueITGK = new LinkedList<String>();
			studentAssistantQueueITGK = new LinkedList<String>();
			
			//TODO: make additional separate queues for each relevant subject
		}
		
		public LinkedList<String> getstudentIPJava(){
			return studentIPJava;
		}
		
		public LinkedList<String> getstudentIPITGK(){
			return studentIPITGK;
		}
		
		public LinkedList<Socket> getstudentQueueJava(){
			return studentQueueJava;
		}
		
		public LinkedList<String> getstudentHelperQueueJava(){
			return studentHelperQueueJava;
		}
		
		public LinkedList<String> getstudentAssistantQueueJava(){
			return studentAssistantQueueJava;
		}
		
		public LinkedList<Socket> getstudentQueueITGK(){
			return studentQueueITGK;
		}
		
		public LinkedList<String> getstudentHelperQueueITGK(){
			return studentHelperQueueITGK;
		}
		
		public LinkedList<String> getstudentAssistantQueueITGK(){
			return studentAssistantQueueITGK;
			
		}
		
		public String getTag(){
			return tag;
		}
		
		public ServerSocket getWelcomeSocket(){
			return welcomeSocket;
		}
		
		public String getIP(){
			return IP;
		}
		
		public Socket getconnectionSocket(){
			return connectionSocket;
		}
		
		public String getHelperAddress(){
			return helperAddress;
		}
		
		public boolean hasMatch(LinkedList<Socket> studentQueue, LinkedList<String> studentAssistantQueue, LinkedList<String> studentHelperQueue){ // exist match to connect student to helper?
			return (!studentQueue.isEmpty() && (!studentAssistantQueue.isEmpty() || !studentHelperQueue.isEmpty()));
		}
		
		public String formatIP(String returnIP){ // receives format: dhcp-10-22-11-63.wlan.ntnu.no. Change to: 10.22.11.63
			if (returnIP.equals("localhost"))
				return returnIP;
			else if(0==1 && !returnIP.matches("^([0-9]{1,4})\\.([0-9]{1,4})\\.([0-9]{1,4})\\.([0-9]{1,4})$")){
				String[] parts = returnIP.split("-");
				//System.out.println(returnIP);
				String part1 = parts[1];
				String part2 = parts[2];
				String part3 = parts[3];
				String oldPart4 = parts[4];
				int index = oldPart4.indexOf(".");
				String newPart4 = oldPart4.substring(0, index);
				return part1 + "." + part2 + "." + part3 + "." + newPart4;
			}
			else
				return returnIP;
		}
		
		
		public void start(){
				try { // to receive connections and sort them into right queue by sent tag
					welcomeSocket = new ServerSocket(9050, 1000);
					while(true) { // receives one connection, and sorts it into the right queue
						try{ // putting this try inside while will retry while-loop if something fucks up, and not crash entire server like if you sorround everything
							connectionSocket = welcomeSocket.accept(); // receive connection
							IP = connectionSocket.getInetAddress().getHostName();
							System.out.println("Received IP: " + IP);
							//IP = formatIP(IP);
							BufferedReader recvBuff = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
							tag = recvBuff.readLine(); // reads one line from connection. Sent messages are end with \n
							System.out.println("Received: " + tag);
							delegateTag(tag, IP, connectionSocket);
						} catch (IOException e) {
							e.printStackTrace(); // dont need to to enything if connection is lost, or not receiving readline as then the socket is not saved as delegateTag is not run

						}
						if (hasMatch(studentQueueJava, studentAssistantQueueJava, studentHelperQueueJava))
							match(studentQueueJava, studentIPJava, studentAssistantQueueJava, studentHelperQueueJava);
						
						if (hasMatch(studentQueueITGK, studentAssistantQueueITGK, studentHelperQueueITGK))
							match(studentQueueITGK, studentIPITGK, studentAssistantQueueITGK, studentHelperQueueITGK);
						
						System.out.println("studentQueueJava: " + studentQueueJava.toString());
						System.out.println("studentHelperQueueJava: " + studentHelperQueueJava.toString());
						System.out.println("studentAssistantQueueJava: " + studentAssistantQueueJava.toString());


						System.out.println("studentQueueITGK: " + studentQueueITGK.toString());
						System.out.println("studentHelperQueueITGK: " + studentHelperQueueITGK.toString());
						System.out.println("studentAssistantQueueITGK: " + studentAssistantQueueITGK.toString());
						
						System.out.println();
					}
				}

				catch(IOException e){
					e.printStackTrace();
				}
		}
		
		public void delegateTag(String tag, String IP, Socket connectionSocket){
			try{
				if (tag.equals("StudentJava")){
					studentQueueJava.addLast(connectionSocket);
					studentIPJava.addLast(IP);
				}
				else if (tag.equals("StudentITGK")){
					studentQueueITGK.addLast(connectionSocket);
					studentIPITGK.addLast(IP);
				}
				else if (tag.equals("StudentAssistantJava")){
					studentAssistantQueueJava.addLast(IP);
					connectionSocket.close(); // don't know if this is smart, or if it should also be saved if they drop out of queue
				}
				else if (tag.equals("StudentAssistantITGK")){
					studentAssistantQueueITGK.addLast(IP);
					connectionSocket.close(); // don't know if this is smart, or if it should also be saved if they drop out of queue
				}
				else if(tag.equals("StudentHelperJava")){
					studentHelperQueueJava.addLast(IP);
					connectionSocket.close();
				}
				else if(tag.equals("StudentHelperITGK")){
					studentHelperQueueITGK.addLast(IP);
					connectionSocket.close();
				}
				else if(tag.equals("StudentJavaDelete"))
					deleteStudentFromQueue(IP, studentIPJava, studentQueueJava);
				else if(tag.equals("StudentITGKDelete"))
					deleteStudentFromQueue(IP, studentIPITGK, studentQueueITGK);
				else if(tag.equals("StudentHelperJavaDelete"))
					deleteStudentHelperFromQueue(IP, studentHelperQueueJava);
				else if(tag.equals("StudentHelperITGKDelete"))
					deleteStudentHelperFromQueue(IP, studentHelperQueueITGK);
				else if(tag.equals("StudentAssistantJavaDelete"))
					deleteStudentAssistantFromQueue(IP, studentAssistantQueueJava);
				else if(tag.equals("StudentAssistantITGKDelete"))
					deleteStudentAssistantFromQueue(IP, studentAssistantQueueITGK);
				}
			catch(IOException e){
				e.printStackTrace();
			}

			


		}
		
		
		//sends helperAddress to the next student in line when a match has occurred, such that he/she can set up a connection to the helper
		//also removing both the helper and student from queues, making way for new matches
		public void match(LinkedList<Socket> studentQueue, LinkedList<String> studentIPQueue, LinkedList<String> studentAssistantQueue, LinkedList<String> studentHelperQueue){
			//System.out.println("Hurra! A match was found!");
			Socket studentSocket = studentQueue.poll(); //connectionSocket
			studentIPQueue.poll();
			//pror
			if (!studentAssistantQueue.isEmpty())
				helperAddress = studentAssistantQueue.poll();
			else if (!studentHelperQueue.isEmpty())
				helperAddress = studentHelperQueue.poll();
			DataOutputStream clientStream;
			try {
				clientStream = new DataOutputStream(studentSocket.getOutputStream());
				clientStream.writeBytes(helperAddress + "\n");
				clientStream.close();
				studentSocket.close();
				
			} catch (IOException e) {
				e.printStackTrace();
				try {
					studentSocket.close();
				} catch (IOException closeEX) {
					closeEX.printStackTrace();
				}
				
			}
		}
		
		// removes the most backmost instance in the studentIPQueue and studentSocketQueue
		public void deleteStudentFromQueue(String IP, LinkedList<String> studentIPQueue, LinkedList<Socket> studentSocketQueue){
			for(int i = studentIPQueue.size() -1; i > -1; i-- ){
				if(IP.equals(studentIPQueue.get(i))){
					try {
						studentSocketQueue.get(i).close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					studentSocketQueue.remove(i);
					studentIPQueue.remove(i);
					break;
				}
			}
		}
		
		
		// removes the most backmost instance in the studentHelperQueue
		public void deleteStudentHelperFromQueue(String IP, LinkedList<String> studentHelperQueue){
			for(int i = studentHelperQueue.size() -1; i > -1; i-- ){
				if(IP.equals(studentHelperQueue.get(i))){
					studentHelperQueue.remove(i);
					break;
				}
			}
		}
		
		
		// removes the most backmost instance in the studentAssistantQueue
		public void deleteStudentAssistantFromQueue(String IP, LinkedList<String> studentAssistantQueue){
			for(int i = studentAssistantQueue.size() -1; i > -1; i-- ){
				if(IP.equals(studentAssistantQueue.get(i))){
					studentAssistantQueue.remove(i);
					break;
				}
			}
		}
		
		public static void main(String[] args) throws Exception{
			System.out.println("Hi, starting server");
			TCPServer server = new TCPServer();
			server.start();
		}
	}	


	
	
