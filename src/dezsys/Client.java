package dezsys;

import java.net.Inet4Address;
import java.net.UnknownHostException;
/**
 * Klasse zur Repraesentation des Clients
 * @author Philipp Adler
 * @author Adin Karic
 * @version 2016-03-03
 */
public class Client extends Thread{
	//Verbindung zum LB
	private SocketClient lb;
	private SocketServer r;
	private String ip;
	/**
	 * Konstruktor des Clients
	 * @param serverport der Port des Servers
	 */
	public Client(int serverport){
		//neuer socketserver wird erstellt
		this.r = new SocketServer(serverport);
		System.out.println("Client is running: "+serverport);
		try {
			//ip adresse
			this.ip = Inet4Address.getLocalHost().getHostAddress()+":"+serverport;
			this.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Ueberschriebene run()-Methode
	 */
	@Override
	public void run() {
		//eltern-run()
		super.run();
		//receive methode wird aufgerufen
		this.receive();
	}
	/**
	 * Klasse zum Senden der Anfrage
	 * @param zahl zahl mit der gerechnet werden soll
	 * @param url url des lb
	 * @param port port des lb
	 */
	public void send(Integer zahl, String url, int port){
		//neuer SocketClient fuer lb
		this.lb = new SocketClient(url, port);
		System.out.println("SENDET ANFRAGE AN LB: "+zahl);
		//anfrage zum rechnen wird an lb gesendet
		this.lb.send(zahl.toString()+","+this.ip);
	}
	/**
	 * Methode zum Empfangen einer antwort
	 */
	public void receive(){
		System.out.println("CLIENT IS WAITING FOR RESULT");
		String request;
		//endlosschleife
		while(true){
			this.r.acceptSocket();
			try{
				//wenn ergebnis not null wird es geprintet und socket wird geschlossen
				request = this.r.receive();
				if(request != null){
					System.out.println("ERGEBNIS "+request);
					this.r.closeSocket();
				}
			}catch(NullPointerException e){

			}
		}
	}

}
