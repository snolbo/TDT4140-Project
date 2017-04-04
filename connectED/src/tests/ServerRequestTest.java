package tests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import T2.ServerRequest;
import junit.framework.TestCase;

public class ServerRequestTest extends TestCase{
	
	ServerRequest serverRequestStudent = new ServerRequest("Student");
	
	
	public void testStudentRequest() throws IOException{
		String returnTag = serverRequestStudent.studentRequest();
		assertEquals("Student",returnTag);
	}
	
	
	
	

}
