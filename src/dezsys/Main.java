package dezsys;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		System.out.println("Ich empfehle Ihnen nur eine Maschine zu starten:"+"\n"+
				"Entweder den LoadBalancer -lp PORT (-least ODER -weight)"+"\n"+
				"Oder Client -cp PORT -m ZAHL -u URL:PORT"+"\n"+
				"Oder -sp PORT -u URL:PORT");


		CLI cli = new CLI();
		cli.addArguments(args);


		if(!(cli.value("LoadBalancer Port").equals("") || cli.value("LoadBalancer Port").isEmpty())){
			if(cli.value("Weighted Distribution").equals("EXIST")){
				LoadBalancer lb = new LoadBalancer(Integer.parseInt(cli.value("LoadBalancer Port")),2);
			}else{
				LoadBalancer lb = new LoadBalancer(Integer.parseInt(cli.value("LoadBalancer Port")),1);
			}
		}


		if(!(cli.value("Client Port").equals("") || cli.value("Client Port").isEmpty())){
			if(!(cli.value("Adresse des LB(url:port)").equals("") || cli.value("Adresse des LB(url:port)").isEmpty())){
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
			Server server = new Server(Integer.parseInt(cli.value("Server Port")));
			if(!(cli.value("Adresse des LB(url:port)").equals("") || cli.value("Adresse des LB(url:port)").isEmpty())){
				server.connectToLB(cli.value("Adresse des LB(url:port)").split(":")[0],
						Integer.parseInt(cli.value("Adresse des LB(url:port)").split(":")[1]));
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