package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InstitucionCRUDFrame extends JFrame {

    private JTextField txtIdInstitucion;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JTextField txtCorreo;
    private JTextField txtSiglas;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JTable tablaInstituciones;
    private DefaultTableModel modeloTabla;

    public InstitucionCRUDFrame() {
        setTitle("CRUD Instituciones");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarInstituciones();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // =====================================
        // PANEL SUPERIOR
        // =====================================
        JPanel panelSuperior = new JPanel(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdInstitucion = new JTextField(15);
        txtNombre = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtDireccion = new JTextField(20);
        txtCorreo = new JTextField(20);
        txtSiglas = new JTextField(20);

        int fila = 0;

        // ID + botón buscar
        JLabel lblId = new JLabel("ID Institución:");
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblId, gbc);

        JPanel panelId = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnBuscar = new JButton("Buscar");

        panelId.add(txtIdInstitucion);
        panelId.add(btnBuscar);

        gbc.gridx = 1;
        panelFormulario.add(panelId, gbc);

        fila++;

        agregarCampo(panelFormulario, gbc, fila++, "Nombre:", txtNombre);
        agregarCampo(panelFormulario, gbc, fila++, "Teléfono:", txtTelefono);
        agregarCampo(panelFormulario, gbc, fila++, "Dirección:", txtDireccion);
        agregarCampo(panelFormulario, gbc, fila++, "Correo:", txtCorreo);
        agregarCampo(panelFormulario, gbc, fila++, "Siglas:", txtSiglas);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        // =====================================
        // BOTONES
        // =====================================
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

        // =====================================
        // TABLA
        // =====================================
        modeloTabla = new DefaultTableModel();

        modeloTabla.setColumnIdentifiers(new String[]{
                "ID",
                "Nombre",
                "Teléfono",
                "Dirección",
                "Correo",
                "Siglas"
        });

        tablaInstituciones = new JTable(modeloTabla);
        tablaInstituciones.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaInstituciones);
        add(scrollPane, BorderLayout.CENTER);

        // =====================================
        // EVENTOS
        // =====================================
        btnBuscar.addActionListener(e -> buscarInstitucion());
        btnGuardar.addActionListener(e -> guardarInstitucion());
        btnActualizar.addActionListener(e -> actualizarInstitucion());
        btnEliminar.addActionListener(e -> eliminarInstitucion());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaInstituciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarInstitucionSeleccionada();
            }
        });
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
    private void guardarInstitucion() {
        String sql = "INSERT INTO Instituciones (" +
                        "    idInstitucion," +
                        "    nombreInstitucion," +
                        "    telefonoInstitucion," +
                        "    direccionInstitucion," +
                        "    correoInstitucion," +
                        "    siglasInstitucion" +
                        ")" +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, Integer.parseInt(txtIdInstitucion.getText()));
            stmt.setString(2, txtNombre.getText());
            stmt.setString(3, txtTelefono.getText());
            stmt.setString(4, txtDireccion.getText());
            stmt.setString(5, txtCorreo.getText());
            stmt.setString(6, txtSiglas.getText());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Institución guardada correctamente");

            cargarInstituciones();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage());
        }
    }

    // =====================================
    // LISTAR
    // =====================================
    private void cargarInstituciones() {
        modeloTabla.setRowCount(0);

        String sql = "SELECT * FROM Instituciones ORDER BY nombreInstitucion";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("idInstitucion"),
                        rs.getString("nombreInstitucion"),
                        rs.getString("telefonoInstitucion"),
                        rs.getString("direccionInstitucion"),
                        rs.getString("correoInstitucion"),
                        rs.getString("siglasInstitucion")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar instituciones: " + ex.getMessage());
        }
    }

    // =====================================
    // BUSCAR
    // =====================================
    private void buscarInstitucion() {
        if (txtIdInstitucion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un ID para buscar.");
            return;
        }

        modeloTabla.setRowCount(0);

        String sql = "SELECT * FROM Instituciones WHERE idInstitucion = ?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, Integer.parseInt(txtIdInstitucion.getText()));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("idInstitucion"),
                        rs.getString("nombreInstitucion"),
                        rs.getString("telefonoInstitucion"),
                        rs.getString("direccionInstitucion"),
                        rs.getString("correoInstitucion"),
                        rs.getString("siglasInstitucion")
                });
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró la institución.");

                cargarInstituciones();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar: " + ex.getMessage());
        }
    }

    // =====================================
    // CARGAR SELECCIONADO
    // =====================================
    private void cargarInstitucionSeleccionada() {
        int fila = tablaInstituciones.getSelectedRow();

        if (fila >= 0) {
            txtIdInstitucion.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtDireccion.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtCorreo.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtSiglas.setText(modeloTabla.getValueAt(fila, 5).toString());

            txtIdInstitucion.setEditable(false);
        }
    }

    // =====================================
    // ACTUALIZAR
    // =====================================
    private void actualizarInstitucion() {
        String sql = "UPDATE Instituciones SET " +
                "nombreInstitucion=?, telefonoInstitucion=?, direccionInstitucion=?, " +
                "correoInstitucion=?, siglasInstitucion=? " +
                "WHERE idInstitucion=?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, txtNombre.getText());
            stmt.setString(2, txtTelefono.getText());
            stmt.setString(3, txtDireccion.getText());
            stmt.setString(4, txtCorreo.getText());
            stmt.setString(5, txtSiglas.getText());
            stmt.setInt(6, Integer.parseInt(txtIdInstitucion.getText()));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Institución actualizada");

            cargarInstituciones();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + ex.getMessage());
        }
    }

    // =====================================
    // ELIMINAR
    // =====================================
    private void eliminarInstitucion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar esta institución?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM Instituciones WHERE idInstitucion=?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, Integer.parseInt(txtIdInstitucion.getText()));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Institución eliminada");

            cargarInstituciones();
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
        txtIdInstitucion.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtCorreo.setText("");
        txtSiglas.setText("");

        txtIdInstitucion.setEditable(true);
        tablaInstituciones.clearSelection();

        cargarInstituciones();
    }
}