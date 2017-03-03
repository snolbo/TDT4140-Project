package communication;

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


// Controls the chat of one connection
public class ChatController {

	@FXML private TextArea userText;
	@FXML private ListView<Label> chatWindow;
	
	private RecieveAndSend connection;
	

	@FXML // listens to keyevents in userText, if keyevent is enter, send CHAT protovol and the text
	public void handleChatEnterKey(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER ){
			String message = userText.getText();
			Platform.runLater(() -> {connection.sendChatMessage("CHAT-" + message);});
//			connection.sendChatMessage("CHAT-" + message);
			viewMessage(message, true);
			event.consume();
		}
	}
	
	
	// used by tab creates in serverChatController on closeRequest of tab
	public void onClosed() {
		connection.sendChatMessage("END-null");
		connection.closeConnection();
	}
	
	// shows message in chatWIndow
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

	// sets a serverCOnnection to this chattab
	public void setRecieveAndSend(RecieveAndSend connection){
		this.connection = connection;
	}

	public void ableToType(boolean tof) {
		this.userText.setEditable(tof);
	}
	
}