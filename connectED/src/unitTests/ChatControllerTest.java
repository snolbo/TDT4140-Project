package unitTests;


import java.io.IOException;
import java.net.Socket;

import communication.ChatController;
import communication.ChatTabController;
import communication.ReceiveAndSend;
import communication.ServerRequest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import mainWindow.MainWindowLaunchHost;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class ChatControllerTest extends ApplicationTest{
	
	public ChatController chatController;
	public ReceiveAndSend receiveAndSend;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		interact(() -> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/communication/ChatTab.fxml")); //don't neccesarily need this. Can load directly instead of creating loader
			Parent root;
			try {
				root = loader.load();
				chatController = loader.getController();
				Scene window = new Scene(root,700,400);
				primaryStage.setScene(window);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
	    });
		
	}

	
	
	@Before
	public void setUp(){
		
	}
	
	
	
	@Test
	public void testViewMessage(){
		interact(()->{
			String message = "This is a test message";
			chatController.viewMessage(message, true);
			assertEquals(message, chatController.getChatWindow().getItems().get(0).getText());
			chatController.viewMessage(message, true);
			System.out.println(chatController.getChatWindow().getItems().toString());
			assertEquals(message, chatController.getChatWindow().getItems().get(1).getText());
		});
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
		interact(()->{
			ServerRequest serverRequestSHJ = new ServerRequest("StudentHelperJava");
			serverRequestSHJ.helperRequest();
			chatController.setHelperHost(true);
			chatController.initializeInteractionTab("java");
			chatController.onClosed("StudentHelperJava");		
		});
	}

	@Test
	public void testOnClosed2(){
		interact(()->{
		ServerRequest serverRequestSAJ = new ServerRequest("StudentAssistantJava");
		serverRequestSAJ.helperRequest();
		chatController.setAssistantHost(true);
		chatController.initializeInteractionTab("java");
		chatController.onClosed("StudentAssistantJava");
		});
	}








	
}
