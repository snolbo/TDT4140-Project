package communication;

import java.io.*;
import java.net.*;

import communication.ChatController;
import javafx.application.Platform;



// Receives and sends data to other party that is connected
public class ReceiveAndSend implements Runnable{
	
	// streams to read and write to socket
	private InputStream input;
	private DataOutputStream output;
	private BufferedReader buffread;
	
	// socket for reading from and writing too
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
	
	


	public void setupStreams() throws IOException{
		input = socket.getInputStream();
		buffread = new BufferedReader(new InputStreamReader(input));
		output = new DataOutputStream(socket.getOutputStream());
		output.flush();
	}
	
	public void sendCodeUrl(String URL){
		System.out.println("sending:" + URL);
		sendChatMessage(URL);
	}

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
	
	
	// closes the connection
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
	
	// sending message to the server, method is used from the controller
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
	


	public void viewMessage(final String text, boolean madeByMe){
		Platform.runLater(() -> {chatController.viewMessage(text, madeByMe);});
	}
	
	public void ableToType(final boolean tof){
		Platform.runLater(() -> { chatController.ableToType(tof);});
	}

	
	public InetAddress getInetAddress(){
		if(socket != null)
			return socket.getInetAddress();
		else
			return null;
	}
	
}