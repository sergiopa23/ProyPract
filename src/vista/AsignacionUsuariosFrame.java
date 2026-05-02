package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AsignacionUsuariosFrame extends JFrame {

    private JComboBox<String> comboUsuarios;
    private JComboBox<String> comboPracticas;
    private JComboBox<String> comboRoles;
    private JTextField txtHoras;

    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JTable tablaAsignaciones;
    private DefaultTableModel modeloTabla;

    public AsignacionUsuariosFrame() {
        setTitle("Asignación de Usuarios a Práctica");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarUsuariosCombo();
        cargarPracticasCombo();
        cargarRolesCombo();
        actualizarHorasAutomaticamente();
        cargarAsignacionesPorPractica();
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

        comboUsuarios = new JComboBox<>();
        comboPracticas = new JComboBox<>();
        comboRoles = new JComboBox<>();
        txtHoras = new JTextField(15);
        txtHoras.setEditable(false);

        int fila = 0;

        agregarCombo(panelFormulario, gbc, fila++, "Usuario:", comboUsuarios);
        agregarCombo(panelFormulario, gbc, fila++, "Práctica:", comboPracticas);
        agregarCombo(panelFormulario, gbc, fila++, "Rol:", comboRoles);

        agregarCampo(panelFormulario, gbc, fila++, "Cantidad Horas:", txtHoras);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        // =====================================
        // BOTONES
        // =====================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // =====================================
        // TABLA
        // =====================================
        modeloTabla = new DefaultTableModel();

        modeloTabla.setColumnIdentifiers(new String[]{
                "Usuario",
                "Cédula",
                "Práctica",
                "Rol",
                "Fecha Asignación",
                "Horas"
        });

        tablaAsignaciones = new JTable(modeloTabla);
        tablaAsignaciones.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaAsignaciones);
        add(scrollPane, BorderLayout.CENTER);

        // =====================================
        // EVENTOS
        // =====================================
        comboRoles.addActionListener(e -> actualizarHorasAutomaticamente());

        comboPracticas.addActionListener(e -> cargarAsignacionesPorPractica());

        btnGuardar.addActionListener(e -> guardarAsignacion());
        btnEliminar.addActionListener(e -> eliminarAsignacion());
        btnLimpiar.addActionListener(e -> limpiarCampos());
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

    // =====================================
    // CARGAR COMBOS
    // =====================================

    private void cargarUsuariosCombo() {
        comboUsuarios.removeAllItems();

        String sql = "SELECT idUsuario, nombreUsuario, apellidoUsuario " +
                     "FROM Usuarios " +
                     "WHERE esAdministrador = 0 " +
                     "ORDER BY nombreUsuario";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String texto = rs.getInt("idUsuario") + " - "
                        + rs.getString("nombreUsuario") + " "
                        + rs.getString("apellidoUsuario");

                comboUsuarios.addItem(texto);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando usuarios: " + ex.getMessage());
        }
    }

    private void cargarPracticasCombo() {
        comboPracticas.removeAllItems();

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

    private void cargarRolesCombo() {
        comboRoles.removeAllItems();

        comboRoles.addItem("Estudiante");
        comboRoles.addItem("Docente");
        comboRoles.addItem("Supervisor");
    }

    // =====================================
    // HORAS AUTOMÁTICAS
    // =====================================

    private void actualizarHorasAutomaticamente() {
        String rol = comboRoles.getSelectedItem() != null
                ? comboRoles.getSelectedItem().toString()
                : "";

        if (rol.equals("Estudiante")) {
            txtHoras.setText("0");
        } else {
            txtHoras.setText("NULL");
        }
    }

    // =====================================
    // GUARDAR
    // =====================================

    private void guardarAsignacion() {
        if (comboUsuarios.getSelectedItem() == null ||
            comboPracticas.getSelectedItem() == null ||
            comboRoles.getSelectedItem() == null) {
            return;
        }

        try (
                Connection conn = ConexionBD.getConnection()
        ) {
            int idUsuario = Integer.parseInt(
                    comboUsuarios.getSelectedItem().toString().split(" - ")[0]
            );

            String codigoPractica = comboPracticas.getSelectedItem().toString();
            String rolNombre = comboRoles.getSelectedItem().toString();

            int idPractica = obtenerIdPractica(conn, codigoPractica);
            int idRol = obtenerIdRol(conn, rolNombre);

            // =====================================
            // VALIDACIÓN ESTUDIANTE
            // =====================================
            if (rolNombre.equals("Estudiante")) {

                if (estudianteYaAsignadoEnOtraPractica(conn, idUsuario, idPractica)) {
                    JOptionPane.showMessageDialog(this,
                            "Este estudiante ya pertenece a otra práctica.");
                    return;
                }

                if (cupoCompleto(conn, idPractica, idRol)) {
                    JOptionPane.showMessageDialog(this,
                            "Cupo completo. Ya existen 8 estudiantes en esta práctica.");
                    return;
                }
            }

            // =====================================
            // VALIDAR SI YA EXISTE EN ESA PRÁCTICA
            // =====================================
            String sqlExiste = "SELECT COUNT(*) total " +
                               "FROM PracticaUsuario " +
                               "WHERE idUsuario = ? " +
                               "AND idPractica = ?";

            try (PreparedStatement stmtExiste = conn.prepareStatement(sqlExiste)) {

                stmtExiste.setInt(1, idUsuario);
                stmtExiste.setInt(2, idPractica);

                ResultSet rs = stmtExiste.executeQuery();

                if (rs.next() && rs.getInt("total") > 0) {

                    // =====================================
                    // UPDATE
                    // =====================================
                    String sqlUpdate = "UPDATE PracticaUsuario " +
                                       "SET idRol = ?, cantidadHoras = ? " +
                                       "WHERE idUsuario = ? " +
                                       "AND idPractica = ?";

                    try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {

                        stmtUpdate.setInt(1, idRol);

                        if (rolNombre.equals("Estudiante")) {
                            stmtUpdate.setInt(2, 0);
                        } else {
                            stmtUpdate.setNull(2, Types.INTEGER);
                        }

                        stmtUpdate.setInt(3, idUsuario);
                        stmtUpdate.setInt(4, idPractica);

                        stmtUpdate.executeUpdate();

                        JOptionPane.showMessageDialog(this,
                                "El usuario ya estaba asignado.\nSe actualizó su rol correctamente.");
                    }

                } else {

                    // =====================================
                    // INSERT
                    // =====================================
                    String sqlInsert = "INSERT INTO PracticaUsuario " +
                            "(idUsuario, idRol, idPractica, cantidadHoras) " +
                            "VALUES (?, ?, ?, ?)";

                    try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {

                        stmtInsert.setInt(1, idUsuario);
                        stmtInsert.setInt(2, idRol);
                        stmtInsert.setInt(3, idPractica);

                        if (rolNombre.equals("Estudiante")) {
                            stmtInsert.setInt(4, 0);
                        } else {
                            stmtInsert.setNull(4, Types.INTEGER);
                        }

                        stmtInsert.executeUpdate();

                        JOptionPane.showMessageDialog(this,
                                "Asignación guardada correctamente");
                    }
                }
            }

            cargarAsignacionesPorPractica();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage());
        }
    }
    
    private boolean estudianteYaAsignadoEnOtraPractica(
            Connection conn,
            int idUsuario,
            int idPracticaActual
    ) throws SQLException {

        String sql = "SELECT COUNT(*) total " +
                     "FROM PracticaUsuario pu " +
                     "INNER JOIN RolUsuario r ON pu.idRol = r.idRol " +
                     "WHERE pu.idUsuario = ? " +
                     "AND r.nombreRol = 'Estudiante' " +
                     "AND pu.idPractica <> ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idPracticaActual);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        }

        return false;
    }

    // =====================================
    // VALIDACIONES
    // =====================================

    private boolean estudianteYaAsignado(Connection conn, int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) total " +
                     "FROM PracticaUsuario pu " +
                     "INNER JOIN RolUsuario r ON pu.idRol = r.idRol " +
                     "WHERE pu.idUsuario = ? " +
                     "AND r.nombreRol = 'Estudiante'";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        }

        return false;
    }

    private boolean cupoCompleto(Connection conn, int idPractica, int idRol) throws SQLException {
        String sql = "SELECT COUNT(*) total " +
                     "FROM PracticaUsuario " +
                     "WHERE idPractica = ? " +
                     "AND idRol = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPractica);
            stmt.setInt(2, idRol);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") >= 8;
            }
        }

        return false;
    }

    // =====================================
    // TABLA FILTRADA POR PRÁCTICA
    // =====================================

    private void cargarAsignacionesPorPractica() {
        modeloTabla.setRowCount(0);

        if (comboPracticas.getSelectedItem() == null) {
            return;
        }

        String codigoPractica = comboPracticas.getSelectedItem().toString();

        String sql = "SELECT u.nombreUsuario, u.apellidoUsuario, u.idUsuario, " +
                     "p.codigoPractica, r.nombreRol, " +
                     "pu.fechaAsignacionUsuario, pu.cantidadHoras " +
                     "FROM PracticaUsuario pu " +
                     "INNER JOIN Usuarios u ON pu.idUsuario = u.idUsuario " +
                     "INNER JOIN Practica p ON pu.idPractica = p.idPractica " +
                     "INNER JOIN RolUsuario r ON pu.idRol = r.idRol " +
                     "WHERE p.codigoPractica = ? " +
                     "ORDER BY r.nombreRol";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, codigoPractica);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getString("nombreUsuario") + " " + rs.getString("apellidoUsuario"),
                        rs.getInt("idUsuario"),
                        rs.getString("codigoPractica"),
                        rs.getString("nombreRol"),
                        rs.getDate("fechaAsignacionUsuario"),
                        rs.getObject("cantidadHoras")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando asignaciones: " + ex.getMessage());
        }
    }

    // =====================================
    // ELIMINAR
    // =====================================

    private void eliminarAsignacion() {
        int fila = tablaAsignaciones.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una asignación de la tabla.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar esta asignación?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try (
                Connection conn = ConexionBD.getConnection()
        ) {
            int idUsuario = Integer.parseInt(
                    modeloTabla.getValueAt(fila, 1).toString()
            );

            String codigoPractica = modeloTabla.getValueAt(fila, 2).toString();
            String rolNombre = modeloTabla.getValueAt(fila, 3).toString();

            int idPractica = obtenerIdPractica(conn, codigoPractica);
            int idRol = obtenerIdRol(conn, rolNombre);

            String sql = "DELETE FROM PracticaUsuario " +
                         "WHERE idUsuario = ? " +
                         "AND idPractica = ? " +
                         "AND idRol = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idUsuario);
                stmt.setInt(2, idPractica);
                stmt.setInt(3, idRol);

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Asignación eliminada");

                cargarAsignacionesPorPractica();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + ex.getMessage());
        }
    }

    // =====================================
    // LIMPIAR
    // =====================================

    private void limpiarCampos() {
        if (comboUsuarios.getItemCount() > 0) {
            comboUsuarios.setSelectedIndex(0);
        }

        if (comboPracticas.getItemCount() > 0) {
            comboPracticas.setSelectedIndex(0);
        }

        comboRoles.setSelectedIndex(0);
        actualizarHorasAutomaticamente();
        cargarAsignacionesPorPractica();
    }

    // =====================================
    // MÉTODOS AUXILIARES
    // =====================================

    private int obtenerIdPractica(Connection conn, String codigoPractica) throws SQLException {
        String sql = "SELECT idPractica FROM Practica WHERE codigoPractica = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigoPractica);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("idPractica");
            }
        }

        return 0;
    }

    private int obtenerIdRol(Connection conn, String nombreRol) throws SQLException {
        String sql = "SELECT idRol FROM RolUsuario WHERE nombreRol = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreRol);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("idRol");
            }
        }

        return 0;
    }
}