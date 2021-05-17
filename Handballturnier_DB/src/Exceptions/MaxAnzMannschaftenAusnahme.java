package Exceptions;

/**
 * Diese Ausnahme wird geworfen, sobald die Anzahl an maximal möglich teilnehmenden Mannschaften überschritten wird.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */
public class MaxAnzMannschaftenAusnahme extends Exception{

	public MaxAnzMannschaftenAusnahme() {
		System.out.println("Anzahl der maximal zugelassenen Mannschaften überschritten!");
	}
}
