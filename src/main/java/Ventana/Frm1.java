package Ventana;

import Clases.Cliente;
import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Frm1 extends JFrame{
    
    private JTextArea textArea;
    private JTextField textField;
    private Cliente cliente;
    private Frm2 frame2; // Referencia al Frame 2

    public Frm1(Cliente cliente) {
        this.cliente = cliente;

        // Registrar listener para recibir mensajes
        this.cliente.registrarListener(this::onMessageReceived);

        setTitle("Chat - Ventana 1");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        textField = new JTextField(20);
        JButton sendButton = new JButton("Enviar");
        JButton openFrame2Button = new JButton("Abrir Frame 2");

        sendButton.addActionListener(e -> enviarMensaje());
        openFrame2Button.addActionListener(e -> {
            if (frame2 == null || !frame2.isVisible()) {
                frame2 = new Frm2(cliente);
                frame2.setVisible(true);
            } else {
                frame2.toFront();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(textField);
        inputPanel.add(sendButton);
        inputPanel.add(openFrame2Button);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);

        // Eliminar listener al cerrar el frame
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cliente.eliminarListener(Frm1.this::onMessageReceived);
            }
        });

        setVisible(true);
    }

    private void enviarMensaje() {
        String mensaje = textField.getText().trim();
        if (!mensaje.isEmpty()) {
            cliente.enviarMensaje("Ventana 1: " + mensaje);
            textField.setText("");
        }
    }

    private void onMessageReceived(String mensaje) {
        SwingUtilities.invokeLater(() -> textArea.append(mensaje + "\n"));
    }
}