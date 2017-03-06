package communication;

import java.io.IOException;

import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.TabPaneSkin;

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
	@FXML private Button giveHelpBtn;
	@FXML private Button getHelpBtn;
	private Connector connector;
	static private int waitingToConnect = 0;
	
	public ChatTabController(){
		this.connector = new Connector(); // connector sets up connections and passes them to ReceiveAndSend
	}
	
	public static int getWaitingToConnect() {
		return waitingToConnect;
	}

	public static void decrementWaitingToConnect() {
		ChatTabController.waitingToConnect = waitingToConnect - 1;
	}
	
	
	public void setHostMode(){
		if(this.connector.isHost() == null)
			connector.setHost();
		else if (this.connector.isHost() == false)
			System.out.println("Already set to be client");
		else
			System.out.println("Already hostMode");
	}
	
	public void setClientMode(){
		if(this.connector.isHost() == null)
			connector.setClient();
		else if(this.connector.isHost() == true)
			System.out.println("Already set to be host");
		else
			System.out.println("Already clientMode");
	}

	@FXML
	public void newChatTab(){ // TODO should send message to server queuing its ip
		if(this.connector.isHost() == null)
			System.out.println("Must choose get help or give help before opening connection");
		// host can serve 3, client can only queue once
		else if(ChatTabController.getWaitingToConnect() < 3 && this.connector.isHost() || ChatTabController.getWaitingToConnect() < 1 && !this.connector.isHost()){
		try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatTab.fxml"));	// loads the content of the given FXML file		
				Node node = (Node) loader.load();				// sets the loaded FXML content in a Node
				Tab newTab = new Tab("New Student", node);	// creates new tab with the content in the Node and adds the tab to the current tabs in GUI
				this.chatTab.getTabs().add(newTab);
				ChatController chatController = loader.getController();
				newTab.setOnCloseRequest((event) -> { 	// on closeRequest, end connection that is tied to this chattab
					chatController.onClosed();
				});
				
				this.connector.passChatTabController(chatController);	// passes the controller of the new tab to the controllerQueue in connector
				ChatTabController.waitingToConnect++;
				new Thread(connector).start();	// starts a thread for accepting from welcomeSocket
				// think it would be smart to save thread to one can access and conttroll it. 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Cannot take any more connections, reached max limit");
		}
	}
	
	public void onCloseRequest(){
		final EventType<Event> closeRequestEventType = Tab.TAB_CLOSE_REQUEST_EVENT;
		final Event closeRequestEvent = new Event(closeRequestEventType);
		for(Tab tab : chatTab.getTabs()){
			Event.fireEvent(tab, closeRequestEvent);
		}
		this.connector.onCloseRequest();
	}


	

}
