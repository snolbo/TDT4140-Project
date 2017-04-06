package ChatManagerTestFX;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;

import communication.ChatTabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class ChatManagerTest extends GuiTest{
	
	ChatTabController chatTabController;
	Button sAButton;
	Button sHButton;
	Button sButton;
	FXMLLoader loader;
	
	
	@Override
	protected Parent getRootNode() {
		try{
			loader  = new FXMLLoader(ChatTabController.class.getResource("ChatManager.fxml"));
			return loader.load();
		}catch (IOException e) {
            System.err.println(e);
		}
		return null;
	}
	
	@Before 
	public void setUp() {
		sAButton = find("#studentAssistantBtn");
		sHButton = find("#studentHelperBtn");
		sButton = find("#studentBtn");
		this.chatTabController = loader.getController();
    }
	@Test
	public void testStudentAssistantButton(){
		clickOn(sAButton);
		assertTrue(this.chatTabController.getConnector().isAssistantHost());
	}
	
	@Test
	public void testStudentHelperButton(){
		clickOn(sHButton);
		assertTrue(this.chatTabController.getConnector().isHelperHost());
	}
	
	@Test
	public void testStudentButton(){
		clickOn(sButton);
		assertFalse(this.chatTabController.getConnector().isAssistantHost());
		assertFalse(this.chatTabController.getConnector().isHelperHost());
	}
	
	
}
