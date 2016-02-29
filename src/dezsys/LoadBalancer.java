package dezsys;

public class LoadBalancer extends Thread{
	private SocketServer lbs;//als LB fuer Client
	private SocketClient lbc;//als LB fuer Server
	private Algo alg;

	public LoadBalancer(int port){
		System.out.println("LoadBalancer läuft auf PORT: "+port);
		this.lbs = new SocketServer(port);
		this.alg = new Algo();
		this.initiate();
	}

	public void initiate(){
		start();
	}
	
	@Override
	public void run() {
		super.run();
		this.request();
//		this.response();
	}
	
	public void request(){//leitet von Client an Server weiter
		System.out.println("WAITING FOR CLIENT REQUEST");
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
						System.out.println("EMPFAENGT ERGEBNIS VOM SERVER "+ip);
						this.pull(request.split(" ")[1],this.alg.clearRelation(ip));//SCHICKT ANTWORT AN CLIENT 
						
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

	private void push(String message, String ip){//LB fungiert als Client und schickt an Server
		String serverip = alg.getServer(ip);//BEKOMMT EINEN ZUFAELLIGEN SERVER
		this.lbc = new SocketClient(serverip.split(":")[0],Integer.parseInt(serverip.split(":")[1]));
		System.out.println("SEND TO SERVER " + serverip+" "+message);
		this.lbc.send(message);
	}

	private void pull(String message, String ip){//LB fungiert als Server und schickt an Client
		SocketClient client = new SocketClient(ip.split(":")[0],Integer.parseInt(ip.split(":")[1]));
		System.out.println("SEND TO CLIENT " +ip+" "+message);
		client.send(message);
		client.close();

	}
}
