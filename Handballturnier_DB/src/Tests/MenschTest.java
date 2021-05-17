package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Zuordnung.Schiedsrichter;
import Zuordnung.Zeitnehmer;

class MenschTest {

	@Test
	void test() {
		Schiedsrichter schiri = new Schiedsrichter("a");
		assertTrue(schiri instanceof Schiedsrichter);

		Zeitnehmer zeitnehmer = new Zeitnehmer("b");
		assertTrue(zeitnehmer instanceof Zeitnehmer);

	}

}
