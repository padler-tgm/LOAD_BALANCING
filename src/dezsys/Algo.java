package dezsys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Algo {
	//LIEFERT DIE IP DES SERVERS
	//BENÖTIGT SERVER POLL MIT ALLEN IPS
	private ArrayList<String> server;
	private HashMap<String, String> relation;
	public Algo(){
		this.server = new ArrayList<>();
		this.relation = new HashMap<>();
	}

	public void addServer(String url){
		if(!this.server.contains(url)){
			this.server.add(url);
		}
	}

	public String clearRelation(String url){
		String client = this.relation.get(url);
		if(this.relation.containsKey(url)){
			this.relation.remove(url);
		}
		return client;
	}


	public void deleteServer(String url){
		server.remove(url);
	}

	/**
	 * 
	 * @param url from Client localhost:????
	 * @return
	 */
	public String getServer(String url){
		//ALGO MUSS KONTROLLIEREN IF RELATION CONTAINS SERVER
		String s = this.server.get(0);
		this.relation.put(s, url);
		return s;
	}

	//	private String algoLeastCon() {
	//		String ip = "";
	//		int minAnz=999;
	//		int ind=-1;
	//		for(int i =0; i < server.size() ; i++){
	//			if(minAnz > server.get(i).getAnzCon()){
	//				minAnz = server.get(i).getAnzCon();
	//				ind = i;
	//			}
	//		}
	//		ip = server.get(ind).getIP();
	//		//erhoehen des counters
	//		server.get(ind).connect();
	//		return ip;
	//		
	//	}
	//	
	//	private String algoWeightDist() {
	//		return "";
	//		
	//	}
	//	public String getServer(int art){
	//		String ip="";
	//		if(art == 1){
	//			ip = this.algoLeastCon();
	//		}else{
	//			if(art == 2){
	//				ip = this.algoWeightDist();
	//			}
	//		}
	//		
	//		return ip;
	//	}
}
