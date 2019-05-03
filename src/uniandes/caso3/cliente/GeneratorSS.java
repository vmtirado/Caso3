package uniandes.caso3.cliente;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

public class GeneratorSS {
	/**
	 * Load Generator service
	 */
	private LoadGenerator generator;

	private int numberOfTasks = 100;
	private long gapsBetweenTasks = 500;

	/**
	 * Constructor de un nuevo generador
	 */
	public GeneratorSS(){
		Task work = createTask();
		generator = new LoadGenerator("Prueba Cliente-Servidor", numberOfTasks, work, gapsBetweenTasks);
		generator.generate();		
	}

	/**
	 * Ayuda para construir un task
	 * @return Task 
	 */
	private Task createTask(){
		return new ClientServerTaskSS();
	}

	private static long totalTiempoTransaccion(){
		long total = 0;
		for (int i = 0; i < ClienteSS.numTransacciones; i++) {
			total += ClienteSS.tiempo.get(i);
		}		

		return total;
	}

	private static double totalUsoCPU(){
		double total = 0;
		for (int i = 0; i < ClienteSS.numTransacciones; i++) {
			total += (ClienteSS.usoCPU.get(i)/100.0); ////Arreglar;
		}		

		return total;
	}

	/**
	 * Inicia la aplicación
	 * @param args
	 */
	public static void main(String[] args) {

		@SuppressWarnings("unused")
		GeneratorSS gen = new GeneratorSS();

		int transaccionesP = gen.numberOfTasks - ClienteSS.numTransacciones;
		double promedio = 0.0;

		if(ClienteSS.numTransacciones != 0){
			promedio = totalTiempoTransaccion()/ClienteSS.numTransacciones;
		}

		long total = totalTiempoTransaccion();

		double cpu = totalUsoCPU();

		System.out.println("Transacciones perdidas: " + transaccionesP);
		System.out.println("Transacciones terminadas: " + ClienteSS.numTransacciones);
		System.out.println("Tiempo total transacciones: " + total + " ms");
		System.out.println("Tiempo de transaccion promedio: " + promedio + " ms");
		System.out.println("Total porcentaje de uso de CPU: " + cpu + " %");
	}

}


