package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Exceptions.NullPointerAusnahme;
import Spielphase.Spiele;
import Spielphase.TabelleErstellen;

class TabelleErstellenTest {

	@Test
	void tabelleErstellenTest() throws NullPointerAusnahme {
		Spiele s = new Spiele(0, 0, null, null, 0, 0, 0, 0, 0);
		TabelleErstellen tab = new TabelleErstellen(s);

		assertTrue(tab instanceof TabelleErstellen);

	}

}
