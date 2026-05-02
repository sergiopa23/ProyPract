package vista;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import modelo.ConexionBD;

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
        lblTitulo.setForeground(Color.WHITE);
        panelFondo.add(lblTitulo, BorderLayout.NORTH);

        // Panel formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(lblCorreo, gbc);

        txtCorreo = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtCorreo, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(Color.WHITE);
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

        String sql = "SELECT * FROM Usuarios "
                   + "WHERE correoUsuario = ? "
                   + "AND contrasenaUsuario = ?";

        try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombreUsuario");
                int esAdministrador = rs.getInt("esAdministrador");
                System.out.println("Administrador: " + esAdministrador);


                JOptionPane.showMessageDialog(
                        this,
                        "Bienvenido " + nombre
                );

                // Validar si es administrador
                if (esAdministrador == 1) {
                    new MenuAdministradorFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Usuario válido, pero aún no tiene menú asignado."
                            
                    );
                }

                dispose(); // cerrar login

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