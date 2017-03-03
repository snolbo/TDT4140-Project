package T2;

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