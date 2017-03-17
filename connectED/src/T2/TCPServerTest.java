package T2;


import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class TCPServerTest {
	
	private LinkedList<Socket> studentQueue;
	private LinkedList<String> studentIP;
	private LinkedList<String> studentHelperQueue;
	private LinkedList<String> studentAssistantQueue;
	
	public TCPServerTest(){
		studentQueue = new LinkedList<Socket>(); // queue to holds student IP's
		studentIP = new LinkedList<String>(); // queue to holds student IP's
		studentHelperQueue = new LinkedList<String>();  // queue to holds helper IP's
		studentAssistantQueue = new LinkedList<String>();
	}
	
	public boolean hasMatch(){ // exist match to connect student to helper?
		return !this.studentQueue.isEmpty() && !this.studentAssistantQueue.isEmpty() || !this.studentHelperQueue.isEmpty();
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
					try{ // puting this try inside while will retry whileloop if something fucks up, and not crash enitre server like if you sorround everything
						Socket connectionSocket;
						connectionSocket = welcomeSocket.accept(); // receive connection
						IP = connectionSocket.getInetAddress().getHostName();
						IP = formatIP(IP);
						BufferedReader recvBuff = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
						tag = recvBuff.readLine(); // reads one line from connection. Sent messages are end with \n
							System.out.println("Received: " + tag);
						if (tag.equals("Student")){
							studentQueue.addLast(connectionSocket);
							studentIP.addLast(IP);
						}
						else if (tag.equals("StudentHelper")){
							studentHelperQueue.addLast(IP);
							connectionSocket.close(); // dont know if this is smart, or if it should also be saved if they drop out of queue
						}
						else if(tag.equals("StudentAssistant")){
							studentAssistantQueue.addLast(IP);
							connectionSocket.close();
						}
						else if(tag.equals("StudentDelete"))
							deleteStudentFromQueue(IP);
						else if(tag.equals("StudentHelperDelete"))
							deleteStudentHelperFromQueue(IP);
						else if(tag.equals("StudentAssistantDelete"))
							deleteStudentAssistantFromQueue(IP);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (hasMatch()){
						System.out.println("Hurra! a match was made");
						Socket studentSocket = studentQueue.poll(); //connectionSocket
						studentIP.poll();
						String helperAddress = null;
						if (!studentAssistantQueue.isEmpty() && studentHelperQueue.isEmpty() || !studentHelperQueue.isEmpty())
							helperAddress = studentAssistantQueue.poll();
						else if(!studentHelperQueue.isEmpty())
							helperAddress = studentHelperQueue.poll();
						DataOutputStream clientStream = new DataOutputStream(studentSocket.getOutputStream());
						clientStream.writeBytes(helperAddress);
						clientStream.close();
						studentSocket.close();
					}
					
					System.out.println(studentAssistantQueue);
					System.out.println(studentHelperQueue);
					System.out.println(studentIP);
			}

		}
			catch(IOException e){
				e.printStackTrace();
			}
	}
	
       
	// removes the most backmost instatnce in the queue
	public void deleteStudentFromQueue(String IP){
		for(int i = studentIP.size() -1; i > -1; i-- ){
			if(IP.equals(studentIP.get(i))){
				try {
					studentQueue.get(i).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				studentQueue.remove(i);
				studentIP.remove(i);
				break;
			}
		}
	}
	
	// removes the most backmost instatnce in the queue
	public void deleteStudentHelperFromQueue(String IP){
		for(int i = studentHelperQueue.size() -1; i > -1; i-- ){
			if(IP.equals(studentHelperQueue.get(i))){
				studentHelperQueue.remove(i);
				break;
			}
		}
	}
	
	public void deleteStudentAssistantFromQueue(String IP){
		for(int i = studentAssistantQueue.size() -1; i > -1; i-- ){
			if(IP.equals(studentAssistantQueue.get(i))){
				studentAssistantQueue.remove(i);
				break;
			}
		}
	}
     
	
	
	
	public static void main(String[] args) throws Exception{
		TCPServerTest server = new TCPServerTest();
		server.start();
	}
}	
	
	

