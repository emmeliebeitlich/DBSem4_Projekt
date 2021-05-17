package Spielphase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Year;
import java.util.Random;

import Exceptions.NullPointerAusnahme;
import Mannschaften.*;

/**
 * In dieser Klasse wird die Tabelle aus den Spielergebnissen generiert.
 * 
 * @author emmeliebeitlich
 * @version 1.0
 *
 */
public class TabelleErstellen {
	
	private Mannschaft platz1;
	private Mannschaft platz2;
	private Mannschaft sieger, verlierer;
	
	private Spiele s;
	
	private String gruppe;
	
	private int tore1, tore2, tore3;
	private int punkte1, punkte2, punkte3;
	private int random1, random2, random3;
	private String gegner1, gegner2;
	private int random;
	private TabelleErstellen tabelle;
	
	private String name;
	private String mow;
	private String jahrgang;

	/**
	 * 
	 * @param S, Objekt von Spiele
	 */
	public TabelleErstellen(Spiele S) {
		this.s = S;
	}


	/**
	 * In dieser Methode wird die Tabelle aus der Gruppenphase generiert. Dafür
	 * werden die Punkte, die die Mannschaften durch die Spiele gewinnen konnten,
	 * zusammengezählt. Haben zwei Mannschaften gleich viele Punkte, kommt es zum
	 * direkten Vergleich, das heißt das Spiel der beiden Mannschaften wird
	 * betrachtet, der Gewinner bekommt den vorderen Platz, der Verlierer den
	 * hinteren. Kam es bei dem Spiel jedoch zum Unentschieden, wird die Anzahl der
	 * gesamt erzielten Tore betrachtet. Die Mannschaft mit der größeren Anzahl an
	 * Toren bekommt den vorderen Platz. Haben alle Mannschaften in der Gruppe
	 * gleich viele Punkte, wird der direkte Vergleich unwirksam und es werden
	 * direkt die geworfenen Tore betrachtet und daraus die Tabelle errechnet. Ist
	 * nun aber die Anzahl der geworfenen Tore auch gleich, soll ein 7m-Werfen
	 * simuliert werden und eine zufällige Zahl generiert den jeweiligen Tabellenplatz.
	 * 
	 * @pre der Spielplan darf nicht leer sein und die Ergebnisse müssen eingetragen
	 *      sein
	 * @post es wurde eine Tabelle erstellt und die Platzierung steht fest
	 * @throws NullPointerAusnahme, wenn der übergebene Spielplan leer ist-
	 */

	public void gruppenTabelleErstellen() throws NullPointerAusnahme {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();
			
			String countString = "SELECT COUNT(*) AS total FROM handball.spielplan";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){ 
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} 
			
			for (int i = 0; i < 24; i++) {

				String query = "SELECT * FROM spielplan WHERE spielplanID=" + (i*3);
				ResultSet rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					gruppe = rs.getString("gruppe");
				}
				
				
				/*
				 * nach absteigender Punktzahl sortieren, 
				 * wenn die gleiche Anzahl an Punkten besteht, wird nach Toren sortiert
				 * wenn alle drei Mannschaften zudem dieselbe Toranzahl haben, wird ein 7m Schießen über eine zufällige 
				 * Zahl simuliert und danach sortiert
				 */
				
				// Neue Spalte mit Rang hinzufügen
				String add = "ALTER TABLE " + gruppe + " ADD rang int NOT NULL";
				stmt.executeUpdate(add);
			
				// sortieren, zuerst nach punkten, dann nach toren, dann nach random
				String order = "SELECT * FROM " + gruppe + " WHERE " + gruppe + "ID=0";
				ResultSet rset1 = stmt.executeQuery(order); 
				
				while(rset1.next()) {
					punkte1 = rset1.getInt("punkte");
					tore1 = rset1.getInt("tore");
					random1 = rset1.getInt("random");
				} rset1.close();
				
				order = "SELECT * FROM " + gruppe + " WHERE " + gruppe + "ID=1";
				ResultSet rset2 = stmt.executeQuery(order); 
				
				while(rset2.next()) {
					punkte2 = rset2.getInt("punkte");
					tore2 = rset2.getInt("tore");
					random2 = rset2.getInt("random");
				} rset2.close();
				
				order = "SELECT * FROM " + gruppe + " WHERE " + gruppe + "ID=2";
				ResultSet rset3 = stmt.executeQuery(order); 
				
				while(rset3.next()) {
					punkte3 = rset3.getInt("punkte");
					tore3 = rset3.getInt("tore");
					random3 = rset3.getInt("random");
				} rset3.close();
				
				if(punkte1 > punkte2) {
					if(punkte1 > punkte3) {
						String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=0";
						stmt.executeUpdate(rang1);
						
						if(punkte2 > punkte3) {
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang3);
						}
						else if(punkte2 < punkte3) {
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang3);
						}
						else {
							if(tore2 > tore3) {
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang3);
							}
							else if (tore2 < tore3) {
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang3);
							}
							else {
								if(random2 > random3) {
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang3);
								}
								else {
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang3);
								}
							}
						}
						
					}
					else if (punkte1 < punkte3) {
						String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
						stmt.executeUpdate(rang1);
						
						String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
						stmt.executeUpdate(rang2);
						
						String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
						stmt.executeUpdate(rang3);
					}
					// punkte1 = punkte3
					else {
						if(tore1 > tore3) {
							String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang1);
							
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang3);
						}
						else if (tore1 < tore3) {
							String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang1);
							
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang3);
						}
						else {
							if(random1 > random3) {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang3);
							}
							else if (random1 < random3) {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang3);
							}
						}
					}
				}
				else if(punkte1 < punkte2) {
					if (punkte2 > punkte3) {
						String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
						stmt.executeUpdate(rang1);
						
						if(punkte1 > punkte3) {
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang3);
						}
						else if(punkte1 < punkte3) {
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang3);
						}
						else {
							if(tore1 > tore3) {
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang3);
							}
							else if(tore1 < tore3) {
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang3);
							}
							else {
								if(random1 > random3) {
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang3);
								}
								else {
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang3);
								}
							}
						}
					}
					else if(punkte2 < punkte3) {
						String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
						stmt.executeUpdate(rang1);
						
						String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
						stmt.executeUpdate(rang2);
						
						String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
						stmt.executeUpdate(rang3);
					}
					else {
						if(tore2 > tore3) {
							String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang1);
							
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang3);
						}
						else if (tore2 < tore3) {
							String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang1);
							
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang3);
						}
						else {
							if(random2 > random3) {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang3);
							}
							else {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang3);
							}
						}
					}
				}
				// punkte1 = punkte2
				else {
					if(punkte1 > punkte3) {
						if(tore1 > tore2) {
							String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang1);
							
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang3);
						}
						else if (tore1 < tore2) {
							String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang1);
							
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang3);
						}
						else {
							if(random1 > random2) {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang3);
							}
							else {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang3);
							}
						}
					}
					else if(punkte1 < punkte3) {
						if(tore1 > tore2) {
							String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang1);
							
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang3);
						}
						else if (tore1 < tore2) {
							String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
							stmt.executeUpdate(rang1);
							
							String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
							stmt.executeUpdate(rang2);
							
							String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
							stmt.executeUpdate(rang3);
						}
						else {
							if(random1 > random2) {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang3);
							}
							else {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang3);
							}
						}
					}
					else {
						if(tore1 > tore2) {
							if(tore1 > tore3) {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang1);
								
								if(tore2 > tore3) {
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang3);
								}
								else if (tore2 < tore3) {
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang3);
								}
								else {
									if(random2 > random3) {
										String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
										stmt.executeUpdate(rang2);
										
										String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
										stmt.executeUpdate(rang3);
									}
									else {
										String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
										stmt.executeUpdate(rang2);
										
										String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
										stmt.executeUpdate(rang3);
									}
								}
							}
							else if(tore1 < tore3) {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang3);
							}
							else {
								if(random1 > random3) {
									String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang1);
									
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang3);
								}
								else {
									String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang1);
									
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang3);
								}
							}
						}
						else if(tore1 < tore2) {
							if(tore2 > tore3) {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang1);
								
								if(tore1 > tore3) {
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang3);
								}
								else if (tore1 < tore3) {
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang3);
								}
								else {
									if(random1 > random3) {
										String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
										stmt.executeUpdate(rang2);
										
										String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
										stmt.executeUpdate(rang3);
									}
									else {
										String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
										stmt.executeUpdate(rang2);
										
										String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
										stmt.executeUpdate(rang3);
									}
								}
							}
							else if(tore2 < tore3) {
								String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
								stmt.executeUpdate(rang1);
								
								String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
								stmt.executeUpdate(rang2);
								
								String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
								stmt.executeUpdate(rang3);
							}
							else {
								if(random2 > random3) {
									String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang1);
									
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang3);
								}
								else {
									String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang1);
									
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang3);
								}
							}
						}
						// tore1 = tore2
						else {
							if(tore1 > tore3) {
								if(random1 > random2) {
									String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang1);
									
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang3);
								}
								else {
									String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang1);
									
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang3);
								}
							}
							else if(tore1 < tore3) {
								if(random1 > random2) {
									String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang1);
									
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang3);
								}
								else {
									String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
									stmt.executeUpdate(rang1);
									
									String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
									stmt.executeUpdate(rang2);
									
									String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
									stmt.executeUpdate(rang3);
								}
							}
							// tore1 = tore2 = tore3
							else {
								if(random1 > random2) {
									if(random1 > random3) {
										String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=0";
										stmt.executeUpdate(rang1);
										
										if(random2 > random3) {
											String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
											stmt.executeUpdate(rang2);
											
											String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
											stmt.executeUpdate(rang3);
										}
										else {
											String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
											stmt.executeUpdate(rang2);
											
											String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
											stmt.executeUpdate(rang3);
										}
									}
									else {
										String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
										stmt.executeUpdate(rang1);
										
										String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
										stmt.executeUpdate(rang2);
										
										String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=1";
										stmt.executeUpdate(rang3);
									}
								}
								else {
									if(random2 > random3) {
										if(random1 > random3) {
											String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
											stmt.executeUpdate(rang1);
											
											String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=0";
											stmt.executeUpdate(rang2);
											
											String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=2";
											stmt.executeUpdate(rang3);
										}
										else {
											String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=1";
											stmt.executeUpdate(rang1);
											
											String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=2";
											stmt.executeUpdate(rang2);
											
											String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
											stmt.executeUpdate(rang3);
										}
										
									}
									else {
										String rang1 = "UPDATE " + gruppe + " SET `rang` = 1 WHERE " + gruppe + "ID=2";
										stmt.executeUpdate(rang1);
										
										String rang2 = "UPDATE " + gruppe + " SET `rang` = 2 WHERE " + gruppe + "ID=1";
										stmt.executeUpdate(rang2);
										
										String rang3 = "UPDATE " + gruppe + " SET `rang` = 3 WHERE " + gruppe + "ID=0";
										stmt.executeUpdate(rang3);
									}
								}
							}
						}
					}
				}

			} 
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public Mannschaft getSiegerVF(int index) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft sieger = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();

			String countString = "SELECT COUNT(*) AS total FROM spielplanVF";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			
			String query = "SELECT * FROM spielplanVF WHERE `spielplanVFID`=" + index;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				tore1 = rs.getInt("tore1");
				tore2 = rs.getInt("tore2");
			
			
			if(tore1 > tore2) {
					sieger.setName(rs.getString("gegner1"));
					sieger.setJahrgang(rs.getString("jahrgang"));
					sieger.setMow(rs.getString("mow"));
			}
			else if (tore1 < tore2) {
					sieger.setName(rs.getString("gegner2"));
					sieger.setJahrgang(rs.getString("jahrgang"));
					sieger.setMow(rs.getString("mow"));
				
			}
			/*
			 * bei einem Unentschieden wird ein 7m Schießen pber eine zufällige Zahl simuliert
			 */
			else {
				random = (int) ((Math.random() * 2) + 1);
					sieger.setName(rs.getString("gegner" + random));
					sieger.setJahrgang(rs.getString("jahrgang"));
					sieger.setMow(rs.getString("mow"));
				}
			}
		rs.close();
		return sieger;
			
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	public Mannschaft getSiegerHF(int index) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft sieger = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();

			String countString = "SELECT COUNT(*) AS total FROM spielplanHF";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			
			String query = "SELECT * FROM spielplanHF WHERE `spielplanHFID`=" + index;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				tore1 = rs.getInt("tore1");
				tore2 = rs.getInt("tore2");
			
			
			if(tore1 > tore2) {
					sieger.setName(rs.getString("gegner1"));
					sieger.setJahrgang(rs.getString("jahrgang"));
					sieger.setMow(rs.getString("mow"));
			}
			else if (tore1 < tore2) {
					sieger.setName(rs.getString("gegner2"));
					sieger.setJahrgang(rs.getString("jahrgang"));
					sieger.setMow(rs.getString("mow"));
			}
			/*
			 * bei einem Unentschieden wird ein 7m Schießen pber eine zufällige Zahl simuliert
			 */
			else {
				random = (int) ((Math.random() * 2) + 1);
					sieger.setName(rs.getString("gegner" + random));
					sieger.setJahrgang(rs.getString("jahrgang"));
					sieger.setMow(rs.getString("mow"));
			}
			}
			rs.close();
			return sieger;
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	public Mannschaft getVerliererHF(int index) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			TabelleErstellen tabelle = new TabelleErstellen(s);
			Mannschaft verlierer = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();

			String countString = "SELECT COUNT(*) AS total FROM spielplanHF";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			
			String query = "SELECT * FROM spielplanHF WHERE `spielplanHFID`=" + index;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				tore1 = rs.getInt("tore1");
				tore2 = rs.getInt("tore2");
				gegner1 = rs.getString("gegner1");
				gegner2 = rs.getString("gegner2");
			
			
			if(tore1 < tore2) {
					verlierer.setName(rs.getString("gegner1"));
					verlierer.setJahrgang(rs.getString("jahrgang"));
					verlierer.setMow(rs.getString("mow"));
			}
			else if (tore1 > tore2) {
					verlierer.setName(rs.getString("gegner2"));
					verlierer.setJahrgang(rs.getString("jahrgang"));
					verlierer.setMow(rs.getString("mow"));
			}
			/*
			 * bei einem Unentschieden wird ein 7m Schießen über eine zufällige Zahl simuliert
			 */
			else {
					if(tabelle.getSiegerHF(index).getName().equals(gegner1)) {
						verlierer.setName(rs.getString("gegner2"));
						verlierer.setJahrgang(rs.getString("jahrgang"));
						verlierer.setMow(rs.getString("mow"));
					}
					else {
						verlierer.setName(rs.getString("gegner1"));
						verlierer.setJahrgang(rs.getString("jahrgang"));
						verlierer.setMow(rs.getString("mow"));
					}
					
				
			}
			} rs.close();
			return verlierer;
			
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}

	public Mannschaft getPlatz1(String gruppe) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft platz1 = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();

			String countString = "SELECT COUNT(*) AS total FROM " + gruppe;
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			
			String query = "SELECT * FROM " + gruppe + " WHERE `rang`=1";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				platz1.setName(rs.getString("name"));
				platz1.setJahrgang(rs.getString("jahrgang"));
				platz1.setMow(rs.getString("mow"));
			}
			rs.close();
			
			return platz1;
		
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
		
	}

	public void setPlatz1(Mannschaft platz1) {
		this.platz1 = platz1;
	}

	public Mannschaft getPlatz2(String gruppe) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft platz2 = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();

			String countString = "SELECT COUNT(*) AS total FROM " + gruppe;
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			
			String query = "SELECT * FROM " + gruppe + " WHERE `rang`=2";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				platz2.setName(rs.getString("name"));
				platz2.setJahrgang(rs.getString("jahrgang"));
				platz2.setMow(rs.getString("mow"));
			}
			rs.close();

			return platz2;
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}

	public void setPlatz2(Mannschaft platz2) {
		this.platz2 = platz2;
	}

	public Mannschaft getSiegerFinale(int index) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft sieger = new Mannschaft(name, mow, jahrgang);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();

			String countString = "SELECT COUNT(*) AS total FROM spielplanFinale";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme();
				}
			} rset.close();
			
			String query = "SELECT * FROM spielplanFinale WHERE `spielplanFID`=" + index;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				tore1 = rs.getInt("tore1");
				tore2 = rs.getInt("tore2");
			
			
			if(tore1 > tore2) {
					sieger.setName(rs.getString("gegner1"));
					sieger.setJahrgang(rs.getString("jahrgang"));
					sieger.setMow(rs.getString("mow"));
			}
			else if (tore1 < tore2) {
					sieger.setName(rs.getString("gegner2"));
					sieger.setJahrgang(rs.getString("jahrgang"));
					sieger.setMow(rs.getString("mow"));
			}
			/*
			 * bei einem Unentschieden wird ein 7m Schießen pber eine zufällige Zahl simuliert
			 */
			else {
				random = (int) ((Math.random() * 2) + 1);
					sieger.setName(rs.getString("gegner" + random));
					sieger.setJahrgang(rs.getString("jahrgang"));
					sieger.setMow(rs.getString("mow"));
			}
			}
			rs.close();
			return sieger;
			
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
		
		
	}
	
	public Mannschaft getVerliererFinale(int index) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Class not found " + e);
		}
		try {
			Mannschaft verlierer = new Mannschaft(name, mow, jahrgang);
			TabelleErstellen tabelle = new TabelleErstellen(s);
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/handball", "username", "password");
			Statement stmt = con.createStatement();

			String countString = "SELECT COUNT(*) AS total FROM spielplanFinale";
			ResultSet rset = stmt.executeQuery(countString);
			
			int count = 0;
			
			while(rset.next()){
			    count = rset.getInt("total");
			    if (count == 0) {
					throw new NullPointerAusnahme(); 
				}
			} rset.close();
			
			String query = "SELECT * FROM spielplanFinale WHERE `spielplanFID`=" + index;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				tore1 = rs.getInt("tore1");
				tore2 = rs.getInt("tore2");
				gegner1 = rs.getString("gegner1");
				gegner2 = rs.getString("gegner2");
			
				if(tabelle.getSiegerFinale(index).getName().equals(gegner1)) {
					verlierer.setName(rs.getString("gegner2"));
					verlierer.setJahrgang(rs.getString("jahrgang"));
					verlierer.setMow(rs.getString("mow"));
				}
				else {
					verlierer.setName(rs.getString("gegner1"));
					verlierer.setJahrgang(rs.getString("jahrgang"));
					verlierer.setMow(rs.getString("mow"));
				}
			}
			
			rs.close();
			return verlierer;
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}

}
