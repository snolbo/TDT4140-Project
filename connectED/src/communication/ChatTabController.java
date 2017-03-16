package communication;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ChatTabController {
	@FXML private TabPane chatTab;
	@FXML private Button studentHelperBtn;
	@FXML private Button studentAssistantBtn;
	@FXML private Button studentBtn;
	
	private Connector connector;
	static private int PotentialConnections = 0;
	private ArrayDeque<Thread> waitingThreads;
	ArrayDeque<ChatController> chatControllerQueue;
	private boolean isWaitingForConnection = false;

	
	public ChatTabController(){
		connector = new Connector(this);
		waitingThreads = new ArrayDeque<Thread>();
		chatControllerQueue = new ArrayDeque<ChatController>();
	}
	
	public static int getPotentialConnections() {
		return PotentialConnections;
	}

	public static void decrementPotentialConnections() {
		ChatTabController.PotentialConnections--;
	}
	

	
	
	public void setStudentHelperMode(){
		if(this.connector.isHost() == null)
			connector.setHost();
		else if (this.connector.isHost() == false)
			System.out.println("Already set to be client");
		else
			System.out.println("Already hostMode");
	}
	
	public void setStudentMode(){
		if(this.connector.isHost() == null)
			connector.setClient();
		else if(this.connector.isHost() == true)
			System.out.println("Already set to be host");
		else
			System.out.println("Already clientMode");
	}
	
	public void setAssistantMode(){
		if(this.connector.isHost() == null)
			connector.setHost();
		else if (this.connector.isHost() == false)
			System.out.println("Already set to be client");
		else
			System.out.println("Already hostMode");
	}

	
	@FXML
	public void newChatTab(){ // TODO should send message to server queuing its ip
		if(connector.isHost() == null)
			System.out.println("Must choose get help or give help before opening connection");
		// host can serve 3, client can only queue once
		else if(ChatTabController.getPotentialConnections() < 3 && connector.isHost() || ChatTabController.getPotentialConnections() < 1 && !connector.isHost()){
		try {
				ChatTabController.PotentialConnections++;
				// setting up the Chat GUI element
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatTab.fxml"));	// loads the content of the given FXML file		
				Node node = (Node) loader.load();				// sets the loaded FXML content in a Node
				Tab newTab = new Tab("Chat session", node);	// creates new tab with the content in the Node and adds the tab to the current tabs in GUI
				this.chatTab.getTabs().add(newTab);
				ChatController chatController = loader.getController();

				if(connector.isHost()){
					connector.sendHelperRequest();
					chatController.setHost(true);
				}
				else {chatController.setHost(false);}
				chatControllerQueue.addLast(chatController);
				
				newTab.setOnCloseRequest((event) -> { 	// on closeRequest, end connection that is tied to this chattab
					chatControllerQueue.remove(chatController);
					ChatTabController.decrementPotentialConnections();
					chatController.onClosed(); // dont know if this is correct!!!!!!
				});
				
				if(this.waitingThreads.isEmpty() && !isWaitingForConnection){
					isWaitingForConnection = true;
					new Thread(connector).start();
				}
				else
					this.waitingThreads.addLast(new Thread(connector));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Cannot take any more connections, reached max limit");
		}
	}
	
	
	
	public void startChatSession(Socket socket){ //, ChatController chatController){
		isWaitingForConnection = false;
		ChatController chatController = chatControllerQueue.poll();
		RecieveAndSend connection = new RecieveAndSend(socket, chatController);	
		new Thread(connection).start();
		if(!this.waitingThreads.isEmpty()){
			this.waitingThreads.poll().start(); // removes and starts the next thread in queue to retrive socket
			isWaitingForConnection = true;
		}
	}
	
	public void onCloseRequest(){
		this.connector.closeWelcomeSocket();
		final EventType<Event> closeRequestEventType = Tab.TAB_CLOSE_REQUEST_EVENT;
		final Event closeRequestEvent = new Event(closeRequestEventType);
		for(Tab tab : chatTab.getTabs()){
			Event.fireEvent(tab, closeRequestEvent);
		}
	}


	

}
