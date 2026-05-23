package vista;

import modelo.ConexionBD;
import modelo.SesionUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CalificarEvidenciasDocenteFrame extends JFrame {

    private JComboBox<String> comboPracticas;
    private JComboBox<String> comboActividades;
    private JTextField txtBuscarDocumento;

    private JTable tablaEvidencias;
    private DefaultTableModel modeloTabla;

    private JTextField txtDocumento;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtActividad;

    private JTextArea txtMensaje;
    private JTextArea txtObservacion;

    private JSpinner spinnerNota;

    private JButton btnGuardar;
    private JButton btnLimpiar;

    private Integer idEvidenciaSeleccionada = null;

    public CalificarEvidenciasDocenteFrame() {

        setTitle("Calificar Evidencias - Docente");
        setSize(1400, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarPracticas();
        cargarActividades();
        cargarEvidencias();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel(
                "CALIFICACIÓN DE EVIDENCIAS",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout());

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelFiltros.add(new JLabel("Práctica:"));

        comboPracticas = new JComboBox<>();
        comboPracticas.addItem("TODAS");

        panelFiltros.add(comboPracticas);

        panelFiltros.add(new JLabel("Actividad:"));

        comboActividades = new JComboBox<>();
        comboActividades.addItem("TODAS");

        panelFiltros.add(comboActividades);

        panelFiltros.add(new JLabel("Documento:"));

        txtBuscarDocumento = new JTextField(15);
        panelFiltros.add(txtBuscarDocumento);

        JButton btnBuscar = new JButton("Buscar");
        panelFiltros.add(btnBuscar);

        panelCentro.add(panelFiltros, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Documento");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("Actividad");
        modeloTabla.addColumn("Mensaje");
        modeloTabla.addColumn("Fecha Cargue");
        modeloTabla.addColumn("Nota");

        tablaEvidencias = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaEvidencias);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDocumento = new JTextField(20);
        txtDocumento.setEditable(false);

        txtNombre = new JTextField(20);
        txtNombre.setEditable(false);

        txtApellido = new JTextField(20);
        txtApellido.setEditable(false);

        txtActividad = new JTextField(20);
        txtActividad.setEditable(false);

        txtMensaje = new JTextArea(4, 20);
        txtMensaje.setEditable(false);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);

        txtObservacion = new JTextArea(4, 20);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);

        spinnerNota = new JSpinner(
                new SpinnerNumberModel(0.0, 0.0, 5.0, 0.1)
        );
                int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Documento:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtDocumento, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Apellido:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtApellido, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Actividad:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtActividad, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Mensaje:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(new JScrollPane(txtMensaje), gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Observación Docente:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(new JScrollPane(txtObservacion), gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Nota:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(spinnerNota, gbc);

        fila++;

        btnGuardar = new JButton("Guardar Calificación");
        btnLimpiar = new JButton("Limpiar");

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(btnGuardar, gbc);

        gbc.gridx = 1;
        panelFormulario.add(btnLimpiar, gbc);

        add(panelFormulario, BorderLayout.EAST);

        comboPracticas.addActionListener(e -> {
            cargarActividades();
            cargarEvidencias();
        });

        comboActividades.addActionListener(e -> cargarEvidencias());

        btnBuscar.addActionListener(e -> cargarEvidencias());

        tablaEvidencias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        btnGuardar.addActionListener(e -> guardarCalificacion());

        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    private void cargarPracticas() {

        String sql =
                "SELECT DISTINCT p.codigoPractica " +
                "FROM PracticaUsuario pu " +
                "INNER JOIN Practica p ON pu.idPractica = p.idPractica " +
                "WHERE pu.idUsuario = ? " +
                "AND pu.idRol = 3 " +
                "ORDER BY p.codigoPractica";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, SesionUsuario.idUsuario);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                comboPracticas.addItem(
                        rs.getString("codigoPractica")
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando prácticas: " + ex.getMessage()
            );
        }
    }

    private void cargarActividades() {

        comboActividades.removeAllItems();
        comboActividades.addItem("TODAS");

        String practica = comboPracticas.getSelectedItem() != null
                ? comboPracticas.getSelectedItem().toString()
                : "TODAS";

        String sql =
                "SELECT a.titulo " +
                "FROM Actividades a " +
                "INNER JOIN Practica p ON a.idPractica = p.idPractica " +
                "WHERE a.idUsuario = ? ";

        if (!practica.equals("TODAS")) {
            sql += "AND p.codigoPractica = ? ";
        }

        sql += "ORDER BY a.titulo";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, SesionUsuario.idUsuario);

            if (!practica.equals("TODAS")) {
                stmt.setString(2, practica);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                comboActividades.addItem(
                        rs.getString("titulo")
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando actividades: " + ex.getMessage()
            );
        }
    }
        private void cargarEvidencias() {

        modeloTabla.setRowCount(0);

        String practica = comboPracticas.getSelectedItem() != null
                ? comboPracticas.getSelectedItem().toString()
                : "TODAS";

        String actividad = comboActividades.getSelectedItem() != null
                ? comboActividades.getSelectedItem().toString()
                : "TODAS";

        String documento = txtBuscarDocumento.getText().trim();

        String sql =
                "SELECT ea.idEvidenciaActividad, " +
                "u.idusuario, " +
                "u.nombreUsuario, " +
                "u.apellidoUsuario, " +
                "a.titulo, " +
                "ea.mensaje, " +
                "ea.fechaHoraCargue, " +
                "ea.nota " +
                "FROM EvidenciaActividad ea " +
                "INNER JOIN Usuarios u ON ea.idUsuario = u.idUsuario " +
                "INNER JOIN Actividades a ON ea.idActividad = a.idActividad " +
                "INNER JOIN Practica p ON ea.idPractica = p.idPractica " +
                "WHERE a.idUsuario = ? ";

        if (!practica.equals("TODAS")) {
            sql += "AND p.codigoPractica = ? ";
        }

        if (!actividad.equals("TODAS")) {
            sql += "AND a.titulo = ? ";
        }

        if (!documento.isEmpty()) {
            sql += "AND u.idusuario LIKE ? ";
        }

        sql += "ORDER BY ea.fechaHoraCargue DESC";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            int indice = 1;

            stmt.setInt(indice++, SesionUsuario.idUsuario);

            if (!practica.equals("TODAS")) {
                stmt.setString(indice++, practica);
            }

            if (!actividad.equals("TODAS")) {
                stmt.setString(indice++, actividad);
            }

            if (!documento.isEmpty()) {
                stmt.setString(indice++, "%" + documento + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                modeloTabla.addRow(new Object[]{
                        rs.getInt("idEvidenciaActividad"),
                        rs.getString("idusuario"),
                        rs.getString("nombreUsuario"),
                        rs.getString("apellidoUsuario"),
                        rs.getString("titulo"),
                        rs.getString("mensaje"),
                        rs.getTimestamp("fechaHoraCargue"),
                        rs.getObject("nota") != null
                                ? rs.getDouble("nota")
                                : ""
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando evidencias: " + ex.getMessage()
            );
        }
    }

    private void cargarSeleccion() {

        int fila = tablaEvidencias.getSelectedRow();

        if (fila < 0) {
            return;
        }

        idEvidenciaSeleccionada = Integer.parseInt(
                modeloTabla.getValueAt(fila, 0).toString()
        );

        txtDocumento.setText(
                modeloTabla.getValueAt(fila, 1).toString()
        );

        txtNombre.setText(
                modeloTabla.getValueAt(fila, 2).toString()
        );

        txtApellido.setText(
                modeloTabla.getValueAt(fila, 3).toString()
        );

        txtActividad.setText(
                modeloTabla.getValueAt(fila, 4).toString()
        );

        txtMensaje.setText(
                modeloTabla.getValueAt(fila, 5).toString()
        );

        cargarCalificacionExistente();
    }

    private void cargarCalificacionExistente() {

        String sql =
                "SELECT observacionDocente, nota " +
                "FROM EvidenciaActividad " +
                "WHERE idEvidenciaActividad = ?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idEvidenciaSeleccionada);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                txtObservacion.setText(
                        rs.getString("observacionDocente") != null
                                ? rs.getString("observacionDocente")
                                : ""
                );

                spinnerNota.setValue(
                        rs.getObject("nota") != null
                                ? rs.getDouble("nota")
                                : 0.0
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando calificación: " + ex.getMessage()
            );
        }
    }

    private void guardarCalificacion() {

        if (idEvidenciaSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una evidencia."
            );
            return;
        }

        double nota = ((Number) spinnerNota.getValue()).doubleValue();

        String sql =
                "UPDATE EvidenciaActividad " +
                "SET observacionDocente = ?, " +
                "nota = ?, " +
                "fechaHoraNota = SYSDATE " +
                "WHERE idEvidenciaActividad = ?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, txtObservacion.getText().trim());
            stmt.setDouble(2, nota);
            stmt.setInt(3, idEvidenciaSeleccionada);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Calificación guardada correctamente."
            );

            cargarEvidencias();
            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error guardando calificación: " + ex.getMessage()
            );
        }
    }

    private void limpiarFormulario() {

        idEvidenciaSeleccionada = null;

        txtDocumento.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtActividad.setText("");
        txtMensaje.setText("");
        txtObservacion.setText("");

        spinnerNota.setValue(0.0);

        tablaEvidencias.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new CalificarEvidenciasDocenteFrame().setVisible(true)
        );
    }
}