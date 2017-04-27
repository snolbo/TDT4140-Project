package unitTests;


import java.net.InetAddress;
import java.net.UnknownHostException;


import voiceclient.ClientVoice;
import voiceserver.ServerVoice;



import org.junit.Before;
import org.junit.Test;


public class VoiceTest{
	
	public ServerVoice sv;
	public ClientVoice cv;
	
	
	@Before
	public void setUp(){
		sv = new ServerVoice();
		cv = new ClientVoice();
	}
	
	
	@Test
	public void testGetAudioFormat(){
		ServerVoice.getAudioformat();
		ClientVoice.getAudioformat();
	}
	
	public void testInitialize(){
		sv.initializeAudio();
		try {
			cv.initializeAudio(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
}