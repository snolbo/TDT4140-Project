package unitTests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import communication.ChatController;
import communication.RecieveAndSend;
import junit.framework.*;

//Error: testSetupStream, testCloseConnection, testSendChatMessage fails (connection refused)
//TODO: find out the general form of junit tests, finish tests for the methods that are commented
//didn't include tests for viewMessage, ableToType or sendCodeURL as these only take usage of methods whose been taken into account earlier
public class RecieveAndSendTest extends TestCase {
	
	public RecieveAndSend receiveAndSend;
	public Socket socket;
	public ChatController chatController;
	
	public void setUp() throws UnknownHostException, IOException{
		this.socket = new Socket("localhost", 6000);
		this.chatController = new ChatController();
		this.receiveAndSend = new RecieveAndSend(socket, chatController);
	}
	
	public void testRun() throws UnknownHostException, IOException{
		setUp();
		//initialize some objects
		receiveAndSend.run();
		//TODO: find test objects for this method
	}
	
	public void testSetupStream() throws IOException{
		setUp();
		receiveAndSend.setupStreams();
		assertNotNull(receiveAndSend.getInputStream());
		assertNotNull(receiveAndSend.getOutputStream());
		assertTrue(receiveAndSend.getBufferedReader().ready());
		
	}
	
	
	public void testWhileReceiving() throws UnknownHostException, IOException{
		setUp();
		//initialize something
		receiveAndSend.whileReceiving();
		//TODO: find test objects for this method
	}
	
	public void testCloseConnection() throws IOException{
		setUp();
		receiveAndSend.setupStreams();
		receiveAndSend.closeConnection();
		assertTrue(socket.isClosed());
		assertNull(receiveAndSend.getInputStream());
		assertNull(receiveAndSend.getBufferedReader());
		assertNull(receiveAndSend.getOutputStream());
	}
	
	public void testSendChatMessage() throws UnknownHostException, IOException{
		setUp();
		receiveAndSend.setupStreams();
		RecieveAndSend connection = new RecieveAndSend(new Socket(), new ChatController());
		connection.setupStreams();
		receiveAndSend.sendChatMessage("heisann");
		String message = connection.getBufferedReader().readLine();
		assertEquals("heisann", message);
	}
	
	
}
