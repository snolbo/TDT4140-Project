/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voiceserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import javax.sound.sampled.SourceDataLine;

import communication.ChatTabController;

public class PlayerThread extends Thread{
    public DatagramSocket datagramSocket;
    public SourceDataLine audioOut;
    byte[] buffer = new byte[512];
    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     * Receives soundbytes from initialized datagram socket and plays it though the initialized SourceDataLine
     */
    @Override
    public void run(){
        DatagramPacket incoming  = new DatagramPacket(buffer, buffer.length);
        System.out.println("Starting to receive soundbytes");
        while(ChatTabController.isVoiceCommunicating) {            
            try {
            	datagramSocket.receive(incoming);
                buffer = incoming.getData();
                audioOut.write(buffer, 0, buffer.length);
            } catch (IOException e) {
            	e.printStackTrace();
            	break;
            }
        }
        audioOut.close();
        audioOut.drain();
        datagramSocket.close();
        System.out.println("PlayerThread stop");
    }
    
    /**
     * @param serverPort
     * @param audioOut
     * Creates a Datagramsocket with the given serverPort and a reference to a SourceDataline to  write bytes to in order to play them as a sound 
     */
    public void initializePlayerThread(int serverPort, SourceDataLine audioOut){
    	try {
			datagramSocket = new DatagramSocket(serverPort);
			this.audioOut = audioOut;
		} catch (SocketException e) {
			e.printStackTrace();
		}
    }
}
