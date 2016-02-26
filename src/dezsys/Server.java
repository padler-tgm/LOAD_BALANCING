package dezsys;

public class Server {
	private int anzCon;
	private String ip;
	
	public Server(String ip,int anzCon) {
		this.ip = ip;
		this.anzCon=anzCon;
	}
	
	public int getAnzCon(){
		return anzCon;
	}
	
	public String getIP(){
		return ip;
	}
	
	public void connect (){
		this.anzCon+=1;
	}
	
	public void disconnect (){
		this.anzCon-=1;
	}
}
