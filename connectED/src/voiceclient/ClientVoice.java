/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voiceclient;

import java.net.InetAddress;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class ClientVoice {

    public int serverPort = 8888;
    TargetDataLine audioIn;
	private AudioFormat format;
	private DataLine.Info info;

    public ClientVoice() {
    	 format = getAudioformat();
         info = new DataLine.Info(TargetDataLine.class, format);
	}

    public boolean recordingIsSupported(){
    	return !AudioSystem.isLineSupported(info);
    }
    
    public static AudioFormat getAudioformat(){
        float sampleRate = 8000.0F;
        int sampleSizeInbits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
    }
    
    public void initializeAudio(InetAddress serverIp){
        try {
            
            if(!AudioSystem.isLineSupported(info)){
                System.out.println("not suport");
                return;
            }
            audioIn = (TargetDataLine) AudioSystem.getLine(info);
            audioIn.open(format);
            audioIn.start();
            RecorderThread recordThread = new RecorderThread();
            recordThread.initializeAudioTransmittion(serverIp, serverPort, audioIn);
            recordThread.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
