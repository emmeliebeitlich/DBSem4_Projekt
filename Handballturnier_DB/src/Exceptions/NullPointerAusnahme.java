package Exceptions;

/**
 * Diese Ausnahme wird geworfen, sobald eine Tabelle, die verwendet werden soll, leer ist.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */

public class NullPointerAusnahme extends Exception{

	public NullPointerAusnahme() {
		System.out.println("Die übergebene Tabelle ist leer!");
	} 
}
