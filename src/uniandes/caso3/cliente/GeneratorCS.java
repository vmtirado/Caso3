package uniandes.caso3.cliente;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;
import uniandes.gload.examples.clientserver.generator.Generator;

public class GeneratorCS extends Thread{
		/**
		 * Load Generator service
		 */
		private LoadGenerator generator;
		
		private static  int numberOfTasks = 200;
		private long gapsBetweenTasks = 20;
		


		/**
		 * Constructor de un nuevo generador
		 */
		public GeneratorCS(Semaforo sem){
			Task work = createTask(sem);
			generator = new LoadGenerator("Prueba Cliente-Servidor", numberOfTasks, work, gapsBetweenTasks);
			generator.generate();		
		}

		/**
		 * Ayuda para construir un task
		 * @return Task 
		 */
		private Task createTask(Semaforo sem){
			return new ClientServerTaskCS(sem);
		}
		
		//LOS DOS QUE SIGUEN ERAN ESTATICOS
		
		private static  long totalTiempoTransaccion(){
			long total = 0;
			for (int i = 0; i < ClienteCS.numTransacciones; i++) {
				total += ClienteCS.tiempo.get(i);
			}		
					
			return total;
		}
		
		private static  double totalUsoCPU(){
			double total = 0;
			for (int i = 0; i < ClienteCS.usoCPU.size(); i++) {
				total += (ClienteCS.usoCPU.get(i)); //Arreglar!!!
			}		
					
			return total/ClienteCS.usoCPU.size();
		}

		/**
		 * Inicia la aplicación
		 * @param args
		 */
		public static void main(String[] args) {
			boolean termino=false;
			 Semaforo sem= new Semaforo(-numberOfTasks+1);
			System.out.println("Entro al constructor");
			@SuppressWarnings("unused")
			GeneratorCS gen = new GeneratorCS (sem);
			System.out.println("Creo el generador");
			boolean bool=true;

//			sem.p();
			double promedio = 0.0;
			
			if(ClienteCS.numTransacciones != 0){
				 promedio = totalTiempoTransaccion()/ClienteCS.numTransacciones;
			}
			
			long total = totalTiempoTransaccion();
			
			double cpu = totalUsoCPU();
			
			int transaccionesP = gen.numberOfTasks - ClienteCS.numTransacciones;
			
			System.out.println("Transacciones perdidas: " + transaccionesP);
			System.out.println("Transacciones terminadas: " + ClienteCS.numTransacciones);
			System.out.println("Tiempo total transacciones: " + total + " ms");
			System.out.println("Tiempo de transaccion promedio: " + promedio + " ms");
			System.out.println("Total porcentaje de uso de CPU: " + cpu + " %");
		}

	}


