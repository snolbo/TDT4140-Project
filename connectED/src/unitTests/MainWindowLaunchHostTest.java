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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import mainWindow.MainWindowLaunchHost;

public class MainWindowLaunchHostTest extends ApplicationTest{
	
	Stage stage; 
	
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(MainWindowLaunchHost.class.getResource("MainFrame.fxml")); 
		Parent root = loader.load();
		Scene scene = new Scene(root, 800, 600);
		this.stage = stage;
        stage.setScene(scene);
        stage.show();
	}

	@Test
    public void testMainWindow(){
		assertTrue(this.stage.isShowing());
		
	}

}
