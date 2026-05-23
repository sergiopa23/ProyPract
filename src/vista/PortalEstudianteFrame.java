package vista;

import modelo.ConexionBD;
import modelo.SesionUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PortalEstudianteFrame extends JFrame {

    private JLabel lblBienvenida;
    private JLabel lblHoras;

    private JComboBox<String> comboPracticas;

    private JTable tablaActividades;
    private DefaultTableModel modeloTabla;

    private JTextField txtTitulo;
    private JTextField txtFechaCierre;
    private JTextField txtNota;

    private JTextArea txtDescripcion;
    private JTextArea txtMensaje;
    private JTextArea txtObservacion;

    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnCerrarSesion;

    private Integer idActividadSeleccionada = null;
    private Integer idPracticaSeleccionada = null;
    private Integer idEvidenciaSeleccionada = null;

    public PortalEstudianteFrame() {

        setTitle("Portal Estudiante");
        setSize(1400, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        iniciarComponentes();
        cargarNombreUsuario();
        cargarHoras();
        cargarPracticas();
        cargarActividades();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout());

        lblBienvenida = new JLabel(
                "Bienvenido",
                SwingConstants.LEFT
        );

        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 20));

        lblHoras = new JLabel(
                "HORAS ACUMULADAS: 0 / 180",
                SwingConstants.RIGHT
        );

        lblHoras.setFont(new Font("Arial", Font.BOLD, 20));

        panelSuperior.add(lblBienvenida, BorderLayout.WEST);
        panelSuperior.add(lblHoras, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout());

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelFiltros.add(new JLabel("Práctica:"));

        comboPracticas = new JComboBox<>();
        comboPracticas.addItem("TODAS");

        panelFiltros.add(comboPracticas);

        panelCentro.add(panelFiltros, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Actividad");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Fecha Cierre");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Nota");

        tablaActividades = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaActividades);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTitulo = new JTextField(20);
        txtTitulo.setEditable(false);

        txtFechaCierre = new JTextField(20);
        txtFechaCierre.setEditable(false);

        txtNota = new JTextField(20);
        txtNota.setEditable(false);

        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        txtMensaje = new JTextArea(5, 20);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);

        txtObservacion = new JTextArea(4, 20);
        txtObservacion.setEditable(false);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);
                int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Título:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtTitulo, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(new JScrollPane(txtDescripcion), gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Fecha Cierre:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtFechaCierre, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Mi Evidencia / Mensaje:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(new JScrollPane(txtMensaje), gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Nota:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtNota, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Observación Docente:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(new JScrollPane(txtObservacion), gbc);

        fila++;

        btnGuardar = new JButton("Guardar Evidencia");
        btnLimpiar = new JButton("Limpiar");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(btnGuardar, gbc);

        gbc.gridx = 1;
        panelFormulario.add(btnLimpiar, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelFormulario.add(btnCerrarSesion, gbc);

        add(panelFormulario, BorderLayout.EAST);

        comboPracticas.addActionListener(e -> cargarActividades());

        tablaActividades.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        btnGuardar.addActionListener(e -> guardarEvidencia());

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnCerrarSesion.addActionListener(e -> {
            SesionUsuario.limpiarSesion();
            new LoginFramePro().setVisible(true);
            dispose();
        });
    }

    private void cargarNombreUsuario() {
        lblBienvenida.setText(
                "Bienvenido, " + SesionUsuario.nombreUsuario
        );
    }

    private void cargarHoras() {

        String sql =
                "SELECT NVL(SUM(cantidadHoras), 0) totalHoras " +
                "FROM PracticaUsuario " +
                "WHERE idUsuario = ? " +
                "AND idRol = 2";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, SesionUsuario.idUsuario);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                lblHoras.setText(
                        "HORAS ACUMULADAS: " +
                        rs.getInt("totalHoras") +
                        " / 180"
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando horas: " + ex.getMessage()
            );
        }
    }

    private void cargarPracticas() {

        String sql =
                "SELECT DISTINCT p.idPractica, p.codigoPractica " +
                "FROM PracticaUsuario pu " +
                "INNER JOIN Practica p ON pu.idPractica = p.idPractica " +
                "WHERE pu.idUsuario = ? " +
                "AND pu.idRol = 2 " +
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

        modeloTabla.setRowCount(0);

        String practica = comboPracticas.getSelectedItem() != null
                ? comboPracticas.getSelectedItem().toString()
                : "TODAS";

        String sql =
                "SELECT a.idActividad, " +
                "a.idPractica, " +
                "a.titulo, " +
                "a.descripcion, " +
                "a.fechaHoraCierre, " +
                "ea.idEvidenciaActividad, " +
                "ea.nota " +
                "FROM Actividades a " +
                "LEFT JOIN EvidenciaActividad ea " +
                "ON a.idActividad = ea.idActividad " +
                "AND ea.idUsuario = ? " +
                "WHERE a.visibilidad = 1 " +
                "AND a.idPractica IN ( " +
                "   SELECT idPractica " +
                "   FROM PracticaUsuario " +
                "   WHERE idUsuario = ? " +
                "   AND idRol = 2 " +
                ") ";

        if (!practica.equals("TODAS")) {
            sql += "AND a.idPractica = ( " +
                    "SELECT idPractica FROM Practica " +
                    "WHERE codigoPractica = ? ) ";
        }

        sql += "ORDER BY a.fechaHoraCierre ASC";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            int indice = 1;

            stmt.setInt(indice++, SesionUsuario.idUsuario);
            stmt.setInt(indice++, SesionUsuario.idUsuario);

            if (!practica.equals("TODAS")) {
                stmt.setString(indice++, practica);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String estado = "SIN ENTREGAR";

                if (rs.getObject("idEvidenciaActividad") != null) {
                    estado = "ENTREGADA";
                }

                if (rs.getObject("nota") != null) {
                    estado = "CALIFICADA";
                }

                modeloTabla.addRow(new Object[]{
                        rs.getInt("idActividad"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fechaHoraCierre"),
                        estado,
                        rs.getObject("nota") != null
                                ? rs.getDouble("nota")
                                : ""
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando actividades: " + ex.getMessage()
            );
        }
    }

    private void cargarSeleccion() {

        int fila = tablaActividades.getSelectedRow();

        if (fila < 0) {
            return;
        }

        idActividadSeleccionada = Integer.parseInt(
                modeloTabla.getValueAt(fila, 0).toString()
        );

        txtTitulo.setText(
                modeloTabla.getValueAt(fila, 1).toString()
        );

        txtDescripcion.setText(
                modeloTabla.getValueAt(fila, 2).toString()
        );

        txtFechaCierre.setText(
                modeloTabla.getValueAt(fila, 3).toString()
        );

        txtNota.setText(
                modeloTabla.getValueAt(fila, 5).toString()
        );

        cargarEvidenciaExistente();
    }

    private void cargarEvidenciaExistente() {

        String sql =
                "SELECT idEvidenciaActividad, " +
                "idPractica, " +
                "mensaje, " +
                "observacionDocente, " +
                "nota " +
                "FROM EvidenciaActividad " +
                "WHERE idActividad = ? " +
                "AND idUsuario = ?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idActividadSeleccionada);
            stmt.setInt(2, SesionUsuario.idUsuario);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                idEvidenciaSeleccionada =
                        rs.getInt("idEvidenciaActividad");

                idPracticaSeleccionada =
                        rs.getInt("idPractica");

                txtMensaje.setText(
                        rs.getString("mensaje") != null
                                ? rs.getString("mensaje")
                                : ""
                );

                txtObservacion.setText(
                        rs.getString("observacionDocente") != null
                                ? rs.getString("observacionDocente")
                                : ""
                );

                if (rs.getObject("nota") != null) {

                    txtMensaje.setEditable(false);
                    btnGuardar.setEnabled(false);

                } else {

                    txtMensaje.setEditable(true);
                    btnGuardar.setEnabled(true);
                }

            } else {

                txtMensaje.setText("");
                txtObservacion.setText("");

                txtMensaje.setEditable(true);
                btnGuardar.setEnabled(true);

                buscarPracticaActividad();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando evidencia: " + ex.getMessage()
            );
        }
    }

    private void buscarPracticaActividad() {

        String sql =
                "SELECT idPractica " +
                "FROM Actividades " +
                "WHERE idActividad = ?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, idActividadSeleccionada);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idPracticaSeleccionada =
                        rs.getInt("idPractica");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error buscando práctica: " + ex.getMessage()
            );
        }
    }
        private void guardarEvidencia() {

        if (idActividadSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una actividad."
            );
            return;
        }

        if (txtMensaje.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un mensaje o evidencia."
            );
            return;
        }

        try (
                Connection conn = ConexionBD.getConnection()
        ) {

            if (idEvidenciaSeleccionada == null) {

                String sql =
                        "INSERT INTO EvidenciaActividad " +
                        "(idEvidenciaActividad, idUsuario, idPractica, idActividad, mensaje, fechaHoraCargue) " +
                        "VALUES " +
                        "(SEQ_EVIDENCIAACTIVIDAD.NEXTVAL, ?, ?, ?, ?, SYSDATE)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setInt(1, SesionUsuario.idUsuario);
                    stmt.setInt(2, idPracticaSeleccionada);
                    stmt.setInt(3, idActividadSeleccionada);
                    stmt.setString(4, txtMensaje.getText().trim());

                    stmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Evidencia enviada correctamente."
                );

            } else {

                String sql =
                        "UPDATE EvidenciaActividad " +
                        "SET mensaje = ?, " +
                        "fechaHoraCargue = SYSDATE " +
                        "WHERE idEvidenciaActividad = ? " +
                        "AND nota IS NULL";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, txtMensaje.getText().trim());
                    stmt.setInt(2, idEvidenciaSeleccionada);

                    stmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Evidencia actualizada correctamente."
                );
            }

            cargarActividades();
            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error guardando evidencia: " + ex.getMessage()
            );
        }
    }

    private void limpiarFormulario() {

        idActividadSeleccionada = null;
        idPracticaSeleccionada = null;
        idEvidenciaSeleccionada = null;

        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtFechaCierre.setText("");
        txtMensaje.setText("");
        txtNota.setText("");
        txtObservacion.setText("");

        txtMensaje.setEditable(true);
        btnGuardar.setEnabled(true);

        tablaActividades.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new PortalEstudianteFrame().setVisible(true)
        );
    }
}