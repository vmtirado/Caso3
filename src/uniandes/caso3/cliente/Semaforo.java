package uniandes.caso3.cliente;


public class Semaforo {

	private int cont;
	
	public Semaforo(int pCont ){
		cont=pCont;
	}
	
	public  synchronized void p(  ){
		cont --; 
		if (cont < 0 ){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void v(){
		cont++;
		System.out.println("ESTOY EN "+ cont);
		if (cont==0){
			this.notifyAll();
			
		}
	}
}
