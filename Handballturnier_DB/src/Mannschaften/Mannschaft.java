package Mannschaften;

/**
 * Mit dieser Klasse soll die Grundlage für dieses Programm geschaffen werden.
 * Hier wird eine Mannschaft definiert durch ihren Namen, das Geschlecht und den
 * Jahrgang.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */
public class Mannschaft {

	private String name;
	private String mow;
	private String jahrgang;

	/**
	 * Konstruktor
	 * 
	 * @param name     Name der Mannschaft
	 * @param mow      Angabe, ob Mannschaft männlich oder weiblich ist
	 * @param jahrgang Angabe, ob es eine Jugend- oder eine Aktiven-Mannschaft ist
	 *                 Ist es eine Jugend-Mannschaft, Angabe welcher Jahrgang
	 *                 (A-/B-Jugend)
	 */
 
	public Mannschaft(String name, String mow, String jahrgang) {
		this.name = name;
		this.mow = mow;
		this.jahrgang = jahrgang;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMow() {
		return mow;
	}

	public void setMow(String mow) {
		this.mow = mow;
	}

	public String getJahrgang() {
		return jahrgang;
	}

	public void setJahrgang(String jahrgang) {
		this.jahrgang = jahrgang;
	}
}
