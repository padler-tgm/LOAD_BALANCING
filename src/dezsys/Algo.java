package dezsys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.UUID;
/**
 * Die Klasse Algo verwaltet die Loadbalancing-Algortihmen
 * @author Philipp Adler
 * @author Adin Karic
 * @version 2016-03-03
 */
public class Algo extends Thread{
	//Collections fuer Server und connections
	private HashMap<String, ServerProperties> server;
	private HashMap<String, Connection> relation;
	private int mode;
	/**
	 * Konstruktor der Algo-Klasse
	 * @param mode Parameter der bestimmt ob man Least Connection(1) oder Weighted Distribution(2) benutzt wird
	 */
	public Algo(int mode){
		this.server = new HashMap<>();
		this.relation = new HashMap<>();
		this.mode = mode;
		if(mode == 1)System.out.println("Least Connection");
		if(mode == 2)System.out.println("Weighted Distribution");
		this.start();
	}
	/**
	 * Methode zum Hinzufuegen von Server
	 * @param url
	 */
	public void addServer(String url){
		//wenn server noch nicht vorhanden
		if(!this.server.containsKey(url)){
			this.server.put(url,new ServerProperties());
		}
	}
	/**
	 * Liefert den Client welcher die Antwort vom Server bekommt
	 * @param url die url
	 * @return der Client
	 */
	public String getRelationClient(String url){
		for(Entry<String, Connection> entry : this.relation.entrySet()) {
			String server = entry.getKey();
			if(server.contains(url.split("\\?")[0])){
				//client
				return entry.getValue().getUrl();
			}
		}
		return null;
	}

	/**
	 * Methode zum Loeschen von Servern
	 * @param url Url des zu loeschenden Servers
	 */
	public void deleteServer(String url){
		Iterator<Entry<String, Connection>> it = this.relation.entrySet().iterator();
		//Server wird von der relation map geloescht
		while (it.hasNext()){
			Entry<String, Connection> entry = it.next();
			String server = entry.getKey();
			if(server.contains(url.split(" ")[1])){
				it.remove();
			}
		}
		System.out.println("DELETE SERVER: "+url.split(" ")[1]);
		//server von der liste entfernen
		server.remove(url);
	}

	/**
	 * Liefert die Adresse vom Server an den die Client Anfrage gesendet wird
	 * @param url die url
	 * @return adresse vom server
	 */
	public String getServer(String url){
		String s = null;
		boolean session = false;
		for(Entry<String, Connection> entry : this.relation.entrySet()) {
			String server = entry.getKey();
			Connection client = entry.getValue();
			//client hat schon einmal eine Anfrage geschickt
			if(client.getUrl().equals(url)){
				System.out.println("SESSION");
				//anzahl der connections erhoehen
				client.increase();
				//speichert den selben client nochmal nur connections um 1 erhoeht
				this.relation.put(server, client);
				ServerProperties pro = this.server.get(server.split("\\?")[1]);
				//erhoeht anzahl der connections am server
				pro.increaseAnzCon();
				this.server.put(server.split("\\?")[1], pro);
				session = true;
				System.out.println(server);
				s = server;
				break;
			}
		}
		//wenn erstes mal anfrage
		if(!session){
			System.out.println("FIRST TIME");
			s = UUID.randomUUID()+"?"+this.getServer(this.mode);
			this.relation.put(s, new Connection(url));
		}
		return s;
	}
	/**
	 * ueberschriebene run-methode des Threads
	 */
	@Override
	public void run() {
		//endlosschleife
		while(true){
			System.out.println("TRY TO DELETE WAIT FOR 20 SEK");
			HashMap<String, Connection> zahler = new HashMap<>();
			for(Entry<String, Connection> entry : this.relation.entrySet()) {
				String key = entry.getKey();
				Connection value = entry.getValue();
				Connection con = new Connection(key.split("\\?")[1]);
				//connection wird gesetzt
				con.setConnection(value.getConnection());
				zahler.put(key, con);
			}
			
			for(Entry<String, Connection> entry : zahler.entrySet()) {
				//ausgabe
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
						//session wird beendet
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
	/**
	 * Methode zum Beenden der Aufgabe
	 */
	private void completeTask(){
		try {
			//angenommen task dauert 20 sek
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Methode zur Anwendung des Least-Connection Algorithmus
	 * @return die ip des zugeteilten servers
	 */
	private String algoLeastCon() {
		String ip = "";
		ServerProperties s = null;
		double minAnz=999;
		for(Entry<String, ServerProperties> entry : this.server.entrySet()) {
			String key = entry.getKey();
			ServerProperties value = entry.getValue();
			//wenn die derzeitige minAnzahl an Connections groesser als die des servers in liste
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
	/**
	 * Methode zur Anwendung des Weighted Distribution Algorithmus
	 * @return die ip des zugeteilten servers
	 */
	private String algoWeightDist() {
		String ip ="";
		ServerProperties s = null;
		double leistung=999;
		for(Entry<String, ServerProperties> entry : this.server.entrySet()) {
			String key = entry.getKey();
			ServerProperties value = entry.getValue();
			//wenn leistungsnummer groesser als die kalkulierte des jeweiligen servers
			//je kleiner die zahl desto besser
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
	/**
	 * Methode die die IP des Servers zurueckgibt der zugeteilt werden soll
	 * @param art die Art des Load Balancing (Weighted Distribution oder Least Connections)
	 * @return die IP des servers
	 */
	private String getServer(int art){
		String ip="";
		//wenn 1 --> Least Connections
		//wenn 2 --> Weighted Distribution
		if(art == 1){
			ip = this.algoLeastCon();
		}else if(art == 2){
			ip = this.algoWeightDist();
		}
		return ip;
	}
}
