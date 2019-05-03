package uniandes.caso3.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

	public static final int PUERTO= 8080;
	public static final String SERVIDOR="localhost";
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Cliente....");
		Socket socket= new Socket(SERVIDOR,PUERTO);
	
		BufferedReader lector= new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter escritor= new PrintWriter(socket.getOutputStream(),true);
		
		System.out.println("Escoja el protocolo que desea usar");
		System.out.println("1. Protocolo con seguridad");
		System.out.println("2. Protocolo sin seguridad");
		
		int respuesta=Integer.parseInt(stdIn.readLine());
		switch (respuesta){
		case 1:
			//Protocolo con seguridad
			ProtocoloSeguridad.procesar(stdIn,lector, escritor);
			break;
		case 2:
			//Protocolo sin seguridad
			ProtocoloSinSeguridad.procesar(stdIn,lector, escritor);
			break;
		}
		
		
		
		//cierra el socket y los flujos 
		lector.close();
		escritor.close();
		socket.close();
		
		
	}

}
