package vista;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import modelo.ConexionBD;
import modelo.SesionUsuario;

public class LoginFramePro extends JFrame {

    private final JTextField txtCorreo;
    private final JPasswordField txtContrasena;
    private final JButton btnLogin;
    private final JButton btnSalir;

    public LoginFramePro() {
        setTitle("Sistema de Prácticas - Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con fondo
        JPanel panelFondo = new JPanel() {
            private final Image imagen = new ImageIcon("src/imagenes/fondo.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panelFondo.setLayout(new BorderLayout());

        // Título superior
        JLabel lblTitulo = new JLabel("Bienvenido al Sistema", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.BLACK);
        panelFondo.add(lblTitulo, BorderLayout.NORTH);

        // Panel formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(lblCorreo, gbc);

        txtCorreo = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtCorreo, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        panelForm.add(txtContrasena, gbc);

        panelFondo.add(panelForm, BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);

        btnLogin = new JButton("Ingresar");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnLogin);
        panelBotones.add(btnSalir);

        panelFondo.add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnLogin.addActionListener(e -> autenticar());
        btnSalir.addActionListener(e -> System.exit(0));

        add(panelFondo);
    }

    private void autenticar() {

    String correo = txtCorreo.getText().trim();
    String contrasena = new String(txtContrasena.getPassword()).trim();

    String sql =
            "SELECT u.idUsuario, u.nombreUsuario, u.esAdministrador, r.nombreRol " +
            "FROM Usuarios u " +
            "LEFT JOIN PracticaUsuario pu ON u.idUsuario = pu.idUsuario " +
            "LEFT JOIN RolUsuario r ON pu.idRol = r.idRol " +
            "WHERE u.correoUsuario = ? " +
            "AND u.contrasenaUsuario = ?";

    try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setString(1, correo);
        stmt.setString(2, contrasena);

        ResultSet rs = stmt.executeQuery();
        System.out.println(rs);
        if (rs.next()) {

            int idUsuario = rs.getInt("idUsuario");
            String nombre = rs.getString("nombreUsuario");
            int esAdministrador = rs.getInt("esAdministrador");
            String rol = rs.getString("nombreRol");

            // GUARDAR SESIÓN
            SesionUsuario.idUsuario = idUsuario;
            System.out.println(idUsuario);
            SesionUsuario.nombreUsuario = nombre;
            System.out.println(nombre);
            SesionUsuario.rolUsuario = rol;
            System.out.println(rol);

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido " + nombre
            );

            // ADMIN
            if (esAdministrador == 1) {
                new MenuAdministradorFrame().setVisible(true);
            }

            // SUPERVISOR
            else if ("Supervisor".equalsIgnoreCase(rol)) {
                new GestionHorasSupervisorFrame().setVisible(true);
            }

            // DOCENTE
            else if ("Docente".equalsIgnoreCase(rol)) {
                    new MenuDocenteFrame().setVisible(true);

            }

            // ESTUDIANTE
            else if ("Estudiante".equalsIgnoreCase(rol)) {
                    new PortalEstudianteFrame().setVisible(true);

            }

            else {
                JOptionPane.showMessageDialog(
                        this,
                        "El usuario no tiene rol asignado."
                );
            }

            dispose();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Credenciales incorrectas",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
                this,
                "Error en la conexión: " + ex.getMessage()
        );
    }
}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new LoginFramePro().setVisible(true)
        );
    }
}

//comentario final