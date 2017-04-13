package voiceserver;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class ServerVoice {

    public int serverPort = 8888;
    private SourceDataLine audioOut;
    private PlayerThread playerThread;
    
    public static AudioFormat getAudioformat(){
        float sampleRate = 8000.0F;
        int sampleSizeInbits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
    }
    
    


    public void initializeAudio(){
        try {
            AudioFormat format = getAudioformat();
            DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format);
            if(!AudioSystem.isLineSupported(info_out)){
                System.out.println("not suport");
                System.exit(0);
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
