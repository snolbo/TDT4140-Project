package mainWindow;

import org.w3c.dom.Document;

import com.sun.glass.ui.EventLoop.State;

import communication.ChatController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;

public class InteractionTabManagerController {
	@FXML
	private WebView sharedCodeBrowser;
	
		
	
	@FXML
	void initialize(){
		this.sharedCodeBrowser.getEngine().setUserStyleSheetLocation("data:,body { font: 15px Helvetica; font-weight: bold; }");
		sharedCodeBrowser.getEngine().load("http://www.lutanho.net/play/tetris.html");
		sharedCodeBrowser.setOnKeyPressed((event) ->{
			handleKeyEvent(event);
		});
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
	
	
	public void sendPageURLWhenLoaded(ChatController chatController){
		sharedCodeBrowser.getEngine().getLoadWorker().stateProperty().addListener((observed, oldValue, newValue) -> {
			if(newValue.equals(Worker.State.SUCCEEDED)){
				System.out.println("URL finished loading, sending to helper...");
				chatController.sendCodeURL();
			}
		});
	}
	
	public void setDefaultURL(){
		System.out.println("Setting default URL");
		sharedCodeBrowser.getEngine().load("http://www.lutanho.net/play/tetris.html");
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


