package T2;
import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class TCPServer {
	
	private LinkedList<Socket> studentQueue;
	private LinkedList<String> studentIP;
	private LinkedList<String> helperQueue;
	
	public TCPServer(){
		studentQueue = new LinkedList<Socket>(); // queue to holds student IP's
		studentIP = new LinkedList<String>(); // queue to holds student IP's
		helperQueue = new LinkedList<String>();  // queue to holds helper IP's
	}
	
	public boolean hasMatch(){ // exist match to connect student to helper?
		return !this.studentQueue.isEmpty() && !this.helperQueue.isEmpty();
	}
	
	public String formatIP(String returnIP){ // receives format: dhcp-10-22-11-63.wlan.ntnu.no. Change to: 10.22.11.63
		if(!returnIP.matches("^([0-9]{1,4})\\.([0-9]{1,4})\\.([0-9]{1,4})\\.([0-9]{1,4})$") && !returnIP.matches("localhost")){
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
						else if (tag.equals("Helper")){
							helperQueue.addLast(IP);
							connectionSocket.close(); // dont know if this is smart, or if it should also be saved if they drop out of queue
						}
						else if(tag.equals("StudentDelete"))
							deleteStudentFromQueue(IP);
						else if(tag.equals("HelperDelete"))
							deleteHelperFromQueue(IP);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (hasMatch()){
						System.out.println("Hurra! a match was made");
						Socket studentSocket = studentQueue.poll(); //connectionSocket
						studentIP.poll();
						String helperAddress = helperQueue.poll();
						DataOutputStream clientStream = new DataOutputStream(studentSocket.getOutputStream());
						clientStream.writeBytes(helperAddress);
						clientStream.close();
						studentSocket.close();
					}
					System.out.println("helperIP:");
					System.out.println(helperQueue);
					System.out.println("studentIP:");
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
	public void deleteHelperFromQueue(String IP){
		for(int i = helperQueue.size() -1; i > -1; i-- ){
			if(IP.equals(helperQueue.get(i))){
				helperQueue.remove(i);
				break;
			}
		}
	}
     
	
	
	
	public static void main(String[] args) throws Exception{
		TCPServer server = new TCPServer();
		server.start();
	}
}	
	
	
