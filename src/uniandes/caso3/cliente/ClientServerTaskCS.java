package uniandes.caso3.cliente;

import uniandes.gload.core.Task;

public class ClientServerTaskCS  extends Task {

	@Override
	public void fail() {
		// TODO Auto-generated method stub
		System.out.println(Task.MENSAJE_FAIL);	
	}

	@Override
	public void success() {
		// TODO Auto-generated method stub
		System.out.println(Task.OK_MESSAGE);	
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		try {
			ClienteCS clienteCS = new ClienteCS();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
