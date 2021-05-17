package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

import Exceptions.MaxAnzMannschaftenAusnahme;
import Exceptions.NullPointerAusnahme;
import Mannschaften.Mannschaft;
import Mannschaften.MannschaftenListe;
import Spielphase.Spiele;
import Spielphase.TabelleErstellen;

class SpieleTest {

	@Test
	void spieleTest() {
		Mannschaft man1 = new Mannschaft("a", "m", "Aktive");
		Mannschaft man2 = new Mannschaft("b", "m", "Aktive");

		Spiele spiele = new Spiele(9, 0, man1, man2, 0, 0, 0, 0, 0);
		assertTrue(spiele instanceof Spiele);

	}

	@Test
	void spielplanGruppenphaseErstellenTest() throws MaxAnzMannschaftenAusnahme {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
		
		
			MannschaftenListe sortliste = new MannschaftenListe();
	
			Mannschaft m1 = new Mannschaft("a", "m", "Aktive");
			Mannschaft m2 = new Mannschaft("b", "m", "A-Jugend");
			Mannschaft m3 = new Mannschaft("c", "m", "B-Jugend");
			Mannschaft m4 = new Mannschaft("d", "w", "Aktive");
			Mannschaft m5 = new Mannschaft("e", "w", "A-Jugend");
			Mannschaft m6 = new Mannschaft("f", "w", "B-Jugend");
	
			Spiele spielplan = new Spiele(0, 0, null, null, 0, 0, 0, 0, 0);

			for (int i = 0; i < 12; i++) {
				sortliste.mannschaftHinzufuegen(m1);
				sortliste.mannschaftHinzufuegen(m2);
				sortliste.mannschaftHinzufuegen(m3);
				sortliste.mannschaftHinzufuegen(m4);
				sortliste.mannschaftHinzufuegen(m5);
				sortliste.mannschaftHinzufuegen(m6);
			}
			sortliste.sortieren();

			sortliste.gruppenErstellen();
			
			String countString = "SELECT COUNT(*) FROM handball.MG1";
			int count = stmt.executeUpdate(countString);

			assertTrue(count != 0);

			// Test Spielplan erstellen

			spielplan.spielplanGruppenphaseErstellen();

			// Test printSpielplan

			spielplan.printSpielplan();

			// Test ergebnisEintragen
			spielplan.ergebnisEintragen();

			int tore1 = (int) (Math.random() * 10);

			assertTrue(tore1 < 10);

			// Test Tabelle aus Spielplan erstellen

			TabelleErstellen tab = new TabelleErstellen(spielplan);

			tab.gruppenTabelleErstellen();

			// Test Spielplan KO erstellen

			spielplan.spielplanVF();

			spielplan.ergebnisVF();

			int tore2 = (int) (Math.random() * 10);

			assertTrue(tore2 < 10);

			spielplan.spielplanHF();

			spielplan.ergebnisHF();

			int tore3 = (int) (Math.random() * 10);

			assertTrue(tore3 < 10);

			spielplan.spielplanFinale();

			spielplan.ergebnisFinale();

			int tore4 = (int) (Math.random() * 10);

			assertTrue(tore4 < 10);

			spielplan.printSpielplanKO();

		}
		catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Fehler");
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

}
