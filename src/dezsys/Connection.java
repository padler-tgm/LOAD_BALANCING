package dezsys;

public class Connection {
	
	private String url;
	private int connection;
	
	public Connection(String url){
		this.url = url;
		this.connection = 1;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getConnection() {
		return connection;
	}

	public void increase() {
		this.connection += 1;
	}
	
	public void setConnection(int c){
		this.connection = c;
	}
	
	
}
