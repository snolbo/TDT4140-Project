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
	
	
}
