package server;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;


// handles GUI for making another connection creating a new tab
public class ServerController {
	@FXML private TextArea userText;
	@FXML private TextArea chatWindow;
	@FXML private ServerConnection serverConnection;
	@FXML private TabPane chatTab;
	private Server server;

	// This creates a instans of the Server for which the GUI is set to display, through the Server's call to this controller
	public ServerController(){
		this.server = new Server();
	}

	@FXML
	public void newChatTab(){
		try {
			// loads the content of the given FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlResources/ChatTab.fxml"));			
			// sets the loaded FXML content in a Node
			Node node = (Node) loader.load();
			// creates new tab with the content in the Node and adds the tab to the current tabs in GUI
			Tab newTab = new Tab("New Student", node);
			this.chatTab.getTabs().add(newTab);
			// get the controller of the new tab
			ServerTabController controller = loader.getController();
			// on closeRequest, end connection that is tied to this chattab
			newTab.setOnCloseRequest((event) -> {
				controller.onClosed();
			});
			// passes the controller of the new tab to the server so it knows what controller to pass to next connection
			this.server.passChatTabController(controller);
			// starts server listening to welcomeSocket
			new Thread(server).start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void windowClosed(){
		// TODO close server
	}

}
