package unitTests;

import java.net.Socket;




import java.util.LinkedList;

import T2.ServerRequest;
import T2.TCPServer;
import unitTests.TCPServerTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TCPServerTest{
	
	public TCPServer tcpserver;
	
	@Before
	public void setUp(){
		tcpserver = new TCPServer();
	}
		
	
	@Test
	public void testTrueHasMatch(){
		try{
		testAddItemsToQueueJava();
		assertTrue(tcpserver.hasMatch(tcpserver.getstudentQueueJava(), tcpserver.getstudentAssistantQueueJava(), tcpserver.getstudentHelperQueueJava()));
		testAddItemsToQueueITGK();
		assertTrue(tcpserver.hasMatch(tcpserver.getstudentQueueITGK(), tcpserver.getstudentAssistantQueueITGK(), tcpserver.getstudentAssistantQueueITGK()));
		}
		catch(Exception e){
			fail("One of the Queues are empty");
		}
	}
	
	public void testAddItemsToQueueJava(){
		Socket testsocket = new Socket();
		tcpserver.getstudentQueueJava().add(testsocket);
		tcpserver.getstudentAssistantQueueJava().add("10.22.16.17");
		tcpserver.getstudentHelperQueueJava().add("10.23.45.65");	
	}
	
	public void testAddItemsToQueueITGK(){
		Socket testsocket = new Socket();
		tcpserver.getstudentQueueITGK().add(testsocket);
		tcpserver.getstudentAssistantQueueITGK().add("10.23.43.55");
		tcpserver.getstudentHelperQueueITGK().add("10.44.66.88");	
	}

	public void testDeleteItemsFromQueueITGK(){
		tcpserver.getstudentQueueITGK().poll();
		tcpserver.getstudentAssistantQueueITGK().poll();
		tcpserver.getstudentHelperQueueITGK().poll();
		
	}
	
	public void testDeleteItemsFromQueueJava(){
		tcpserver.getstudentQueueJava().poll();
		tcpserver.getstudentAssistantQueueJava().poll();
		tcpserver.getstudentHelperQueueJava().poll();
		
	}
	
	@Test
	public void testFalseHasMatch(){
		LinkedList<Socket> testsocket = new LinkedList<Socket>();
		try{
		testDeleteItemsFromQueueITGK();
		assertFalse(tcpserver.hasMatch(testsocket, tcpserver.getstudentAssistantQueueITGK(), tcpserver.getstudentHelperQueueITGK()));
		testDeleteItemsFromQueueJava();
		assertFalse(tcpserver.hasMatch(testsocket, tcpserver.getstudentAssistantQueueJava(), tcpserver.getstudentHelperQueueJava()));
		}
		catch (Exception e){
		}
	}
	
	
	@Test
	public void testFormatIP(){
		String testIP = "localhost";
		String returnIP = tcpserver.formatIP(testIP);
		assertEquals(returnIP,testIP);
		String testIP2 = "dhcp-10-22-11-63.wlan.ntnu.no";
		String returnIP2 = tcpserver.formatIP(testIP2);
		assertEquals(returnIP2,"10.22.11.63");
		String testIP3 = "10.23.54.65";
		String returnIP3 = tcpserver.formatIP(testIP3);
		assertEquals(testIP3,returnIP3);
	}

	
	@Test
	public void testDelegateTags(){
		String tag = "StudentJava";
		String IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		Socket connectionSocket = new Socket();
		String newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertEquals("10.22.11.63", tcpserver.getstudentIPJava().getLast());
		assertEquals(connectionSocket, tcpserver.getstudentQueueJava().getLast());
		
		tag = "StudentITGK";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertEquals("10.22.11.63", tcpserver.getstudentIPITGK().getLast());
		assertEquals(connectionSocket, tcpserver.getstudentQueueITGK().getLast());
		
		tag = "StudentAssistantJava";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertEquals("10.22.11.63", tcpserver.getstudentAssistantQueueJava().getLast());
		assertTrue(connectionSocket.isClosed());
		
		tag = "StudentAssistantITGK";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertEquals("10.22.11.63", tcpserver.getstudentAssistantQueueITGK().getLast());
		assertTrue(connectionSocket.isClosed());
		
		tag = "StudentHelperJava";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertEquals("10.22.11.63", tcpserver.getstudentHelperQueueJava().getLast());
		assertTrue(connectionSocket.isClosed());
		
		tag = "StudentHelperITGK";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertEquals("10.22.11.63", tcpserver.getstudentHelperQueueITGK().getLast());
		assertTrue(connectionSocket.isClosed());
		
		
		tag = "StudentJavaDelete";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertTrue(tcpserver.getstudentIPJava().isEmpty());
		assertTrue(tcpserver.getstudentQueueJava().isEmpty());
		
		tag = "StudentITGKDelete";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertTrue(tcpserver.getstudentIPITGK().isEmpty());
		assertTrue(tcpserver.getstudentQueueITGK().isEmpty());
		
		tag = "StudentHelperJavaDelete";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertTrue(tcpserver.getstudentHelperQueueJava().isEmpty());
		
		tag = "StudentHelperITGKDelete";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertTrue(tcpserver.getstudentHelperQueueITGK().isEmpty());
		
		tag = "StudentAssistantJavaDelete";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertTrue(tcpserver.getstudentAssistantQueueJava().isEmpty());
		
		tag = "StudentAssistantITGKDelete";
		IP = "dhcp-10-22-11-63.wlan.ntnu.no";
		connectionSocket = new Socket();
		newIP = tcpserver.formatIP(IP);
		tcpserver.delegateTag(tag, newIP, connectionSocket);
		assertTrue(tcpserver.getstudentAssistantQueueITGK().isEmpty());
	}
	
	
	
	@Test
	public void testMatch(){
		LinkedList<Socket> studentSocket = new LinkedList<Socket>();
		Socket socket = new Socket();
		studentSocket.add(socket);
		tcpserver.getstudentAssistantQueueJava().add("10.22.12.11");
		tcpserver.getstudentIPJava().add("10.23.42.11");
		tcpserver.match(studentSocket, tcpserver.getstudentIPJava(), tcpserver.getstudentAssistantQueueJava(), tcpserver.getstudentHelperQueueJava());
		assertEquals("10.22.12.11",tcpserver.getHelperAddress());
		
		
		LinkedList<Socket> studentSocket2 = new LinkedList<Socket>();
		Socket socket2 = new Socket();
		studentSocket2.add(socket2);
		tcpserver.getstudentHelperQueueITGK().add("10.34.53.21");
		tcpserver.match(studentSocket2, tcpserver.getstudentIPITGK(), tcpserver.getstudentAssistantQueueITGK(), tcpserver.getstudentHelperQueueITGK());
		assertEquals("10.34.53.21",tcpserver.getHelperAddress());
		
		
	}

	
}
