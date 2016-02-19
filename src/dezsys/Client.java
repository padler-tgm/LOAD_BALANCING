package dezsys;

public class Client {
	public static void main(String[] args) {
		SocketClient c = new SocketClient("localhost", 9099);
		c.send("S");
	}
}
