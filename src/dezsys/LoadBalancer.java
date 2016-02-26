package dezsys;

public class LoadBalancer extends Thread{
	private SocketServer lbs;//als LB fuer Client
	private SocketClient lbc;//als LB fuer Server

	public LoadBalancer(int port){
		System.out.println("LoadBalancer läuft auf PORT: "+port);
		this.lbs = new SocketServer(port);
		//UNKLAR WELCHER SERVER
		//this.lbs = new SocketClient("localhost", port);
	}

	public void initiate(){
		start();
		this.response();
	}
	
	@Override
	public void run() {
		super.run();
		this.request();
	}
	
	


	public void request(){//leitet von Client an Server weiter
		System.out.println("WAITING FOR CLIENT REQUEST");
		String request;
		while(true){
			try{
				if((request = this.lbs.receive()) != null){//Client Anfrage
					System.out.println("Anfrage vom Client "+request);
					this.push(request);
				}
			}catch(NullPointerException e){

			}
		}

	}

	public void response(){
		System.out.println("WAITING FOR SERVER REPLY");
		String response;
		while(true){
			try{
				if((response = this.lbc.receive()) != null){//Server Antwort
					System.out.println("Antwort vom Server "+response);
					this.pull(response);
				}
			}catch(NullPointerException e){

			}
		}
	}

	private void push(String message){//LB fungiert als Client und schickt an Server
		this.lbc.send(message);
	}

	private void pull(String message){//LB fungiert als Server und schickt an Client
		this.lbs.send(message);

	}
}
