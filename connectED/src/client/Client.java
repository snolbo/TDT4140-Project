package client;

import java.io.*;
import java.net.*;

import javafx.application.Platform;




public class Client implements Runnable{
	
	private InputStream input;
	private DataOutputStream output;
	private BufferedReader buffread;
	
	// info about server and port to connect to
	private String serverIP;
	private int serverPort;
	
	private Socket socket;
	
	// controller to GUI
	
	// class handeling protocoll of message
	private ClientProtocolParser protocolParser;
	private ClientController controller;
	
	public Client(ClientController clientController){  
		this.serverIP = ""; //  localhost
		this.serverPort = 7000;
		this.controller = clientController;
		this.protocolParser = new ClientProtocolParser(clientController);
	}
	
	
	
	
	// this function runs as a thread
	@Override
	public void run() {
		try{
			// use socket to connect to server
			connectToServer();
			// setup output and input streams
			setupStreams();
			// read from inputstream while still chatting (loop)
			whileRecieving();
		}catch(EOFException e){
			viewMessage("Server terminated the connection", true);
		}
		catch(IOException e){
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	
	

	//connect to server
	private void connectToServer() throws IOException{
//		viewMessage("Attempting connection...", true);
		this.socket = new Socket(InetAddress.getByName(serverIP), serverPort); // connect to the server
		viewMessage("Connected to:" + socket.getInetAddress().getHostName(), true);
		}
	
	// setting up output and input streams to read and write from using the connected socket
	private void setupStreams() throws IOException{
		// read from stream
		input = socket.getInputStream();
		// buffer the input from stream into this reader
		buffread = new BufferedReader(new InputStreamReader(input));
		// stream that writes data to the socket
		output = new DataOutputStream(socket.getOutputStream());
		// householding
		output.flush();
	}
	
	
	// code running during the conversation
	private void whileRecieving(){
		String message = "You are now connected!";
		viewMessage(message, true);
		// tell controller that textfield should be editable
		ableToType(true);
		do{
			try{
				message = buffread.readLine();
				
				// TODO handle protocoll of what message is for
				protocolParser.handleMessageProtocoll(message);
				// TODO
				
				// messageParser should replace viewMessage. parser should deal with different kind of messages
				//viewMessage(message, false);
			}catch(IOException e){
				e.printStackTrace();
				break;
			}
		} while(message != null && !message.substring(0, 3).equals("END"));
	}
	
	
	// closes the connection
	public void closeConnection(){
		viewMessage("Closing connection...", true);
		// tell controller that we should no longer be able to type
		ableToType(false);
		try{
			this.output.close();
			this.input.close();
			this.buffread.close();
			this.socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			System.exit(1);
		}
	}
	
	// sending message to the server, method is used from the controller
	public void sendChatMessage(String message){
			try{
				this.output.writeBytes(message +"\r");
				this.output.flush();
			}catch(IOException e){
				e.printStackTrace();
			}
	}
	
	// Platform.runlater give the thread handeling the GUI a massage that the given action needs to be performed sometime in the future as some in- or output that has 
		// happened is to be shown at the GUI. Since the GUI is handled by another thread we cannot make the call to directly ask the Controller to perform some action 
		// on the GUI, but we let the Thread on which the Controller is run know that some action: controller.viewMessage(text), needs to be performed by the Thread
	
	// tells the controller to show the message on clients GUI
	private void viewMessage(final String text, boolean madeByMe){
		Platform.runLater(() -> {controller.viewMessage(text, madeByMe);}); // not sure if need runlater
	}
	
	private void ableToType(final boolean tof){
		Platform.runLater(() -> { controller.ableToType(tof);}); // not sure if need runlater
	}



	
	
	
}