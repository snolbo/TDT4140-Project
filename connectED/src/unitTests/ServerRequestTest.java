package unitTests;


import java.io.IOException;

import T2.ServerRequest;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class ServerRequestTest{
	
	public ServerRequest serverRequestStudent;
	public ServerRequest serverRequestAssistant;
	public ServerRequest serverRequestAssistant2;
	public ServerRequest serverRequestDelete;
	
	@Before
	public void setUp(){
		serverRequestStudent = new ServerRequest("StudentJava");
		serverRequestAssistant = new ServerRequest("StudentAssistantJava");
		serverRequestAssistant2 = new ServerRequest("StudentAssistantJava");
		serverRequestDelete = new ServerRequest("StudentAssistantJavaDelete");
	}
	
	@Test
	public void testStudentRequest() throws IOException{
		serverRequestAssistant.helperRequest();
		String returnIP = serverRequestStudent.studentRequest();
		assertEquals("10.22.43.121", returnIP);
	}
	
	@Test
	public void  testRemoveAdressFromQueue(){
		serverRequestAssistant2.studentRequest();
		serverRequestDelete.removeAdressFromQueue();
	}
	
	
	
	

}
