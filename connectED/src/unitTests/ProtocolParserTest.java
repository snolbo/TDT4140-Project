package unitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import communication.ChatController;
import communication.ProtocolParser;

public class ProtocolParserTest {
	
	ProtocolParser protocolParser;
	ChatController chatController;
	
	@Before
	public void setUp(){
		chatController = new ChatController();
		protocolParser = new ProtocolParser(chatController);
	}
	
	@Test
	public void testSplitUpString(){
		protocolParser.splitUpString("CHAT-Hei");
		assertEquals("CHAT", protocolParser.getProtocol());
		assertEquals("Hei", protocolParser.getMessage());
	}
}
