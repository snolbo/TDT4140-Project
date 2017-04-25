package unitTests;



import communication.ChatTabController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import static org.junit.Assert.*;



import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import mainWindow.MainFrameController;

public class ChatTabControllerTest extends ApplicationTest{
	
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
	public void testSetStudentHelperMode1() throws Exception{
		chatTabController.setStudentHelperMode();
		assertTrue(chatTabController.getConnector().isHelperHost());
		assertFalse(chatTabController.getConnector().isAssistantHost());
		//assertTrue(chatTabController.getStage().isShowing());
		
	}
	
	@Test
	public void testSetStudentHelperMode2() throws Exception{
		chatTabController.getConnector().setClient();
		chatTabController.setStudentHelperMode();
		assertTrue(chatTabController.getConnector().isHelperHost());
		assertFalse(chatTabController.getConnector().isAssistantHost());
	}
	
	@Test
	public void testSetStudentHelperMode3() throws Exception{
		chatTabController.getConnector().setHelperHost();
		chatTabController.setStudentHelperMode();
		assertTrue(chatTabController.getConnector().isHelperHost());
		assertFalse(chatTabController.getConnector().isAssistantHost());
	}
	
	@Test
	public void testSetStudentHelperMode4() throws Exception{
		chatTabController.getConnector().setAssistantHost();
		assertTrue(chatTabController.getConnector().isAssistantHost());
		chatTabController.setStudentHelperMode();
		assertTrue(chatTabController.getConnector().isHelperHost());
	}
	
	@Test
	public void testSetStudentAssistantMode1() throws Exception{
		chatTabController.setAssistantMode();
		assertTrue(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
	}
	
	@Test
	public void testSetStudentAssistantMode2() throws Exception{
		chatTabController.getConnector().setClient();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		chatTabController.setAssistantMode();
		assertFalse(chatTabController.getConnector().isHelperHost());
	}
	
	@Test
	public void testSetStudentAssistantMode3() throws Exception{
		chatTabController.getConnector().setAssistantHost();
		chatTabController.setAssistantMode();
		assertFalse(chatTabController.getConnector().isHelperHost());
		assertTrue(chatTabController.getConnector().isAssistantHost());
	}
	
	@Test
	public void testSetStudentAssistantMode4() throws Exception{
		chatTabController.getConnector().setHelperHost();
		assertTrue(chatTabController.getConnector().isHelperHost());
		chatTabController.setAssistantMode();
		assertTrue(chatTabController.getConnector().isAssistantHost());
	}
	
	
	@Test
	public void testSetStudentMode1() throws Exception{
		chatTabController.setStudentMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
	}
	
	@Test
	public void testSetStudentMode2() throws Exception{
		chatTabController.getConnector().setAssistantHost();
		assertTrue(chatTabController.getConnector().isAssistantHost());
		chatTabController.setStudentMode();
		assertFalse(chatTabController.getConnector().isHelperHost());
	}
	
	@Test
	public void testSetStudentMode3()throws Exception{
		chatTabController.getConnector().setClient();
		chatTabController.setStudentMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
	}
	
	@Test
	public void testSetStudentMode4()throws Exception{
		chatTabController.getConnector().setHelperHost();
		assertTrue(chatTabController.getConnector().isHelperHost());
		chatTabController.setStudentMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
	}
	
	@Test
	public void testSubject(){
		chatTabController.setSubject("java");
		assertEquals(chatTabController.getSubject(), "java");
	}
	
	@Test
	public void testSetTag(){
		chatTabController.setTag("StudentHelperITGK");
		assertEquals("StudentHelperITGK", chatTabController.getTag());
	}
	@Test
	public void testMode(){
		chatTabController.setMode("assistant");
		assertEquals(chatTabController.getMode(),"assistant");
	}
	
	@Test
	public void testModeAndSubject(){
		chatTabController.setMode("assistant");
		assertEquals(chatTabController.getMode(),"assistant");
		assertFalse(chatTabController.combineTags());
		chatTabController.setSubject("java");
		assertEquals(chatTabController.getSubject(),"java");
		assertTrue(chatTabController.modeAndSubjectIsSet());
		assertTrue(chatTabController.combineTags());
		assertEquals(chatTabController.getTag(), "assistantjava");
	}
	
	@Test
	public void testTabLogic(){
		
	}
	
	

	@Override
	public void start(Stage stage) throws Exception {
		
		
	}

	
	
}
