package unitTests;

import javafx.application.Application;
import javafx.stage.Stage;
import junit.framework.*;
import mainWindow.MainWindowLaunchHost;

public class MainWindowLaunchHostTest extends TestCase {
	
	MainWindowLaunchHost mainWindow = new MainWindowLaunchHost();
	
	public void testStart(){
		Stage primaryStage = new Stage();
		mainWindow.start(primaryStage);
		assertTrue(primaryStage.isShowing());
		assertEquals("Main Window", primaryStage.getTitle());
	}
	
	public static void main(String[] args) {
		
	}

}
