package uniandes.caso3.cliente;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * Esta clase se encarga de generar las llaves simetricas y asimetricas 
 * @author Vilma Tirado Gómez
 *
 */
public class K {
	
	/**
	 * Genera una llave privada
	 * @param algSimetrico algoritmo simetrico para generar la llave 
	 * @return SecretKey llave simetrica 
	 * @throws NoSuchAlgorithmException
	 */
	public static SecretKey simetricKey(String algSimetrico) throws NoSuchAlgorithmException{
		KeyGenerator generator=KeyGenerator.getInstance(algSimetrico);
		return generator.generateKey();
	}
	
	/**
	 * Genera un par de llaves (Publica y privada) para cifrar y desifrar un algoritmo
	 * @param algAsimetrico algoritmo asimetrico para generar las llaves 
	 * @return Key pair llave publica y privada
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair asimetricKey (String algAsimetrico) throws NoSuchAlgorithmException{
		KeyPairGenerator generator=KeyPairGenerator.getInstance(algAsimetrico);
		generator.initialize(1024);
		KeyPair keyPair = generator.generateKeyPair();
		return keyPair;
	}
	
	/**
	 * Cifra un texto con la llave dada segun el algoritmo dado 
	 * @param llave llave para cifrar el texto 
	 * @param algoritmo segun el cual se cifra el texto 
	 * @param texto texto a cifrar 
	 * @return byte[] con el texto cifrado 
	 */
	public static byte[] cifrar (Key llave,String algoritmo, byte[] texto ){
		byte[] textoCifrado;
		try {
			Cipher cifrador= Cipher.getInstance(algoritmo);
			byte[] textoClaro=texto;

			cifrador.init(Cipher.ENCRYPT_MODE, llave);
			textoCifrado=cifrador.doFinal(textoClaro);
		} catch (Exception e) {
			System.out.println("Excepcion:" + e.getMessage());
			return null;
		}
		return textoCifrado;
	}
	
	public static byte[] decifrar(Key llave, String algoritmo, byte[] texto){
		byte[] textoClaro;
		try {
			Cipher cifrador= Cipher.getInstance(algoritmo);
			cifrador.init(Cipher.DECRYPT_MODE, llave);
			textoClaro=cifrador.doFinal(texto);
		} catch (Exception e) {
			System.out.println("Excepcion:" + e.getMessage());
			return null;
		}
		return textoClaro;
	}
	
//	public static byte[] getDigest(String algoritm, byte[] buffer){
//		try {
//			MessageDigest digest=MessageDigest.getInstance(algoritm);
//			digest.update(buffer);
//			return digest.digest();
//		} catch (Exception e) {
//			System.err.println("Se genero el error: "+ e.getMessage());
//			return null;
//		}
//	}
	
	public static byte[] generateHmac (String algoritm, Key llave, byte[] texto){
		Mac mac;
		try {
			mac = Mac.getInstance(algoritm);
			mac.init(llave);
			return mac.doFinal(texto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return null;
		} 
		
		
	}
}
