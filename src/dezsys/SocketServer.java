package dezsys;
import java.net.*;
import java.io.*;

/**
 * Socket fuer den Server
 * @author Philipp Adler
 * @version 2015-11-27
 */
public class SocketServer {
	
	private ServerSocket server;
	private Socket socket;
	
	/**
	 * Erstellt einen Server, der auf eine Connection wartet
	 * @param port Port auf dem die Nachrichten ausgetauscht werden
	 */
	public SocketServer(int port){
		try {
			this.server = new ServerSocket(port);
			this.socket = server.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * empfaengt Nachrichten vom Client
	 * @return die empfangene Nachricht
	 */
	public String receive(){
		String answer = "";
		String inputLine = "";
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    answer = input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	/**
	 * sendet Nachrichten an den Client
	 * @message die Message die verschickt wird
	 */
	public void send(String message){
		try {
			PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
			out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Diese Methode beendet alle laufenden Prozesse
	 */
	public void close(){
		try {
			this.socket.close();
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}