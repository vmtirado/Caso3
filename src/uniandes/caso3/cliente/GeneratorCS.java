package uniandes.caso3.cliente;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

public class GeneratorCS {
	

		/**
		 * Load Generator service
		 */
		private LoadGenerator generator;
		
		private int numberOfTasks = 400;
		private long gapsBetweenTasks = 500;

		/**
		 * Constructor de un nuevo generador
		 */
		public GeneratorCS(){
			Task work = createTask();
			generator = new LoadGenerator("Prueba Cliente-Servidor", numberOfTasks, work, gapsBetweenTasks);
			generator.generate();		
		}

		/**
		 * Ayuda para construir un task
		 * @return Task 
		 */
		private Task createTask(){
			return new ClientServerTaskCS();
		}
		
		private static long totalTiempoTransaccion(){
			long total = 0;
			for (int i = 0; i < Cliente.nTransacciones; i++) {
				total += Cliente.tiempoTransaccion;
			}		
					
			return total;
		}
		
		private static double totalUsoCPU(){
			double total = 0;
			for (int i = 0; i < Cliente.nTransacciones; i++) {
				total += (Cliente.porcentajeUsoCPU/Cliente.cantCPU);
			}		
					
			return total;
		}

		/**
		 * Inicia la aplicación
		 * @param args
		 */
		public static void main(String[] args) {

			@SuppressWarnings("unused")
			Generator gen = new Generator();
			
			int transaccionesP = gen.numberOfTasks - Cliente.nTransacciones;
			double promedio = 0.0;
			
			if(Cliente.nTransacciones != 0){
				 promedio = totalTiempoTransaccion()/Cliente.nTransacciones;
			}
			
			long total = totalTiempoTransaccion();
			
			double cpu = totalUsoCPU();
			
			DecimalFormat formato1 = new DecimalFormat("#.###");
			
			System.out.println("Transacciones perdidas: " + transaccionesP);
			System.out.println("Transacciones terminadas: " + Cliente.nTransacciones);
			System.out.println("Tiempo total transacciones: " + total + " ms");
			System.out.println("Tiempo de transaccion promedio: " + formato1.format(promedio) + " ms");
			System.out.println("Total porcentaje de uso de CPU: " + formato1.format(cpu) + " %");
	 	}

	}

}
