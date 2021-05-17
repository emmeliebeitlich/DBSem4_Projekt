package Exceptions;

/**
 * Diese Ausnahme wird geworfen, sobald die Anzahl an maximal benötigten Hallen überschritten wird.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */

public class maxAnzHallenAusnahme extends Exception{
	
	public maxAnzHallenAusnahme() {
		System.out.println("Anzahl an maximal benötigten Hallen überschritten!");
	}
}
