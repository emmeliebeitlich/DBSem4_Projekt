package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Year;

import org.junit.jupiter.api.Test;

import Exceptions.maxAnzPersonenAusnahme;
import Zuordnung.Mensch;
import Zuordnung.PersonenZuordnen;
import Zuordnung.Schiedsrichter;
import Zuordnung.Zeitnehmer;

class PersonenZuordnenTest {

	@Test
	void PersonenZuordnenTest() {
		PersonenZuordnen personen = new PersonenZuordnen("a", "schiedsrichter");
		assertTrue(personen instanceof PersonenZuordnen);
	}

	@Test
	void personenHinzufuegenTest() throws maxAnzPersonenAusnahme {
		PersonenZuordnen personen = new PersonenZuordnen("a", "schiedsrichter");
		Schiedsrichter schiri = new Schiedsrichter(null);
		Zeitnehmer zeitn = new Zeitnehmer(null);

		personen.personenHinzufuegen(schiri, zeitn);

		try {
			String expected = "schiedsrichter";
			String actual = personen.getPesonenJob(0);

			assertEquals(expected, actual);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Fehler");
		}

	}
	
	@Test
	void personKannNichtHinzugefuegtWerden() {
		PersonenZuordnen personen = new PersonenZuordnen("a", "schiedsrichter");
		Schiedsrichter schiri = new Schiedsrichter(null);
		Zeitnehmer zeitn = new Zeitnehmer(null);
		Schiedsrichter schiri2much = new Schiedsrichter(null); // Schiri und Zeitnehmer werden zu viel hinzugefügt
		Zeitnehmer zeitn2much = new Zeitnehmer(null); // max Anzahl überschritten
		
		try {
			for(int i = 0; i < 12; i++) {
				personen.personenHinzufuegen(schiri, zeitn);
			}
			personen.personenHinzufuegen(schiri2much, zeitn2much);
		}
		catch (maxAnzPersonenAusnahme e) {
			System.out.println(e.getMessage());
		}
		
	}

}
