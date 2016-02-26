package dezsys;

public class Server {
	private SocketServer server;
	private SocketClient lb;
	private double anzCon;
	private String ip;
	private double gewichtung;
	
//	public Server(String ip,int anzCon) {
//		this.ip = ip;
//		this.anzCon=anzCon;
//	}
	
	public Server(int port){
		System.out.println("Server laeuft auf PORT: "+port);
		this.server = new SocketServer(port);
	}
	
	public void connectToLB(String url, int port){
		this.lb = new SocketClient(url, port);
	}
	
	public double getAnzCon(){
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
	
	public double power (int base, int exponent){
		return Math.pow(base,exponent);
	}
	
	public double getGewichtung(){
		return gewichtung;
	}
	
	public double calculateLeistung() {
		double a = anzCon/gewichtung;
		return a;
	}
	
}
