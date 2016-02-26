package dezsys;

public class MainClient {
	public static void main(String[] args) {
		Client client = new Client(10001);
		client.connectToLB("localhost", 9999);
		client.send(2);
	}
}
