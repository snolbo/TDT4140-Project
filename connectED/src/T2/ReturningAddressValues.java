package T2;

// hvorfor trenger vi denne klassen? den gjør jo bare om infoen til mind re tilgjengelig info ved å gjøre den til en mer kompleks string
public class ReturningAddressValues {
    
	public final String IP;
    public final int port;

    public ReturningAddressValues(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }
    
    @Override
    public String toString() {
    	String Address = "IP: " + IP + ", Port: " + Integer.toString(port);
    	return Address;
    }
}