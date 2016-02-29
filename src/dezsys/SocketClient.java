package dezsys;
import java.io.*;
import java.net.*;


/**
 * Socket fuer den Client
 * @author Philipp Adler
 * @version 2015-11-27
 */
public class SocketClient extends Thread{

	private Socket socket;

	/**
	 * Baut ein Verbindung mit dem Server auf
	 * @param adresse Adresse des Servers
	 * @param port Port auf dem die Nachrichten ausgetauscht werden
	 */
	public SocketClient(String adresse, int port){
		try {
			this.socket = new Socket(adresse, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * empfaengt Nachrichten vom Server
	 * @return die empfangene Nachricht
	 */
	public String receive(){
		String answer = "";
		try {
			//Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            answer = br.readLine();
//            System.out.println("Message received from the server : " + answer);
//            br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	/**
	 * sendet Nachrichten an den Server
	 * @message die Message die verschickt wird
	 */
	public void send(String message){
		try {
			OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(message + "\n");
            bw.flush();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
