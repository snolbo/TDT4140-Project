package unitTests;

import communication.ChatTabController;
import communication.PopUpSubjectController;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PopUpSubjectControllerTest{
	
	public ChatTabController chatTabController;
	public PopUpSubjectController popUpSubjectController;
	
	@Before
	public void setUp() throws Exception{
		this.popUpSubjectController = new PopUpSubjectController();
		this.chatTabController = new ChatTabController();
		chatTabController.getConnector().setAssistantHost();
		popUpSubjectController.passChatTabController(chatTabController);
	}
	
	@Test
	public void testPassChatTabController(){
		popUpSubjectController.passChatTabController(chatTabController);
		assertEquals(chatTabController, popUpSubjectController.getChatTabController());
	}
	
	@Test
	public void testInitializeJavaButton() throws Exception{
		setUp();
		chatTabController.initializePopUpSubject();
		popUpSubjectController.initializeJavaButton();
		String tag = chatTabController.getTag();
		assertEquals("StudentAssistantJava", tag);
		assertFalse(chatTabController.getStage().isShowing()); 
		
	}
	
	@Test
	public void testInitializeITGKButton() throws Exception{
		setUp();
		chatTabController.initializePopUpSubject();
		PopUpSubjectControllerTest testITGK = new PopUpSubjectControllerTest();
		popUpSubjectController.initializeITGKButton();
		String tag = chatTabController.getTag();
		assertEquals("StudentAssistantITGK", tag);
		assertFalse(chatTabController.getStage().isShowing());
	}
	
	
}
