package T2;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class TCPServerUpdated {
	
		private LinkedList<Socket> studentQueueJava;
		private LinkedList<String> studentIPJava;
		
		private LinkedList<String> studentIPITGK;
		private LinkedList<Socket> studentQueueITGK;
		
		private LinkedList<String> studentHelperQueueJava;
		private LinkedList<String> studentAssistantQueueJava;
		
		private LinkedList<String> studentHelperQueueITGK;
		private LinkedList<String> studentAssistantQueueITGK;
		
		public TCPServerUpdated(){
			
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
		
		public boolean hasMatch(LinkedList<Socket> studentQueue, LinkedList<String> studentAssistantQueue, LinkedList<String> studentHelperQueue){ // exist match to connect student to helper?
			return !studentQueue.isEmpty() && !studentAssistantQueue.isEmpty() || !studentHelperQueue.isEmpty();
		}
		
		public String formatIP(String returnIP){ // receives format: dhcp-10-22-11-63.wlan.ntnu.no. Change to: 10.22.11.63
			if(!returnIP.matches("^([0-9]{1,4})\\.([0-9]{1,4})\\.([0-9]{1,4})\\.([0-9]{1,4})$")){
				String[] parts = returnIP.split("-");
				System.out.println(returnIP);
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
				String tag; // to hold received tag
				ServerSocket welcomeSocket; // welcomeSocket accepting connections
				String IP; // hold IP of sender
				try { // to receive connections and sort them into right queue by sent tag
					welcomeSocket = new ServerSocket(6000, 1000);
					while(true) { // receives one connection, and sorts it into the right queue
						try{ // putting this try inside while will retry while-loop if something fucks up, and not crash entire server like if you sorround everything
							Socket connectionSocket;
							connectionSocket = welcomeSocket.accept(); // receive connection
							IP = connectionSocket.getInetAddress().getHostName();
							IP = formatIP(IP);
							BufferedReader recvBuff = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
							tag = recvBuff.readLine(); // reads one line from connection. Sent messages are end with \n
								System.out.println("Received: " + tag);
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
							else if(tag.equals("StudentDeleteJava"))
								deleteStudentFromQueue(IP, studentIPJava, studentQueueJava);
							else if(tag.equals("StudentDeleteITGK"))
								deleteStudentFromQueue(IP, studentIPITGK, studentQueueITGK);
							else if(tag.equals("StudentHelperDeleteJava"))
								deleteStudentHelperFromQueue(IP, studentHelperQueueJava);
							else if(tag.equals("StudentHelperDeleteITGK"))
								deleteStudentHelperFromQueue(IP, studentHelperQueueITGK);
							else if(tag.equals("StudentAssistantDeleteJava"))
								deleteStudentAssistantFromQueue(IP, studentAssistantQueueJava);
							else if(tag.equals("StudentAssistantDeleteITGK"))
								deleteStudentAssistantFromQueue(IP, studentAssistantQueueITGK);
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						if (hasMatch(studentQueueJava, studentAssistantQueueJava, studentHelperQueueJava))
							match(studentQueueJava, studentIPJava, studentAssistantQueueJava, studentHelperQueueJava);
						
						if (hasMatch(studentQueueITGK, studentAssistantQueueITGK, studentHelperQueueITGK))
							match(studentQueueITGK, studentIPITGK, studentAssistantQueueITGK, studentHelperQueueITGK);
					}
				}

				catch(IOException e){
					e.printStackTrace();
				}
		}
		
		
		//sends helperAddress to the next student in line when a match has occurred, such that he/she can set up a connection to the helper
		//also removing both the helper and student from queues, making way for new matches
		public void match(LinkedList<Socket> studentQueue, LinkedList<String> studentIPQueue, LinkedList<String> studentAssistantQueue, LinkedList<String> studentHelperQueue){
			System.out.println("Hurra! A match was found!");
			Socket studentSocket = studentQueue.poll(); //connectionSocket
			studentIPQueue.poll();
			String helperAddress = null;
			//pror
			if (!studentAssistantQueue.isEmpty() && studentHelperQueue.isEmpty() || !studentHelperQueue.isEmpty())
				helperAddress = studentAssistantQueue.poll();
			else if (!studentHelperQueue.isEmpty())
				helperAddress = studentHelperQueue.poll();
			DataOutputStream clientStream;
			try {
				clientStream = new DataOutputStream(studentSocket.getOutputStream());
				clientStream.writeBytes(helperAddress);
				clientStream.close();
				studentSocket.close();
				
			} catch (IOException e) {
				e.printStackTrace();
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
			TCPServerUpdated server = new TCPServerUpdated();
			server.start();
		}
	}	

