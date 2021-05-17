package Zuordnung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import Exceptions.NullPointerAusnahme;

/**
 * In dieser Klasse werden die Hallen und Personen einander zugeteilt.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */
public class Zuordnung {

	private String name;
	private String job;
	private String halleName;
	private String halleAdrOrt;
	private String halleAdrStr;
	
	private String zeitnehmer1, zeitnehmer2;
	private String schiedsrichter1, schiedsrichter2;

	private Hallen halle;

	private PersonenZuordnen[][] person;

	/**
	 * 
	 * @param name
	 * @param job
	 * @param halleName
	 * @param halleAdrOrt
	 * @param halleAdrStr
	 */
	public Zuordnung(String name, String job, String halleName, String halleAdrOrt, String halleAdrStr) {
		this.setName(name);
		this.setJob(job);
		this.setHalleName(halleName);
		this.setHalleAdrOrt(halleAdrOrt);
		this.setHalleAdrStr(halleAdrStr);
	}

	/**
	 * Mit diesem setter werden die Hallen in das neue Array zuordnung gespeichert
	 * 
	 * @pre die Hallenliste darf nicht leer sein
	 * @param hallen
	 * @param halleIndex
	 * @throws NullPointerAusnahme
	 */
	public void setHallenListe(Hallen hallen, int halleIndex) throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e); 
		}
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
		 
			if (halleIndex == 0) {
				stmt.execute("DROP TABLE IF EXISTS zuordnung");
				stmt.execute("CREATE TABLE zuordnung (`zuordnungID` int NOT NULL, `halle` varchar(50) NOT NULL, `schiedsrichter1` varchar(50) NOT NULL, `schiedsrichter2` varchar(50) NOT NULL, `zeitnehmer1` varchar(50) NOT NULL, `zeitnehmer2` varchar(50) NOT NULL, PRIMARY KEY(`zuordnungID`))");
			}
			
			String countString = "SELECT COUNT(*) AS total FROM hallen";
			ResultSet rset = stmt.executeQuery(countString);
			
			while(rset.next()){
			    int count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			
			String insert = "INSERT INTO zuordnung VALUES(" + halleIndex + ", '" + hallen.getHalle(halleIndex).getHalName() + "', 'schiri1', 'schiri2', 'zeit1', 'zeit2')";
			stmt.execute(insert);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
			
		
	}

	/**
	 * Mit diesem setter werden die Personen in das Array zuordnung gespeichert
	 * 
	 * @pre die Personenliste darf nicht leer sein
	 * @param person
	 * @param personIndex
	 * @throws NullPointerAusnahme
	 */
	public void setPersonenListe(PersonenZuordnen person, int personIndex) throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String countString = "SELECT COUNT(*) AS total FROM menschen";
			ResultSet rset = stmt.executeQuery(countString);
			
			while(rset.next()){
			    int count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				} 
			} rset.close();
			
			String insert = "UPDATE zuordnung SET `schiedsrichter1` = '"+ person.getMensch(personIndex*4).getName() + "' WHERE zuordnungID=" + personIndex;
			stmt.execute(insert);
		
			insert = "UPDATE zuordnung SET `schiedsrichter2` = '" + person.getMensch(personIndex*4+2).getName() + "' WHERE zuordnungID=" + personIndex;
			stmt.execute(insert);

			insert = "UPDATE zuordnung SET `zeitnehmer1` = '" + person.getMensch(personIndex*4+1).getName() + "' WHERE zuordnungID=" + personIndex;
			stmt.execute(insert);

			insert = "UPDATE zuordnung SET `zeitnehmer2` = '" + person.getMensch(personIndex*4+3).getName() + "' WHERE zuordnungID=" + personIndex;
			stmt.execute(insert);
		
				
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * Mit dieser Methode wird die Zuordnung von Hallen und Personen ausgegeben.
	 * 
	 * @pre die Liste darf nicht leer sein
	 * @throws NullPointerAusnahme, wenn die übergebene Zuordnung leer ist.
	 */

	public void printListe() throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
		
			String countString = "SELECT COUNT(*) AS total FROM zuordnung";
			ResultSet rset = stmt.executeQuery(countString);
			
			while(rset.next()){
			    int count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			
			for (int j = 0; j < 6; j++) {
					
				String query =  "SELECT * FROM handball.zuordnung WHERE zuordnungID=" + j;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					halleName = rs.getString("halle");
					zeitnehmer1 = rs.getString("zeitnehmer1");
					zeitnehmer2 = rs.getString("zeitnehmer2");
					schiedsrichter1 = rs.getString("schiedsrichter1");
					schiedsrichter2 = rs.getString("schiedsrichter2");
				}
				rs.close();
			
				System.out.println("\nHalle " + (j + 1) + ": " + halleName);
				System.out.println("Schiedsrichter 1: " + schiedsrichter1);
				System.out.println("Schiedsrichter 2: " + schiedsrichter2);
				System.out.println("Zeitnehmer 1: " + zeitnehmer1);
				System.out.println("Zeitnehmer 2: " + zeitnehmer2 + "\n");
			}
			System.out.println("____________________________________________\n");
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
			
		
	}

	public Hallen getHallen() {
		return halle;
	}

	public void setHallen(Hallen halle) {
		this.halle = halle;
	}

	public PersonenZuordnen[][] getPerson() {
		return person;
	}

	public void setPerson(PersonenZuordnen[][] person) {
		this.person = person;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getHalleName() {
		return halleName;
	}

	public void setHalleName(String hallenName) {
		this.halleName = hallenName;
	}

	public String getHalleAdrOrt() {
		return halleAdrOrt;
	}

	public void setHalleAdrOrt(String halleAdrOrt) {
		this.halleAdrOrt = halleAdrOrt;
	}

	public String getHalleAdrStr() {
		return halleAdrStr;
	}

	public void setHalleAdrStr(String halleAdrStr) {
		this.halleAdrStr = halleAdrStr;
	}

}
