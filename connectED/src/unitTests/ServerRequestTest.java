package unitTests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import T2.ServerRequest;
import junit.framework.TestCase;

public class ServerRequestTest extends TestCase{
	
	ServerRequest serverRequestStudent = new ServerRequest("StudentJava");
	ServerRequest serverRequestAssistant = new ServerRequest("StudentAssistantJava");
	ServerRequest serverRequestAssistant2 = new ServerRequest("StudentAssistantJava");
	ServerRequest serverRequestDelete = new ServerRequest("StudentAssistantJavaDelete");
	
	
	public void testStudentRequest() throws IOException{
		serverRequestAssistant.helperRequest();
		String returnIP = serverRequestStudent.studentRequest();
		assertEquals("localhost", returnIP);
	}
	
	public void  testRemoveAdressFromQueue(){
		serverRequestAssistant2.studentRequest();
		serverRequestDelete.removeAdressFromQueue();
	}
	
	
	
	

}
