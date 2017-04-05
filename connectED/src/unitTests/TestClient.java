package unitTests;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {
	
	
	public void connect() throws UnknownHostException, IOException{
		Socket clientSocket = new Socket("10.22.43.121", 9006);
		clientSocket.close();
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		TestClient testClient = new TestClient();
		testClient.connect();
	}
}
