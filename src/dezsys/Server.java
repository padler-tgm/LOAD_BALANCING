package dezsys;

public class Server extends Thread{
	private SocketServer server;
	private SocketClient lb;//connect to LB
	private int anzCon;
	private String ip;
	
//	public Server(String ip,int anzCon) {
//		this.ip = ip;
//		this.anzCon=anzCon;
//	}
	
	public Server(int port){
		System.out.println("Server läuft auf PORT: "+port);
		this.server = new SocketServer(port);
		this.ip = "localhost:"+port;
		this.start();
	}
	
	@Override
	public void run() {
		super.run();
		this.receive();
	}
	
	public void receive(){
		System.out.println("SERVER IS WAITING FOR REQUEST");
		String request;
		while(true){
			this.server.acceptSocket();
			try{
				if((request = this.server.receive()) != null){//Client Anfrage
					System.out.println("LB ANFRAGE "+request);
					this.server.closeSocket();
					System.out.println("SERVER SENDET ERGEBNIS AN LB: "+Integer.parseInt(request)*2);
					this.send("localhost", 9999, "Reply "+(Integer.parseInt(request)*2)+" "+this.ip);
				}
			}catch(NullPointerException e){

			}
		}
	}
	
	public void connectToLB(String url, int port){
		System.out.println("REGISTRIERT SICH BEIM LB");
		this.send(url, port, "Register "+this.ip);
	}
	
	public void send(String url, int port, String message){
		this.lb = new SocketClient(url, port);
		this.lb.send(message);
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
