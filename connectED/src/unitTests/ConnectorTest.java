package unitTests;

import junit.framework.*;

import java.net.ServerSocket;

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
	
	
	public void testSetHelperHost(){
		setUp();
		connector.setHelperHost();
		assertTrue(connector.isHelperHost());
		assertFalse(connector.isAssistantHost());
		assertNotNull(connector.getWelcomeSocket());
	}
	
	public void testSetAssistantHost(){
		setUp();
		connector.setAssistantHost();
		assertFalse(connector.isHelperHost());
		assertTrue(connector.isAssistantHost());
		assertNotNull(connector.getWelcomeSocket());
	}
	
	
	public void testSetCLient(){
		setUp();
		connector.setClient();
		assertFalse(connector.isHelperHost());
		assertFalse(connector.isAssistantHost());
		
	}
	
	public void testCloseWelcomeSocket(){
		setUp();
		connector.setHelperHost();
		connector.closeWelcomeSocket();
		assertTrue(connector.getWelcomeSocket().isClosed());
	}
	
	public void testSendHelperRequest(){
		connector.sendHelperRequest("StudentAssistantJava");
		//TODO: find test objects for this method
	}
	
	public void testRun(){
		setUp();
		//sette host eller client
		connector.run();
		//TODO: find test objects for this method
	}
	
}
