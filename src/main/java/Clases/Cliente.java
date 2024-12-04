
package Clases;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Queue;
import java.util.function.Consumer;

/**
 *
 * @author ronaldandrade
 */
public class Cliente implements Runnable {
    
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private String host = "127.0.0.1";
    private int port = 6001;
    private Encriptacion encriptador;
    private List<Consumer<String>> messageListeners = new ArrayList<>();
    private List<String> historial = new ArrayList<>();

    public Cliente() {
        try {
            encriptador = new Encriptacion("clave16caractere");
            socket = new Socket(host, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(this).start();
            System.out.println("Cliente conectado al servidor.");
        } catch (Exception e) {
            System.err.println("Error al conectar al servidor: " + e.getMessage());
        }
    }

    public void enviarMensaje(String mensaje) {
        try {
            String mensajeEncriptado = encriptador.encriptar(mensaje);
            System.out.println("Mensaje enviado (encriptado): " + mensajeEncriptado);
            output.println(mensajeEncriptado);
        } catch (Exception e) {
            System.err.println("Error al encriptar el mensaje: " + e.getMessage());
        }
    }
    public List<String> obtenerHistorial() {
        return new ArrayList<>(historial); // Devuelve una copia del historial
    }

    public void registrarListener(Consumer<String> listener) {
        messageListeners.add(listener); // Agrega un listener a la lista
    }

    public void eliminarListener(Consumer<String> listener) {
        messageListeners.remove(listener); // Elimina un listener si ya no es necesario
    }

    @Override
     public void run() {
        String mensajeEncriptado;
        try {
            while ((mensajeEncriptado = input.readLine()) != null) {
                try {
                    String mensajeDesencriptado = encriptador.desencriptar(mensajeEncriptado);
                    historial.add(mensajeDesencriptado); // Agregar al historial
                    for (Consumer<String> listener : messageListeners) {
                        listener.accept(mensajeDesencriptado);
                    }
                } catch (Exception e) {
                    System.err.println("Error al desencriptar el mensaje: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error en la conexión con el servidor: " + e.getMessage());
        }
    }
}