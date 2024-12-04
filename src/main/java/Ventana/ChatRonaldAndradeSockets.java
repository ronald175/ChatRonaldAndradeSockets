
package Ventana;

import Clases.Cliente;
import Clases.Servidor;

/**
 *
 * @author ronaldandrade
 */
public class ChatRonaldAndradeSockets {

   public static void main(String[] args) {
        // Iniciar el servidor en un hilo separado
        Thread servidorThread = new Thread(() -> {
            Servidor servidor = new Servidor(); // Crear y ejecutar el servidor
            new Thread(servidor).start();
        });
        servidorThread.start();
        
        // Esperar a que el servidor se inicie
        try {
            Thread.sleep(1);  // Ajusta este tiempo si es necesario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Crear e iniciar el cliente y su ventana
        Cliente cliente = new Cliente();  // Crear el cliente
        Frm1 frame1 = new Frm1(cliente);  // Crear la primera ventana y pasarle el cliente
        frame1.setVisible(true);          // Hacer visible la ventana
    }
    
}
