package dezsys;

public class MainClient {
	public static void main(String[] args) {
		Client client = new Client(3333);
		client.send(2,"localhost", 9999);
	}
}
