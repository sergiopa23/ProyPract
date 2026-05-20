package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestionActividadesAdminFrame extends JFrame {

    private JComboBox<String> comboPracticas;
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JTextField txtFechaCierre;

    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnOcultarMostrar;
    private JButton btnLimpiar;

    private JTable tablaActividades;
    private DefaultTableModel modeloTabla;

    private Integer idActividadSeleccionada = null;

    public GestionActividadesAdminFrame() {
        setTitle("Gestión de Actividades");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarPracticasCombo();
        cargarActividadesPorPractica();
    }

    private void iniciarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboPracticas = new JComboBox<>();
        txtTitulo = new JTextField(20);
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtFechaCierre = new JTextField(20);

        int fila = 0;

        agregarCombo(panelFormulario, gbc, fila++, "Práctica:", comboPracticas);
        agregarCampo(panelFormulario, gbc, fila++, "Título:", txtTitulo);

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(new JScrollPane(txtDescripcion), gbc);
        fila++;

        agregarCampo(
                panelFormulario,
                gbc,
                fila++,
                "Fecha cierre (yyyy-MM-dd HH:mm:ss):",
                txtFechaCierre
        );

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnGuardar = new JButton("Guardar / Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnOcultarMostrar = new JButton("Ocultar / Mostrar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnOcultarMostrar);
        panelBotones.add(btnLimpiar);

        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();

        modeloTabla.setColumnIdentifiers(new String[]{
                "ID",
                "Práctica",
                "Docente",
                "Título",
                "Descripción",
                "Fecha Creación",
                "Fecha Cierre",
                "Visible"
        });

        tablaActividades = new JTable(modeloTabla);
        tablaActividades.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaActividades);
        add(scrollPane, BorderLayout.CENTER);

        comboPracticas.addActionListener(e -> cargarActividadesPorPractica());

        btnGuardar.addActionListener(e -> guardarActividad());
        btnEliminar.addActionListener(e -> eliminarActividad());
        btnOcultarMostrar.addActionListener(e -> cambiarVisibilidad());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaActividades.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarActividadSeleccionada();
            }
        });
    }

    private void agregarCombo(JPanel panel, GridBagConstraints gbc,
                              int fila, String texto, JComboBox<String> combo) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(texto), gbc);

        gbc.gridx = 1;
        panel.add(combo, gbc);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc,
                              int fila, String texto, JTextField campo) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(texto), gbc);

        gbc.gridx = 1;
        panel.add(campo, gbc);
    }

    private void cargarPracticasCombo() {
        comboPracticas.removeAllItems();
        comboPracticas.addItem("TODAS");

        String sql = "SELECT codigoPractica FROM Practica ORDER BY codigoPractica";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                comboPracticas.addItem(rs.getString("codigoPractica"));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando prácticas: " + ex.getMessage());
        }
    }

    private void guardarActividad() {
        if (comboPracticas.getSelectedItem() == null ||
                comboPracticas.getSelectedItem().toString().equals("TODAS")) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una práctica.");
            return;
        }

        if (txtTitulo.getText().trim().isEmpty() ||
                txtDescripcion.getText().trim().isEmpty() ||
                txtFechaCierre.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Complete todos los campos.");
            return;
        }

        try (
                Connection conn = ConexionBD.getConnection()
        ) {
            String codigoPractica = comboPracticas.getSelectedItem().toString();

            int idPractica = obtenerIdPractica(conn, codigoPractica);
            int idDocente = obtenerDocenteDePractica(conn, idPractica);

            if (idDocente == 0) {
                JOptionPane.showMessageDialog(this,
                        "No existe docente asignado a esta práctica.");
                return;
            }

            if (idActividadSeleccionada == null) {

                String sqlInsert = "INSERT INTO Actividades " +
                        "(idUsuario, idPractica, titulo, descripcion, fechaHoraCreacion, fechaHoraCierre, visibilidad) " +
                        "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, 1)";

                try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {

                    stmt.setInt(1, idDocente);
                    stmt.setInt(2, idPractica);
                    stmt.setString(3, txtTitulo.getText().trim());
                    stmt.setString(4, txtDescripcion.getText().trim());
                    stmt.setTimestamp(5, Timestamp.valueOf(txtFechaCierre.getText().trim()));

                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(this,
                            "Actividad creada correctamente.");
                }

            } else {

                String sqlUpdate = "UPDATE Actividades " +
                        "SET idUsuario=?, idPractica=?, titulo=?, descripcion=?, fechaHoraCierre=? " +
                        "WHERE idActividad=?";

                try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

                    stmt.setInt(1, idDocente);
                    stmt.setInt(2, idPractica);
                    stmt.setString(3, txtTitulo.getText().trim());
                    stmt.setString(4, txtDescripcion.getText().trim());
                    stmt.setTimestamp(5, Timestamp.valueOf(txtFechaCierre.getText().trim()));
                    stmt.setInt(6, idActividadSeleccionada);

                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(this,
                            "Actividad actualizada correctamente.");
                }
            }

            cargarActividadesPorPractica();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage());
        }
    }

    private void cargarActividadesPorPractica() {
        modeloTabla.setRowCount(0);

        String practicaSeleccionada = comboPracticas.getSelectedItem() != null
                ? comboPracticas.getSelectedItem().toString()
                : "TODAS";

        String sql;

        if (practicaSeleccionada.equals("TODAS")) {
            sql = "SELECT a.idActividad, p.codigoPractica, " +
                    "u.nombreUsuario, u.apellidoUsuario, " +
                    "a.titulo, a.descripcion, " +
                    "a.fechaHoraCreacion, a.fechaHoraCierre, a.visibilidad " +
                    "FROM Actividades a " +
                    "INNER JOIN Practica p ON a.idPractica = p.idPractica " +
                    "INNER JOIN Usuarios u ON a.idUsuario = u.idUsuario " +
                    "ORDER BY a.idActividad DESC";
        } else {
            sql = "SELECT a.idActividad, p.codigoPractica, " +
                    "u.nombreUsuario, u.apellidoUsuario, " +
                    "a.titulo, a.descripcion, " +
                    "a.fechaHoraCreacion, a.fechaHoraCierre, a.visibilidad " +
                    "FROM Actividades a " +
                    "INNER JOIN Practica p ON a.idPractica = p.idPractica " +
                    "INNER JOIN Usuarios u ON a.idUsuario = u.idUsuario " +
                    "WHERE p.codigoPractica = ? " +
                    "ORDER BY a.idActividad DESC";
        }

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            if (!practicaSeleccionada.equals("TODAS")) {
                stmt.setString(1, practicaSeleccionada);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("idActividad"),
                        rs.getString("codigoPractica"),
                        rs.getString("nombreUsuario") + " " + rs.getString("apellidoUsuario"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fechaHoraCreacion"),
                        rs.getTimestamp("fechaHoraCierre"),
                        rs.getInt("visibilidad") == 1 ? "VISIBLE" : "OCULTA"
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando actividades: " + ex.getMessage());
        }
    }

    private void eliminarActividad() {

        if (idActividadSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una actividad.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar actividad?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try (
                Connection conn = ConexionBD.getConnection()
        ) {

            String sql = "DELETE FROM Actividades WHERE idActividad=?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idActividadSeleccionada);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Actividad eliminada.");

            cargarActividadesPorPractica();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error eliminando: " + ex.getMessage());
        }
    }

    private void cambiarVisibilidad() {

        if (idActividadSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una actividad.");
            return;
        }

        try (
                Connection conn = ConexionBD.getConnection()
        ) {

            String sqlConsulta = "SELECT visibilidad FROM Actividades WHERE idActividad=?";

            int visibilidadActual = 1;

            try (PreparedStatement stmt = conn.prepareStatement(sqlConsulta)) {
                stmt.setInt(1, idActividadSeleccionada);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    visibilidadActual = rs.getInt("visibilidad");
                }
            }

            int nuevaVisibilidad = visibilidadActual == 1 ? 0 : 1;

            String sqlUpdate = "UPDATE Actividades SET visibilidad=? WHERE idActividad=?";

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
                stmt.setInt(1, nuevaVisibilidad);
                stmt.setInt(2, idActividadSeleccionada);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "Visibilidad actualizada.");

            cargarActividadesPorPractica();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error actualizando visibilidad: " + ex.getMessage());
        }
    }

    private void cargarActividadSeleccionada() {
        int fila = tablaActividades.getSelectedRow();

        if (fila < 0) {
            return;
        }

        idActividadSeleccionada = Integer.parseInt(
                modeloTabla.getValueAt(fila, 0).toString()
        );

        comboPracticas.setSelectedItem(
                modeloTabla.getValueAt(fila, 1).toString()
        );

        txtTitulo.setText(
                modeloTabla.getValueAt(fila, 3).toString()
        );

        txtDescripcion.setText(
                modeloTabla.getValueAt(fila, 4).toString()
        );

        txtFechaCierre.setText(
                modeloTabla.getValueAt(fila, 6).toString()
        );
    }

    private void limpiarCampos() {
        idActividadSeleccionada = null;

        txtTitulo.setText("");
        txtDescripcion.setText("");

        txtFechaCierre.setText(
                LocalDateTime.now().plusDays(7)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        tablaActividades.clearSelection();
    }

    private int obtenerIdPractica(Connection conn, String codigoPractica) throws SQLException {
        String sql = "SELECT idPractica FROM Practica WHERE codigoPractica=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigoPractica);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("idPractica");
            }
        }

        return 0;
    }

    private int obtenerDocenteDePractica(Connection conn, int idPractica) throws SQLException {
        String sql = "SELECT idUsuario " +
                "FROM PracticaUsuario " +
                "WHERE idPractica=? AND idRol=3";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPractica);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("idUsuario");
            }
        }

        return 0;
    }
}