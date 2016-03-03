package dezsys;
/**
 * Klasse fuer die verschiedenen Servereigenschaften
 * @author Philipp Adler
 * @author Adin Karic
 * @version 2016-03-03
 */
public class ServerProperties {
	//attribute fuer die anzahl der connections und die leistungsgewichtung (je hoeher desto besser)
	private double anzCon;
	private double gewichtung;
	/**
	 * Konstruktor der ServerProperties in dem die anzahl der verbindungen auf 0 gesetzt wird und
	 * die gewichtung eine zufallszahl von 1-10 ist
	 */
	public ServerProperties(){
		this.anzCon = 0;
		this.gewichtung =  (int) ((Math.random()*10)+1);;//VON 1-10
	}
	/**
	 * Getter-Methode fuer die anzahl der Connections
	 * @return die Anzahl der Connections
	 */
	public double getAnzCon() {
		return anzCon;
	}
	/**
	 * Methode zum erhoehen der Anzahl der Connections
	 */
	public void increaseAnzCon() {
		this.anzCon += 1;
	}
	/**
	 * Getter-Methode fuer die gewichtung
	 * @return die gewichtung von 1-10
	 */
	public double getGewichtung() {
		return gewichtung;
	}
	/**
	 * Setter-Methode fuer die gewichtung
	 * @param gewichtung die gewichtungszahl
	 */
	public void setGewichtung(int gewichtung) {
		this.gewichtung = gewichtung;
	}
	/**
	 * Methode zum Berechnen der Leistungszahl (je kleiner desto besser)
	 * @return die Leistungszahl
	 */
	public double calculateLeistung() {
		//die anzahl der Verbindugen wird durch die gewichtung des Servers dividiert
		double a = anzCon/gewichtung;
		return a;
	}


}
