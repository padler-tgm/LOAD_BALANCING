package dezsys;

public class MainServer {
	public static void main(String[] args) {
		Server server1 = new Server(4242);
		server1.connectToLB("localhost", 9999);
	}
}
