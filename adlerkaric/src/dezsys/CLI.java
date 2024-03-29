package dezsys;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
/**
 * Klasse fuer die CLI-Funktionalitaet
 * @author Philipp Adler
 * @version 2016-03-03
 */
public class CLI {
	//verschiedene parameter
	public static String[][] optionen = {
		{"lp", "true", "LoadBalancer Port"},{"least", "false", "Least Connection"},{"weight", "false", "Weighted Distribution"},
		{"cp", "true", "Client Port"},
		{"sp", "true", "Server Port"},
		{"lu", "true", "Adresse des LB(url:port)"}};
	private CommandLine cmd;//hier ist gespeichert ob die Option verwendet wurde und wenn die Value der Option
	private Options options;//speichert sich die Optionen die verwendet werden koennen
	private Map<String, String> arguments;//speichert sich die Value der Optionen

	/**
	 * Der Konstruktor nimmt eine Liste von Optionen entgegen die von Application verwaltet werden
	 */
	public CLI(){
		//speichert sich alle Optionen in einem Objekt
		this.options = new Options();
		System.out.println("Parameter:");
		for(int i=0; i<optionen.length; i++){//gibt alle Optionen aus
			for(int j=0; j<optionen[i].length; j++){
				System.out.print(" -"+optionen[i][j]+" "+optionen[i][j+2]);
				options.addOption(optionen[i][j], Boolean.parseBoolean(optionen[i][++j]), optionen[i][++j]);
			}
			if(i == 2 || i == 3|| i == 4)System.out.println();
		}
		System.out.println();
	}

	/**
	 * Diese Methode fuegt eine neue Option hinzu
	 * @param opt die Option z.B. h, a, ..
	 * @param hasArg ob die Option ein Value hat
	 * @param description die Beschreibung der Option
	 */
	public void addOption(String opt, boolean hasArg, String description){
		this.options.addOption(opt, hasArg, description);
		System.out.println(" -"+opt+" "+description);
	}

	/**
	 * Diese Methode gibt alle Optionen zurueck
	 * @return alle Optionen die es gibt
	 */
	private Options getOptions(){
		return this.options;
	}

	/**
	 * In dieser Methode werden die Argumente von CLI uebergeben
	 * @param args die Argumente die der Benutzer eingegeben hat
	 */
	public void addArguments(String[] args){
		CommandLineParser parser = new GnuParser();
		try {
			//hier steht welche Option verwendet und welche Value die Option hat
			this.cmd = parser.parse(this.options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.arguments = this.getOptionValue(this.cmd);
	}

	/**
	 * Diese Methode benutzt den CommandLineParser um zu kontrollieren welche Optionen der Benutzer gewählt hat
	 * @param cmd der CommandLineParser der die verwendeten Optionen und die dazugehoerige Value beinhaltet
	 * @return map mit Options
	 */
	private Map<String,String> getOptionValue(CommandLine cmd) {
		Map<String,String> map = new HashMap<>();
		Options optionen = this.getOptions();
		Iterator i = optionen.getOptions().iterator();
		while(i.hasNext()){
			Option o = (Option) i.next();
			map.put(o.getDescription(),"");
		}

		Option[] array = cmd.getOptions();
		for(Option o : array){
			map.replace(o.getDescription(), o.getValue());
		}
		return map;
	}

	/**
	 * Diese Methode gibt zum angegebenen Key die Value zurueck
	 * @param key der Key von der die Value gesucht wird
	 * @return die Value zum angegegebenen Key
	 */
	public String value(String key){
		String value="";
		try{
		if(!this.arguments.get(key).equals("")){
			value = this.arguments.get(key);
		}
		}catch(NullPointerException e){value="EXIST";}
		return value;
	}

}
