package dezsys;

import java.util.Scanner;

/**
 * Diese Klasse dient als Startklasse zum Starten des Programms
 * @author Philipp Adler
 * @author Adin Karic
 * @version 2016-03-03
 */
public class Main {
	/**
	 * Die main-Methode die beim Starten des Programms ausgefuehrt wird
	 * @param args
	 */
	public static void main(String[] args) {
		//Hinweise zur Ausfuehrung
		System.out.println("Ich empfehle Ihnen nur eine Maschine zu starten:"+"\n"+
				"Entweder den LoadBalancer -lp PORT (-least ODER -weight)"+"\n"+
				"Oder Client -cp PORT -m ZAHL -u URL:PORT"+"\n"+
				"Oder -sp PORT -u URL:PORT");

		//neues CLI wird erzeugt und parameter werden uebergeben
		CLI cli = new CLI();
		cli.addArguments(args);

		//wenn lb angegeben
		if(!(cli.value("LoadBalancer Port").equals("") || cli.value("LoadBalancer Port").isEmpty())){
			//Entscheidung ueber die Art des Load Balancing
			if(cli.value("Weighted Distribution").equals("EXIST")){
				//loadbalancer mit weighted distribution-art wir erzeugt
				LoadBalancer lb = new LoadBalancer(Integer.parseInt(cli.value("LoadBalancer Port")),2);
			}else{
				//loadbalancer mit least connection-art wird erzeugt
				LoadBalancer lb = new LoadBalancer(Integer.parseInt(cli.value("LoadBalancer Port")),1);
			}
		}

		//Parameter des Clients und LB werden geprueft
		if(!(cli.value("Client Port").equals("") || cli.value("Client Port").isEmpty())){
			if(!(cli.value("Adresse des LB(url:port)").equals("") || cli.value("Adresse des LB(url:port)").isEmpty())){
				//neuer Client mit dem angegebenen Client Port wird erzeugt
				Client client = new Client(Integer.parseInt(cli.value("Client Port")));
				while(true){
					Scanner scanner = new Scanner(System.in);
					if(scanner.hasNextInt())
						client.send(scanner.nextInt(),cli.value("Adresse des LB(url:port)").split(":")[0],
								Integer.parseInt(cli.value("Adresse des LB(url:port)").split(":")[1]));
				}
			}else{
				System.out.println("Es wurde Adresse zum LoadBalancer angegeben");
			}
		}

		
		if(!(cli.value("Server Port").equals("") || cli.value("Server Port").isEmpty())){
			//Server wird erzeugt und mit LB verbunden
			Server server = new Server(Integer.parseInt(cli.value("Server Port")));
			if(!(cli.value("Adresse des LB(url:port)").equals("") || cli.value("Adresse des LB(url:port)").isEmpty())){
				server.connectToLB(cli.value("Adresse des LB(url:port)").split(":")[0],
						Integer.parseInt(cli.value("Adresse des LB(url:port)").split(":")[1]));
				//wenn der Nutzer Logout eingibt wird der server ausgeloggt
				while(true){
					Scanner scanner = new Scanner(System.in);
					if(scanner.hasNext()){
						if(scanner.next().equals("Logout"))server.logout();
					}
				}
			}else{
				System.out.println("Es wurde Adresse zum LoadBalancer angegeben");
			}
		}
	}
}