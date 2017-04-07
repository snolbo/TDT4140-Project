package unitTests;


import java.net.Socket;

import communication.ChatController;
import communication.RecieveAndSend;
import javafx.scene.control.Tab;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import T2.ServerRequest;

public class ChatControllerTest {
	
	public ChatController chatController;
	public RecieveAndSend recieveAndSend;
	
	@Before
	public void setUp(){
		this.chatController = new ChatController();
	}
	
	@Test
	public void testSetHelperHost(){
		chatController.setHelperHost(true);
		assertTrue(chatController.isHelperHost());
	}
	
	@Test
	public void testSetAssistantHost(){
		chatController.setAssistantHost(true);
		assertTrue(chatController.isAssistantHost());
	}
	
	@Test
	public void testSetRecieveAndSendConnection(){
		Socket socket = new Socket();
		this.recieveAndSend = new RecieveAndSend(socket, chatController);
		chatController.setRecieveAndSendConnection(recieveAndSend);
		assertEquals(recieveAndSend, chatController.getReceiveAndSendConnection());
	}
	
	@Test
	public void testSetChatTab(){
		Tab chatTab = new Tab();
		chatController.setChatTab(chatTab);
		assertEquals(chatTab, chatController.getChatTab());
	}
	
	@Test
	public void testOnClosed1(){
		ServerRequest serverRequestSHJ = new ServerRequest("StudentHelperJava");
		serverRequestSHJ.helperRequest();
		chatController.setHelperHost(true);
		chatController.onClosed("StudentHelperJava");
	}
	
	@Test
	public void testOnClosed2(){
		ServerRequest serverRequestSAJ = new ServerRequest("StudentAssistantJava");
		serverRequestSAJ.helperRequest();
		chatController.setAssistantHost(true);
		chatController.onClosed("StudentAssistantJava");
	}

	
}
