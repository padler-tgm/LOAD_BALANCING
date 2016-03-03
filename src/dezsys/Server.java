package dezsys;

public class Server extends Thread{
	private SocketServer server;
	private SocketClient lb;//connect to LB
	private String ip;
	private String LBIP;
	
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
					System.out.println("LB ANFRAGE "+Integer.parseInt(request.split("\\?")[1]));
					this.server.closeSocket();
					System.out.println("SERVER SENDET ERGEBNIS AN LB: "+Integer.parseInt(request.split("\\?")[1])*2);
					this.send(this.LBIP.split(":")[0], Integer.parseInt(this.LBIP.split(":")[1]), "Reply "+(Integer.parseInt(request.split("\\?")[1])*2)+" "+request.split("\\?")[0]+"?"+this.ip);
				}
			}catch(NullPointerException e){

			}
		}
	}
	
	public void connectToLB(String url, int port){
		this.LBIP = url+":"+port;
		System.out.println("REGISTRIERT SICH BEIM LB");
		this.send(url, port, "Register "+this.ip);
	}
	
	public void send(String url, int port, String message){
		this.lb = new SocketClient(url, port);
		this.lb.send(message);
	}
	
	public void logout(){
		this.send(this.LBIP.split(":")[0], Integer.parseInt(this.LBIP.split(":")[1]), "Logout "+this.ip);
		System.exit(1);
	}

}
