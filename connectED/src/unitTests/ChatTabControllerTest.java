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
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertFalse(chatTabController.getConnector().isHelperHost());
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
		chatTabController.setStudentHelperMode();
		assertFalse(chatTabController.getConnector().isHelperHost());
		assertTrue(chatTabController.getConnector().isAssistantHost());
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
		chatTabController.setAssistantMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
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
		chatTabController.setAssistantMode();
		assertTrue(chatTabController.getConnector().isHelperHost());
		assertFalse(chatTabController.getConnector().isAssistantHost());
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
		chatTabController.setStudentMode();
		assertTrue(chatTabController.getConnector().isAssistantHost());
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
		chatTabController.setStudentMode();
		assertFalse(chatTabController.getConnector().isAssistantHost());
		assertTrue(chatTabController.getConnector().isHelperHost());
	}
	
	
	@Test
	public void testMergeTags(){
		chatTabController.mergeTags("Java");
		chatTabController.getConnector().setAssistantHost();
		chatTabController.mergeTags("Java");
		assertEquals("StudentAssistantJava", chatTabController.getTag());
		chatTabController.getConnector().setHelperHost();
		chatTabController.mergeTags("Java");
		assertEquals("StudentHelperJava", chatTabController.getTag());
		chatTabController.getConnector().setClient();
		chatTabController.mergeTags("Java");
		assertEquals("StudentJava", chatTabController.getTag());
	}
	
	@Test
	public void testSetTag(){
		chatTabController.setTag("StudentHelperITGK");
		assertEquals("StudentHelperITGK", chatTabController.getTag());
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		
	}

	
	
}
