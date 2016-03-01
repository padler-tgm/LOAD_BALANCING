package dezsys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.UUID;

public class Algo extends Thread{
	//LIEFERT DIE IP DES SERVERS
	//BENÖTIGT SERVER POLL MIT ALLEN IPS
	private ArrayList<String> server;
	private HashMap<String, Connection> relation;

	public Algo(){
		this.server = new ArrayList<>();
		this.relation = new HashMap<>();
		this.start();
	}

	public void addServer(String url){
		if(!this.server.contains(url)){
			this.server.add(url);
		}
	}

	public String getRelationClient(String url){//LIEFERT DEN CLIENT WELCHER DIE ANTWORT VOM SERVER BEKOMMT
		for(Entry<String, Connection> entry : this.relation.entrySet()) {
			String server = entry.getKey();
			if(server.contains(url)){
				return entry.getValue().getUrl();
			}
		}
		return null;
	}


	public void deleteServer(String url){
		server.remove(url);
	}

	/**
	 * 
	 * @param url Lieft die Adresse vom Server an dem die Client Anfrage gesendet wird
	 * @return
	 */
	public String getServer(String url){
		String s = null;
		boolean session = false;
		for(Entry<String, Connection> entry : this.relation.entrySet()) {
			String server = entry.getKey();
			Connection client = entry.getValue();
			if(client.getUrl().equals(url)){//DER CLIENT HAT SCHON EINMAL EINE ANFRAGE GESCHICKT
				System.out.println("SESSION");
				client.increase();//ERHOEHT CONNECTION ATTRIBUT
				this.relation.put(server, client);//SPEICHERT DEN SELBEN CLIENT NUR DIE CONNECTION WURDE UM 1 ERHOEHT
				session = true;
				s = server.split("\\?")[1];
				break;
			}
		}
		
		if(!session){
			System.out.println("FIRST TIME");
			s = this.server.get(0);//STATTDESSEN ALGO
			System.out.println(s);
			this.relation.put(UUID.randomUUID()+"?"+s, new Connection(url));
		}
		return s;
	}

	@Override
	public void run() {
		while(true){
			System.out.println("TRY TO DELETE WAIT FOR 20 SEK");
			//VIELLEICHT DIREKTE REFERENZE AUF ADRESSE
			HashMap<String, Connection> zahler = new HashMap<>();
			for(Entry<String, Connection> entry : this.relation.entrySet()) {
				String key = entry.getKey();
				Connection value = entry.getValue();
				zahler.put(key, value);
			}

			completeTask();

			System.out.println("TRY TO DELETE");
			System.out.println(this.relation.entrySet().size());
			for(Entry<String, Connection> entry : this.relation.entrySet()) {
				String newKey = entry.getKey();
				Connection newConnection = entry.getValue();
				for(Entry<String, Connection> entry1 : zahler.entrySet()){
					String oldKey = entry1.getKey();
					Connection oldConnection = entry1.getValue();
					//WENN DER CLIENT KEINE ANFRAGEN MEHR GETAETIGT HAT
					if(newKey.equals(oldKey) && newConnection.getConnection() == oldConnection.getConnection()){
						if(this.relation.containsKey(newKey)){
							System.out.println("DELETE SESSION "+newKey);
							this.relation.remove(newKey);
						}
					}
				}
			}
		}
	}

	private void completeTask(){
		try {
			//assuming it takes 20 secs to complete the task
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
