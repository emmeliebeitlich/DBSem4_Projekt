package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Exceptions.MaxAnzMannschaftenAusnahme;
import Mannschaften.Mannschaft;

class MannschaftTest {

	@Test
	void mannschaftSollteAngelegtWerden() throws MaxAnzMannschaftenAusnahme {
		Mannschaft mannschaft = new Mannschaft("a", "m", "Aktive");
		assertTrue(mannschaft instanceof Mannschaft);
	}

}
