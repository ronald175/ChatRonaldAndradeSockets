/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ronaldandrade
 */
public class Encriptacion {
    
    private static final String ALGORITMO = "AES";
    private static final String TRANSFORMACION = "AES/ECB/PKCS5Padding";

    private SecretKey claveSecreta;

    public Encriptacion(String clave) {
        if (clave.length() != 16) {
            throw new IllegalArgumentException("La clave secreta debe tener exactamente 16 caracteres.");
        }
        this.claveSecreta = new SecretKeySpec(clave.getBytes(), ALGORITMO);
    }

    public String encriptar(String mensaje) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMACION);
        cipher.init(Cipher.ENCRYPT_MODE, claveSecreta);
        byte[] mensajeEncriptado = cipher.doFinal(mensaje.getBytes());
        return Base64.getEncoder().encodeToString(mensajeEncriptado);
    }

    public String desencriptar(String mensajeEncriptado) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMACION);
        cipher.init(Cipher.DECRYPT_MODE, claveSecreta);
        byte[] mensajeDesencriptado = cipher.doFinal(Base64.getDecoder().decode(mensajeEncriptado));
        return new String(mensajeDesencriptado);
    }
}