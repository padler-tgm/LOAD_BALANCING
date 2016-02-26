package dezsys;

public class Main {
	public static void main(String[] args) {
		LoadBalancer lb = new LoadBalancer(9999);
		Server server1 = new Server(4242);
		lb.initiate();
		
		
//		SocketServer lb = new SocketServer(9099);
//		SocketClient c = new SocketClient("localhost", 9099);
//		System.out.println(lb.receive());
//		c.send("request");
		
	}
}
