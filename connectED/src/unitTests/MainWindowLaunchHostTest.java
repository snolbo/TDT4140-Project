package unitTests;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import communication.ChatTabController;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mainWindow.InteractionTabManagerController;
import mainWindow.MainFrameController;
import mainWindow.MainWindowLaunchHost;


public class MainWindowLaunchHostTest extends ApplicationTest{
	
	Stage stage1; 
	MainFrameController mainFrameController1;
	MainFrameController mainFrameController2;
	MainWindowLaunchHost mainWindowLaunch1;
	Node rootNode;
	InteractionTabManagerController interactionController;
	boolean isFinishedLoading;
	private Stage stage2;
	private MainWindowLaunchHost mainWindowLaunch2;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		interact(() -> {
			stage1 = primaryStage;
			mainWindowLaunch1 = new MainWindowLaunchHost();
			mainWindowLaunch1.start(primaryStage);
			this.mainFrameController1 = mainWindowLaunch1.getMainFrameController();
			
			stage2 = new Stage();;
			mainWindowLaunch2 = new MainWindowLaunchHost();
			mainWindowLaunch2.start(primaryStage);
			mainFrameController2 = mainWindowLaunch2.getMainFrameController();
	    });
	}

	
	@Test
    public void testMainWindow(){
		assertTrue(this.stage1.isShowing());
	}
	
	
	@Test
	public void testLoadNewInteractionArea(){
		interact(() -> {
			this.rootNode = mainFrameController1.getStartingInteractionTab();
			mainFrameController1.loadNewInteractionArea(rootNode, interactionController);
	    });
		assertEquals(rootNode, mainFrameController1.getCurrentInteractionArea());
	}
	
	@Test
	public void testSetURL(){
		interact(() -> {
			this.interactionController = mainFrameController1.getInteractionTabManagerController();
			interactionController.setURL("http://www.lutanho.net/play/tetris.html");
	    });
		assertEquals("http://www.lutanho.net/play/tetris.html", interactionController.getURL());
	}
	
	@Test
	public void testModeAndSubjectSelection(){
		
		interact(()->{
			String mode = "StudentAssistant", subject = "Java";
			mainFrameController1.interactionTabManagerController.modeSelectionController.modeSelection(mode);
			mainFrameController1.interactionTabManagerController.modeSelectionController.subjectSelection(subject);
			mainFrameController1.chatTabController.combineTags();
			assertTrue(mainFrameController1.chatTabController.getMode().equals(mode));
			assertEquals(mainFrameController1.chatTabController.getSubject(), subject);
			assertEquals(mainFrameController1.chatTabController.getTag(), mode+subject);
			
			mode = "Student";
			subject = "ITGK";
			mainFrameController1.interactionTabManagerController.modeSelectionController.modeSelection(mode);
			mainFrameController1.interactionTabManagerController.modeSelectionController.subjectSelection(subject);
			mainFrameController1.chatTabController.combineTags();
			assertTrue(mainFrameController1.chatTabController.getMode().equals(mode));
			assertEquals(mainFrameController1.chatTabController.getSubject(), subject);
			assertEquals(mainFrameController1.chatTabController.getTag(), mode+subject);
			
			mode = "StudentHelper";
			mainFrameController1.interactionTabManagerController.modeSelectionController.modeSelection(mode);
			mainFrameController1.chatTabController.combineTags();
			assertTrue(mainFrameController1.chatTabController.getMode().equals(mode));
			assertEquals(mainFrameController1.chatTabController.getTag(), mode+subject);
			
		});
	}

	@Test
	public void testChatTabLogic(){
		interact(()->{
			String mode = "StudentAssistant", subject = "Java";
			mainFrameController1.interactionTabManagerController.modeSelectionController.modeSelection(mode);
			mainFrameController1.interactionTabManagerController.modeSelectionController.subjectSelection(subject);
			mainFrameController1.chatTabController.combineTags();
			mainFrameController1.chatTabController.newChatTab("java");
			assertEquals(mainFrameController1.chatTabController.getTabCount(),1);
			mainFrameController1.chatTabController.newChatTab("java");
			mainFrameController1.chatTabController.newChatTab("java");
			assertEquals(mainFrameController1.chatTabController.getTabCount(),3);
			mainFrameController1.chatTabController.newChatTab("java");
			assertEquals(mainFrameController1.chatTabController.getTabCount(),3);
			mainFrameController1.chatTabController.onCloseRequest();
			assertEquals(mainFrameController1.chatTabController.getTabCount(),0);
		});
		
	}
	
	
	
	
		
}

	
	
	
	
	

