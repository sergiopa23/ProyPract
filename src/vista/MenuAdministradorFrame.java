package vista;

import javax.swing.*;
import java.awt.*;

public class MenuAdministradorFrame extends JFrame {

    private JButton btnUsuarios;
    private JButton btnInstituciones;
    private JButton btnPracticas;
    private JButton btnAsignaciones;
    private JButton btnActividades;
    private JButton btnCerrarSesion;
    

    public MenuAdministradorFrame() {
        setTitle("Menú Administrador - Sistema de Prácticas");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        // Título superior
        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE PRÁCTICAS", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 2, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        btnUsuarios = new JButton("Gestión de Usuarios");
        btnInstituciones = new JButton("Gestión de Instituciones");
        btnPracticas = new JButton("Gestión de Prácticas");
        btnAsignaciones = new JButton("Asignación de Usuarios");
        btnActividades = new JButton("Gestión de Actividades");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        panelBotones.add(btnUsuarios);
        panelBotones.add(btnInstituciones);
        panelBotones.add(btnPracticas);
        panelBotones.add(btnAsignaciones);
        panelBotones.add(btnActividades);
        panelBotones.add(btnCerrarSesion);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        // Eventos de botones

        btnUsuarios.addActionListener(e -> {
            new UsuarioCRUDFrame().setVisible(true);
        });

        btnInstituciones.addActionListener(e -> {
            new InstitucionCRUDFrame().setVisible(true);
        });

        btnPracticas.addActionListener(e -> {
            new PracticaCRUDFrame().setVisible(true);
        });

        btnAsignaciones.addActionListener(e -> {
            new AsignacionUsuariosFrame().setVisible(true);
        });

        btnActividades.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Aquí abrirá la Gestión de Actividades");
            // new ActividadesCRUDFrame().setVisible(true);
        });

        btnCerrarSesion.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea cerrar sesión?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                new LoginFramePro().setVisible(true);
                dispose();
            }
        });

        add(panelPrincipal);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuAdministradorFrame().setVisible(true);
        });
    }
}