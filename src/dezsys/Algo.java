package dezsys;

import java.util.ArrayList;

public class Algo {
	//LIEFERT DIE IP DES SERVERS
	//BENÖTIGT SERVER POLL MIT ALLEN IPS
	private ArrayList<Server> server;
	public Algo(ArrayList<Server> server){
		this.server = server;
	}
	
	private String algoLeastCon() {
		String ip = "";
		int minAnz=999;
		int ind=-1;
		for(int i =0; i < server.size() ; i++){
			if(minAnz > server.get(i).getAnzCon()){
				minAnz = server.get(i).getAnzCon();
				ind = i;
			}
		}
		ip = server.get(ind).getIP();
		//erhoehen des counters
		server.get(ind).connect();
		return ip;
		
	}
	
	private String algoWeightDist() {
		return "";
		
	}
	public String getServer(int art){
		String ip="";
		if(art == 1){
			ip = this.algoLeastCon();
		}else{
			if(art == 2){
				ip = this.algoWeightDist();
			}
		}
		
		return ip;
	}
}