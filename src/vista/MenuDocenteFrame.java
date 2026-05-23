package vista;

import modelo.SesionUsuario;

import javax.swing.*;
import java.awt.*;

public class MenuDocenteFrame extends JFrame {

    private JButton btnActividades;
    private JButton btnCalificar;
    private JButton btnUsuariosAsignados;
    private JButton btnCerrarSesion;

    public MenuDocenteFrame() {

        setTitle("Menú Docente - Sistema de Prácticas");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel(
                "SISTEMA DE GESTIÓN DE PRÁCTICAS - DOCENTE",
                JLabel.CENTER
        );

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();

        panelBotones.setLayout(
                new GridLayout(2, 2, 20, 20)
        );

        panelBotones.setBorder(
                BorderFactory.createEmptyBorder(40, 60, 40, 60)
        );

        btnActividades = new JButton("Gestión de Actividades");
        btnCalificar = new JButton("Calificar Evidencias");
        btnUsuariosAsignados = new JButton("Usuarios Asignados");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        panelBotones.add(btnActividades);
        panelBotones.add(btnCalificar);
        panelBotones.add(btnUsuariosAsignados);
        panelBotones.add(btnCerrarSesion);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        btnActividades.addActionListener(e -> {
            new GestionActividadesDocenteFrame().setVisible(true);
        });

        btnCalificar.addActionListener(e -> {
            new CalificarEvidenciasDocenteFrame().setVisible(true);
        });

        btnUsuariosAsignados.addActionListener(e -> {
            new UsuariosAsignadosDocenteFrame().setVisible(true);
        });

        btnCerrarSesion.addActionListener(e -> {

            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea cerrar sesión?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                SesionUsuario.limpiarSesion();
                new LoginFramePro().setVisible(true);
                dispose();
            }
        });

        add(panelPrincipal);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new MenuDocenteFrame().setVisible(true)
        );
    }
}