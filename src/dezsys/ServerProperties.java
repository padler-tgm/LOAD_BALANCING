package dezsys;

public class ServerProperties {
	
	private double anzCon;
	private double gewichtung;
	
	public ServerProperties(){
		this.anzCon = 0;
		this.gewichtung =  (int) ((Math.random()*10)+1);;//VON 1-10
	}

	public double getAnzCon() {
		return anzCon;
	}

	public void increaseAnzCon() {
		this.anzCon += 1;
	}

	public double getGewichtung() {
		return gewichtung;
	}

	public void setGewichtung(int gewichtung) {
		this.gewichtung = gewichtung;
	}
	
	public double calculateLeistung() {
		double a = anzCon/gewichtung;
		return a;
	}
	
	
}
