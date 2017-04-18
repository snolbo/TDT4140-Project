package mainWindow;

import java.io.IOException;

import org.w3c.dom.Document;

import com.sun.glass.ui.EventLoop.State;

import communication.ChatController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;

public class InteractionTabManagerController {
	@FXML private WebView sharedCodeBrowser;
	@FXML private TabPane tabContainer;
	@FXML private Button assistantBtn;
	@FXML private Button helperBtn;
	@FXML private Button studentBtn;
	@FXML private Button javaBtn;
	@FXML private Button itgkPythonBtn;
	@FXML private Button itgkMatlabBtn;


	
	
	
	
	private Node selectionModeContent;
	private Tab selectionModeTab;
	
		
	
	@FXML
	void initialize(){
		this.sharedCodeBrowser.getEngine().setUserStyleSheetLocation("data:,body { font: 15px Helvetica; font-weight: bold; }");
		sharedCodeBrowser.getEngine().load("defaultHTML.html");

	
		sharedCodeBrowser.setOnKeyPressed((event) ->{
			handleKeyEvent(event);
		});
		
		initializeSelectionModeContent();
		tabContainer.getSelectionModel().select(0);
	}

	
	private void initializeSelectionModeContent(){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ModeSelection.fxml"));
		try {
			selectionModeContent = loader.load();
			selectionModeTab = new Tab("Queue Selection", selectionModeContent);
			tabContainer.getTabs().add(0,selectionModeTab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setURL(String URL){
		System.out.println("Loading the given URL: " + URL);
		sharedCodeBrowser.getEngine().load(URL);
	}
	
	public String getURL(){
		Document doc = sharedCodeBrowser.getEngine().getDocument();
		return doc.getBaseURI();
	}
	
	public void reloadCodeEditor(){
		sharedCodeBrowser.getEngine().reload();
	}
	

	
	public void setDefaultURL(){
		System.out.println("Setting default URL");
		sharedCodeBrowser.getEngine().load("defaultHTML.html");
	}
	
	public boolean isFinishedLoading(){
		if(sharedCodeBrowser.getEngine().getLoadWorker().getState().equals(Worker.State.SUCCEEDED))
			return true;
		else
			return false;
	}
	
	
	public void handleKeyEvent(KeyEvent event){
		if(event.isControlDown()){
			double currentVal = sharedCodeBrowser.getFontScale();
			if(event.getCode() == KeyCode.MINUS && currentVal > 0.5)
				sharedCodeBrowser.setFontScale(currentVal - 0.1);
			else if(event.getCode() == KeyCode.PLUS && currentVal < 2)
				sharedCodeBrowser.setFontScale(currentVal + 0.1);
		}
	}
	
	
	
	public void sendPageURLWhenLoaded(ChatController chatController){
		sharedCodeBrowser.getEngine().getLoadWorker().stateProperty().addListener((observed, oldValue, newValue) -> {
			if(newValue.equals(Worker.State.SUCCEEDED)){
				System.out.println("URL finished loading, sending to helper...");
				chatController.sendCodeURL();
			}
		});
	}
	
	public void deleteFirepad() {
		if(getURL().startsWith("https://connected-1e044.firebaseapp.com"))
			sharedCodeBrowser.getEngine().executeScript("deleteFirepadReference();");	
		sharedCodeBrowser.getEngine().load(null);
	}
	
	public void changeCodeLanguage(String codeLanguage){
		if(getURL().startsWith("https://connected-1e044.firebaseapp.com"))
			sharedCodeBrowser.getEngine().executeScript("changeCodeLanguage(" + "'" + codeLanguage + "'" + ");");
	}
	
}


