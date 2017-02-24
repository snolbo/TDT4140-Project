package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;


// controls the clients chatbox and chatwindow, displays and triggers sending of data
public class ClientController {

	@FXML private TextArea userText;
	@FXML private ListView<Label> chatWindow;
	private Client client;
	
	
	public ClientController(){
		this.client = new Client(this);
	}
	
	@FXML
	void initialize(){
		new Thread(client).start();
	}
	
	
	@FXML // listens to keyevents in userText, if keyevent is enter, send CHAT protovol and the text
	public void handleChatEnterKey(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER ){
			String message = userText.getText();
//			Platform.runLater(() -> {serverConnection.sendChatMessage("CHAT-" + message);});
			client.sendChatMessage("CHAT-" + message);
			viewMessage(message, true);
			event.consume();
		}
	}
	
	@FXML
	public void onClosed(){
		client.sendChatMessage("END-null");
		client.closeConnection();
	}
	
	// shows message in chat window
	public void viewMessage(String text, boolean madeByMe){  // TODO: want the label to be smaller, wrap text, and positioned at one side
		Label message = new Label(text);
		message.prefWidthProperty().bind(chatWindow.widthProperty().subtract(25));
		message.setWrapText(true);
		if(madeByMe) {
			message.setTextFill(Color.WHITE);
			message.setBackground(new Background(new BackgroundFill(Color.web("#4678FB"), new CornerRadii(5), null)));
		}
		else {
			message.setTextFill(Color.BLACK);
			message.setBackground(new Background(new BackgroundFill(Color.web("#EFEEEE"), new CornerRadii(5), null)));
		}	
		Platform.runLater(() -> {chatWindow.getItems().add(message);});
		this.userText.clear();
	}
	
	
	public void ableToType(boolean tof){
		this.userText.setEditable(tof);
	}
	
	
}
