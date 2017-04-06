package unitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import T2.ServerRequest;
import communication.ChatTabController;
import communication.Connector;


public class ConnectorTest{
	
	
	public ChatTabController chatTabController;
	public Connector connector;
	
	@Before
	public void setUp(){
		this.chatTabController = new ChatTabController();
		this.connector = new Connector(chatTabController);
		
	}
	
	@Test
	public void testSetHelperHost() throws IOException{
		connector.setHelperHost();
		assertTrue(connector.isHelperHost());
		assertFalse(connector.isAssistantHost());
		assertNotNull(connector.getWelcomeSocket());
		connector.getWelcomeSocket().close();
	}
	
	@Test
	public void testSetAssistantHost() throws IOException{
		connector.setAssistantHost();
		assertFalse(connector.isHelperHost());
		assertTrue(connector.isAssistantHost());
		connector.getWelcomeSocket().close();
	}
	
	@Test
	public void testSetClient(){
		connector.setClient();
		assertFalse(connector.isHelperHost());
		assertFalse(connector.isAssistantHost());
		
	}
	
	@Test
	public void testCloseWelcomeSocket() {
		connector.setHelperHost();
		connector.closeWelcomeSocket();
		assertTrue(connector.getWelcomeSocket().isClosed());
	}
	
	@Test
	public void testConnect1() throws IOException{
		connector.setHelperHost();
		connector.connect();
		assertTrue(connector.getSocket().isConnected());
		connector.getWelcomeSocket().close();
		//må kjøre TestClient
	}
	
	@Test
	public void testConnect2() throws IOException{
		connector.setAssistantHost();
		connector.connect();
		assertTrue(connector.getSocket().isConnected());
		connector.getWelcomeSocket().close();
	}
	
	@Test
	public void testConnect3() throws IOException{
		//må kjøre TCPServer (ikke lokalt)!!
		ServerRequest serverRequest = new ServerRequest("StudentAssistantJava");
		serverRequest.helperRequest();
		connector.setClient();
		chatTabController.setTag("StudentJava");
		connector.connect();
		assertEquals("10.22.43.121", connector.getHelperIP());
		//assertTrue(connector.getSocket().isConnected());
		//connector.getWelcomeSocket().close();
	}
	
	
	
}
