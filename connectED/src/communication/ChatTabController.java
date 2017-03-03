package communication;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;


// handles GUI for making another connection creating a new tab
public class ChatTabController {
//	@FXML private TextArea userText;
//	@FXML private TextArea chatWindow;
	@FXML private TabPane chatTab;
	@FXML private Button giveHelpBtn;
	@FXML private Button getHelpBtn;
	private Connector connector;

	// This creates a instans of the Server for which the GUI is set to display, through the Server's call to this controller
	public ChatTabController(){
		// the program creating connections is set to work as a host, hosting connections
		this.connector = new Connector();
	}
	
	public void setHostMode(){
		if(this.connector.isHost() == null || this.connector.isHost() == false)
			connector.setHost();
		else
			System.out.println("Already hostMode");
	}
	
	public void setClientMode(){
		if(this.connector.isHost() == null || this.connector.isHost() == true)
			connector.setClient();
		else
			System.out.println("Already clientMode");
	}

	@FXML
	public void newChatTab(){ // TODO should send message to server queuing its ip
		if(this.connector.isHost() == null)
			System.out.println("Must choose get help or give help before opening connection");
		else{
		try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatTab.fxml"));	// loads the content of the given FXML file		
				Node node = (Node) loader.load();				// sets the loaded FXML content in a Node
				Tab newTab = new Tab("New Student", node);	// creates new tab with the content in the Node and adds the tab to the current tabs in GUI
				this.chatTab.getTabs().add(newTab);
				ChatController controller = loader.getController();
				newTab.setOnCloseRequest((event) -> { 	// on closeRequest, end connection that is tied to this chattab
					controller.onClosed();
				});
				this.connector.passChatTabController(controller);	// passes the controller of the new tab to the controllerQueue in connector
				new Thread(connector).start();	// starts a thread for accepting from welcomeSocket
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onCloseRequest(){
		this.connector.onCloseRequest();
	}
	

}
