package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Exceptions.NullPointerAusnahme;
import Zuordnung.PersonenZuordnen;
import Zuordnung.Schiedsrichter;
import Zuordnung.Zuordnung;

/*
 * In dieser Test-Klasse soll die Klasse Zuordnung getestet werden.
 */

class ZuordnungTest {

	@Test
	void test() {
		Zuordnung zuordnung = new Zuordnung(null, null, null, null, null);
		assertTrue(zuordnung instanceof Zuordnung);

	}

	@Test
	void printListeTest() {
		Zuordnung zuordnung = new Zuordnung(null, null, null, null, null);
		try {
			zuordnung.printListe();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Fehler");
		}
	}

}
