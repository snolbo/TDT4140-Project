package voiceclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.TargetDataLine;

import communication.ChatTabController;


public class RecorderThread extends Thread{
    public TargetDataLine audioIn = null;
    private DatagramSocket datagramSocket;
    byte byteBuffer[] = new byte[512];
    public InetAddress serverIp;
    public int serverPort;
    
    
    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     * Reads soundbytes from initialized TargetDataLine and send them thorugh the initialized Datagramsocket to the given IP adress and portnumber
     */
    @Override
    public void run(){
        while(ChatTabController.isVoiceCommunicating){
            try {
                audioIn.read(byteBuffer, 0, byteBuffer.length);
                DatagramPacket data = new DatagramPacket(byteBuffer, byteBuffer.length, serverIp, serverPort);
                datagramSocket.send(data);
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
        }
        System.out.println("Exiting from loop sending sound bytes.. closing datagramSocket...");
        audioIn.close();
        audioIn.drain();
        datagramSocket.close();
        System.out.println("RecordThread has stopped succsessfully");
    }
    
    
    
    /**
     * @param serverIp
     * @param serverPort
     * @param audioIn
     * Creates a Datagramsocket and sets serverIP,port and reference to audio input in order to read data from.
     */
    public void initializeAudioTransmittion(InetAddress serverIp, int serverPort, TargetDataLine audioIn){
    	try {
			datagramSocket = new DatagramSocket();
	    	this.serverIp = serverIp;
	    	this.serverPort = serverPort;
	    	this.audioIn = audioIn;
		} catch (SocketException e) {
			e.printStackTrace();
		}
    }
}
