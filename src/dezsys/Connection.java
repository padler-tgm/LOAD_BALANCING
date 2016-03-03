package dezsys;
/**
 * Klasse zur Repraesentation der Verbindung
 * @author Philipp Adler
 * @version 2016-03-03
 */
public class Connection {
	//Attribute fuer die url und connection
	private String url;
	private int connection;
	/**
	 * Konstruktor der Connection Klasse
	 * @param url url der Verbindung
	 */
	public Connection(String url){
		this.url = url;
		this.connection = 1;
	}
	/**
	 * Getter-Methode fuer die url
	 * @return die url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * Setter-Methode fuer die url
	 * @param url die url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * Getter-Methode fuer die Verbindungszahl
	 * @return verbindungszahl
	 */
	public int getConnection() {
		return connection;
	}
	/**
	 * Methode zum erhoehen der Verbindungszahl um 1
	 */
	public void increase() {
		this.connection += 1;
	}
	/**
	 * Setter-Methode fuer die Verbindungszahl
	 * @param c neue verbindungszahl
	 */
	public void setConnection(int c){
		this.connection = c;
	}
	
	
}
