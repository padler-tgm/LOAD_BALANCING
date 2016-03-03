package dezsys;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Client extends Thread{
	private SocketClient lb;//connect to LB
	private SocketServer r;
	private String ip;
	
	public Client(int serverport){
		this.r = new SocketServer(serverport);
		System.out.println("Client is running: "+serverport);
		try {
			this.ip = Inet4Address.getLocalHost().getHostAddress()+":"+serverport;
			this.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		super.run();
		this.receive();
	}
	
	public void send(Integer zahl, String url, int port){
		this.lb = new SocketClient(url, port);
		System.out.println("SENDET ANFRAGE AN LB: "+zahl);
		this.lb.send(zahl.toString()+","+this.ip);
	}
	
	public void receive(){
		System.out.println("CLIENT IS WAITING FOR RESULT");
		String request;
		while(true){
			this.r.acceptSocket();
			try{
				request = this.r.receive();
				if(request != null){//Client Anfrage
					System.out.println("ERGEBNIS "+request);
					this.r.closeSocket();
				}
			}catch(NullPointerException e){

			}
		}
	}

}
