package Main;

import Zuordnung.*;
import Exceptions.*;
import Mannschaften.*;
import Spielphase.*;

/**
 * Diese Klasse stellt die main-Methode dar. Hier werden die Mannschaften
 * hinzugefügt und das Turnier simuliert.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 * 
 * @pre es müssen 72 Mannschaften angemeldet sein, damit es 12 Gruppen à 3
 *      Mannschaften geben kann, von den 72 Mannschaften müssen 6 Mannschaften
 *      Männer Aktive sein, 6 Mannschaften Frauen Aktive, 6 männliche A-Jugend,
 *      6 männliche B-Jugend, 6 weibliche A-Jugend und 6 weibliche B-Jugend
 *
 */
public class Main { 

	/**
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Hallen halle = new Hallen(null, null, null);
		PersonenZuordnen person = new PersonenZuordnen(null, null);
		Zuordnung zuordnung = new Zuordnung(null, null, null, null, null);
		MannschaftenListe manliste = new MannschaftenListe();
		Spiele spielplan = new Spiele(0, 0, null, null, 0, 0, 0, 0, 0);
		TabelleErstellen tabelle;
		Spiele spielplanKOSpiele = new Spiele(0, 0, null, null, 0, 0, 0, 0, 0);

		/**
		 * Hallen hinzufügen
		 */
		try {
			halle.halleHinzufuegen(new Hallen("Rundsporthalle", "Lange Straße", "Stuttgart"));
		} catch (maxAnzHallenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			halle.halleHinzufuegen(new Hallen("Sporthalle Königswiesen", "Hauptstraße", "Stuttgart"));
		} catch (maxAnzHallenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			halle.halleHinzufuegen(new Hallen("Georg-Scherer-Halle", "Schulstraße", "Stuttgart"));
		} catch (maxAnzHallenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			halle.halleHinzufuegen(new Hallen("Sporthalle am Sportzentrum", "Bahnhofstraße", "Stuttgart"));
		} catch (maxAnzHallenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			halle.halleHinzufuegen(new Hallen("Stadionhalle", "Kirchstraße", "Stuttgart"));
		} catch (maxAnzHallenAusnahme e3) {
			// TODO Auto-generated catch block 
			e3.printStackTrace();
		}
		try {
			halle.halleHinzufuegen(new Hallen("Langhanshalle", "Schillerstraße", "Stuttgart"));
		} catch (maxAnzHallenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} 
		
		 
		

		/**
		 * Personen hinzufügen
		 * 
		 * Die Namen der Personen dienen nur der Veranschaulichung, hierbei handelt es
		 * sich nicht um echte Personen
		 */
		
		try {
			person.personenHinzufuegen(new Schiedsrichter("Max Mustermann"), new Zeitnehmer("Hans Schmidt"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Lena Keller"), new Zeitnehmer("Frank Hauser"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Jürgen Wagner"), new Zeitnehmer("Marie Schneider"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Maximilian Andreas"), new Zeitnehmer("Paul Weigert"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Leon Busche"), new Zeitnehmer("Lucas Hahn"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Axel Eder"), new Zeitnehmer("Sophie Richter"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Luisa Wager"), new Zeitnehmer("Anne Irmer"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Tim Wagenknecht"), new Zeitnehmer("Klara Fischer"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Felix Weiß"), new Zeitnehmer("Moritz Frech"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Jonas Heinz"), new Zeitnehmer("Lina König"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Christoph Meyer"), new Zeitnehmer("Stephie Koch"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			person.personenHinzufuegen(new Schiedsrichter("Louis Klein"), new Zeitnehmer("Henrik Neumann"));
		} catch (maxAnzPersonenAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		

		/**
		 * Hallen und Personen einander zuordnen
		 */

		for (int i = 0; i < 6; i++) {
			try {
				zuordnung.setHallenListe(halle, i);
			} catch (NullPointerAusnahme e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i = 0; i<6; i++) {
			try {
				zuordnung.setPersonenListe(person, i);
			} catch (NullPointerAusnahme e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		}
		try {
			zuordnung.printListe();
		} catch (NullPointerAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		/**
		 * Mannschaften hinzufügen
		 */
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ASV Dachau", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ASV Dachau", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ASV Dachau", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ASV Dachau", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ASV Dachau", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ASV Dachau", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("SV Allensbach", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("SV Allensbach", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("SV Allensbach", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("SV Allensbach", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("SV Allensbach", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("SV Allensbach", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Haunstetten", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Haunstetten", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Haunstetten", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Haunstetten", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Haunstetten", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Haunstetten", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ESV Regensburg", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ESV Regensburg", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ESV Regensburg", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ESV Regensburg", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ESV Regensburg", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("ESV Regensburg", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Wolfschlugen", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Wolfschlugen", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Wolfschlugen", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Wolfschlugen", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Wolfschlugen", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Wolfschlugen", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("Tus Metzingen", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("Tus Metzingen", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("Tus Metzingen", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("Tus Metzingen", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("Tus Metzingen", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("Tus Metzingen", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TS Herzogenaurach", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TS Herzogenaurach", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TS Herzogenaurach", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TS Herzogenaurach", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TS Herzogenaurach", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TS Herzogenaurach", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TV Nellingen", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TV Nellingen", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TV Nellingen", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TV Nellingen", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TV Nellingen", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TV Nellingen", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("FA Göppingen", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("FA Göppingen", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("FA Göppingen", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("FA Göppingen", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("FA Göppingen", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("FA Göppingen", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Heiningen", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Heiningen", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Heiningen", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Heiningen", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Heiningen", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("TSV Heiningen", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HSG Würm-Mitte", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HSG Würm-Mitte", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HSG Würm-Mitte", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HSG Würm-Mitte", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HSG Würm-Mitte", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HSG Würm-Mitte", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HCD Gröbenzell", "m", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HCD Gröbenzell", "m", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HCD Gröbenzell", "m", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HCD Gröbenzell", "w", "Aktive"));
		} catch (MaxAnzMannschaftenAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HCD Gröbenzell", "w", "A-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			manliste.mannschaftHinzufuegen(new Mannschaft("HCD Gröbenzell", "w", "B-Jugend"));
		} catch (MaxAnzMannschaftenAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			manliste.print();
		} catch (NullPointerAusnahme e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		/*
		 * Mannschaften sortieren
		 */
		try {
			manliste.sortieren();
		} catch (NullPointerAusnahme e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		/**
		 * Gruppen erstellen
		 */
		
		try {
			manliste.gruppenErstellen();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			manliste.printGruppenliste();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * Spielplan Gruppenphase erstellen
		 */

		try {
			spielplan.spielplanGruppenphaseErstellen();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			spielplan.printSpielplan();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * Ergebnisse der Spiele eintragen
		 */
		try {
			spielplan.ergebnisEintragen();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * Tabelle erstellen nach Gruppenphase
		 */

		tabelle = new TabelleErstellen(spielplan);
		try {
			tabelle.gruppenTabelleErstellen();
		} catch (NullPointerAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			spielplanKOSpiele.setTabelle(tabelle);
		} catch (NullPointerAusnahme e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/**
		 * Spielpläne K.O.-Runden erstellen & Ergebnisse eintragen
		 */

		try {
			spielplanKOSpiele.spielplanVF();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			spielplanKOSpiele.ergebnisVF();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			spielplanKOSpiele.spielplanHF();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			spielplanKOSpiele.ergebnisHF();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			spielplanKOSpiele.spielplanFinale();
		} catch (NullPointerAusnahme e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			spielplanKOSpiele.ergebnisFinale();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			spielplanKOSpiele.printSpielplanKO();
		} catch (NullPointerAusnahme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

} 
