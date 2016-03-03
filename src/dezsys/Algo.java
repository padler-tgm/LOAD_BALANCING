package dezsys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.UUID;

public class Algo extends Thread{
	//LIEFERT DIE IP DES SERVERS
	//BENÖTIGT SERVER POLL MIT ALLEN IPS
	private HashMap<String, ServerProperties> server;
	private HashMap<String, Connection> relation;
	private int mode;

	public Algo(int mode){
		this.server = new HashMap<>();
		this.relation = new HashMap<>();
		this.mode = mode;
		if(mode == 1)System.out.println("Least Connection");
		if(mode == 2)System.out.println("Weighted Distribution");
		this.start();
	}

	public void addServer(String url){
		if(!this.server.containsKey(url)){
			this.server.put(url,new ServerProperties());
		}
	}

	public String getRelationClient(String url){//LIEFERT DEN CLIENT WELCHER DIE ANTWORT VOM SERVER BEKOMMT
		for(Entry<String, Connection> entry : this.relation.entrySet()) {
			String server = entry.getKey();
			if(server.contains(url.split("\\?")[0])){
				return entry.getValue().getUrl();
			}
		}
		return null;
	}


	public void deleteServer(String url){
		Iterator<Entry<String, Connection>> it = this.relation.entrySet().iterator();
		while (it.hasNext()){//DELETE SERVER FROM RELEATION MAP
			Entry<String, Connection> entry = it.next();
			String server = entry.getKey();
			if(server.contains(url.split(" ")[1])){
				it.remove();
			}
		}
		System.out.println("DELETE SERVER: "+url.split(" ")[1]);
		server.remove(url);//DELETE SERVER FROM LIST
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
				ServerProperties pro = this.server.get(server.split("\\?")[1]);
				pro.increaseAnzCon();//ERHOEHT DIE CONNECTION ANZAHL AM SERVER
				this.server.put(server.split("\\?")[1], pro);
				session = true;
				System.out.println(server);
				s = server;
				break;
			}
		}

		if(!session){
			System.out.println("FIRST TIME");
			s = UUID.randomUUID()+"?"+this.getServer(this.mode);//STATTDESSEN ALGO
			this.relation.put(s, new Connection(url));
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
				Connection con = new Connection(key.split("\\?")[1]);
				con.setConnection(value.getConnection());
				zahler.put(key, con);
			}
			
			for(Entry<String, Connection> entry : zahler.entrySet()) {
				String key = entry.getKey();
				Connection value = entry.getValue();
				System.out.println("BEFORE Zahler: "+key+" "+value.getConnection());
			}

			completeTask();

			System.out.println("TRY TO DELETE");
			System.out.println(this.relation.entrySet().size());
			Iterator<Entry<String, Connection>> it = this.relation.entrySet().iterator();

			for(Entry<String, Connection> entry : zahler.entrySet()) {
				String key = entry.getKey();
				Connection value = entry.getValue();
				System.out.println("AFTER Zahler: "+key+" "+value.getConnection());
			}
			
			while (it.hasNext()){
				Entry<String, Connection> entry = it.next();
				String newKey = entry.getKey();
				Connection newConnection = entry.getValue();
				try{
					if(zahler.get(newKey).getConnection() == newConnection.getConnection()){
						System.out.println("OLD "+zahler.get(newKey).getConnection()+" NEW "+newConnection.getConnection());
						System.out.println("DELETE SESSION "+newKey);
						it.remove();
					}
				}catch(NullPointerException e){}
			}

			//			while (it.hasNext()){
			//				Entry<String, Connection> entry = it.next();
			//				String newKey = entry.getKey();
			//				Connection newConnection = entry.getValue();
			//				while (it1.hasNext()){
			//					Entry<String, Connection> entry1 = it1.next();
			//					String oldKey = entry1.getKey();
			//					Connection oldConnection = entry1.getValue();
			//					//WENN DER CLIENT KEINE ANFRAGEN MEHR GETAETIGT HAT
			//					if(newKey.equals(oldKey) && newConnection.getConnection() == oldConnection.getConnection()){
			//						if(this.relation.containsKey(newKey)){
			//							System.out.println("DELETE SESSION "+newKey);
			//							it.remove();
			//						}
			//					}
			//				}
			//			}
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

	private String algoLeastCon() {
		String ip = "";
		ServerProperties s = null;
		double minAnz=999;
		for(Entry<String, ServerProperties> entry : this.server.entrySet()) {
			String key = entry.getKey();
			ServerProperties value = entry.getValue();
			if(minAnz > value.getAnzCon()){
				minAnz = value.getAnzCon();
				ip = key;
				s = value;
			}
		}
		//erhoehen des counters
		s.increaseAnzCon();
		this.server.put(ip, s);
		return ip;

	}
	//	
	private String algoWeightDist() {
		String ip ="";
		ServerProperties s = null;
		double leistung=999;
		for(Entry<String, ServerProperties> entry : this.server.entrySet()) {
			String key = entry.getKey();
			ServerProperties value = entry.getValue();
			if(leistung > value.calculateLeistung()){
				leistung = value.calculateLeistung();
				ip = key;
				s = value;
			}
		}
		//erhoehen des counters
		s.increaseAnzCon();
		this.server.put(ip, s);
		return ip;
	}

	private String getServer(int art){
		String ip="";
		if(art == 1){
			ip = this.algoLeastCon();
		}else if(art == 2){
			ip = this.algoWeightDist();
		}
		return ip;
	}
}
