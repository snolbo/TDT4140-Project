package unitTests;

import communication.ChatTabController;
import communication.PopUpSubjectController;
import junit.framework.*;

//Error: "Could not initialize class javafx.scene.control.Button", fail in both initializebuttons-methods
//TODO: find out the general form of junit tests

public class PopUpSubjectControllerTest extends TestCase {
	
	public ChatTabController chatTabController;
	public PopUpSubjectController popUpSubjectController;
	
	public void setUp() throws Exception{
		this.popUpSubjectController = new PopUpSubjectController();
		this.chatTabController = new ChatTabController();
		chatTabController.getConnector().setAssistantHost();
		popUpSubjectController.passChatTabController(chatTabController);
	}
	
	public void testPassChatTabController(){
		popUpSubjectController.passChatTabController(chatTabController);
		assertEquals(chatTabController, popUpSubjectController.getChatTabController());
	}
	
	/*public void testInitializeJavaButton() throws Exception{
		setUp();
		chatTabController.initializePopUpSubject();
		popUpSubjectController.initializeJavaButton();
		String tag = chatTabController.getTag();
		assertEquals("StudentAssistantJava", tag);
		assertFalse(chatTabController.getStage().isShowing()); 
		
	}
	
	public void testInitializeITGKButton() throws Exception{
		setUp();
		chatTabController.initializePopUpSubject();
		PopUpSubjectControllerTest testITGK = new PopUpSubjectControllerTest();
		popUpSubjectController.initializeITGKButton();
		String tag = chatTabController.getTag();
		assertEquals("StudentAssistantITGK", tag);
		assertFalse(chatTabController.getStage().isShowing());
	}
	*/
	
	
}
