package Spielphase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


import Exceptions.NullPointerAusnahme;
import Mannschaften.*;

/**
 * In dieser Klasse wird der Spielplan erstellt. Zuerst werden die Spiele
 * innerhalb der Gruppen geplant. Die Spiele dauern 20 min, zwischen den Spielen
 * gibt es 5 min Pause. Gruppe 1 bis 4 eines Jahrgangs und eines Geschlechts
 * spielen immer abwechselnd, die anderen Gruppen des anderen Geschlechts und
 * der anderen Jahgänge spielen parallel dazu, es finden somit immer 6 Spiele
 * gleichzeitig statt.
 * 
 * Für eine Sieg in einem Spiel bekommt man 2 Punkte, bei einem unentschieden
 * bekommt man einen Punkt, verliert man, bekommt man keinen. Aus den Punkten
 * wird später die Tabelle errechnet.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */

public class Spiele {

	private int h, h2, min, min2;
	private int tore1, tore2, tore3;
	private Mannschaft man1, man2;
	private int punkte1, punkte2, punkte3;

	private TabelleErstellen tabelle;
	private Spiele s;

	private String name1;
	private String name2;
	private String name3;
	private String gruppe;
	private String gruppe1;
	private String gruppe2;
	
	private String name;
	private String jahrgang;
	private String mow;
	
	// TODO Boolean spielplanerstellen nur wenn SetMannschaftsliste gestartet wurde

	/**
	 * Konstruktor
	 * 
	 * @param h       Uhrzeit - Stunden
	 * @param min     Uhrzeit - Minuten
	 * @param man1    erstgenannte Mannschaft in einem Spiel
	 * @param man2    zweitgenannte Mannschaft in einem Spiel
	 * @param tore1   erzielte Tore der erstgenannten Mannschaft
	 * @param tore2   erzielte Tore der zweitgenannten Mannschaft
	 * @param punkte1 aus den Ergebnissen berechneten Punkte der ersten Mannschaft
	 *                der Gruppe
	 * @param punkte2 aus den Ergebnissen berechnete Punkte der zweiten Mannschaft
	 *                der Gruppe
	 * @param punkte3 aus den Ergebnissen berechnete Punkte der dritten Mannschaft
	 *                der Gruppe
	 */

	public Spiele(int h, int min, Mannschaft man1, Mannschaft man2, int tore1, int tore2, int punkte1, int punkte2,
			int punkte3) {

		this.h = h;
		this.min = min;
		this.man1 = man1;
		this.man2 = man2;
		this.tore1 = tore1;
		this.tore2 = tore2;
		this.setPunkte1(punkte1);
		this.setPunkte2(punkte2);
		this.setPunkte3(punkte3);
		this.tabelle = new TabelleErstellen(this);
	}

	public void setTabelle(TabelleErstellen tab) throws NullPointerAusnahme {
		if (tab == null) {
			throw new NullPointerAusnahme();
		}
		this.tabelle = tab;
	}
	
	/**
	 * In dieser Methode wird der Spielplan der Gruppenphase erstellt.
	 * Das Turnier beginnt um 9 Uhr. Die Gruppenphase findet zwischen 9 und 14:00
	 * Uhr statt.
	 * 
	 * @pre 	die gruppenliste ist nicht leer
	 * @post	der Spielplan der Gruppenphase wurde erstellt
	 * @throws 	NullPointerAusnahme, wenn die übergebene Gruppenliste leer ist
	 */

	public void spielplanGruppenphaseErstellen() throws NullPointerAusnahme {

		/**
		 * Spielplan:
		 * 
		 * spielplanID	gruppe	gegner1		gegner2		stunde		minute		tore1	tore2
		 * 
		 * 0			MG1 	Man 1		Man 2		9			00 
		 * 1			MG1 	Man 3		Man 1		10			40
		 * 2			MG1 	Man 2		Man 3		12			20
		 * 
		 * 3			MG2 	Man 1		Man 2		9			25 
		 * 4			MG2 	Man 3		Man 1		11			05
		 * 5			MG2 	Man 2		Man 3		12			45
		 * 
		 * 6			MG3 	Man 1		Man 2		9			50 
		 * 7			MG3 	Man 3		Man 1		11			30
		 * 8			MG3 	Man 2		Man 3		13			10
		 * 
		 * 9			MG4 	Man 1		Man 2		10			15 
		 * 10			MG4 	Man 3		Man 1		11			55
		 * 11			MG4 	Man 2		Man 3		13			35
		 * 
		 * ...
		 */
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft mann1 = new Mannschaft(name1, mow, jahrgang);
			Mannschaft mann2 = new Mannschaft(name2, mow, jahrgang);
			Mannschaft mann3 = new Mannschaft(name3, mow, jahrgang);
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			stmt.execute("DROP TABLE IF EXISTS spielplan");
			stmt.execute("CREATE TABLE spielplan (`spielplanID` int NOT NULL, `gruppe` varchar(50) NOT NULL, `gegner1` varchar(50) NOT NULL,`gegner2` varchar(50) NOT NULL, `uhrzeit - stunde` int NOT NULL, `uhrzeit - minute` int NOT NULL, `tore1` int NOT NULL, `tore2` int NOT NULL, PRIMARY KEY(`spielplanID`))");
		
			/*
			 * Männer Gruppe 1-4
			 */
			 
			for(int i = 1; i <= 4; i++) {
				
				h = 9;
				min = 0 + (i-1)*25;
				if(min >= 60) {
					h += 1;
					min -= 60;
				}
				
				String countString = "SELECT COUNT(*) AS total FROM handball.MG" + i;
				ResultSet rset = stmt.executeQuery(countString);
				
				while(rset.next()){
				    int count = rset.getInt("total");
				    if (count == 0) {
						throw new NullPointerAusnahme();
					}
				} rset.close();
				
				String query1 =  "SELECT * FROM handball.MG" + i + " WHERE MG" + i + "ID=0";
				ResultSet rs = stmt.executeQuery(query1);
				
				while(rs.next()) {
					mann1.setName(rs.getString("name"));
				} rs.close();
				
				String query2 =  "SELECT * FROM handball.MG" + i + " WHERE MG" + i + "ID=1";
				ResultSet rs2 = stmt.executeQuery(query2);
				
				
				while(rs2.next()) {
					mann2.setName(rs2.getString("name"));
				} rs2.close();
				
				String query3 =  "SELECT * FROM handball.MG" + i + " WHERE MG" + i + "ID=2";
				ResultSet rs3 = stmt.executeQuery(query3);
				
				while(rs3.next()) {
					mann3.setName(rs3.getString("name"));
				} rs3.close();
				
				String insert1 = "INSERT INTO spielplan VALUES(" + ((i-1)*3) + ", 'MG" + i + "', '" + mann1.getName() + "', '" + mann2.getName() + "', " + h + ", " + min + ", 0, 0)";
					
				/*
				 * ein Spiel dauert 20 min, 5 min Pause zwischen Spielen,
				 * zuerst spielt Gruppe 1, nach 25 min G2, dann G3, dann G4 und dann wieder G1
				 * das nächste Spiel in der Gruppe ist nach 4 Spielrunden (4x25min --> nach 1:40h)
				 */
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert2 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+1) + ", 'MG" + i + "', '" + mann3.getName() + "', '" + mann1.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert3 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+2) + ", 'MG" + i + "', '" + mann2.getName() + "', '" + mann3.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				stmt.execute(insert1);
				stmt.execute(insert2);
				stmt.execute(insert3);
				
				}		
				
				
			
			
			/*
			 * Frauen Gruppe 1-4
			 */
			
			for(int i = 1; i <= 4; i++) {
				
				h = 9;
				min = 0 + (i-1)*25;
				if(min >= 60) {
					h += 1;
					min -= 60;
				}
				
				String countString = "SELECT COUNT(*) AS total FROM handball.FG" + i;
				ResultSet rset = stmt.executeQuery(countString);
				
				while(rset.next()){
				    int count = rset.getInt("total");
				    if (count == 0) {
						throw new NullPointerAusnahme();
					}
				} rset.close();
				
				String query1 =  "SELECT * FROM handball.FG" + i + " WHERE FG" + i + "ID=0";
				ResultSet rs = stmt.executeQuery(query1);
				
				while(rs.next()) {
					mann1.setName(rs.getString("name"));
				} rs.close();
				
				
				String query2 =  "SELECT * FROM handball.FG" + i + " WHERE FG" + i + "ID=1";
				ResultSet rs2 = stmt.executeQuery(query2);
				
				while(rs2.next()) {
					mann2.setName(rs2.getString("name"));
				} rs2.close();
				
				
				String query3 =  "SELECT * FROM handball.FG" + i + " WHERE FG" + i + "ID=2";
				ResultSet rs3 = stmt.executeQuery(query3);
				
				while(rs3.next()) {
					mann3.setName(rs3.getString("name"));
				} rs3.close();
				
				
				String insert1 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+12) + ", 'FG" + i + "', '" + mann1.getName() + "', '" + mann2.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert2 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+13) + ", 'FG" + i + "', '" + mann3.getName() + "', '" + mann1.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert3 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+14) + ", 'FG" + i + "', '" + mann2.getName() + "', '" + mann3.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				stmt.execute(insert1);
				stmt.execute(insert2);
				stmt.execute(insert3);
				
			}
			
			/*
			 * männliche A Gruppe 1-4
			 */
			
			for(int i = 1; i <= 4; i++) {
				
				h = 9;
				min = 0 + (i-1)*25;
				if(min >= 60) {
					h += 1;
					min -= 60;
				}
				
				String countString = "SELECT COUNT(*) AS total FROM handball.MAG" + i;
				ResultSet rset = stmt.executeQuery(countString);
				
				while(rset.next()){
				    int count = rset.getInt("total");
				    if (count == 0) {
						throw new NullPointerAusnahme();
					}
				} rset.close();
				 
				
				String query1 =  "SELECT * FROM handball.MAG" + i + " WHERE MAG" + i + "ID=0";
				ResultSet rs = stmt.executeQuery(query1);
				
				while(rs.next()) {
					mann1.setName(rs.getString("name"));
				} rs.close();
				
				String query2 =  "SELECT * FROM handball.MAG" + i + " WHERE MAG" + i + "ID=1";
				ResultSet rs2 = stmt.executeQuery(query2);
				
				while(rs2.next()) {
					mann2.setName(rs2.getString("name"));
				} rs2.close();
				
				
				String query3 =  "SELECT * FROM handball.MAG" + i + " WHERE MAG" + i + "ID=2";
				ResultSet rs3 = stmt.executeQuery(query3);
				
				while(rs3.next()) {
					mann3.setName(rs3.getString("name"));
				} rs3.close();
				
				
				String insert1 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+24) + ", 'MAG" + i + "', '" + mann1.getName() + "', '" + mann2.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert2 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+25) + ", 'MAG" + i + "', '" + mann3.getName() + "', '" + mann1.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert3 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+26) + ", 'MAG" + i + "', '" + mann2.getName() + "', '" + mann3.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				stmt.execute(insert1);
				stmt.execute(insert2);
				stmt.execute(insert3);
				
			}
			
			/*
			 * männliche B Gruppe 1-4
			 */
			
			for(int i = 1; i <= 4; i++) {
				
				h = 9;
				min = 0 + (i-1)*25;
				if(min >= 60) {
					h += 1;
					min -= 60;
				}
				
				String countString = "SELECT COUNT(*) AS total FROM handball.MBG" + i;
				ResultSet rset = stmt.executeQuery(countString);
				
				while(rset.next()){
				    int count = rset.getInt("total");
				    if (count == 0) {
						throw new NullPointerAusnahme();
					}
				} rset.close();
				
				
				String query1 =  "SELECT * FROM handball.MBG" + i + " WHERE MBG" + i + "ID=0";
				ResultSet rs = stmt.executeQuery(query1);
				
				while(rs.next()) {
					mann1.setName(rs.getString("name"));
				} rs.close();
				
				
				String query2 =  "SELECT * FROM handball.MBG" + i + " WHERE MBG" + i + "ID=1";
				ResultSet rs2 = stmt.executeQuery(query2);
				
				while(rs2.next()) {
					mann2.setName(rs2.getString("name"));
				} rs2.close();
				
				
				String query3 =  "SELECT * FROM handball.MBG" + i + " WHERE MBG" + i + "ID=2";
				ResultSet rs3 = stmt.executeQuery(query3);
				
				while(rs3.next()) {
					mann3.setName(rs3.getString("name"));
				} rs3.close();
				
				
				String insert1 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+36) + ", 'MBG" + i + "', '" + mann1.getName() + "', '" + mann2.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert2 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+37) + ", 'MBG" + i + "', '" + mann3.getName() + "', '" + mann1.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert3 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+38) + ", 'MBG" + i + "', '" + mann2.getName() + "', '" + mann3.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				stmt.execute(insert1);
				stmt.execute(insert2);
				stmt.execute(insert3);
				
			}
			
			/*
			 * weibliche A Gruppe 1-4
			 */
			
			for(int i = 1; i <= 4; i++) {
				
				h = 9;
				min = 0 + (i-1)*25;
				if(min >= 60) {
					h += 1;
					min -= 60;
				}
				
				String countString = "SELECT COUNT(*) AS total FROM handball.WAG" + i;
				ResultSet rset = stmt.executeQuery(countString);
				
				while(rset.next()){
				    int count = rset.getInt("total");
				    if (count == 0) {
						throw new NullPointerAusnahme();
					}
				} rset.close();
				
				
				String query1 =  "SELECT * FROM handball.WAG" + i + " WHERE WAG" + i + "ID=0";
				ResultSet rs = stmt.executeQuery(query1);
				
				while(rs.next()) {
					mann1.setName(rs.getString("name"));
				} rs.close();
				
				
				String query2 =  "SELECT * FROM handball.WAG" + i + " WHERE WAG" + i + "ID=1";
				ResultSet rs2 = stmt.executeQuery(query2);
				
				while(rs2.next()) {
					mann2.setName(rs2.getString("name"));
				} rs2.close();
				
				
				String query3 =  "SELECT * FROM handball.WAG" + i + " WHERE WAG" + i + "ID=2";
				ResultSet rs3 = stmt.executeQuery(query3);
				
				while(rs3.next()) {
					mann3.setName(rs3.getString("name"));
				} rs3.close();
				
				
				String insert1 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+48) + ", 'WAG" + i + "', '" + mann1.getName() + "', '" + mann2.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert2 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+49) + ", 'WAG" + i + "', '" + mann3.getName() + "', '" + mann1.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert3 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+50) + ", 'WAG" + i + "', '" + mann2.getName() + "', '" + mann3.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				stmt.execute(insert1);
				stmt.execute(insert2);
				stmt.execute(insert3);
				
			}
			
			/*
			 * weibliche B Gruppe 1-4
			 */
			
			for(int i = 1; i <= 4; i++) {
				
				h = 9;
				min = 0 + (i-1)*25;
				if(min >= 60) {
					h += 1;
					min -= 60;
				}
				
				String countString = "SELECT COUNT(*) AS total FROM handball.WBG" + i;
				ResultSet rset = stmt.executeQuery(countString);
				
				while(rset.next()){
				    int count = rset.getInt("total");
				    if (count == 0) {
						throw new NullPointerAusnahme();
					}
				} rset.close();
				
				String query1 =  "SELECT * FROM handball.WBG" + i + " WHERE WBG" + i + "ID=0";
				ResultSet rs = stmt.executeQuery(query1);
				
				while(rs.next()) {
					mann1.setName(rs.getString("name"));
				} rs.close();
				
				
				String query2 =  "SELECT * FROM handball.WBG" + i + " WHERE WBG" + i + "ID=1";
				ResultSet rs2 = stmt.executeQuery(query2);
				
				while(rs2.next()) {
					mann2.setName(rs2.getString("name"));
				} rs2.close();
				
				
				String query3 =  "SELECT * FROM handball.WBG" + i + " WHERE WBG" + i + "ID=2";
				ResultSet rs3 = stmt.executeQuery(query3);
				
				while(rs3.next()) {
					mann3.setName(rs3.getString("name"));
				} rs3.close();
				
				
				
				String insert1 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+60) + ", 'WBG" + i + "', '" + mann1.getName() + "', '" + mann2.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert2 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+61) + ", 'WBG" + i + "', '" + mann3.getName() + "', '" + mann1.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				for(int skip = 0; skip < 4; skip++) {
					min = min + 25;
					if (min >= 60) {
						h = h + 1;
						min = min - 60;
					}
				}
				
				String insert3 = "INSERT INTO spielplan VALUES(" + ((i-1)*3+62) + ", 'WBG" + i + "', '" + mann2.getName() + "', '" + mann3.getName() + "', " + h + ", " + min + ", 0, 0)";
				
				stmt.execute(insert1);
				stmt.execute(insert2);
				stmt.execute(insert3);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}	
	

	}

	/**
	 * Mit dieser print-Methode soll der Spielplan geprintet werden. Es sollen die
	 * Gruppen mit den Spielen mitsamt ihrer Uhrzeiten asugegeben werden.
	 * 
	 * @pre		der Spielplan darf nicht leer sein
	 * @throws 	NullPointerAusnahme, wenn übergebener Spielplan leer ist.
	 */

	public void printSpielplan() throws NullPointerAusnahme {
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
		
			String countString = "SELECT COUNT(*) AS total FROM handball.spielplan";
			ResultSet rset = stmt.executeQuery(countString);
			
			while(rset.next()){
			    int count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			

			System.out.println("\nSpielplan Gruppenphase:");
			
			for(int i = 0; i < 72; i++) {
				
				String query =  "SELECT * FROM handball.spielplan WHERE spielplanID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					gruppe = rs.getString("gruppe");
					name1 = rs.getString("gegner1");
					name2 = rs.getString("gegner2");
					h = rs.getInt("uhrzeit - stunde");
					min = rs.getInt("uhrzeit - minute");
				
				
				if(i%3 == 0) {
					System.out.println("_______________________________________________");
					System.out.println("\n" + gruppe + ":");
					System.out.println("\nSpiel 1:");
				}
				else if (i%3 == 1) {
					System.out.println("\nSpiel 2:");
				}
				else if (i%3 == 2) {
					System.out.println("\nSpiel 3:");
				}
				
				if (min >= 10) {
					System.out.println(name1 + " : " + name2 + ", " + h + ":" + min);
				}
				else {
					System.out.println(name1 + " : " + name2 + ", " + h + ":0" + min);
				}
				}
				rs.close();
			}
			
			
				
		System.out.println("_______________________________________________");
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Mithilfe dieser Methode werden zufällige Ergebnisse der Spiele generiert. Eine
	 * Mannschaft kann bis zu 9 Tore erzielen. Anhand der Ergebnisse werden die
	 * Punkte der Mannschaften generiert. Für einen Gewinn erhält die Mannschaft 2
	 * Punkte, bei einem Unentschieden einen Punkt, sonst keinen. Mithilfe dieser
	 * Punkte wird später die Tabelle errechnet.
	 * 
	 * @pre		der Spielplan darf nicht leer sein, die Tore sollen noch nicht 
	 * 			eingetragen sein
	 * @post	zu jedem Spiel der Gruppenphase findet man ein Ergebnis	
	 * @throws 	NullPointerAusnahme, wenn übergebener Spielplan leer ist.
	 */
	
	public void ergebnisEintragen() throws NullPointerAusnahme {
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
			
			for (int i = 0; i < 24; i++) {

				String query = "SELECT * FROM spielplan WHERE spielplanID=" + (i*3);
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					gruppe = rs.getString("gruppe");
				}
				rs.close();
				
				punkte1 = punkte2 = punkte3 = 0;
				int toreman1, toreman2, toreman3;
				
				// Spiel 1
				tore1 = (int) (Math.random() * 10) + 1;
				toreman1 = tore1;
				tore2 = (int) (Math.random() * 10) + 1;
				toreman2 = tore2;
				
				String update = "UPDATE spielplan SET `tore1` = " + tore1 + ", `tore2` = " + tore2 + " WHERE spielplanID=" + (i*3);
				stmt.execute(update);
				
				if (tore1 > tore2) {
					punkte1 = punkte1 + 2;
				} else if (tore1 < tore2) {
					punkte2 = punkte2 + 2;
				} else {
					punkte1 = punkte1 + 1;
					punkte2 = punkte2 + 1;
				}
				
				// Spiel 2
				tore3 = (int) (Math.random() * 10) + 1;
				toreman3 = tore3;
				tore1 = (int) (Math.random() * 10) + 1;
				toreman1 += tore1;
				
				update = "UPDATE spielplan SET `tore1` = " + tore3 + ", `tore2` = " + tore1 + " WHERE spielplanID=" + (i*3+1);
				stmt.execute(update);
				
				if (tore3 > tore1) {
					punkte3 = punkte3 + 2;
				} else if (tore3 < tore1) {
					punkte1 = punkte1 + 2;
				} else {
					punkte1 = punkte1 + 1;
					punkte3 = punkte3 + 1;
				}
				
				// Spiel 3
				tore2 = (int) (Math.random() * 10) + 1;
				toreman2 += tore2;
				tore3 = (int) (Math.random() * 10) + 1;
				toreman3 += tore3;
				
				update = "UPDATE spielplan SET `tore1` = " + tore2 + ", `tore2` = " + tore3 + " WHERE spielplanID=" + (i*3+2);
				stmt.execute(update);
				
				if (tore2 > tore3) {
					punkte2 = punkte2 + 2;
				} else if (tore2 < tore3) {
					punkte3 = punkte3 + 2;
				} else {
					punkte2 = punkte2 + 1;
					punkte3 = punkte3 + 1;
				}
				
				/*
				 * hier werden drei verschiedene zufällige Zahlen generiert, die später für die Rangordnung benutzt werden, 
				 * falls die Punkt- & Torezahl der Mannschaften gleich sein sollte
				 */
				
				int random1 = (int) (Math.random() * 100);
				int random2 = (int) (Math.random() * 100);
				if(random2 == random1) {
					boolean a = true;
					while(a) {
						random2 = (int) (Math.random() * 100);
						if(random2 != random1) {
							a = false;
						}
					}
					
				}
				int random3 = (int) (Math.random() * 100);
				if(random3 == random1 || random3 == random2) {
					boolean a = true;
					while(a) {
						random3 = (int) (Math.random() * 100);
						if(random3 != random1 && random3 != random2) {
							a = false;
						}
					}
					
				}
				
				String insert1 = "UPDATE " + gruppe + " SET `punkte` = " + punkte1 + ", `tore` = " + toreman1 + ", `random` = " + random1 + " WHERE " + gruppe + "ID=0";
				String insert2 = "UPDATE " + gruppe + " SET `punkte` = " + punkte2 + ", `tore` = " + toreman2 + ", `random` = " + random2 + " WHERE " + gruppe + "ID=1";
				String insert3 = "UPDATE " + gruppe + " SET `punkte` = " + punkte3 + ", `tore` = " + toreman3 + ", `random` = " + random3 + " WHERE " + gruppe + "ID=2";
				
				stmt.execute(insert1);
				stmt.execute(insert2);
				stmt.execute(insert3);
				
			}
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	/**
	 * In dieser Methode soll nun der Spielplan für die K.O Runden erstellt werden.
	 * Im Viertelfinale spielt der Sieger einer Gruppe gegen den Zweitplatzierten
	 * der nächsten Gruppe. Im Halbfinale spielt der Gewinner des ersten Viertel-
	 * finales gegen den Gewinner des dritten und der Gewinner des zweiten VF gegen
	 * den des vierten, damit die Mannschaften vor dem Finale nicht zwei Mal gegen
	 * dieselbe Mannschaft spielen, sondern nur gegen Mannschaften aus anderen
	 * Gruppen.
	 * 
	 * @pre		die Tabelle muss nach der Gruppenphase erstellt worden sein 
	 * 			und der Erst- und Zweitplatzierte jeder Gruppe muss feststehen
	 * @post	der Spielplan des Viertelfinales ist erstellt
	 * @throws 	NullPointerAusnahme, wenn übergebene Tabelle leer ist
	 * 
	 */

	public void spielplanVF() throws NullPointerAusnahme {
		// Viertelfinale
		tabelle.gruppenTabelleErstellen();
		
		h = 14;
		min = 30;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft mannschaft1 = new Mannschaft(name, mow, jahrgang);
			Mannschaft mannschaft2 = new Mannschaft(name, mow, jahrgang);
			Mannschaft mannschaft3 = new Mannschaft(name, mow, jahrgang);
			Mannschaft mannschaft4 = new Mannschaft(name, mow, jahrgang);
			TabelleErstellen tabelle = new TabelleErstellen(s);
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			for (int i = 0; i < 24; i++) {

				String query = "SELECT * FROM spielplan WHERE spielplanID=" + (i*3);
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					gruppe1 = rs.getString("gruppe");
				}
				rs.close();
			
				String countString = "SELECT COUNT(*) AS total FROM " + gruppe1;
				ResultSet rset = stmt.executeQuery(countString);
				
				int count = 0;
				
				while(rset.next()){
				    count = rset.getInt("total");
				    if (count == 0) {
						throw new NullPointerAusnahme();
					}
				} rset.close();
			}
			
			stmt.execute("DROP TABLE IF EXISTS spielplanVF");
			stmt.execute("CREATE TABLE spielplanVF (`spielplanVFID` int NOT NULL, `gruppen` varchar(50), `jahrgang` varchar(50) NOT NULL, `mow` varchar(5), `gegner1` varchar(50) NOT NULL,`gegner2` varchar(50) NOT NULL, `uhrzeit - stunde` int NOT NULL, `uhrzeit - minute` int NOT NULL, `tore1` int NOT NULL, `tore2` int NOT NULL, PRIMARY KEY(`spielplanVFID`))");
			
			/*
			 * im Viertelfinale gibt es je Geschlecht/Jahrgang 4 Spiele,
			 * es spielt jeweils der erste einer Gruppe gegen den zweiten einer anderen Gruppe
			 * MG1.1 - MG2.2, MG1.2 - MG2.1, MG3.1 - MG4.2, MG3.2 - MG4.1
			 */
				
			for (int j = 0; j < 12; j++) {
				
				/*
				 * in allen 6 Hallen wird gleichzeitig gespielt, nach vier eingefügten Spielen wird die Uhrzeit wieder zurückgesetzt
				 */
				
				h = 14;
				min = 30 + (j%2)*50;
				if(min >= 60) {
					h += 1;
					min -= 60;
				}
				
				h2 = 14;
				min2 = 55 + (j%2)*50;
				if(min2 >= 60) {
					h2 += 1;
					min2 -= 60;
				}
				
				
				
				/*
				 * hier werden zwei Gruppen geholt, aus denen jeweils der erste gegen den zweiten der anderen Gruppe antritt
				 */

				String query1 = "SELECT * FROM spielplan WHERE spielplanID=" + (j*6);
				ResultSet rs = stmt.executeQuery(query1);
				
				while(rs.next()) {
					gruppe1 = rs.getString("gruppe");
				}
				rs.close();
				
				String query2 = "SELECT * FROM spielplan WHERE spielplanID=" + (j*6+3);
				ResultSet rs2 = stmt.executeQuery(query2);
				
				while(rs2.next()) {
					gruppe2 = rs2.getString("gruppe");
				}
				rs2.close();
				
				String query3 = "SELECT * FROM " + gruppe1;
				ResultSet rs3 = stmt.executeQuery(query3);
				
				while(rs3.next()) {
					jahrgang = rs3.getString("jahrgang");
					mow = rs3.getString("mow");
				}
				rs3.close();
				
				mannschaft1 = tabelle.getPlatz1(gruppe1);
				mannschaft2 = tabelle.getPlatz2(gruppe2);
				mannschaft3 = tabelle.getPlatz1(gruppe2);
				mannschaft4 = tabelle.getPlatz2(gruppe1);
			
				
				String game = "INSERT INTO spielplanVF VALUES(" + j*2 + ", '" + gruppe1 + ", " + gruppe2 + "', '" + jahrgang + "', '" + mow + "', '" + mannschaft1.getName() + "', '" + mannschaft2.getName() + "', " + h + ", " + min + ", 0, 0)";
				String game2 = "INSERT INTO spielplanVF VALUES(" + (j*2+1) + ", '" + gruppe1 + ", " + gruppe2 + "', '" + jahrgang + "', '" + mow + "','" + mannschaft3.getName() + "', '" + mannschaft4.getName() + "', " + h2 + ", " + min2 + ", 0, 0)";
				
				stmt.executeUpdate(game);
				stmt.executeUpdate(game2);
			
			}
		
			

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	/**
	 * In dieser Methode werden random Ergebnisse für die Spiele des Viertelfinales
	 * generiert. Daraus ergeben sich die Gewinner, die in die nächste Runde weiter
	 * dürfen und die Verlierer, die ausscheiden.
	 * 
	 * @pre		der Viertelfinal-Spielplan darf nicht leer sein
	 * @post	die Ergebnisse zu jedem VF-Spiel müssen eingetragen sein
	 * @throws 	NullPointerAusnahme, wenn übergebener Spielplan leer ist.
	 */

	public void ergebnisVF() throws NullPointerAusnahme {
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
			
			String countString = "SELECT COUNT(*) AS total FROM spielplanVF";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			
			for(int i = 0; i < 24; i++) {
				tore1 = (int) (Math.random() * 10);
				tore2 = (int) (Math.random() * 10);
				
				String insert = "UPDATE spielplanVF SET `tore1` = " + tore1 + ", `tore2` = " + tore2 + " WHERE spielplanVFID=" + i;
				stmt.executeUpdate(insert);
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * In dieser Methode wird der Spielplan für das Halbfinale mit den Gewinnern des
	 * Viertelfinales erstellt.
	 * 
	 * @pre		die Gewinner des Viertelfinales müssen feststehen
	 * @post	der Spielplan für das Halbfinale ist erstellt
	 * @throws 	NullPointerAusnahme, wenn übergebener Spielplan aus Viertelfinale mit
	 *          Ergebnissen leer ist.
	 */

	public void spielplanHF() throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft mann1 = new Mannschaft(name, mow, jahrgang);
			Mannschaft mann2 = new Mannschaft(name, mow, jahrgang);
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String countString = "SELECT COUNT(*) AS total FROM spielplanVF";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();			
			
			stmt.execute("DROP TABLE IF EXISTS spielplanHF");
			stmt.execute("CREATE TABLE spielplanHF (`spielplanHFID` int NOT NULL, `jahrgang` varchar(50) NOT NULL, `mow` varchar(5), `gegner1` varchar(50) NOT NULL,`gegner2` varchar(50) NOT NULL, `uhrzeit - stunde` int NOT NULL, `uhrzeit - minute` int NOT NULL, `tore1` int NOT NULL, `tore2` int NOT NULL, PRIMARY KEY(`spielplanHFID`))");
			
			/*
			 * spielplan Halbfinale erstellen --> sieger aus zeile 1 mit sieger aus zeile 3, sieger aus zeile 2 mit zeile 4 ...
			 */
			
			for(int i = 0; i < 12; i++) {
				String query = "SELECT * FROM spielplanVF WHERE spielplanVFID=" + (i*2);
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					jahrgang = rs.getString("jahrgang");
					mow = rs.getString("mow");
				
			
					h = 16;
					min = 30 + (i%2)*25;
					if(min >= 60) {
						h += 1;
						min -= 60;
					}
				} rs.close();
				
				
				int index, index2;
				
				if(i % 2 == 0) {
					index = 2 * i;
					index2 = 2 * i + 2;
					
					mann1 = tabelle.getSiegerVF(index);
					mann2 = tabelle.getSiegerVF(index2);
					
					String insert = "INSERT INTO spielplanHF VALUES(" + i + ", '" + jahrgang + "', '" + mow + "', '" + mann1.getName() + "', '" + mann2.getName() + "', " + h + ", " + min + ", 0, 0)";
					stmt.executeUpdate(insert);
				}
				else {
					index = 2 * i - 1;
					index2 = 2 * i + 1;
					
					mann1 = tabelle.getSiegerVF(index);
					mann2 = tabelle.getSiegerVF(index2);
					
					String insert = "INSERT INTO spielplanHF VALUES(" + i + ", '" + jahrgang + "', '" + mow + "', '" + mann1.getName() + "', '" + mann2.getName() + "', " + h + ", " + min + ", 0, 0)";
					stmt.executeUpdate(insert);
				}
				
				
			}
		}
		
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * In dieser Methode werden die Ergebnisse der Halbfinalspiele random generiert.
	 * Daraus ergeben sich Gewinner, die ins große Finale kommen und um den ersten
	 * Platz kämpfen und Verlierer, die ins kleine Finale kommen und um den dritten
	 * Platz kämpfen.
	 * 
	 * @pre		der Spielplan des HF darf nicht leer sein
	 * @post	die Ergebnisse müssen eingetragen sein
	 * @throws 	NullPointerAusnahme, wenn übergebener Spielplan leer ist.
	 * 
	 */

	public void ergebnisHF() throws NullPointerAusnahme {
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
			
			String countString = "SELECT COUNT(*) AS total FROM spielplanHF";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();	
				
			for(int i = 0; i < 12; i++) {
				tore1 = (int) (Math.random() * 10);
				tore2 = (int) (Math.random() * 10);
				
				String insert = "UPDATE spielplanHF SET `tore1` = " + tore1 + ", `tore2` = " + tore2 + " WHERE spielplanHFID=" + i;
				stmt.executeUpdate(insert); 
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * In dieser Methode wird aus den Gewinnern und Verlierern des Halbfinales der
	 * Spielplan für das Finale erstellt.
	 * 
	 * @pre		die Gewinner und Verlierer des HF müssen feststehen
	 * @post	der Spielplan des Finales ist erstellt
	 * @throws 	NullPointerAusnahme, wenn übergebener Spielplan des Halbfinales mit
	 *          Ergebnissen leer ist.
	 */

	public void spielplanFinale() throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft verl1 = new Mannschaft(name, mow, jahrgang);
			Mannschaft verl2 = new Mannschaft(name, mow, jahrgang);
			Mannschaft gew1 = new Mannschaft(name, mow, jahrgang);
			Mannschaft gew2 = new Mannschaft(name, mow, jahrgang);
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String countString = "SELECT COUNT(*) AS total FROM spielplanHF";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();	
			
			stmt.execute("DROP TABLE IF EXISTS spielplanFinale");
			stmt.execute("CREATE TABLE spielplanFinale (`spielplanFID` int NOT NULL, `KF/GF` varchar(50), `jahrgang` varchar(50) NOT NULL, `mow` varchar(5), `gegner1` varchar(50) NOT NULL,`gegner2` varchar(50) NOT NULL, `uhrzeit - stunde` int NOT NULL, `uhrzeit - minute` int NOT NULL, `tore1` int NOT NULL, `tore2` int NOT NULL, PRIMARY KEY(`spielplanFID`))");
			
			
			for(int i = 0; i < 6; i++) {
				String query = "SELECT * FROM spielplanHF WHERE spielplanHFID=" + (2*i);
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					jahrgang = rs.getString("jahrgang");
					mow = rs.getString("mow");
				}
				rs.close();
				
				
				h = 17;
				min = 30;
				
				h2 = 17;
				min2 = 55;
				
				
				verl1 = tabelle.getVerliererHF(2*i);
				verl2 = tabelle.getVerliererHF(2*i+1);
				gew1 = tabelle.getSiegerHF(2*i);
				gew2 = tabelle.getSiegerHF(2*i+1);
				
				
				String insert = "INSERT INTO spielplanFinale VALUES(" + 2*i + ", 'kleines Finale', '" + jahrgang + "', '" + mow + "', '" + verl1.getName() + "', '" + verl2.getName() + "', " + h + ", " + min + ", 0, 0)";
				stmt.executeUpdate(insert);
			
				insert = "INSERT INTO spielplanFinale VALUES(" + (2*i+1) + ", 'großes Finale', '" + jahrgang + "', '" + mow + "', '" + gew1.getName() + "', '" + gew2.getName() + "', " + h2 + ", " + min2 + ", 0, 0)";
				stmt.executeUpdate(insert);
				
				
				
			}
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * In dieser Methode werden die Ergebnisse der Finalspiele random generiert.
	 * Daraus ergeben sich Platz 1, 2 und 3.
	 * 
	 * @pre		der Spielplan des Finales darf nicht leer sein
	 * @post	die Ergebnisse des Finales sind eingetragen und Erst- bis 
	 * 			Drittplatzierter stehen fest
	 * @throws 	NullPointerAusnahme, wenn übergebener Spielplan des Finales leer ist.
	 * 
	 */

	public void ergebnisFinale() throws NullPointerAusnahme {
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
			
			String countString = "SELECT COUNT(*) AS total FROM spielplanFinale";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();	
				
			for(int i = 0; i < 12; i++) {
				tore1 = (int) (Math.random() * 10);
				tore2 = (int) (Math.random() * 10);
		
				String insert = "UPDATE spielplanFinale SET `tore1` = " + tore1 + ", `tore2` = " + tore2 + " WHERE spielplanFID=" + i;
				stmt.executeUpdate(insert);
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * Mit dieser print-Methode sollen die Spielpläne der K.O Runden, sowie die
	 * Erst- bis Drittplatzierung ausgegeben werden.
	 * 
	 * @pre		die Spielpläne dürfen nicht leer sein und die Platzierung 
	 * 			muss feststehen
	 * @throws 	NullPointerAusnahme, wenn mindestens einer der übergebenen Spielpläne
	 *                              leer ist.
	 * 
	 */

	public void printSpielplanKO() throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft gewFin = new Mannschaft(name, mow, jahrgang);
			Mannschaft verlFin = new Mannschaft(name, mow, jahrgang);
			Mannschaft gewKlFin = new Mannschaft(name, mow, jahrgang);
			TabelleErstellen tabelle = new TabelleErstellen(s);
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String countString = "SELECT COUNT(*) AS total FROM spielplanVF";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();	
			
			countString = "SELECT COUNT(*) AS total FROM spielplanHF";
			ResultSet rset2 = stmt.executeQuery(countString);
			
			while(rset2.next()){
			    count = rset2.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset2.close();	
			
			countString = "SELECT COUNT(*) AS total FROM spielplanFinale";
			ResultSet rset3 = stmt.executeQuery(countString);
			
			while(rset3.next()){
			    count = rset3.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset3.close();	
			
		
			System.out.println("___________________________________________________");
			System.out.println("\nViertelfinale: \n");
			
			for (int i = 0; i < 24; i++) {
				
				String query = "SELECT * FROM spielplanVF WHERE spielplanVFID=" + i;
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					name1 = rs.getString("gegner1");
					name2 = rs.getString("gegner2");
					h = rs.getInt("uhrzeit - stunde");
					min = rs.getInt("uhrzeit - minute");
				} 
				rs.close();
				
				if (i == 0) {
					System.out.println("\nMänner: \n");
				} else if (i == 4) {
					System.out.println("\nFrauen: \n");
				} else if (i == 8) {
					System.out.println("\nmännliche A-Jugend: \n");
				} else if (i == 12) {
					System.out.println("\nmännliche B-Jugend: \n");
				} else if (i == 16) {
					System.out.println("\nweibliche A-Jugend: \n");
				} else if (i == 20) {
					System.out.println("\nweibliche B-Jugend: \n");
				}
				if (min >= 10) {
					System.out.println(name1 + " : " + name2 + ", " + h + ":" + min);
				}
				else {
					System.out.println(name1 + " : " + name2 + ", " + h + ":0" + min);
				}
			}

		System.out.println("___________________________________________________");
		System.out.println("\nHalbfinale: \n");
		
		for (int i = 0; i < 12; i++) {
			
			String query = "SELECT * FROM spielplanHF WHERE spielplanHFID=" + i;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				name1 = rs.getString("gegner1");
				name2 = rs.getString("gegner2");
				h = rs.getInt("uhrzeit - stunde");
				min = rs.getInt("uhrzeit - minute");
			} 
			rs.close();
			
			if (i == 0) {
				System.out.println("\nMänner: \n");
			} else if (i == 2) {
				System.out.println("\nFrauen: \n");
			} else if (i == 4) {
				System.out.println("\nmännliche A-Jugend: \n");
			} else if (i == 6) {
				System.out.println("\nmännliche B-Jugend: \n");
			} else if (i == 8) {
				System.out.println("\nweibliche A-Jugend: \n");
			} else if (i == 10) {
				System.out.println("\nweibliche B-Jugend: \n");
			}
			if (min >= 10) {
				System.out.println(name1 + " : " + name2 + ", " + h + ":" + min);
			}
			else {
				System.out.println(name1 + " : " + name2 + ", " + h + ":0" + min);
			}
			
		}

		System.out.println("___________________________________________________");
		System.out.println("\nkleines Finale:\n");
		
		for (int i = 0; i < 6; i++) {
			
			String query = "SELECT * FROM spielplanFinale WHERE spielplanFID=" + (i*2);
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				name1 = rs.getString("gegner1");
				name2 = rs.getString("gegner2");
				h = rs.getInt("uhrzeit - stunde");
				min = rs.getInt("uhrzeit - minute");
			} 
			rs.close();
			
			if (i == 0) {
				System.out.println("\nMänner: \n");
			} else if (i == 1) {
				System.out.println("\nFrauen: \n");
			} else if (i == 2) {
				System.out.println("\nmännliche A-Jugend: \n");
			} else if (i == 3) {
				System.out.println("\nmännliche B-Jugend: \n");
			} else if (i == 4) {
				System.out.println("\nweibliche A-Jugend: \n");
			} else if (i == 5) {
				System.out.println("\nweibliche B-Jugend: \n");
			}
			if (min >= 10) {
				System.out.println(name1 + " : " + name2 + ", " + h + ":" + min);
			}
			else {
				System.out.println(name1 + " : " + name2 + ", " + h + ":0" + min);
			}
		}

		System.out.println("___________________________________________________");
		System.out.println("\ngroßes Finale:\n");
		for (int i = 0; i < 6; i++) {
			
			String query = "SELECT * FROM spielplanFinale WHERE spielplanFID=" + (i*2+1);
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				name1 = rs.getString("gegner1");
				name2 = rs.getString("gegner2");
				h = rs.getInt("uhrzeit - stunde");
				min = rs.getInt("uhrzeit - minute");
			} 
			rs.close();
			
			if (i == 0) {
				System.out.println("\nMänner: \n");
			} else if (i == 1) {
				System.out.println("\nFrauen: \n");
			} else if (i == 2) {
				System.out.println("\nmännliche A-Jugend: \n");
			} else if (i == 3) {
				System.out.println("\nmännliche B-Jugend: \n");
			} else if (i == 4) {
				System.out.println("\nweibliche A-Jugend: \n");
			} else if (i == 5) {
				System.out.println("\nweibliche B-Jugend: \n");
			}
			System.out.println(name1 + " : " + name2 + ", " + h + ":" + min);
		}


		System.out.println("___________________________________________________");
		System.out.println("\nPlatzierung:\n");
		for (int j = 0; j < 6; j++) {
			if (j == 0) {
				System.out.println("\nMänner: \n");
			} else if (j == 1) {
				System.out.println("\nFrauen: \n");
			} else if (j == 2) {
				System.out.println("\nmännliche A-Jugend: \n");
			} else if (j == 3) {
				System.out.println("\nmännliche B-Jugend: \n");
			} else if (j == 4) {
				System.out.println("\nweibliche A-Jugend: \n");
			} else if (j == 5) {
				System.out.println("\nweibliche B-Jugend: \n");
			}
			/*
			 * Ausgeben von 1. - 3. Platz jedes Alters/ Geschlechts
			 */
			
			// Gewinner großes Finale
			gewFin = tabelle.getSiegerFinale(2*j+1);
			// Verlierer großes Finale
			verlFin = tabelle.getVerliererFinale(2*j+1); 
			// Sieger kleines Finale
			gewKlFin = tabelle.getSiegerFinale(2*j);
			
			System.out.println("1. Platz: " + gewFin.getName());
			System.out.println("2. Platz: " + verlFin.getName());
			System.out.println("3. Platz: " + gewKlFin.getName());
		}
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	public int getH() {
		return h;
	}

	public int getMin() {
		return min;
	}

	public int getTore1() {
		return tore1;
	}

	public void setTore1(int tore1) {
		this.tore1 = tore1;
	}

	public void setTore2(int tore2) {
		this.tore2 = tore2;
	}

	public int getTore2() {
		return tore2;
	}

	public String getMan1Name() {
		return man1.getName();
	}

	public String getMan2Name() {
		return man2.getName();
	}

	public Mannschaft getMan1() {
		return man1;
	}

	public Mannschaft getMan2() {
		return man2;
	}

	public int getPunkte2() {
		return punkte2;
	}

	public void setPunkte2(int punkte2) {
		this.punkte2 = punkte2;
	}

	public int getPunkte1() {
		return punkte1;
	}

	public void setPunkte1(int punkte1) {
		this.punkte1 = punkte1;
	}

	public int getPunkte3() {
		return punkte3;
	}

	public void setPunkte3(int punkte3) {
		this.punkte3 = punkte3;
	}
 

}
