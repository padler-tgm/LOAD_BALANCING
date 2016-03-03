package dezsys;

import java.net.Inet4Address;
import java.net.UnknownHostException;
/**
 * Klasse zur Repraesentation eines Servers
 * @author Philipp Adler
 * @version 2016-03-03
 */
public class Server extends Thread{
	private SocketServer server;
	private SocketClient lb;//connect to LB
	private String ip;
	private String LBIP;
	/**
	 * Konstruktor der Server-Klasse
	 * @param port serverport
	 */
	public Server(int port){
		System.out.println("Server laeuft auf PORT: "+port);
		//neuer socketserver mit port
		this.server = new SocketServer(port);
		try {
			this.ip = Inet4Address.getLocalHost().getHostAddress()+":"+port;
			this.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ueberschriebene run-Methode
	 */
	@Override
	public void run() {
		super.run();
		//server wartet auf anfragen
		this.receive();
	}
	/**
	 * Methode zum erhalten von anfragen an den server
	 */
	public void receive(){
		System.out.println("SERVER IS WAITING FOR REQUEST");
		String request;
		//endlosschleife
		while(true){
			this.server.acceptSocket();
			try{
				//Client Anfrage
				if((request = this.server.receive()) != null){
					System.out.println("LB ANFRAGE "+Integer.parseInt(request.split("\\?")[1]));
					this.server.closeSocket();
					//server berechnet ergebnis und sendet an lb
					System.out.println("SERVER SENDET ERGEBNIS AN LB: "+Integer.parseInt(request.split("\\?")[1])*2);
					this.send(this.LBIP.split(":")[0], Integer.parseInt(this.LBIP.split(":")[1]), "Reply "+(Integer.parseInt(request.split("\\?")[1])*2)+" "+request.split("\\?")[0]+"?"+this.ip);
				}
			}catch(NullPointerException e){

			}
		}
	}
	/**
	 * Methode zum Verbinden des Servers an LB
	 * @param url url des lb
	 * @param port port des lb
	 */
	public void connectToLB(String url, int port){
		this.LBIP = url+":"+port;
		System.out.println("REGISTRIERT SICH BEIM LB");
		this.send(url, port, "Register "+this.ip);
	}
	/**
	 * Methode zum senden an lb
	 * @param url url des lb
	 * @param port port des lb
	 * @param message die konkrete nachricht die gesendet werden soll
	 */
	public void send(String url, int port, String message){
		this.lb = new SocketClient(url, port);
		this.lb.send(message);
	}
	/**
	 * Methode zum Logout des Servers
	 */
	public void logout(){
		this.send(this.LBIP.split(":")[0], Integer.parseInt(this.LBIP.split(":")[1]), "Logout "+this.ip);
		System.exit(1);
	}

}
