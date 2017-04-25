package unitTests;


import java.net.Socket;

import communication.ChatController;
import communication.ChatTabController;
import communication.ReceiveAndSend;
import communication.ServerRequest;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class ChatControllerTest{
	
	public ChatController chatController;
	public ReceiveAndSend receiveAndSend;
	
	
	@Before
	public void setUp(){
		chatController = new ChatController();
	}
	
	
	@Test
	public void testDeniedVoiceCommunucation(){
		chatController.handleDeniedVoiceRequest();
		assertFalse(ChatTabController.isVoiceCommunicating);

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
		this.receiveAndSend = new ReceiveAndSend(socket, chatController);
		chatController.setRecieveAndSendConnection(receiveAndSend);
		assertEquals(receiveAndSend, chatController.getReceiveAndSendConnection());
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
