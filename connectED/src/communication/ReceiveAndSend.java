package communication;

import java.io.*;
import java.net.*;

import communication.ChatController;
import javafx.application.Platform;



/**
 * @author snorr
 * Receive from and send data to the socket set in constructor
 */
public class ReceiveAndSend implements Runnable{
	
	private InputStream input;
	private DataOutputStream output;
	private BufferedReader buffread;
	
	private Socket socket;
	private ProtocolParser protocolParser;
	private ChatController chatController;
	
	public ReceiveAndSend(Socket clientSocket, ChatController chatController){
		this.socket = clientSocket;
		this.chatController = chatController;
		chatController.setRecieveAndSendConnection(this);
		this.protocolParser = new ProtocolParser(chatController);
	}
	
	@Override
	public void run() {
		try{
			setupStreams();
			if(!chatController.isAssistantHost() && !chatController.isHelperHost()){ 
					Platform.runLater( () -> {
						if(chatController.codeEditorFinishedLoading())
							chatController.sendCodeURL();
						else
							chatController.sendCodeURLWhenLoaded();
					});
			}				
			whileReceiving();
		}catch(EOFException e){
			viewMessage("Server terminated the connection", true);
		}
		catch(IOException e){
			e.printStackTrace();
		}finally{
			chatController.disableMicButton(true);
			closeConnection();
		}
	}
	
	


	/**
	 * @throws IOException
	 * set up input and output streams from the socket
	 */
	public void setupStreams() throws IOException{
		input = socket.getInputStream();
		buffread = new BufferedReader(new InputStreamReader(input));
		output = new DataOutputStream(socket.getOutputStream());
		output.flush();
	}
	

	/**
	 * Reads line from the socket and hands them on to the protocolparser
	 */
	public void whileReceiving(){
		String message = "-----You are now connected!-----";
		viewMessage(message, false);
		ableToType(true);
		chatController.disableMicButton(false);
		do{
			try{
				message = buffread.readLine();
				protocolParser.handleMessageProtocol(message);
			}catch(IOException e){

				if (socket.isClosed()){
					System.out.println("Socket is closed so we stop looping in whileReceiving");
					break;
				}
			}
		} while(message != null && !socket.isClosed());
	}
	
	
	/**
	 * Closes the input and outputstreams, and the socket
	 */
	public void closeConnection(){
		viewMessage("Closing connection...", true);
		viewMessage("--------------------------",false);
		ableToType(false);
		try{
			this.output.close();
			this.input.close();
			this.buffread.close();
			this.socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @param message
	 * Sends message in forms of lines to the socket
	 */
	public void sendChatMessage(String message){
			try{
				this.output.writeBytes(message +"\r\n");
				this.output.flush();
			}catch(IOException e){
				 e.printStackTrace();
				 if(socket.isClosed())
					 System.out.println("Socket for communication is closed, running closing protocol...");
					 closeConnection();
			}
	}
	

	/**
	 * @param text
	 * @param madeByMe
	 * Tells the chatcontroller to view the String text
	 */
	public void viewMessage(final String text, boolean madeByMe){
		Platform.runLater(() -> {chatController.viewMessage(text, madeByMe);});
	}
	
	/**
	 * @param tof
	 * Tells chatcontroller wheather or not being able to type in textfield is enabled
	 */
	public void ableToType(final boolean tof){
		Platform.runLater(() -> { chatController.ableToType(tof);});
	}

	
	/**
	 * @return
	 * Returns the inteadress accociaterd with the socket, meaning the ip adress of the other party
	 */
	public InetAddress getInetAddress(){
		if(socket != null)
			return socket.getInetAddress();
		else
			return null;
	}
	
}