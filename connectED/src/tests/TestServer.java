package tests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
	
	public void connect() throws IOException{
		ServerSocket welcomeSocket = new ServerSocket(9999,1000);
		while(true){
		Socket connectionSocket = welcomeSocket.accept();
		BufferedReader recvBuff = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		String tag = recvBuff.readLine(); 
		DataOutputStream clientStream = new DataOutputStream(connectionSocket.getOutputStream());
		clientStream.writeBytes(tag);
		connectionSocket.close();
		clientStream.close();
		}
	}
	
	public static void main(String[] args) throws IOException {
		TestServer testserver = new TestServer();
		testserver.connect();
	}

}
