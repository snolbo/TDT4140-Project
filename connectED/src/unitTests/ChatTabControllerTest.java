package unitTests;



import communication.ChatTabController;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import mainWindow.MainFrameController;

public class ChatTabControllerTest{
	
	public ChatTabController chatTabController;
	
	@Before
	public void setUp(){
		this.chatTabController = new ChatTabController();
	}
	
	@Test
	public void testDecrementPotentialConnections(){
		ChatTabController.getPotentialConnections();
		ChatTabController.decrementPotentialConnections();
		assertEquals(-1, ChatTabController.getPotentialConnections());
	}
	
	@Test
	public void testGetPotentialConnections(){
		int pc = ChatTabController.getPotentialConnections();
		assertEquals(0, pc);
	}
	
	@Test
	public void testPassMainFrameController(){
		MainFrameController mainFrameController = new MainFrameController();
		chatTabController.passMainFrameController(mainFrameController);
		assertEquals(mainFrameController, chatTabController.getMainFrameController());
	}
	
	@Test
	public void testSetStudentHelperMode() throws Exception{
		chatTabController.setStudentHelperMode();
		assertTrue(chatTabController.getConnector().isHelperHost());
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertTrue(chatTabController.getStage().isShowing());
		chatTabController.getConnector().getWelcomeSocket().close();
		
		chatTabController.getConnector().setClient();
		chatTabController.setStudentHelperMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
		chatTabController.getConnector().getWelcomeSocket().close();
		
		chatTabController.getConnector().setHelperHost();
		chatTabController.getConnector().getWelcomeSocket().close();
		chatTabController.setStudentHelperMode();
		assertTrue(chatTabController.getConnector().isHelperHost());
		assertFalse(chatTabController.getConnector().isAssistantHost());
		chatTabController.getConnector().getWelcomeSocket().close();
		
	}
	
	@Test
	public void testSetStudentAssistantMode() throws Exception{
		chatTabController.setAssistantMode();
		assertTrue(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
		assertTrue(chatTabController.getStage().isShowing());
		chatTabController.getConnector().getWelcomeSocket().close();
		
		chatTabController.getConnector().setClient();
		chatTabController.setAssistantMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
		
		chatTabController.getConnector().setAssistantHost();
		chatTabController.getConnector().getWelcomeSocket().close();
		chatTabController.setAssistantMode();
		assertFalse(chatTabController.getConnector().isHelperHost());
		assertTrue(chatTabController.getConnector().isAssistantHost());
	}
	
	@Test
	public void testSetStudentMode() throws Exception{
		chatTabController.setStudentMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
		assertTrue(chatTabController.getStage().isShowing());
		
		chatTabController.getConnector().setAssistantHost();
		chatTabController.getConnector().getWelcomeSocket().close();
		chatTabController.setStudentMode();
		assertTrue(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
		
		chatTabController.getConnector().setClient();
		chatTabController.setStudentMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
	}
	
	@Test
	public void testInitializePopUpSubject() throws Exception{
		chatTabController.initializePopUpSubject();
		assertTrue(chatTabController.getStage().isShowing());
	}
	
	@Test
	public void testClosePopUp() throws Exception{
		chatTabController.initializePopUpSubject();
		chatTabController.closePopUp();
		assertFalse(chatTabController.getStage().isShowing());
	}
	
	@Test
	public void testMergeTags(){
		chatTabController.getConnector().setAssistantHost();
		chatTabController.mergeTags("Java");
		assertEquals("StudentAssistantJava", chatTabController.getTag());
		chatTabController.getConnector().setHelperHost();
		chatTabController.mergeTags("Java");
		assertEquals("StudentHelperJava", chatTabController.getTag());
		chatTabController.getConnector().setClient();;
		chatTabController.mergeTags("Java");
		assertEquals("StudentJava", chatTabController.getTag());
	}
	
	@Test
	public void testSetTag(){
		chatTabController.setTag("StudentHelperITGK");
		assertEquals("StudentHelperITGK", chatTabController.getTag());
	}
	
	
}
