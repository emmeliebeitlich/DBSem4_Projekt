package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Exceptions.maxAnzHallenAusnahme;
import Zuordnung.Hallen;

class HallenTest {

	@Test
	void HallenTest() {
		Hallen hallen = new Hallen(null, null, null);

		assertTrue(hallen instanceof Hallen);

	}

	@Test
	void halleHinzufuegenTest() {
		Hallen hallen = new Hallen(null, null, null);
		Hallen halle = new Hallen("a", "b", "c");
		try {
			hallen.halleHinzufuegen(halle);
		} catch (maxAnzHallenAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Fehler");
		}

	}
	
	@Test
	void halleKannNichtHinzugefügtWerden() {
		Hallen hallen = new Hallen(null, null, null);
		Hallen halle = new Hallen("a", "b", "c");
		Hallen halle2much = new Hallen("b", "c", "d"); // bei zufügen dieser Halle wird die max Anzahl überschritten
		
		try {
			for(int i = 0; i < 6; i++) {
				hallen.halleHinzufuegen(halle);
			}
			hallen.halleHinzufuegen(halle2much);
			
		}
		catch (maxAnzHallenAusnahme e) {
			System.out.println(e.getMessage());
		}
	}

}
