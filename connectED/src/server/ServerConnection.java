package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javafx.application.Platform;

public class ServerConnection implements Runnable{
	 // Socket to get socket client have connected too
	private Socket socket;
	 // Controller for the content inside the tab
	
	// streams and reader to read from and write datao to/from
	private InputStream input;
	private DataOutputStream output; 
	private BufferedReader buffread;
	private ServerTabController chatTabController;

	
	private ServerProtocolParser protocolParser;
	
	public ServerConnection(Socket clientSocket, ServerTabController chatTabController){
		this.socket = clientSocket;
		this.chatTabController = chatTabController;
		// tells the controller that it is handeling this thread
		chatTabController.setServerConnection(this);
		this.protocolParser = new ServerProtocolParser(chatTabController);

	}


	@Override
	public void run() {
		try{
			viewMessage("Now connected to " + socket.getInetAddress().getHostName(), true);
			// setup input and output streams
			setupStreams();
			// reads data form incoming stream ( loop)
			whileRecieving();
		}catch(EOFException e){
			viewMessage("Client terminated the connection", true);
		}
		catch(IOException e){// when conversation is done, EOFEx catched end of stream / connection
			e.printStackTrace();
		}
		finally{
		// close IO streams
		closeConnection();
		}
	}
	
	//Set the streams to recieve and send data from the socket of the connection
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
				
			}catch(IOException e){
				e.printStackTrace();
				break;
			}
		} while(message != null && !message.substring(0, 3).equals("END"));
	}
	
	
	//closes the connection. Housekeeping stuff after being done with streams etc
	public void closeConnection(){
		viewMessage("Closing connections", true);
		ableToType(false);
		try{
			output.close(); // close the stream
			input.close(); // close the stream
			buffread.close();
			socket.close(); // closes the main connection between computers
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	

	// send message to client used by the controller of the connection
	public void sendChatMessage(String protocolMessage){
		try{
			output.writeBytes(protocolMessage + "\r");
			output.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	// Platform.runlater give the thread handeling the GUI a massage that the given action needs to be performed sometime in the future as some in- or output that has 
	// happened is to be shown at the GUI. Since the GUI is handled by another thread we cannot make the call to directly ask the Controller to perform some action 
	// on the GUI, but we let the Thread on which the Controller is run know that some action: controller.viewMessage(text), needs to be performed by the Thread
	private void viewMessage(String text, boolean madeByMe){
		Platform.runLater(() -> {chatTabController.viewMessage(text, madeByMe);}); //let controller update chatWindow);
//		chatTabController.viewMessage(text, madeByMe);
	}
	
	private void ableToType(boolean tof){
		Platform.runLater(() -> {chatTabController.ableToType(tof);} );
//		chatTabController.ableToType(tof);
	}

	
}

	
	