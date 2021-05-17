package Mannschaften;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import Exceptions.*;

/**
 * In dieser Klasse soll aus den Mannschaften eine Liste erstellt werden. Zuerst
 * können die Mannschaften hinzugefügt werden. Diese werden weiterhin nach ihrem
 * Geschlecht und ihrem Jahrgang beziehungsweise Alter sortiert. Später werden
 * aus der neuen Aufteilung zufällig Gruppen für die Gruppenphase des Turniers
 * erstellt.
 * 
 * @inv das Geschlecht der Mannschaften muss "m" oder "w" sein, sonst kann die
 *      Mannschaft nicht einer Liste zugeordnet werden
 * @inv der Jahrgang der Mannschaften muss "Aktive", "A-Jugend" oder "B-Jugend"
 *      sein, sonst kann die Mannschaft nicht entsprechend zugeordnet werden
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */

public class MannschaftenListe {

	private final static int maxZahlMannschaften = 72; // die maximale Anzahl der teilnehmenden Mannschaften beträgt 72

	private Mannschaft mannschaft;

	private int zahl = 0;
	private int zahl2 = 0;
	private int zahl3 = 0;
	private int zahl4 = 0;
	
	private String name;
	private String jahrgang;
	private String mow;
	

	/**
	 * Konstruktor
	 * 
	 */

	public MannschaftenListe() {
		zahl = 0;
	}

	/**
	 * Mit dieser Methode werden die Mannschaften dem Array hinzugefügt.
	 * 
	 * @param mannschaft
	 * @throws MaxAnzMannschaftenAusnahme, sobald die maximale Anzahl an
	 *                                     zugelassenen Mannschaften überschritten
	 *                                     wird.
	 */

	public void mannschaftHinzufuegen(Mannschaft mannschaft) throws MaxAnzMannschaftenAusnahme {
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
			stmt.execute("DROP TABLE IF EXISTS mannschaften");
			stmt.execute("CREATE TABLE mannschaften (`mannschaftID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, PRIMARY KEY(`mannschaftID`))");
			
		}
		
		if (zahl == maxZahlMannschaften) {
		throw new MaxAnzMannschaftenAusnahme();
		}
		
		String name = mannschaft.getName();
		String mow = mannschaft.getMow();
		String jahrgang = mannschaft.getJahrgang();
 		
		String insert = "INSERT INTO mannschaften VALUES(" + zahl + ", '" + name + "', '" + mow + "', '" + jahrgang +"')";
		
		stmt.execute(insert);
		
		zahl++;
		}
		
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * Mit dieser print-Methode soll die Liste der teilnehmenden Mannschaften
	 * geprintet werden.
	 * 
	 * @pre die Liste darf nicht leer sein
	 * @throws NullPointerAusnahme , wenn die übergebene Liste leer ist.
	 */

	public void print() throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			
			Mannschaft mannschaft = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String countString = "SELECT COUNT(*) AS total FROM handball.mannschaften";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			}
		
		
			System.out.println("teilnehmende Mannschaften:\n");
			
			for (int i = 0; i < count; i++) {
				String query =  "SELECT * FROM handball.mannschaften WHERE mannschaftID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
			
				System.out.println(mannschaft.getName() + ", " + mannschaft.getMow() + ", " + mannschaft.getJahrgang());
				
			
			}
			System.out.println("___________________________________________________");
		}
		
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * In dieser Methode werden die Mannschaften zuerst nach ihrem Geschlecht und
	 * später nach ihrem Alter sortiert. Die neue Sortierung wird zeilenweise in dem
	 * Array "sortliste" gespeichert
	 * 
	 * @pre es müssen 72 Mannschaften angemeldet sein, damit es 12 Gruppen à 3
	 *      Mannschaften geben kann, von den 72 Mannschaften müssen 6 Mannschaften
	 *      Männer Aktive sein, 6 Mannschaften Frauen Aktive, 6 männliche A-Jugend,
	 *      6 männliche B-Jugend, 6 weibliche A-Jugend und 6 weibliche B-Jugend
	 * @post es gibt eine Liste, die die Mannschaften nach Alter und Geschlecht
	 *       sortiert beinhaltet
	 * @throws NullPointerAusnahme , wenn die übergebene liste leer ist.
	 */

	public void sortieren() throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft mannschaft = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String countString = "SELECT COUNT(*) AS total FROM handball.mannschaften";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			 
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme(); 
				}
			
		

		/**
		 * nach männlich und weiblich sortieren 
		 * --> neue Tabellen mannschaftMännlich und mannschaftWeiblich
		 * 
		 */
			
		zahl = zahl2 = zahl3 = 0;	

		for (int i = 0; i < count; i++) {
			String query = "SELECT * FROM handball.mannschaften WHERE mannschaftID=" + i;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				mannschaft.setName(rs.getString("name"));
				mannschaft.setMow(rs.getString("mow"));
				mannschaft.setJahrgang(rs.getString("jahrgang"));
			}
			rs.close();
			
			if (mannschaft.getMow().equals("m")) {
				if (zahl == 0) {
					stmt.execute("DROP TABLE IF EXISTS mannschaftenMännlich");
					stmt.execute("CREATE TABLE mannschaftenMännlich (`männlID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, PRIMARY KEY(`männlID`))");
				}
				
				String name = mannschaft.getName();
				String jahrgang = mannschaft.getJahrgang();
		 		
				String insert = "INSERT INTO mannschaftenMännlich VALUES(" + zahl + ", '" + name + "', 'm', '" + jahrgang +"')";
				
				stmt.execute(insert);
				
				zahl++;

			} else {
				if (zahl2 == 0) {
					stmt.execute("DROP TABLE IF EXISTS mannschaftenWeiblich");
					stmt.execute("CREATE TABLE mannschaftenWeiblich (`weiblID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, PRIMARY KEY(`weiblID`))");
				}
				
				String name = mannschaft.getName();
				String jahrgang = mannschaft.getJahrgang();
		 		
				String insert = "INSERT INTO mannschaftenWeiblich VALUES(" + zahl2 + ", '" + name + "', 'w', '" + jahrgang +"')";
				
				stmt.execute(insert);
				
				zahl2++;
			}
		}
		
		rset.close();

		/**
		 * Männermannschaften nach Alter sortieren
		 * 
		 * drei neu Tabellen männer, männlichA, männlichB
		 */
		zahl = zahl2 = zahl3 = 0;	
		
		countString = "SELECT COUNT(*) AS total FROM handball.mannschaftenMännlich";
		rset = stmt.executeQuery(countString);
		
		count = 0;
		
		while(rset.next()){
		    count = rset.getInt("total");
		    if (count == 0) {
				throw new NullPointerAusnahme();
			}
		

		for (int i = 0; i < count; i++) {
			
			String query = "SELECT * FROM handball.mannschaftenMännlich WHERE männlID=" + i;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				mannschaft.setName(rs.getString("name"));
				mannschaft.setMow(rs.getString("mow"));
				mannschaft.setJahrgang(rs.getString("jahrgang"));
			}
			rs.close();
			
			if (mannschaft.getJahrgang().equals("Aktive")) {
				
				if (zahl == 0) {
					stmt.execute("DROP TABLE IF EXISTS männer");
					stmt.execute("CREATE TABLE männer (`männerID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, PRIMARY KEY(`männerID`))");
				}
				
				String name = mannschaft.getName();
		 		
				String insert = "INSERT INTO männer VALUES(" + zahl + ", '" + name + "', 'm', 'Aktive')";
				
				stmt.execute(insert);
				
				zahl++;
				
			} else if (mannschaft.getJahrgang().equals("A-Jugend")) {
				
				if (zahl2 == 0) {
					stmt.execute("DROP TABLE IF EXISTS männlichA");
					stmt.execute("CREATE TABLE männlichA (`männlichAID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, PRIMARY KEY(`männlichAID`))");
				}
				
				String name = mannschaft.getName();
		 		
				String insert = "INSERT INTO männlichA VALUES(" + zahl2 + ", '" + name + "', 'm', 'A-Jugend')";
				
				stmt.execute(insert);
				
				zahl2++;
				
			} else {
				
				if (zahl3 == 0) {
					stmt.execute("DROP TABLE IF EXISTS männlichB");
					stmt.execute("CREATE TABLE männlichB (`männlichBID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, PRIMARY KEY(`männlichBID`))");
				}
				
				String name = mannschaft.getName();
		 		
				String insert = "INSERT INTO männlichB VALUES(" + zahl3 + ", '" + name + "', 'm', 'B-Jugend')";
				
				stmt.execute(insert);
				
				zahl3++;
			}
		
		} 
		 rset.close();

		/**
		 * Frauenmannschaften nach Alter sortieren
		 * 
		 * 
		 * drei neu Tabellen frauen, weiblichA, weiblichB
		 */

		zahl = zahl2 = zahl3 = 0;	

		countString = "SELECT COUNT(*) AS total FROM handball.mannschaftenWeiblich";
		rset = stmt.executeQuery(countString);
		
		count = 0;
		
		while(rset.next()){
		    count = rset.getInt("total");
		    if (count == 0) {
				throw new NullPointerAusnahme();
			}
		

		for (int i = 0; i < count; i++) {
			
			String query = "SELECT * FROM handball.mannschaftenWeiblich WHERE weiblID=" + i;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				mannschaft.setName(rs.getString("name"));
				mannschaft.setMow(rs.getString("mow"));
				mannschaft.setJahrgang(rs.getString("jahrgang"));
			}
			rs.close();
			
			if (mannschaft.getJahrgang().equals("Aktive")) {
				
				if (zahl == 0) {
					stmt.execute("DROP TABLE IF EXISTS frauen");
					stmt.execute("CREATE TABLE frauen (`frauenID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, PRIMARY KEY(`frauenID`))");
				}
				
				String name = mannschaft.getName();
		 		
				String insert = "INSERT INTO frauen VALUES(" + zahl + ", '" + name + "', 'w', 'Aktive')";
				
				stmt.execute(insert);
				
				zahl++;
				
			} else if (mannschaft.getJahrgang().equals("A-Jugend")) {
				
				if (zahl2 == 0) {
					stmt.execute("DROP TABLE IF EXISTS weiblichA");
					stmt.execute("CREATE TABLE weiblichA (`weiblichAID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, PRIMARY KEY(`weiblichAID`))");
				}
				
				String name = mannschaft.getName();
		 		
				String insert = "INSERT INTO weiblichA VALUES(" + zahl2 + ", '" + name + "', 'w', 'A-Jugend')";
				
				stmt.execute(insert);
				
				zahl2++;
				
			} else {
				
				if (zahl3 == 0) {
					stmt.execute("DROP TABLE IF EXISTS weiblichB");
					stmt.execute("CREATE TABLE weiblichB (`weiblichBID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, PRIMARY KEY(`weiblichBID`))");
				}
				
				String name = mannschaft.getName();
		 		
				String insert = "INSERT INTO weiblichB VALUES(" + zahl3 + ", '" + name + "', 'w', 'B-Jugend')";
				
				stmt.execute(insert);
				
				zahl3++;
			}

		}
		 rset.close();
		}}	}
	}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 *
	 * @param index,  Zeile
	 * @param index2, Spalte
	 * @return das Geschlecht einer Mannschaft an einer bestimmten Stelle im Array
	 */

	public String getMannschaftMoW(int index) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft mannschaft = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String query = "SELECT * FROM handball.mannschaften WHERE mannschaftID=" + index;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				mannschaft.setName(rs.getString("name"));
				mannschaft.setMow(rs.getString("mow"));
				mannschaft.setJahrgang(rs.getString("jahrgang"));
			}
			rs.close();
		}
			
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return mannschaft.getMow();
	}

	/**
	 * In dieser Methode werden nun pro Jahrgang und Geschlecht jeweils 4 Gruppen
	 * erstellt. Die Zuteilung erfolgt zufällig. Zu Ende dieser Methode haben wir
	 * also insgesamt 24 Gruppen mit jeweils 3 Mannschaften. Wir haben 4 Gruppen für
	 * die Männermannschaften, 4 für die männliche A-Jugend, 4 für die männliche
	 * B-Jugend & noch einmal genauso viele für die Frauen. Die Gruppen werden in
	 * dem neuen Array "gruppenliste" gespeichert.
	 * 
	 * @pre es werden jeweils 6 Mannschaften jedes Jahrgangs eines jeden Geschlechts
	 *      benötigt
	 * @post es gibt pro Jahrgang eines Geschlechts vier Gruppen à 3 Mannschaften
	 * @throws NullPointerAusnahme, wenn das übergebene Feld sortlist leer ist.
	 */

	public void gruppenErstellen() throws NullPointerAusnahme {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		
		try {
			Mannschaft mannschaft = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			/*
			 * Männermannschaften
			 */
			
			String countString = "SELECT COUNT(*) AS total FROM handball.männer";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			/*
			 * vier neue Tabellen: Männer Gruppe 1-4
			 * MG1, MG2, MG3, MG4
			 */
			
			zahl = zahl2 = zahl3 = zahl4 = 0;
			
			/*
			 * Gruppe 1
			 */
			
			for(int i = 0; i < count/4; i++) {
				if (zahl == 0) {
					stmt.execute("DROP TABLE IF EXISTS MG1");
					stmt.execute("CREATE TABLE MG1 (`MG1ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MG1ID`))");
				}
				
				String query = "SELECT * FROM handball.männer WHERE männerID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MG1 VALUES(" + zahl + ", '" + name + "', 'm', 'Aktive', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl++;
			}
			
			
			/*
			 * Gruppe 2
			 */
			
			for(int i = 3; i < count/4+3; i++) {
				if (zahl2 == 0) {
					stmt.execute("DROP TABLE IF EXISTS MG2");
					stmt.execute("CREATE TABLE MG2 (`MG2ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MG2ID`))");
				}
				
				String query = "SELECT * FROM handball.männer WHERE männerID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MG2 VALUES(" + zahl2 + ", '" + name + "', 'm', 'Aktive', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl2++;
			}
			
			/*
			 * Gruppe 3
			 */
			
			for(int i = 6; i < count/4+6; i++) {
				if (zahl3 == 0) {
					stmt.execute("DROP TABLE IF EXISTS MG3");
					stmt.execute("CREATE TABLE MG3 (`MG3ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MG3ID`))");
				}				
				
				String query = "SELECT * FROM handball.männer WHERE männerID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MG3 VALUES(" + zahl3 + ", '" + name + "', 'm', 'Aktive', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl3++;
			}
			
			/*
			 * Gruppe 4
			 */
			
			for(int i = 9; i < count/4+9; i++) {
				if (zahl4 == 0) {
					stmt.execute("DROP TABLE IF EXISTS MG4");
					stmt.execute("CREATE TABLE MG4 (`MG4ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MG4ID`))");
				}
				
				String query = "SELECT * FROM handball.männer WHERE männerID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MG4 VALUES(" + zahl4 + ", '" + name + "', 'm', 'Aktive', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl4++;
			}
			rset.close();
			
			/*
			 * Frauenmannschaften
			 */
			
			countString = "SELECT COUNT(*) AS total FROM handball.frauen";
			rset = stmt.executeQuery(countString);
			
			count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			/*
			 * vier neue Tabellen: Frauen Gruppe 1-4
			 * FG1, FG2, FG3, FG4
			 */
			
			zahl = zahl2 = zahl3 = zahl4 = 0;
			
			/*
			 * Gruppe 1
			 */
			
			for(int i = 0; i < count/4; i++) {
				if (zahl == 0) {
					stmt.execute("DROP TABLE IF EXISTS FG1");
					stmt.execute("CREATE TABLE FG1 (`FG1ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`FG1ID`))");
				}
				
				String query = "SELECT * FROM handball.frauen WHERE frauenID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO FG1 VALUES(" + zahl + ", '" + name + "', 'w', 'Aktive', 0, 0, 0)";
				stmt.execute(insert);
				
				String delete = "DELETE FROM handball.frauen WHERE frauenID=" + i;
				stmt.execute(delete);
				
				zahl++;
			}
			
			/*
			 * Gruppe 2
			 */
			
			for(int i = 3; i < count/4+3; i++) {
				if (zahl2 == 0) {
					stmt.execute("DROP TABLE IF EXISTS FG2");
					stmt.execute("CREATE TABLE FG2 (`FG2ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`FG2ID`))");
				}
				
				String query = "SELECT * FROM handball.frauen WHERE frauenID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO FG2 VALUES(" + zahl2 + ", '" + name + "', 'w', 'Aktive', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl2++;
			}
			
			/*
			 * Gruppe 3
			 */
			
			for(int i = 6; i < count/4+6; i++) {
				if (zahl3 == 0) {
					stmt.execute("DROP TABLE IF EXISTS FG3");
					stmt.execute("CREATE TABLE FG3 (`FG3ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`FG3ID`))");
				}
				
				String query = "SELECT * FROM handball.frauen WHERE frauenID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO FG3 VALUES(" + zahl3 + ", '" + name + "', 'w', 'Aktive', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl3++;
			}
			
			/*
			 * Gruppe 4
			 */
			
			for(int i = 9; i < count/4+9; i++) {
				if (zahl4 == 0) {
					stmt.execute("DROP TABLE IF EXISTS FG4");
					stmt.execute("CREATE TABLE FG4 (`FG4ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`FG4ID`))");
				}
				
				String query = "SELECT * FROM handball.frauen WHERE frauenID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO FG4 VALUES(" + zahl4 + ", '" + name + "', 'w', 'Aktive', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl4++;
			}
			rset.close();
			
			/*
			 * männliche A-Jugend
			 */
			
			countString = "SELECT COUNT(*) AS total FROM handball.männlichA";
			rset = stmt.executeQuery(countString);
			
			count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			/*
			 * vier neue Tabellen: männliche A-Jugend Gruppe 1-4
			 * MAG1, MAG2, MAG3, MAG4
			 */
			
			zahl = zahl2 = zahl3 = zahl4 = 0;
			
			/*
			 * Gruppe 1
			 */
			
			for(int i = 0; i < count/4; i++) {
				if (zahl == 0) {
					stmt.execute("DROP TABLE IF EXISTS MAG1");
					stmt.execute("CREATE TABLE MAG1 (`MAG1ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MAG1ID`))");
				}
				
				String query = "SELECT * FROM handball.männlichA WHERE männlichAID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MAG1 VALUES(" + zahl + ", '" + name + "', 'm', 'A-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl++;
			}
			
			/*
			 * Gruppe 2
			 */
			
			for(int i = 3; i < count/4+3; i++) {
				if (zahl2 == 0) {
					stmt.execute("DROP TABLE IF EXISTS MAG2");
					stmt.execute("CREATE TABLE MAG2 (`MAG2ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MAG2ID`))");
				}
				
				String query = "SELECT * FROM handball.männlichA WHERE männlichAID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MAG2 VALUES(" + zahl2 + ", '" + name + "', 'm', 'A-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl2++;
			}
			
			/*
			 * Gruppe 3
			 */
			
			for(int i = 6; i < count/4+6; i++) {
				if (zahl3 == 0) {
					stmt.execute("DROP TABLE IF EXISTS MAG3");
					stmt.execute("CREATE TABLE MAG3 (`MAG3ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MAG3ID`))");
				}
				
				String query = "SELECT * FROM handball.männlichA WHERE männlichAID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MAG3 VALUES(" + zahl3 + ", '" + name + "', 'm', 'A-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl3++;
			}
			
			/*
			 * Gruppe 4
			 */
			
			for(int i = 9; i < count/4+9; i++) {
				if (zahl4 == 0) {
					stmt.execute("DROP TABLE IF EXISTS MAG4");
					stmt.execute("CREATE TABLE MAG4 (`MAG4ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MAG4ID`))");
				}
				
				String query = "SELECT * FROM handball.männlichA WHERE männlichAID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MAG4 VALUES(" + zahl4 + ", '" + name + "', 'm', 'A-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl4++;
			}
			rset.close();
			
			/*
			 * männliche B-Jugend
			 */
			
			countString = "SELECT COUNT(*) AS total FROM handball.männlichB";
			rset = stmt.executeQuery(countString);
			
			count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			/*
			 * vier neue Tabellen: männliche B-Jugend Gruppe 1-4
			 * MBG1, MBG2, MBG3, MBG4
			 */
			
			zahl = zahl2 = zahl3 = zahl4 = 0;
			
			/*
			 * Gruppe 1
			 */
			
			for(int i = 0; i < count/4; i++) {
				if (zahl == 0) {
					stmt.execute("DROP TABLE IF EXISTS MBG1");
					stmt.execute("CREATE TABLE MBG1 (`MBG1ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MBG1ID`))");
				}
				
				String query = "SELECT * FROM handball.männlichB WHERE männlichBID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MBG1 VALUES(" + zahl + ", '" + name + "', 'm', 'B-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl++;
			}
			
			/*
			 * Gruppe 2
			 */
			
			for(int i = 3; i < count/4+3; i++) {
				if (zahl2 == 0) {
					stmt.execute("DROP TABLE IF EXISTS MBG2");
					stmt.execute("CREATE TABLE MBG2 (`MBG2ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MBG2ID`))");
				}
				
				String query = "SELECT * FROM handball.männlichB WHERE männlichBID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MBG2 VALUES(" + zahl2 + ", '" + name + "', 'm', 'B-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl2++;
			}
			
			/*
			 * Gruppe 3
			 */
			
			for(int i = 6; i < count/4+6; i++) {
				if (zahl3 == 0) {
					stmt.execute("DROP TABLE IF EXISTS MBG3");
					stmt.execute("CREATE TABLE MBG3 (`MBG3ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MBG3ID`))");
				}
				
				String query = "SELECT * FROM handball.männlichB WHERE männlichBID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MBG3 VALUES(" + zahl3 + ", '" + name + "', 'm', 'B-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl3++;
			}
			
			/*
			 * Gruppe 4
			 */
			
			for(int i = 9; i < count/4+9; i++) {
				if (zahl4 == 0) {
					stmt.execute("DROP TABLE IF EXISTS MBG4");
					stmt.execute("CREATE TABLE MBG4 (`MBG4ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`MBG4ID`))");
				}
				
				String query = "SELECT * FROM handball.männlichB WHERE männlichBID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO MBG4 VALUES(" + zahl4 + ", '" + name + "', 'm', 'B-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl4++;
			}
			rset.close();
			
			/*
			 * weibliche A-Jugend
			 */
			
			countString = "SELECT COUNT(*) AS total FROM handball.weiblichA";
			rset = stmt.executeQuery(countString);
			
			count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			
			/*
			 * vier neue Tabellen: weibliche A-Jugend Gruppe 1-4
			 * WAG1, WAG2, WAG3, WAG4
			 */
			
			zahl = zahl2 = zahl3 = zahl4 = 0;
			
			/*
			 * Gruppe 1
			 */
			
			for(int i = 0; i < count/4; i++) {
				if (zahl == 0) {
					stmt.execute("DROP TABLE IF EXISTS WAG1");
					stmt.execute("CREATE TABLE WAG1 (`WAG1ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`WAG1ID`))");
				}
				
				String query = "SELECT * FROM handball.weiblichA WHERE weiblichAID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO WAG1 VALUES(" + zahl + ", '" + name + "', 'w', 'A-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl++;
			}
			
			/*
			 * Gruppe 2
			 */
			
			for(int i = 3; i < count/4+3; i++) {
				if (zahl2 == 0) {
					stmt.execute("DROP TABLE IF EXISTS WAG2");
					stmt.execute("CREATE TABLE WAG2 (`WAG2ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`WAG2ID`))");
				}
				
				String query = "SELECT * FROM handball.weiblichA WHERE weiblichAID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO WAG2 VALUES(" + zahl2 + ", '" + name + "', 'w', 'A-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl2++;
			}
			
			/*
			 * Gruppe 3
			 */
			
			for(int i = 6; i < count/4+6; i++) {
				if (zahl3 == 0) {
					stmt.execute("DROP TABLE IF EXISTS WAG3");
					stmt.execute("CREATE TABLE WAG3 (`WAG3ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`WAG3ID`))");
				}
				
				String query = "SELECT * FROM handball.weiblichA WHERE weiblichAID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO WAG3 VALUES(" + zahl3 + ", '" + name + "', 'w', 'A-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl3++;
			}
			
			/*
			 * Gruppe 4
			 */
			
			for(int i = 9; i < count/4+9; i++) {
				if (zahl4 == 0) {
					stmt.execute("DROP TABLE IF EXISTS WAG4");
					stmt.execute("CREATE TABLE WAG4 (`WAG4ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`WAG4ID`))");
				}
				
				String query = "SELECT * FROM handball.weiblichA WHERE weiblichAID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO WAG4 VALUES(" + zahl4 + ", '" + name + "', 'w', 'A-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl4++;
			}
			rset.close();
			
			/*
			 * weibliche B-Jugend
			 */
			
			countString = "SELECT COUNT(*) AS total FROM handball.weiblichB";
			rset = stmt.executeQuery(countString);
			
			count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			/*
			 * vier neue Tabellen: weibliche B-Jugend Gruppe 1-4
			 * WBG1, WBG2, WBG3, WBG4
			 */
			
			zahl = zahl2 = zahl3 = zahl4 = 0;
			
			/*
			 * Gruppe 1
			 */
			
			for(int i = 0; i < count/4; i++) {
				if (zahl == 0) {
					stmt.execute("DROP TABLE IF EXISTS WBG1");
					stmt.execute("CREATE TABLE WBG1 (`WBG1ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`WBG1ID`))");
				}
				
				String query = "SELECT * FROM handball.weiblichB WHERE weiblichBID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO WBG1 VALUES(" + zahl + ", '" + name + "', 'w', 'B-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl++;
			}
			
			/*
			 * Gruppe 2
			 */
			
			for(int i = 3; i < count/4+3; i++) {
				if (zahl2 == 0) {
					stmt.execute("DROP TABLE IF EXISTS WBG2");
					stmt.execute("CREATE TABLE WBG2 (`WBG2ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`WBG2ID`))");
				}
				
				String query = "SELECT * FROM handball.weiblichB WHERE weiblichBID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO WBG2 VALUES(" + zahl2 + ", '" + name + "', 'w', 'B-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl2++;
			}
			
			/*
			 * Gruppe 3
			 */
			
			for(int i = 6; i < count/4+6; i++) {
				if (zahl3 == 0) {
					stmt.execute("DROP TABLE IF EXISTS WBG3");
					stmt.execute("CREATE TABLE WBG3 (`WBG3ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`WBG3ID`))");
				}
				
				String query = "SELECT * FROM handball.weiblichB WHERE weiblichBID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO WBG3 VALUES(" + zahl3 + ", '" + name + "', 'w', 'B-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl3++;
			}
			
			/*
			 * Gruppe 4
			 */
			
			for(int i = 9; i < count/4+9; i++) {
				if (zahl4 == 0) {
					stmt.execute("DROP TABLE IF EXISTS WBG4");
					stmt.execute("CREATE TABLE WBG4 (`WBG4ID` int NOT NULL, `name` varchar(50) NOT NULL, `mow` varchar(5) NOT NULL,`jahrgang` varchar(20) NOT NULL, `punkte` int NOT NULL, `tore` int NOT NULL, `random` int NOT NULL, PRIMARY KEY(`WBG4ID`))");
				}
				
				String query = "SELECT * FROM handball.weiblichB WHERE weiblichBID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					mannschaft.setName(rs.getString("name"));
					mannschaft.setMow(rs.getString("mow"));
					mannschaft.setJahrgang(rs.getString("jahrgang"));
				}
				rs.close();
				
				String name = mannschaft.getName();
				
				String insert = "INSERT INTO WBG4 VALUES(" + zahl4 + ", '" + name + "', 'w', 'B-Jugend', 0, 0, 0)";
				stmt.execute(insert);
				
				zahl4++;
			}
			}
		}}}}}}
		catch (Exception e) {
			// TODO: handle exception
		}

		


	}

	/**
	 * Mit dieser print-Methode soll das zweidimensionale "gruppenliste"-Array
	 * ausgegeben werden.
	 * 
	 * @pre die Gruppenliste darf nicht leer sein
	 * @throws NullPointerAusnahme, wenn das übergebene Feld gruppenliste leer ist.
	 */

	public void printGruppenliste() throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft mannschaft = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String countString = "SELECT COUNT(*) AS total FROM handball.mannschaften";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			}
			
			System.out.println("\n\nGruppeneinteilung:");
			System.out.println("--------------------------------");
			
			System.out.println("\nMänner:");
			
			countString = "SELECT COUNT(*) AS total FROM handball.MG1";
			rset = stmt.executeQuery(countString);
			
			int counter1 = 0;
			
			while(rset.next()){
			    counter1 = rset.getInt("total");
			    if (counter1 == 0) {
					throw new NullPointerAusnahme();
				}			
			
			for(int j = 1; j <= 4; j++) {
				System.out.println("\nGruppe " + j + ":\n");
				for(int i = 0; i < counter1; i++) {
					
					String query = "SELECT * FROM handball.MG" + j + " WHERE MG" + j + "ID=" + i;
					ResultSet rs = stmt.executeQuery(query);
					
					while(rs.next()) {
						mannschaft.setName(rs.getString("name"));
					}
					rs.close();
					
					String name = mannschaft.getName();
					
					System.out.println((i+1) + ". " + name);
				}
			}
			rset.close();
			
			System.out.println("\nFrauen:");
			
			countString = "SELECT COUNT(*) AS total FROM handball.FG1";
			rset = stmt.executeQuery(countString);
			
			int counter2 = 0;
			
			while(rset.next()){
			    counter2 = rset.getInt("total");
			    if (counter2 == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			for(int j = 1; j <= 4; j++) {
				System.out.println("\nGruppe " + j + ":\n");
				for(int i = 0; i < counter2; i++) {
					
					String query = "SELECT * FROM handball.FG" + j + " WHERE FG" + j + "ID=" + i;
					ResultSet rs = stmt.executeQuery(query);
					
					while(rs.next()) {
						mannschaft.setName(rs.getString("name"));
					}
					rs.close();
					
					String name = mannschaft.getName();
					
					System.out.println((i+1) + ". " + name);
				}
			}
			rset.close();
			
			System.out.println("\nMännliche A-Jugend:");
			
			countString = "SELECT COUNT(*) AS total FROM handball.MAG1";
			rset = stmt.executeQuery(countString);
			
			int counter3 = 0;
			
			while(rset.next()){
			    counter3 = rset.getInt("total");
			    if (counter3 == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			for(int j = 1; j <= 4; j++) {
				System.out.println("\nGruppe " + j + ":\n");
				for(int i = 0; i < counter3; i++) {
					
					String query = "SELECT * FROM handball.MAG" + j + " WHERE MAG" + j + "ID=" + i;
					ResultSet rs = stmt.executeQuery(query);
					
					while(rs.next()) {
						mannschaft.setName(rs.getString("name"));
					}
					rs.close();
					
					String name = mannschaft.getName();
					
					System.out.println((i+1) + ". " + name);
				}
			}
			rset.close();
			
			System.out.println("\nMännliche B-Jugend:");
			
			countString = "SELECT COUNT(*) AS total FROM handball.MBG1";
			rset = stmt.executeQuery(countString);
			
			int counter4 = 0;
			
			while(rset.next()){
			    counter4 = rset.getInt("total");
			    if (counter4 == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			for(int j = 1; j <= 4; j++) {
				System.out.println("\nGruppe " + j + ":\n");
				for(int i = 0; i < counter4; i++) {
					
					String query = "SELECT * FROM handball.MBG" + j + " WHERE MBG" + j + "ID=" + i;
					ResultSet rs = stmt.executeQuery(query);
					
					while(rs.next()) {
						mannschaft.setName(rs.getString("name"));
					}
					rs.close();
					
					String name = mannschaft.getName();
					
					System.out.println((i+1) + ". " + name);
				}
			}
			rset.close();
			
			System.out.println("\nWeibliche A-Jugend:");
			
			countString = "SELECT COUNT(*) AS total FROM handball.WAG1";
			rset = stmt.executeQuery(countString);
			
			int counter5 = 0;
			
			while(rset.next()){
			    counter5 = rset.getInt("total");
			    if (counter5 == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			for(int j = 1; j <= 4; j++) {
				System.out.println("\nGruppe " + j + ":\n");
				for(int i = 0; i < counter5; i++) {
					
					String query = "SELECT * FROM handball.WAG" + j + " WHERE WAG" + j + "ID=" + i;
					ResultSet rs = stmt.executeQuery(query);
					
					while(rs.next()) {
						mannschaft.setName(rs.getString("name"));
					}
					rs.close();
					
					String name = mannschaft.getName();
					
					System.out.println((i+1) + ". " + name);
				}
			}
			rset.close();
			
			System.out.println("\nWeibliche B-Jugend:");
			
			countString = "SELECT COUNT(*) AS total FROM handball.WBG1";
			rset = stmt.executeQuery(countString);
			
			int counter6 = 0;
			
			while(rset.next()){
			    counter6 = rset.getInt("total");
			    if (counter6 == 0) {
					throw new NullPointerAusnahme();
				}
			
			
			for(int j = 1; j <= 4; j++) {
				System.out.println("\nGruppe " + j + ":\n");
				for(int i = 0; i < counter6; i++) {
					
					String query = "SELECT * FROM handball.WBG" + j + " WHERE WBG" + j + "ID=" + i;
					ResultSet rs = stmt.executeQuery(query);
					
					while(rs.next()) {
						mannschaft.setName(rs.getString("name"));
					}
					rs.close();
					
					String name = mannschaft.getName();
					
					System.out.println((i+1) + ". " + name);
				}
			}
			rset.close();
			System.out.println("____________________");
		
		}}}}}}}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static int getMaxzahlmannschaften() {
		return maxZahlMannschaften;
	}

	public int getZahl() {
		return zahl;
	}
	
	public int getZahl2() {
		return zahl2;
	}
	
	public int getZahl3() {
		return zahl3;
	}
	
	public int getZahl4() {
		return zahl4;
	}
}
