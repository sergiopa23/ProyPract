package vista;

import modelo.ConexionBD;
import modelo.SesionUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UsuariosAsignadosDocenteFrame extends JFrame {

    private JComboBox<String> comboPracticas;
    private JTextField txtBuscarDocumento;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    public UsuariosAsignadosDocenteFrame() {

        setTitle("Usuarios Asignados - Docente");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarPracticas();
        cargarUsuarios();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel(
                "USUARIOS ASIGNADOS AL DOCENTE",
                SwingConstants.CENTER
        );

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panelFiltros.add(new JLabel("Práctica:"));

        comboPracticas = new JComboBox<>();
        comboPracticas.addItem("TODAS");

        panelFiltros.add(comboPracticas);

        panelFiltros.add(new JLabel("Documento:"));

        txtBuscarDocumento = new JTextField(15);
        panelFiltros.add(txtBuscarDocumento);

        JButton btnBuscar = new JButton("Buscar");
        panelFiltros.add(btnBuscar);

        add(panelFiltros, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("Documento");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("Rol");
        modeloTabla.addColumn("Práctica");
        modeloTabla.addColumn("Cantidad Horas");

        tablaUsuarios = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> cargarUsuarios());

        comboPracticas.addActionListener(e -> cargarUsuarios());
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

    private void cargarUsuarios() {

        modeloTabla.setRowCount(0);

        String practica = comboPracticas.getSelectedItem() != null
                ? comboPracticas.getSelectedItem().toString()
                : "TODAS";

        String documento = txtBuscarDocumento.getText().trim();

        String sql =
                "SELECT u.idusuario, " +
                "u.nombreUsuario, " +
                "u.apellidoUsuario, " +
                "r.nombreRol, " +
                "p.codigoPractica, " +
                "pu.cantidadHoras " +
                "FROM PracticaUsuario pu " +
                "INNER JOIN Usuarios u ON pu.idUsuario = u.idUsuario " +
                "INNER JOIN RolUsuario r ON pu.idRol = r.idRol " +
                "INNER JOIN Practica p ON pu.idPractica = p.idPractica " +
                "WHERE pu.idRol IN (2, 4) " +
                "AND pu.idPractica IN ( " +
                "   SELECT idPractica " +
                "   FROM PracticaUsuario " +
                "   WHERE idUsuario = ? " +
                "   AND idRol = 3 " +
                ") ";

        if (!practica.equals("TODAS")) {
            sql += "AND p.codigoPractica = ? ";
        }

        if (!documento.isEmpty()) {
            sql += "AND u.idusuario LIKE ? ";
        }

        sql += "ORDER BY r.nombreRol, u.nombreUsuario";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            int indice = 1;

            stmt.setInt(indice++, SesionUsuario.idUsuario);

            if (!practica.equals("TODAS")) {
                stmt.setString(indice++, practica);
            }

            if (!documento.isEmpty()) {
                stmt.setString(indice++, "%" + documento + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Object horas = "";

                if ("Estudiante".equalsIgnoreCase(
                        rs.getString("nombreRol"))) {
                    horas = rs.getInt("cantidadHoras");
                }

                modeloTabla.addRow(new Object[]{
                        rs.getString("idusuario"),
                        rs.getString("nombreUsuario"),
                        rs.getString("apellidoUsuario"),
                        rs.getString("nombreRol"),
                        rs.getString("codigoPractica"),
                        horas
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando usuarios: " + ex.getMessage()
            );
        }
    }
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new UsuariosAsignadosDocenteFrame().setVisible(true)
        );
    }
}