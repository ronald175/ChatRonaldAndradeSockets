package Ventana;

import Clases.Cliente;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import javax.swing.*;
import java.util.Observer;

public class Frm2 extends JFrame {
    
    private JTextArea textArea;
    private JTextField textField;
    private Cliente cliente;

    public Frm2(Cliente cliente) {
        this.cliente = cliente;

        // Registrar listener para recibir mensajes
        this.cliente.registrarListener(this::onMessageReceived);

        setTitle("Chat - Ventana 2");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Cargar mensajes anteriores
        cliente.obtenerHistorial().forEach(mensaje -> textArea.append(mensaje + "\n"));

        textField = new JTextField(20);
        JButton sendButton = new JButton("Enviar");

        sendButton.addActionListener(e -> enviarMensaje());

        JPanel inputPanel = new JPanel();
        inputPanel.add(textField);
        inputPanel.add(sendButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);

        // Eliminar listener al cerrar el frame
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cliente.eliminarListener(Frm2.this::onMessageReceived);
            }
        });

        setVisible(true);
    }

    private void enviarMensaje() {
        String mensaje = textField.getText().trim();
        if (!mensaje.isEmpty()) {
            cliente.enviarMensaje("Ventana 2: " + mensaje);
            textField.setText("");
        }
    }

    private void onMessageReceived(String mensaje) {
        SwingUtilities.invokeLater(() -> textArea.append(mensaje + "\n"));
    }
}