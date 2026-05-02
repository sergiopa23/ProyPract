package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UsuarioCRUDFrame extends JFrame {

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JTextField txtCorreo;
    private JTextField txtContrasena;
    private JCheckBox chkAdministrador;

    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;


    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    public UsuarioCRUDFrame() {
        setTitle("CRUD Usuarios");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarUsuarios();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // =================================================
        // PANEL SUPERIOR (FORMULARIO + BOTONES)
        // =================================================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BorderLayout());

        // -------------------------
        // FORMULARIO
        // -------------------------
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCedula = new JTextField(20);
        txtNombre = new JTextField(20);
        txtApellido = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtDireccion = new JTextField(20);
        txtCorreo = new JTextField(20);
        txtContrasena = new JTextField(20);
        chkAdministrador = new JCheckBox("Es Administrador");

        int fila = 0;

        JLabel lblCedula = new JLabel("Cédula:");
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblCedula, gbc);

        JPanel panelCedula = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        txtCedula = new JTextField(15);
        btnBuscar = new JButton("Buscar");

        panelCedula.add(txtCedula);
        panelCedula.add(btnBuscar);

        gbc.gridx = 1;
        panelFormulario.add(panelCedula, gbc);

        fila++;
        
        agregarCampo(panelFormulario, gbc, fila++, "Nombre:", txtNombre);
        agregarCampo(panelFormulario, gbc, fila++, "Apellido:", txtApellido);
        agregarCampo(panelFormulario, gbc, fila++, "Teléfono:", txtTelefono);
        agregarCampo(panelFormulario, gbc, fila++, "Dirección:", txtDireccion);
        agregarCampo(panelFormulario, gbc, fila++, "Correo:", txtCorreo);
        agregarCampo(panelFormulario, gbc, fila++, "Contraseña:", txtContrasena);

        gbc.gridx = 1;
        gbc.gridy = fila;
        panelFormulario.add(chkAdministrador, gbc);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        // -------------------------
        // BOTONES
        // -------------------------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        btnGuardar.setPreferredSize(new Dimension(130, 35));
        btnActualizar.setPreferredSize(new Dimension(130, 35));
        btnEliminar.setPreferredSize(new Dimension(130, 35));
        btnLimpiar.setPreferredSize(new Dimension(130, 35));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // =================================================
        // TABLA
        // =================================================
        modeloTabla = new DefaultTableModel();

        modeloTabla.setColumnIdentifiers(new String[]{
                "Cédula",
                "Nombre",
                "Apellido",
                "Teléfono",
                "Dirección",
                "Correo",
                "Administrador"
        });

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setPreferredSize(new Dimension(900, 300));

        add(scrollPane, BorderLayout.CENTER);

        // =================================================
        // EVENTOS
        // =================================================
        btnGuardar.addActionListener(e -> guardarUsuario());
        btnActualizar.addActionListener(e -> actualizarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(e -> buscarUsuario());


        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarUsuarioSeleccionado();
            }
        });
    }
    
    private void buscarUsuario() {

        if (txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese una cédula para buscar."
            );
            return;
        }

        modeloTabla.setRowCount(0);

        String sql = "SELECT * FROM Usuarios WHERE idUsuario = ?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, Integer.parseInt(txtCedula.getText().trim()));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                modeloTabla.addRow(new Object[]{
                        rs.getInt("idUsuario"),
                        rs.getString("nombreUsuario"),
                        rs.getString("apellidoUsuario"),
                        rs.getString("telefonoUsuario"),
                        rs.getString("direccionUsuario"),
                        rs.getString("correoUsuario"),
                        rs.getInt("esAdministrador") == 1 ? "Sí" : "No"
                });

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró ningún usuario con esa cédula."
                );

                cargarUsuarios(); // vuelve a listar todo si no encuentra
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al buscar: " + ex.getMessage()
            );
        }
    }



    private void agregarCampo(JPanel panel, GridBagConstraints gbc,
                              int fila, String texto, JTextField campo) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(texto), gbc);

        gbc.gridx = 1;
        panel.add(campo, gbc);
    }

    // =====================================
    // GUARDAR
    // =====================================
    private void guardarUsuario() {
        String sql = "INSERT INTO Usuarios "
                + "(idUsuario, nombreUsuario, apellidoUsuario, telefonoUsuario, "
                + "direccionUsuario, correoUsuario, contrasenaUsuario, esAdministrador) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, Integer.parseInt(txtCedula.getText()));
            stmt.setString(2, txtNombre.getText());
            stmt.setString(3, txtApellido.getText());
            stmt.setString(4, txtTelefono.getText());
            stmt.setString(5, txtDireccion.getText());
            stmt.setString(6, txtCorreo.getText());
            stmt.setString(7, txtContrasena.getText());
            stmt.setInt(8, chkAdministrador.isSelected() ? 1 : 0);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Usuario guardado correctamente");

            cargarUsuarios();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage());
        }
    }

    // =====================================
    // LISTAR
    // =====================================
    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);

        String sql = "SELECT * FROM Usuarios ORDER BY nombreUsuario";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("idUsuario"),
                        rs.getString("nombreUsuario"),
                        rs.getString("apellidoUsuario"),
                        rs.getString("telefonoUsuario"),
                        rs.getString("direccionUsuario"),
                        rs.getString("correoUsuario"),
                        rs.getInt("esAdministrador") == 1 ? "Sí" : "No"
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar usuarios: " + ex.getMessage());
        }
    }

    // =====================================
    // CARGAR SELECCIONADO
    // =====================================
    private void cargarUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();

        if (fila >= 0) {
            txtCedula.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtApellido.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtDireccion.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtCorreo.setText(modeloTabla.getValueAt(fila, 5).toString());

            chkAdministrador.setSelected(
                    modeloTabla.getValueAt(fila, 6).toString().equals("Sí")
            );

            txtCedula.setEditable(false);
        }
    }

    // =====================================
    // ACTUALIZAR
    // =====================================
    private void actualizarUsuario() {
        String sql = "UPDATE Usuarios SET "
                + "nombreUsuario=?, apellidoUsuario=?, telefonoUsuario=?, "
                + "direccionUsuario=?, correoUsuario=?, contrasenaUsuario=?, "
                + "esAdministrador=? "
                + "WHERE idUsuario=?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtApellido.getText());
            stmt.setString(3, txtTelefono.getText());
            stmt.setString(4, txtDireccion.getText());
            stmt.setString(5, txtCorreo.getText());
            stmt.setString(6, txtContrasena.getText());
            stmt.setInt(7, chkAdministrador.isSelected() ? 1 : 0);
            stmt.setInt(8, Integer.parseInt(txtCedula.getText()));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Usuario actualizado");

            cargarUsuarios();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + ex.getMessage());
        }
    }

    // =====================================
    // ELIMINAR
    // =====================================
    private void eliminarUsuario() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar este usuario?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM Usuarios WHERE idUsuario=?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, Integer.parseInt(txtCedula.getText()));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Usuario eliminado");

            cargarUsuarios();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + ex.getMessage());
        }
    }

    // =====================================
    // LIMPIAR
    // =====================================
    private void limpiarCampos() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        chkAdministrador.setSelected(false);

        txtCedula.setEditable(true);
        tablaUsuarios.clearSelection();

        // IMPORTANTE:
        // vuelve a cargar toda la tabla sin filtros
        cargarUsuarios();
    }
}