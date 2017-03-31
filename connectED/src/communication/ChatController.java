package communication;

import java.io.IOException;

import org.w3c.dom.Document;

import T2.ServerRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import mainWindow.InteractionTabManagerController;
import mainWindow.MainFrameController;


// Controls the chat of one connection
public class ChatController {

	@FXML private TextArea userText;
	@FXML private ListView<Label> chatWindow;
	
	private RecieveAndSend receiveAndSend = null;
	private boolean isHost= false;
	private Tab chatTab;
	private Node InteractionArea;
	private InteractionTabManagerController interactionTabManagerController;
	

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}
	
	@FXML // listens to keyevents in userText, if keyevent is enter, send CHAT protovol and the text
	public void handleChatEnterKey(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER ){
			String message = userText.getText();
			Platform.runLater(() -> {receiveAndSend.sendChatMessage("CHAT-" + message);});
			viewMessage(message, true);
			event.consume();
		}
		else if(event.getCode() == KeyCode.I){
			System.out.println(interactionTabManagerController.isFinishedLoading());
		}
	}
	
	// used by tab creates in serverChatController on closeRequest of tab
	public void onClosed() {
		System.out.println("Closing chatController...");
		if(receiveAndSend != null){
			System.out.println("Closing chatController - sends END message and closes connection...");
			receiveAndSend.sendChatMessage("END-null");
			receiveAndSend.closeConnection();
		}
		if(isHost && receiveAndSend == null){
			System.out.println("Closing chatController - sends HelperDelete to TCPserver since in queue...");
			ServerRequest request = new ServerRequest("HelperDelete");
			request.removeAdressFromQueue();
		}
		else if(receiveAndSend == null){
			System.out.println("Closing chatController - sends StudentDelete to TCPserver since in queue...");
			ServerRequest request = new ServerRequest("StudentDelete");
			request.removeAdressFromQueue();
		}
		if(!isHost())
			interactionTabManagerController.deleteFirepad();
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
	public void setRecieveAndSendConnection(RecieveAndSend connection){
		this.receiveAndSend = connection;
	}

	public void ableToType(boolean tof) {
		this.userText.setEditable(tof);
	}

	public void setChatTab(Tab chatTab) {
		this.chatTab = chatTab;
	}
	
	
	
	public Tab getChatTab(){
		return this.chatTab;
	}

	public void initializeInteractionArea() {
		System.out.println("Initializing interactionArea assisiated with this chatController");
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("/mainWindow/InteractionTabManager.fxml"));
		try{
		this.InteractionArea  = loader.load();
		this.interactionTabManagerController = loader.getController();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		if(!isHost()){
			interactionTabManagerController.setURL("http://connected-1e044.firebaseapp.com");
		}
		
		else{
			setDefaultURL();

		}
	}
	
	public void setDefaultURL(){
		interactionTabManagerController.setDefaultURL();
	}
	
	
	public Node getInteractionArea(){
		return this.InteractionArea;
	}
	
	public void setCodeUrl(String URL){
		this.interactionTabManagerController.setURL(URL);
	}
	
	public String getCodeURL(){
		return interactionTabManagerController.getURL();
	}
	
//	public void reloadCodeEditor(){
//		interactionTabManagerController.reloadCodeEditor();
//	}

	public boolean codeEditorFinishedLoading() {
		return interactionTabManagerController.isFinishedLoading();
	}

	public void sendCodeURL() {
		System.out.println("Sending CodeURL to helper...");
		receiveAndSend.sendCodeUrl("CODEURL-" +interactionTabManagerController.getURL());
	}
	
	public void sendCodeURLWhenLoaded(){
		System.out.println("Sending codeURL to helper when the page is finished loading...");
		interactionTabManagerController.sendPageURLWhenLoaded(this);
	}
	
}
