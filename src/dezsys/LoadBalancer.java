package dezsys;

public class LoadBalancer {
	private SocketServer lbs;//als LB fuer Client
	private SocketClient lbc;//als LB fuer Server

	public LoadBalancer(int port){
		this.lbs = new SocketServer(port);
		//UNKLAR WELCHER SERVER
		//this.lbs = new SocketClient("localhost", port);
	}

	public void request(){//leitet von Client an Server weiter
		String request;
		if((request = this.lbs.receive()) != null){//Client Anfrage
			this.push(request);
		}

	}

	public void response(){
		String response;
		if((response = this.lbc.receive()) != null){//Server Antwort
			this.pull(response);
		}
	}

	private void push(String message){//LB fungiert als Client und schickt an Server
		this.lbc.send(message);
	}

	private void pull(String message){//LB fungiert als Server und schickt an Client
		this.lbs.send(message);

	}
}
