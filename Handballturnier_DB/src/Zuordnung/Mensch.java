package Zuordnung;

/**
 * Diese Klasse dient als super-Klasse für den Schiedsrichter und den
 * Zeitnehmer.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */

public abstract class Mensch {

	private String name;
	private String job;

	/**
	 * 
	 * @param name
	 * @param job
	 */
	public Mensch(String name, String job) {
		this.name = name;
		this.job = job; 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

}
