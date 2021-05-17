package Zuordnung;

import Exceptions.maxAnzHallenAusnahme;
import java.sql.*;


/**
 * Mit dieser Klasse werden die Hallen für die Spiele erstellt.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */
public class Hallen {

	private String halName;
	private String adrStr;
	private String adrOrt;

	private Hallen halle;

	private final static int maxAnzahlHallen = 6;
	private int zahl;
	
    private String query;

	/**
	 * 
	 * @param halName Name der Halle
	 * @param adrStr  Adresse - Straße
	 * @param adrOrt  Adresse - Ort
	 */
	public Hallen(String hallenName, String adrStr, String adrOrt) {
		this.halName = hallenName;
		this.adrStr = adrStr;
		this.adrOrt = adrOrt;
		
		zahl = 0;

		
	}

	/**
	 * Mit dieser Methode sollen Hallen hinzugefügt werden.
	 * 
	 * @param halle
	 * @pre die maximale Anzahl an Hallen darf nicht überschritten werden, es muss
	 *      sich um ein Objekt Halle handeln
	 * @throws maxAnzHallenAusnahme, wenn die maximal benötigte Zahl an Hallen
	 *         überschritten wird.
	 */

	public void halleHinzufuegen(Hallen halle) throws maxAnzHallenAusnahme {
		try {
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
		Statement stmt = con.createStatement();
		
		if (zahl == 0) {
			stmt.execute("DROP TABLE IF EXISTS hallen");
			stmt.execute("CREATE TABLE hallen (`id` int NOT NULL, `halName` varchar(50) NOT NULL, `adrStr` varchar(50) NOT NULL, `adrOrt` varchar(50) NOT NULL, PRIMARY KEY(`id`))");
		}
		if (zahl == maxAnzahlHallen) {
			throw new maxAnzHallenAusnahme();
		}
		
		String insert = "INSERT INTO hallen VALUES(" + zahl + ", '" + halle.halName + "', '" + halle.adrStr + "', '" + halle.adrOrt +"')";
		
		stmt.execute(insert); 
		 
		zahl++; 
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String getHalName() {
		return this.halName;
	}

	public void setHalName(String hallenName) {
		this.halName = hallenName;
	}

	public String getAdrStr() {
		return adrStr;
	}

	public void setAdrStr(String adrStr) {
		this.adrStr = adrStr;
	}

	public String getAdrOrt() {
		return adrOrt;
	}

	public void setAdrOrt(String adrOrt) {
		this.adrOrt = adrOrt;
	}

	public Hallen getHalle() {
		return halle;
	}

	public void setHalle(Hallen halle) {
		this.halle = halle;
	}

	public static int getMaxanzahlhallen() {
		return maxAnzahlHallen;
	}

	public Hallen getHalle(int index) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}	
		try {
			halle = new Hallen(halName, adrStr, adrOrt);
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			query =  "SELECT * FROM hallen WHERE `id`=" + index;
			ResultSet rs = stmt.executeQuery(query);
			
			
			while(rs.next()) {
				halle.setHalName(rs.getString("halName"));
				halle.setAdrStr(rs.getString("adrStr"));
				halle.setAdrOrt(rs.getString("adrOrt"));
			}
			rs.close();
			
		}
		catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return halle;
	}

}
