package dezsys;

public class Server {
	private SocketServer server;
	private SocketClient lb;
	private int anzCon;
	private String ip;
	
//	public Server(String ip,int anzCon) {
//		this.ip = ip;
//		this.anzCon=anzCon;
//	}
	
	public Server(int port){
		System.out.println("Server läuft auf PORT: "+port);
		this.server = new SocketServer(port);
	}
	
	public void connectToLB(String url, int port){
		this.lb = new SocketClient(url, port);
	}
	
	public int getAnzCon(){
		return anzCon;
	}
	
	public String getIP(){
		return ip;
	}
	
	public void connect (){
		this.anzCon+=1;
	}
	
	public void disconnect (){
		this.anzCon-=1;
	}

}
