package dezsys;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		CLI cli = new CLI();
		cli.addArguments(args);


		if(!(cli.value("LoadBalancer Port").equals("") || cli.value("LoadBalancer Port").isEmpty())){
			LoadBalancer lb = new LoadBalancer(Integer.parseInt(cli.value("LoadBalancer Port")));
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
			}else{
				System.out.println("Es wurde Adresse zum LoadBalancer angegeben");
			}
		}
	}
}