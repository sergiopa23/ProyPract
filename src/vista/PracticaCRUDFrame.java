package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PracticaCRUDFrame extends JFrame {

    private JTextField txtIdPractica;
    private JTextField txtCodigoPractica;
    private JComboBox<String> comboInstituciones;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JTable tablaPracticas;
    private DefaultTableModel modeloTabla;

    public PracticaCRUDFrame() {
        setTitle("CRUD Prácticas");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarInstitucionesCombo();
        cargarPracticas();
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

        txtIdPractica = new JTextField(15);
        txtCodigoPractica = new JTextField(20);
        txtCodigoPractica.setEditable(false);

        comboInstituciones = new JComboBox<>();

        int fila = 0;

        // ID + Buscar
        JLabel lblId = new JLabel("ID Práctica:");
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(lblId, gbc);

        JPanel panelId = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnBuscar = new JButton("Buscar");

        panelId.add(txtIdPractica);
        panelId.add(btnBuscar);

        gbc.gridx = 1;
        panelFormulario.add(panelId, gbc);

        fila++;

        agregarCampo(panelFormulario, gbc, fila++, "Código Práctica:", txtCodigoPractica);

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Institución:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(comboInstituciones, gbc);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        // =====================================
        // BOTONES
        // =====================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

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
                "Código Práctica",
                "Institución"
        });

        tablaPracticas = new JTable(modeloTabla);
        tablaPracticas.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaPracticas);
        add(scrollPane, BorderLayout.CENTER);

        // =====================================
        // EVENTOS
        // =====================================
        btnBuscar.addActionListener(e -> buscarPractica());
        btnGuardar.addActionListener(e -> guardarPractica());
        btnActualizar.addActionListener(e -> actualizarPractica());
        btnEliminar.addActionListener(e -> eliminarPractica());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        comboInstituciones.addActionListener(e -> generarCodigoPractica());

        tablaPracticas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarPracticaSeleccionada();
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
    // CARGAR COMBO INSTITUCIONES
    // =====================================
    private void cargarInstitucionesCombo() {
        comboInstituciones.removeAllItems();

        String sql = "SELECT nombreInstitucion FROM Instituciones ORDER BY nombreInstitucion";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                comboInstituciones.addItem(rs.getString("nombreInstitucion"));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando instituciones: " + ex.getMessage());
            System.out.println(ex.getMessage());        
        }
    }

    // =====================================
    // GENERAR CÓDIGO AUTOMÁTICO
    // =====================================
    private void generarCodigoPractica() {
    if (comboInstituciones.getSelectedItem() == null) {
        return;
    }

    String nombreInstitucion = comboInstituciones.getSelectedItem().toString();

    String siglas = "";
    int idInstitucion = 0;

    String sqlSiglas = "SELECT idInstitucion, siglasInstitucion "
            + "FROM Instituciones "
            + "WHERE nombreInstitucion = ?";

    try (
            Connection conn = ConexionBD.getConnection();
            PreparedStatement stmtSiglas = conn.prepareStatement(sqlSiglas)
    ) {

        stmtSiglas.setString(1, nombreInstitucion);

        try (ResultSet rs = stmtSiglas.executeQuery()) {
            if (rs.next()) {
                idInstitucion = rs.getInt("idInstitucion");
                siglas = rs.getString("siglasInstitucion");
            }
        }

        if (idInstitucion == 0) {
            return;
        }

        String sqlCount = "SELECT COUNT(*) total "
                + "FROM Practica "
                + "WHERE idInstitucion = ?";

        try (
                PreparedStatement stmtCount = conn.prepareStatement(sqlCount)
        ) {
            stmtCount.setInt(1, idInstitucion);

            try (ResultSet rsCount = stmtCount.executeQuery()) {
                if (rsCount.next()) {
                    int siguiente = rsCount.getInt("total") + 1;
                    txtCodigoPractica.setText(siglas + "-" + siguiente);
                }
            }
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                "Error generando código: " + ex.getMessage()

        );
        System.out.println(ex.getMessage());
    }
}

    // =====================================
    // GUARDAR
    // =====================================
    private void guardarPractica() {
        if (comboInstituciones.getSelectedItem() == null) return;

        String nombreInstitucion = comboInstituciones.getSelectedItem().toString();

        String sqlInstitucion = "SELECT idInstitucion FROM Instituciones WHERE nombreInstitucion = ?";
        String sqlInsert = "INSERT INTO Practica (codigoPractica, idInstitucion) VALUES (?, ?)";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmtInst = conn.prepareStatement(sqlInstitucion)
        ) {
            stmtInst.setString(1, nombreInstitucion);
            ResultSet rs = stmtInst.executeQuery();

            if (rs.next()) {
                int idInstitucion = rs.getInt("idInstitucion");

                try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                    stmtInsert.setString(1, txtCodigoPractica.getText());
                    stmtInsert.setInt(2, idInstitucion);

                    stmtInsert.executeUpdate();

                    JOptionPane.showMessageDialog(this,
                            "Práctica guardada correctamente");

                    cargarPracticas();
                    limpiarCampos();
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage());
        }
    }

    // =====================================
    // LISTAR
    // =====================================
    private void cargarPracticas() {
        modeloTabla.setRowCount(0);

        String sql = "SELECT p.idPractica, p.codigoPractica, i.nombreInstitucion " +
                "FROM Practica p " +
                "INNER JOIN Instituciones i ON p.idInstitucion = i.idInstitucion " +
                "ORDER BY p.codigoPractica";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("idPractica"),
                        rs.getString("codigoPractica"),
                        rs.getString("nombreInstitucion")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando prácticas: " + ex.getMessage());
        }
    }

    // =====================================
    // BUSCAR
    // =====================================
    private void buscarPractica() {
        if (txtIdPractica.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un ID para buscar.");
            return;
        }

        modeloTabla.setRowCount(0);

        String sql = "SELECT p.idPractica, p.codigoPractica, i.nombreInstitucion " +
                "FROM Practica p " +
                "INNER JOIN Instituciones i ON p.idInstitucion = i.idInstitucion " +
                "WHERE p.idPractica = ?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, Integer.parseInt(txtIdPractica.getText()));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("idPractica"),
                        rs.getString("codigoPractica"),
                        rs.getString("nombreInstitucion")
                });
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró la práctica.");

                cargarPracticas();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar: " + ex.getMessage());
        }
    }

    // =====================================
    // CARGAR SELECCIONADO
    // =====================================
    private void cargarPracticaSeleccionada() {
        int fila = tablaPracticas.getSelectedRow();

        if (fila >= 0) {
            txtIdPractica.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtCodigoPractica.setText(modeloTabla.getValueAt(fila, 1).toString());
            comboInstituciones.setSelectedItem(
                    modeloTabla.getValueAt(fila, 2).toString()
            );

            txtIdPractica.setEditable(false);
        }
    }

    // =====================================
    // ACTUALIZAR
    // =====================================
    private void actualizarPractica() {
        if (comboInstituciones.getSelectedItem() == null) return;

        String nombreInstitucion = comboInstituciones.getSelectedItem().toString();

        String sqlInst = "SELECT idInstitucion FROM Instituciones WHERE nombreInstitucion = ?";
        String sqlUpdate = "UPDATE Practica SET codigoPractica=?, idInstitucion=? WHERE idPractica=?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmtInst = conn.prepareStatement(sqlInst)
        ) {
            stmtInst.setString(1, nombreInstitucion);
            ResultSet rs = stmtInst.executeQuery();

            if (rs.next()) {
                int idInstitucion = rs.getInt("idInstitucion");

                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setString(1, txtCodigoPractica.getText());
                    stmtUpdate.setInt(2, idInstitucion);
                    stmtUpdate.setInt(3, Integer.parseInt(txtIdPractica.getText()));

                    stmtUpdate.executeUpdate();

                    JOptionPane.showMessageDialog(this,
                            "Práctica actualizada");

                    cargarPracticas();
                    limpiarCampos();
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + ex.getMessage());
        }
    }

    // =====================================
    // ELIMINAR
    // =====================================
    private void eliminarPractica() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar esta práctica?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Practica WHERE idPractica=?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, Integer.parseInt(txtIdPractica.getText()));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Práctica eliminada");

            cargarPracticas();
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
        txtIdPractica.setText("");
        txtCodigoPractica.setText("");
        txtIdPractica.setEditable(true);

        if (comboInstituciones.getItemCount() > 0) {
            comboInstituciones.setSelectedIndex(0);
            generarCodigoPractica();
        }

        tablaPracticas.clearSelection();
        cargarPracticas();
    }
}

