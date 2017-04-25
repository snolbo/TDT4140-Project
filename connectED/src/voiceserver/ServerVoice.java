package voiceserver;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * @author snorr
 * Class setting up and handling receiving and playback of voice bytes
 */
public class ServerVoice {

    public int serverPort = 8888;
    private SourceDataLine audioOut;
    private PlayerThread playerThread;
	private DataLine.Info info_out;
	private AudioFormat format;
    
    public ServerVoice() {
    	 format = getAudioformat();
         info_out = new DataLine.Info(SourceDataLine.class, format);
	}
    
    /**
     * @return
     * Returns a boolean value to see if the audioforamt set in this object is supported by the device
     */
    public boolean playingIsSupported(){
    	return AudioSystem.isLineSupported(info_out);
    }
    
    /**
     * @return
     * Sets an audioformat with information on how to play handle playback of audio
     */
    public static AudioFormat getAudioformat(){
        float sampleRate = 8000.0F;
        int sampleSizeInbits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
    }
    
    

    /**
     * Initializes required object for audiocapture and start a thread that reads bytes form a datagram and plays them
     */
    public void initializeAudio(){
        try {
            if(!AudioSystem.isLineSupported(info_out)){
                System.out.println("not suport");
                return;
            }
            audioOut = (SourceDataLine)AudioSystem.getLine(info_out);
            audioOut.open(format);
            audioOut.start();
            playerThread = new PlayerThread();
            playerThread.initializePlayerThread(serverPort, audioOut);
            playerThread.start();
        } catch (LineUnavailableException e) {
        	e.printStackTrace();
        }
    }
    
    
}
