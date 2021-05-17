package Zuordnung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import Exceptions.maxAnzPersonenAusnahme;

/**
 * Mit dieser Klasse sollen die Schiedsrichter und Zeitnehmer den Hallen
 * zugeordnet werden. Es gibt immer zwei Zeitnehmer und zwei Schiedsrichter pro
 * Halle
 * 
 * @author emmeliebeitlich
 * @version 1.0
 */

public class PersonenZuordnen {

	private String name;
	private String job; // zeitnehmer oder schiedsrichter

	private Mensch mensch;

	private final static int maxAnzahlPers = 24;
	private int zahl;

	/**
	 * 
	 * @param name
	 * @param job
	 */
	public PersonenZuordnen(String name, String job) {
		this.setName(name);
		this.setJob(job);

	}

	/**
	 * Mit dieser Methode sollen Personen hinzugefügt werden
	 * 
	 * @param person,  ein Schiedsrichter
	 * @param person2, ein Zeitnehmer
	 * @pre die maximale Anzahl darf nicht überschritten werden
	 * @post es gibt 12 Zeitnehmer und 12 Schiedsrichter, jeweils zwei pro Halle
	 * @throws maxAnzPersonenAusnahme, wenn die Anzahl der maximal benötigten
	 *         Personen überschritten wurde
	 */

	public void personenHinzufuegen(Schiedsrichter person, Zeitnehmer person2) throws maxAnzPersonenAusnahme {
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
		
		if (zahl == 0) { 
			stmt.execute("DROP TABLE IF EXISTS menschen");
			stmt.execute("CREATE TABLE menschen (`menschID` int NOT NULL, `name` varchar(50) NOT NULL, `job` varchar(50) NOT NULL, PRIMARY KEY(`menschID`))");
		}
		
		if (zahl == maxAnzahlPers) {
			throw new maxAnzPersonenAusnahme();
		}
		
		if (person.getJob().equals("schiedsrichter")) {
			String name = person.getName();
			String job = person.getJob();
			String insert = "INSERT INTO menschen VALUES(" + zahl + ", '" + name + "', '" + job + "')";
			stmt.execute(insert);	
			zahl++;
		}
		
		if (person2.getJob().equals("zeitnehmer")) {
			String name = person2.getName();
			String job = person2.getJob();
			String insert = "INSERT INTO menschen VALUES(" + zahl + ", '" + name + "', '" + job + "')";
			stmt.execute(insert);	
			zahl++;
		}
		
		}
		
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	public String getPesonenJob(int index) {
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
			
			String query =  "SELECT * FROM handball.menschen WHERE menschID=" + index;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
			job = rs.getString("job");
			}
			rs.close();
			
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return job;
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

	public Mensch getMensch(int index) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			mensch = new Mensch(name, job) {
			};
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String query =  "SELECT * FROM handball.menschen WHERE menschID=" + index;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
			mensch.setName(rs.getString("name"));
			mensch.setJob(rs.getString("job"));
			}
			rs.close();
			
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return mensch;
	}

}
