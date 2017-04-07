package unitTests;

import static org.junit.Assert.*;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mainWindow.InteractionTabManagerController;
import mainWindow.MainFrameController;
import mainWindow.MainWindowLaunchHost;

public class MainWindowLaunchHostTest extends ApplicationTest{
	
	Stage primaryStage; 
	MainFrameController mainController;
	MainWindowLaunchHost mainWindowLaunch;
	Node rootNode;
	InteractionTabManagerController interactionController;
	boolean isFinishedLoading;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		interact(() -> {
			this.primaryStage = primaryStage;
			this.mainWindowLaunch = new MainWindowLaunchHost();
			mainWindowLaunch.start(primaryStage);
			this.mainController = mainWindowLaunch.getMainFrameController();
	    });
	}

	@Test
    public void testMainWindow(){
		assertTrue(this.primaryStage.isShowing());
		
	}
	
	@Test
	public void testLoadNewInteractionArea(){
		interact(() -> {
			this.rootNode = mainController.getStartingInteractionTab();
			mainController.loadNewInteractionArea(rootNode);
	    });
		
		assertEquals(rootNode, mainController.getCurrentInteractionArea());
	}
	
	@Test
	public void testSetURL(){
		interact(() -> {
			this.interactionController = mainController.getInteractionTabManagerController();
			interactionController.setURL("http://www.lutanho.net/play/tetris.html");
	    });
		assertEquals("http://www.lutanho.net/play/tetris.html", interactionController.getURL());
	}
	
	@Test
	public void testSetDefaultURL(){
		interact(() -> {
			this.interactionController = mainController.getInteractionTabManagerController();
			interactionController.setDefaultURL();
	    });
		assertEquals("http://www.lutanho.net/play/tetris.html", interactionController.getURL());
	}
	
	@Test
	public void testIsFinishedLoading(){
		interact(() -> {
			this.interactionController = mainController.getInteractionTabManagerController();
			isFinishedLoading = interactionController.isFinishedLoading();
	    });
		assertTrue(this.isFinishedLoading);
	}
	
	@Test
	public void testReloadCodeEditor(){
		interact(() -> {
			this.interactionController = mainController.getInteractionTabManagerController();
			interactionController.reloadCodeEditor();
	    });
		assertEquals("http://www.lutanho.net/play/tetris.html", interactionController.getURL());
	}
	
}
