package PopUpSubjectTestFX;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import communication.ChatTabController;
import communication.PopUpSubjectController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class TestPopUpSubject extends GuiTest{
	

	Button javaButton;
	Button itgkButton;
	FXMLLoader loader;
	ChatTabController chatTabController;
	PopUpSubjectController popUpSubjectController;
	
	@Before 
	public void setUp() throws Exception{
		javaButton = find("#javaBtn");
		itgkButton = find("#itgkBtn");
		chatTabController = new ChatTabController();
		chatTabController.setStudentMode();
		this.popUpSubjectController = loader.getController();
		popUpSubjectController.passChatTabController(chatTabController);
	}
	@Override
	protected Parent getRootNode() {
		try{
			loader  = new FXMLLoader(ChatTabController.class.getResource("PopUpSubject.fxml"));
			this.chatTabController = loader.getController();
			return loader.load();
            
		}catch (IOException e) {
            System.err.println(e);
        }
		return null;
	}
	
	@Test
	public void testJavaButton(){
		clickOn(javaButton);
		assertEquals("StudentJava", this.popUpSubjectController.getChatTabController().getTag());
	}
	
	@Test
	public void testITGKButton(){
		clickOn(itgkButton);
		assertEquals("StudentITGK", this.popUpSubjectController.getChatTabController().getTag());
	}
}
