package dezsys;
/**
 * Klasse des LoadBalancers
 * @author Philipp Adler
 * @version 2016-03-03
 */
public class LoadBalancer extends Thread{
	private SocketServer lbs;//als LB fuer Client
	private SocketClient lbc;//als LB fuer Server
	private Algo alg;
	/**
	 * Konstruktor des LoadBalancers
	 * @param port portangabe
	 * @param mode art des LoadBalancing (1->LC,2->WD)
	 */
	public LoadBalancer(int port, int mode){
		System.out.println("LoadBalancer laeuft auf PORT: "+port);
		this.lbs = new SocketServer(port);
		this.alg = new Algo(mode);
		this.initiate();
	}
	/**
	 * LoadBalancer wird gestartet
	 */
	public void initiate(){
		start();
	}
	/**
	 * ueberschriebene run-Methode
	 */
	@Override
	public void run() {
		super.run();
		this.request();
//		this.response();
	}
	/**
	 * Methode zum annehmen von requests von den clients und weiterleiten an server
	 */
	public void request(){
		System.out.println("WAITING FOR CLIENT REQUEST");
		//endlosschleife
		while(true){
			this.lbs.acceptSocket();
			try{
				String request = this.lbs.receive();
				if(request != null){//Anfrage
					if((request.split(" ")[0]).equals("Register")){//SERVER MELDET SICH AN
						this.alg.addServer(request.split(" ")[1]);//WIRD IN DIE LISTE DER VERFUEGBAREN SERVER GESPEICHERT
						System.out.println("SERVER "+request.split(" ")[1]+" HAT SICH REGISTRIERT");
						
					}else if((request.split(" ")[0]).equals("Reply")){//SERVER ANTWORTET AUF ANFRAGE
						String ip = request.split(" ")[2];
						System.out.println("EMPFAENGT ERGEBNIS VOM SERVER "+ip.split("\\?")[1]);
						this.pull(request.split(" ")[1],this.alg.getRelationClient(ip));//SCHICKT ANTWORT AN CLIENT 
						
					}else if((request.split(" ")[0]).equals("Logout")){//SERVER ABMELDEN
						this.alg.deleteServer(request);
						
					}else{//ANFRAGE VOM CLIENT
						System.out.println("ANFRAGE VOM CLIENT "+request.split(",")[1]+" :"+request.split(",")[0]);
						this.push(request.split(",")[0], request.split(",")[1]);//SCHICKT ANFRAGE AN SERVER
					}
					this.lbs.closeSocket();
				}
			}catch(NullPointerException e){
			}
		}

	}

//	public void response(){
//		System.out.println("WAITING FOR SERVER REPLY");
//		String response;
//		while(true){
//			try{
//				if((response = this.lbc.receive()) != null){//Server Antwort
//					System.out.println("Antwort vom Server "+response);
//					this.pull(response);
//				}
//			}catch(NullPointerException e){
//
//			}
//		}
//	}
	/**
	 * Methode um vom LB an den Server zu senden
	 * @param message die Nachricht
	 * @param ip ip des Servers
	 */
	private void push(String message, String ip){
		//server wird mit LB-Methode bestimmt
		String server = alg.getServer(ip);
		String serverip = server.split("\\?")[1];
		this.lbc = new SocketClient(serverip.split(":")[0],Integer.parseInt(serverip.split(":")[1]));
		System.out.println("SEND TO SERVER " + serverip+" "+message);
		//message wird gesendet
		this.lbc.send(server.split("\\?")[0]+"?"+message);
	}
	/**
	 * Methode um vom LB zum Client zu senden
	 * @param message die Nachricht
	 * @param ip ip des clients
	 */
	private void pull(String message, String ip){
		SocketClient client = new SocketClient(ip.split(":")[0],Integer.parseInt(ip.split(":")[1]));
		//message wird an client gesendet
		System.out.println("SEND TO CLIENT " +ip+" "+message);
		client.send(message);
		//client wird geschlossen
		client.close();

	}
}
