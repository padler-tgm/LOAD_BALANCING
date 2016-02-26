package dezsys;

public class Client {
	private SocketClient lb;//connect to LB
	private SocketServer c;//for response
	
	public Client(int port){
		System.out.println("Client is running: "+port);
		this.c = new SocketServer(port);
	}
	
	public void connectToLB(String url, int port){
		this.lb = new SocketClient(url, port);
	}
	
	public void send(Integer zahl){
		this.lb.send(zahl.toString());
	}
}
