/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author ronaldandrade
 */
public class Servidor implements Runnable{
    
    private ServerSocket serverSocket;
    private List<Socket> clientes;
    private Queue<String> mensajesPendientes;
    private Encriptacion encriptador;

    public Servidor() {
        try {
            serverSocket = new ServerSocket(6001);
            clientes = new ArrayList<>();
            mensajesPendientes = new LinkedList<>();  // Cola para almacenar los mensajes pendientes
            encriptador = new Encriptacion("clave16caractere");
            System.out.println("Servidor iniciado. Esperando clientes...");
        } catch (Exception e) {
            System.err.println("Error al inicializar el servidor: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket cliente = serverSocket.accept();
                clientes.add(cliente);
                System.out.println("Nuevo cliente conectado.");
                new Thread(() -> manejarCliente(cliente)).start();
            }
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private void manejarCliente(Socket cliente) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(cliente.getInputStream()))) {
            String mensajeEncriptado;
            while ((mensajeEncriptado = input.readLine()) != null) {
                try {
                    String mensaje = encriptador.desencriptar(mensajeEncriptado);
                    System.out.println("Mensaje recibido: " + mensaje);
                    mensajesPendientes.add(mensaje);  // Guardamos el mensaje en la cola
                    enviarATodos(encriptador.encriptar(mensaje));  // Enviamos el mensaje a todos los clientes conectados
                } catch (Exception e) {
                    System.err.println("Error al desencriptar/enviar mensaje: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al manejar el cliente: " + e.getMessage());
        }
    }

    private void enviarATodos(String mensajeEncriptado) {
        for (Socket cliente : clientes) {
            try {
                PrintWriter output = new PrintWriter(cliente.getOutputStream(), true);
                output.println(mensajeEncriptado);
            } catch (Exception e) {
                System.err.println("Error al enviar mensaje al cliente: " + e.getMessage());
            }
        }
    }

    // Método para obtener los mensajes pendientes (esto se invoca cuando un cliente se reconecta)
    public Queue<String> obtenerMensajesPendientes() {
        return mensajesPendientes;
    }
}