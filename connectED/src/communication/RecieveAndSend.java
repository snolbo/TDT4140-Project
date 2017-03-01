package communication;

import java.io.*;
import java.net.*;

import communication.ChatController;
import javafx.application.Platform;



// recieves and sends data to other party that is connected
public class RecieveAndSend implements Runnable{
	
	// streams to read and write to socket
	private InputStream input;
	private DataOutputStream output;
	private BufferedReader buffread;
	
	// socket for reading from and writing too
	private Socket socket;
	
	// Handeling type of message sent according to protocol in it
	private ProtocolParser protocolParser;
	
	// Controller for chatTab this connection is related to
	private ChatController chatController;

	
	public RecieveAndSend(Socket clientSocket, ChatController chatController){
		this.socket = clientSocket;
		this.chatController = chatController;
		// tells the controller that it is handeling this thread
		chatController.setRecieveAndSend(this);
		this.protocolParser = new ProtocolParser(chatController);
	}
	
	
	// this function runs as a thread
	@Override
	public void run() {
		try{
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
	
	// setting up output and input streams to read and write from using the connected socket
	private void setupStreams() throws IOException{
		input = socket.getInputStream();
		buffread = new BufferedReader(new InputStreamReader(input));
		output = new DataOutputStream(socket.getOutputStream());
		output.flush();
	}
	
	
	// Reading from the socket, passing message to ProtocolParser
	private void whileRecieving(){
		String message = "You are now connected!";
		viewMessage(message, true);			// views message in chatTab handles by controller
		ableToType(true); 					// tell controller that textfield should be editable
		do{
			try{
				message = buffread.readLine();
				protocolParser.handleMessageProtocoll(message);		// pass message to ProtocolParser and let it handle request
			}catch(IOException e){
				e.printStackTrace();
				break;
			}
		} while(message != null && !message.substring(0, 3).equals("END"));
	}
	
	
	// closes the connection
	public void closeConnection(){
		viewMessage("Closing connection...", true);
		ableToType(false);				// tell controller that we should no longer be able to type
		try{
			this.output.close();
			this.input.close();
			this.buffread.close();
			this.socket.close();
		}catch(IOException e){
			e.printStackTrace();
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
	

	// tells the controller to show the message on clients GUI
	private void viewMessage(final String text, boolean madeByMe){
		Platform.runLater(() -> {chatController.viewMessage(text, madeByMe);}); // not sure if need runlater
	}
	
	private void ableToType(final boolean tof){
		Platform.runLater(() -> { chatController.ableToType(tof);}); // not sure if need runlater
	}
	
	
}