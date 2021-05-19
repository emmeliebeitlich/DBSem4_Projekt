package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

import Exceptions.MaxAnzMannschaftenAusnahme;
import Exceptions.NullPointerAusnahme;
import Mannschaften.Mannschaft;
import Mannschaften.MannschaftenListe;

/*
 * In dieser Test-Klasse soll die Klasse Mannschaften-Liste getestet werden.
 */

class MannschaftenListeTest {

	@Test
	void mannschaftenListeSollteAngelegtWerden() {
		MannschaftenListe mannliste = new MannschaftenListe();
		assertTrue(mannliste instanceof MannschaftenListe);
	}

	@Test
	void mannschaftSollteHinzugefuegtWerden() throws MaxAnzMannschaftenAusnahme {
		MannschaftenListe mannliste = new MannschaftenListe();
		Mannschaft m1 = new Mannschaft("a", "m", "Aktive");

		try {
			mannliste.mannschaftHinzufuegen(m1);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			fail("Sollte nicht passieren");
		}

	}
	
	@Test 
	void mannschaftKannNichtHinzugefuegtWerden() {
		MannschaftenListe mannliste = new MannschaftenListe();
		Mannschaft mannschaft = new Mannschaft("a", "m", "Aktive");
		Mannschaft mann2much = new Mannschaft("b", "w", "A-Jugend"); /* mit dieser Mannschaft soll die maximale Anzahl 
																		überschritten werden und es soll in die Exception 
																		gehen*/
		
			try {
				for(int i = 0; i < 72; i++) {
					mannliste.mannschaftHinzufuegen(mannschaft);
				}
				mannliste.mannschaftHinzufuegen(mann2much);
				
			}catch (MaxAnzMannschaftenAusnahme e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		
		

	}

	@Test
	void printTest() throws MaxAnzMannschaftenAusnahme, NullPointerAusnahme {
		MannschaftenListe mannliste = new MannschaftenListe();
		Mannschaft m1 = new Mannschaft("a", "m", "Aktive");

		for (int i = 0; i < 72; i++) {
			mannliste.mannschaftHinzufuegen(m1);
		}

		mannliste.print();
		try {
			System.out.println("1");
			mannliste.print();

		} catch (NullPointerAusnahme e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

	@Test
	void listeSollteNichtGeprintetWerden() {
		MannschaftenListe newmanliste = new MannschaftenListe();

		try {
			newmanliste.print();
		} catch (Exception e) {
			// TODO: handle exception

			System.out.println("Fehler");

		}
	}

	@Test
	void sortierenTest() throws MaxAnzMannschaftenAusnahme {
		MannschaftenListe ml = new MannschaftenListe();
		MannschaftenListe sortliste = new MannschaftenListe();

		try {
			Mannschaft m1 = new Mannschaft("a", "w", "Aktive");
			Mannschaft m2 = new Mannschaft("b", "m", "A-Jugend");
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "root", "!13Renee601!");
			Statement stmt = con.createStatement();
			
			for (int i = 0; i < 36; i++) {
				ml.mannschaftHinzufuegen(m1);
				ml.mannschaftHinzufuegen(m2);
			}

			ml.sortieren();
			
			String query = "SELECT * FROM handball.frauen WHERE frauenID=0";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				m1.setName(rs.getString("name"));
				m1.setMow(rs.getString("mow"));
				m1.setJahrgang(rs.getString("jahrgang"));
			}
			rs.close();

			String expected = "w";
			String actual = m1.getMow();

			assertEquals(expected, actual);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Fehler");
		}

		try {

			Mannschaft m1 = new Mannschaft("a", "m", "Aktive");
			Mannschaft m2 = new Mannschaft("b", "w", "Aktive");
			Mannschaft m3 = new Mannschaft("c", "m", "A-Jugend");
			Mannschaft m4 = new Mannschaft("d", "m", "B-Jugend");
			Mannschaft m5 = new Mannschaft("e", "w", "A-Jugend");
			Mannschaft m6 = new Mannschaft("f", "w", "B-Jugend");
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "root", "!13Renee601!");
			Statement stmt = con.createStatement();
			
			for (int i = 0; i < 12; i++) {
				sortliste.mannschaftHinzufuegen(m1);
				sortliste.mannschaftHinzufuegen(m2);
				sortliste.mannschaftHinzufuegen(m3);
				sortliste.mannschaftHinzufuegen(m4);
				sortliste.mannschaftHinzufuegen(m5);
				sortliste.mannschaftHinzufuegen(m6);
			}

			sortliste.sortieren();
			
			String query = "SELECT * FROM handball.männer WHERE männerID=0";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				m1.setName(rs.getString("name"));
				m1.setMow(rs.getString("mow"));
				m1.setJahrgang(rs.getString("jahrgang"));
			}
			rs.close();

			String expected = "m";
			String actual = m1.getMow();

			assertEquals(expected, actual);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Fehler");
		}

	}

	@Test
	void gruppenErstellenTest() throws MaxAnzMannschaftenAusnahme {
		MannschaftenListe ml = new MannschaftenListe();
		MannschaftenListe sortliste = new MannschaftenListe();

		try {
			Mannschaft m1 = new Mannschaft("a", "w", "Aktive");
			Mannschaft m2 = new Mannschaft("b", "m", "A-Jugend");
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "root", "!13Renee601!");
			Statement stmt = con.createStatement();
			
			for (int i = 0; i < 36; i++) {
				ml.mannschaftHinzufuegen(m1);
				ml.mannschaftHinzufuegen(m2);
			}

			ml.sortieren();
			
			String query = "SELECT * FROM handball.FG1 WHERE FG1ID=0";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				m1.setName(rs.getString("name"));
				m1.setMow(rs.getString("mow"));
				m1.setJahrgang(rs.getString("jahrgang"));
			}
			rs.close();

			String expected = "w";
			String actual = m1.getMow();

			assertEquals(expected, actual);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Fehler");
		}

		try {

			Mannschaft m1 = new Mannschaft("a", "m", "Aktive");
			Mannschaft m2 = new Mannschaft("b", "w", "Aktive");
			Mannschaft m3 = new Mannschaft("c", "m", "A-Jugend");
			Mannschaft m4 = new Mannschaft("d", "m", "B-Jugend");
			Mannschaft m5 = new Mannschaft("e", "w", "A-Jugend");
			Mannschaft m6 = new Mannschaft("f", "w", "B-Jugend");
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "root", "!13Renee601!");
			Statement stmt = con.createStatement();
			
			for (int i = 0; i < 12; i++) {
				sortliste.mannschaftHinzufuegen(m1);
				sortliste.mannschaftHinzufuegen(m2);
				sortliste.mannschaftHinzufuegen(m3);
				sortliste.mannschaftHinzufuegen(m4);
				sortliste.mannschaftHinzufuegen(m5);
				sortliste.mannschaftHinzufuegen(m6);
			}

			sortliste.sortieren();
			
			String query = "SELECT * FROM handball.MG1 WHERE MG1ID=0";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				m1.setName(rs.getString("name"));
				m1.setMow(rs.getString("mow"));
				m1.setJahrgang(rs.getString("jahrgang"));
			}
			rs.close();

			String expected = "m";
			String actual = m1.getMow();

			assertEquals(expected, actual);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Fehler");
		}

	}

}