package unitTests;

import junit.framework.*;

import java.io.IOException;
import java.net.ServerSocket;

import T2.ServerRequest;
import communication.ChatTabController;
import communication.Connector;

//Error: testSetAssistantHost and testCloseWelcomeSocket fails
//TODO: find out the general form of junit tests, finish testRun and testSendHelperRequest 

public class ConnectorTest extends TestCase{
	
	
	public ChatTabController chatTabController;
	public Connector connector;
	
	
	public void setUp(){
		this.chatTabController = new ChatTabController();
		this.connector = new Connector(chatTabController);
		
	}
	
	
	public void testSetHelperHost() throws IOException{
		connector.setHelperHost();
		assertTrue(connector.isHelperHost());
		assertFalse(connector.isAssistantHost());
		assertNotNull(connector.getWelcomeSocket());
		connector.getWelcomeSocket().close();
	}
	
	
	public void testSetAssistantHost() throws IOException{
		connector.setAssistantHost();
		assertFalse(connector.isHelperHost());
		assertTrue(connector.isAssistantHost());
		connector.getWelcomeSocket().close();
	}
	
	public void testSetClient(){
		connector.setClient();
		assertFalse(connector.isHelperHost());
		assertFalse(connector.isAssistantHost());
		
	}
	
	public void testCloseWelcomeSocket() {
		connector.setHelperHost();
		connector.closeWelcomeSocket();
		assertTrue(connector.getWelcomeSocket().isClosed());
	}
	
	public void testRun() throws IOException{
		connector.setHelperHost();
		connector.run();
		assertTrue(connector.getSocket().isConnected());
		connector.getWelcomeSocket().close();
		//TODO: find out how to test and start chat sessions (last line in run())
		
		ServerRequest serverRequest = new ServerRequest("StudentAssistantJava");
		serverRequest.helperRequest();
		connector.setClient();
		chatTabController.setTag("StudentJava");
		connector.run();
		assertEquals("10.22.43.121", connector.getHelperIP());
		assertTrue(connector.getSocket().isConnected());
		connector.getWelcomeSocket().close();
	}
	
	
	
	
}
