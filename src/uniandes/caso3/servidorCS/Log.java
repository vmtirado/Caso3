package uniandes.caso3.servidorCS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Log {
	public Log() {		
	}
	
	public synchronized void agregarValores(Long tiempo, Double carga) {
		imprimirResultados(tiempo, carga);
	}
	
	/**
	 * Guarda los resultados en un archivo de LOG
	 * No ejecutarlo por si solo !
	 */
	
	private void imprimirResultados(Long tiempo, Double carga) {
		File logMonitor = new File("./log.txt");
		try(PrintWriter escritor = new PrintWriter(new BufferedWriter(new FileWriter(logMonitor, true)))) {						
			String elemento = "" + tiempo + ";" + carga;
			escritor.println(elemento);			
		}		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}



