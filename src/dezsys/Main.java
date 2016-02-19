package dezsys;

public class Main {
	public static void main(String[] args) {
		SocketServer lb = new SocketServer(9099);
		SocketClient c = new SocketClient("localhost", 9099);
		System.out.println(lb.receive());
		c.send("request");
		
	}
}
