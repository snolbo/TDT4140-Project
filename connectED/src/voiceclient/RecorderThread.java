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
    @Override
    public void run(){
        int i = 0;
        while(ChatTabController.isVoiceCommunicating){
            try {
                audioIn.read(byteBuffer, 0, byteBuffer.length);
                DatagramPacket data = new DatagramPacket(byteBuffer, byteBuffer.length, serverIp, serverPort);
                datagramSocket.send(data);
                System.out.println("sending package #" + i);
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
        }
        audioIn.close();
        audioIn.drain();
        datagramSocket.close();
        System.out.println("RecordThread stop");
    }
    
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
