package uniandes.caso3.servidorCS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Log {
	public Log() {		
	}
	
	public synchronized void agregarValores(Long tiempo, Double cargaIni,Double cargaMit,Double cargaFin) {
		imprimirResultados(tiempo, cargaIni,cargaMit,cargaFin);
	}
	
	/**
	 * Guarda los resultados en un archivo de LOG
	 * No ejecutarlo por si solo !
	 */
	
	private void imprimirResultados(Long tiempo, Double cargaIni,Double cargaMit,Double cargaFin) {
		File logMonitor = new File("./log.txt");
		try(PrintWriter escritor = new PrintWriter(new BufferedWriter(new FileWriter(logMonitor, true)))) {						
			String elemento = "" + tiempo + ";" + cargaIni+ ";" +cargaMit+ ";" +cargaFin;
			escritor.println(elemento);			
		}		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}



