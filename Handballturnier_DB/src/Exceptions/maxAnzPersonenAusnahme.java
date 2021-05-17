
package Exceptions;

/**
 * Diese Ausnahme wird geworfen, wenn die maximale Anzahl der benötigten Schiedsrichter und Zeitnehmer überschritten wird.
 *  
 * @author emmeliebeitlich
 * @version 1.0
 *
 */

public class maxAnzPersonenAusnahme extends Exception{

	public maxAnzPersonenAusnahme() {
		System.out.println("Anzahl der maximal benötigten Personen überschritten!");
	} 
}
