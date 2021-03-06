package uniandes.caso3.cliente;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class ClienteCS {
	
	public static  int numTransacciones; 
	public static ArrayList<Long> tiempo = new ArrayList<Long>();
	public static ArrayList<Double> usoCPU = new ArrayList<Double>();

	
	//Configuracion puertos 
	public static final int PUERTO= 8080;
	public static final String SERVIDOR="localhost";
	
		//Algoritmos simetricos
		public static final String AES="AES";
		public static final String BLOWFISH="Blowfish";

		//Algoritmo Asimetrico 
		public static final String RSA="RSA";

		//Algoritmos MAC
		public static final String SHA1= "HMACSHA1";
		public static final String SHA256= "HMACSHA256";
		public static final String SHA384= "HMACSHA384";
		public static final String SHA512= "HMACSHA512";
		
		//variable donde se guardan los algoritmos que el usuario decide usar 
		private static String simetrico=BLOWFISH;
		private static String asimetrico=RSA;
		private static String Hmac=SHA1;

		private static KeyPair llaveAsimetrica;
		private static SecretKey llaveSimetrica;
		private static Key llavePublicaServidor;
		private static byte[] llaveSimetricaServidor;
		public static int idCliente= (int) (Math.random()*10);


		public  ClienteCS (Semaforo sem) throws Exception{
//			sem.v();
			long tiempoIni=System.currentTimeMillis();
			
			Socket socket= new Socket(SERVIDOR,PUERTO);
			
			BufferedReader pIn= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter pOut= new PrintWriter(socket.getOutputStream(),true);
			
			String fromUser="";
			String fromServer="";
			System.out.println("------------------Protocolo con seguridad------------------");
			boolean continuar=true;

			while(continuar)
			{
				
				double cpuMit=-1;
				//****************************************************************
				//ETAPA 1
				//****************************************************************
				//Inicia el protocolo con el servidor 
				pOut.println("HOLA");
				System.out.println("Mensaje al servidor: "+fromUser);

				//Espera la respuesta del servidor 
				fromServer=pIn.readLine();
				System.out.println("Respuesta del servidor: "+fromServer);

				//Si el servidor responde correctamente s escojen los algoritmos 
				if (fromServer!=null && fromServer.equalsIgnoreCase("OK")){
					fromUser= "ALGORITMOS:"+simetrico+":"+RSA+":"+Hmac;
					pOut.println(fromUser);
					System.out.println("Mensaje al servidor: "+ fromUser);
				}
				else{
					System.err.println("La respuesta no es la esperada");
				}

				//se lee la respuesta del servidor 
				fromServer= pIn.readLine();
				System.out.println("Respuesta del servidor: "+ fromServer);

				//Si la respuesta es correcta se genera un certificado y se manda al servidor 
				
				//****************************************************************
				//ETAPA 2
				//****************************************************************
				
				
				if (fromServer!=null && fromServer.equalsIgnoreCase("OK")){
					//Genero la llave asimetrica que usare durante el intercambio 
					llaveAsimetrica =K.asimetricKey(RSA);
					//Genero un CD
					java.security.cert.X509Certificate certificado = generarCertificado(llaveAsimetrica);
					byte[] certificadoEnBytes = certificado.getEncoded( );
					String certificadoEnString = DatatypeConverter.printHexBinary(certificadoEnBytes);
					pOut.println(certificadoEnString);
					System.out.println("Se le envio el certificado al servidor ");
				}

				else {
					System.err.println("Error en el servidor");
					break;
				}

				//Espera el certificado del servidor 
				String certServidor=pIn.readLine();
				System.out.println("Respuesta del servidor "+certServidor);

				if(certServidor!=null ){
					//Obtiene el certificado del servidor
					byte[] certificadoServidorBytes =  DatatypeConverter.parseHexBinary(certServidor);
					CertificateFactory creador = CertificateFactory.getInstance("X.509");
					InputStream in = new ByteArrayInputStream(certificadoServidorBytes);
					X509Certificate certificadoServidor = (X509Certificate)creador.generateCertificate(in);
					
					//Obtiene la llave publica del servidor 
					llavePublicaServidor= certificadoServidor.getPublicKey();

					//Genera una llave simetrica la cifra con la publica del servidor 
					llaveSimetrica= K.simetricKey(simetrico);
					byte[] textoSimetrica=  llaveSimetrica.getEncoded();
					byte[] cifrado= K.cifrar(llavePublicaServidor,asimetrico,textoSimetrica);
					pOut.println(DatatypeConverter.printHexBinary(cifrado));
					System.out.println("Se le envio la llave simetrica cifrada al servidor: ");
					
					
				}
				else {
					System.err.println("El servidor mando una cadena vacia en vez del CD ");
					break;
				}

				//Espero la llave simetrica del servidor 

				fromServer=pIn.readLine();
				System.out.println("Respuesta del servidor con la llave simetrica: "+ fromServer);

				//Si lo enviado por el servidor no es nulo decifro la llave simetrica y la guardo 
				if(fromServer!=null){
					byte[] llaveSinDecifrar=DatatypeConverter.parseHexBinary(fromServer);
					System.out.println("la llave que voy a decifrar es ");
					
					//obtiene la llave simetrica del servidor 
					llaveSimetricaServidor=K.decifrar(llaveAsimetrica.getPrivate(), asimetrico, llaveSinDecifrar);
					fromUser="OK";
					pOut.println(fromUser);
					System.out.println("Mensaje enviado al servidor "+ fromUser);
				}
				else 
				{
					System.err.println("No se pudo obtener llave simetrica");
					break;
				}
				
				//****************************************************************
				//ETAPA 3
				//****************************************************************

				//Obtiene las coordenadas para mandarselas al servidor
				String coordenadas= "15;4,24";
				System.out.println("coordenadas "+coordenadas);
				
				byte[]cifrado=K.cifrar(llaveSimetrica, simetrico, coordenadas.getBytes());
				
				fromUser=DatatypeConverter.printHexBinary(cifrado);
				pOut.println(fromUser);
				
				System.out.println("Se enviaron los datos cifrados al servidor");
				
				//Se genera un Hmac con la llave simetrica y se envia al servidor
				byte[] hmac =K.generateHmac(Hmac,llaveSimetrica, coordenadas.getBytes());
				pOut.println(DatatypeConverter.printHexBinary(hmac));
				System.out.println("Se envio el hash cifrado al servidor");
				System.out.println(DatatypeConverter.printHexBinary(hmac));
				
				fromServer=pIn.readLine();
				System.out.println("El mensaje del servidor: "+ fromServer);
				byte[] decifrado=K.decifrar(llavePublicaServidor, asimetrico, DatatypeConverter.parseHexBinary(fromServer));
				
				if(sameHash(DatatypeConverter.printHexBinary(hmac),DatatypeConverter.printHexBinary(decifrado))){
					//Tiempo de la transaccion 
					long tiempoFin= System.currentTimeMillis();
					tiempo.add(tiempoFin-tiempoIni);
					//se suma 1 transaccion 
					numTransacciones++;
					
					System.out.println("LO LOGRAMOS!! "+numTransacciones);
					continuar=false;
				}
				else{
					System.out.println("almost there");
					
					continuar=false;
				}
			}
			
			
		}


		public static X509Certificate generarCertificado(KeyPair llaves) throws Exception{
			try {
				//formato de las fechas usadas para el cd
				SimpleDateFormat format= new SimpleDateFormat("dd-MM-yyy");
				//genera el numero cerial del cd 
				BigInteger serialNumber =BigInteger.valueOf(System.currentTimeMillis());
				//genera un calendario con la hora actual
				Calendar calStart= Calendar.getInstance();
				Date startDate= new Date(calStart.getTimeInMillis());
				//Genera la fecha de vencimiento del certificado (5 anos)
				calStart.add(Calendar.DATE, 1825); // Le suma 5 anos a la fecha de inicio 
				Calendar calEnd= calStart;
				Date endDate = new Date(calEnd.getTimeInMillis());
				X500Name issuer = new X500Name("C=ISSUER");
				X500Name subject = new X500Name("C=SUBJECT");

				X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
						issuer, serialNumber, startDate, endDate, subject,
						SubjectPublicKeyInfo.getInstance(llaves.getPublic().getEncoded()));
				String certificateHmac="";
				switch (Hmac){
				case SHA1:
					certificateHmac="SHA1with"+asimetrico;
					break;
				case SHA256:
					certificateHmac="SHA256with"+asimetrico;
				case SHA384:
					certificateHmac="SHA384with"+asimetrico;
				case SHA512:
					certificateHmac="SHA512with"+asimetrico;
				}
				
				JcaContentSignerBuilder builder = new JcaContentSignerBuilder(certificateHmac);
				ContentSigner signer = builder.build(llaves.getPrivate());


				byte[] certBytes = certBuilder.build(signer).getEncoded();
				CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
				return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certBytes));

			} catch (Exception e) {
				// TODO: handle exception
				throw new Exception("No se pudo generar el certificado: "+ e.getMessage());
			}
		}
		
		public static boolean sameHash(String h1, String h2)
		{
			return h1.compareTo(h2)==0;
		}
		

}
