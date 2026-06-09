package vista;

import modelo.ConexionBD;
import modelo.SesionUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GestionActividadesDocenteFrame extends JFrame {

    private JComboBox<String> comboPracticas;

    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JTextField txtFechaCierre;

    private JTable tablaActividades;
    private DefaultTableModel modeloTabla;

    private JButton btnGuardar;
    private JButton btnOcultarMostrar;
    private JButton btnLimpiar;
    private JButton btnReportePDF;

    private Integer idActividadSeleccionada = null;
    private Integer idPracticaSeleccionada = null;

    public GestionActividadesDocenteFrame() {

        setTitle("Gestión de Actividades - Docente");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        iniciarComponentes();
        cargarPracticasDocente();
        cargarActividades();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel(
                "GESTIÓN DE ACTIVIDADES DEL DOCENTE",
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

        panelCentro.add(panelFiltros, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Práctica");
        modeloTabla.addColumn("Título");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Fecha Creación");
        modeloTabla.addColumn("Fecha Cierre");
        modeloTabla.addColumn("Visible");

        tablaActividades = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaActividades);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTitulo = new JTextField(20);

        txtDescripcion = new JTextArea(5, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        txtFechaCierre = new JTextField(20);

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
        panelFormulario.add(
                new JLabel("Fecha cierre (yyyy-MM-dd HH:mm:ss):"),
                gbc
        );

        gbc.gridx = 1;
        panelFormulario.add(txtFechaCierre, gbc);

        fila++;

        btnGuardar = new JButton("Guardar / Actualizar");
        btnOcultarMostrar = new JButton("Ocultar / Mostrar");
        btnLimpiar = new JButton("Limpiar");
        btnReportePDF = new JButton("Generar PDF");

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(btnGuardar, gbc);

        gbc.gridx = 1;
        panelFormulario.add(btnOcultarMostrar, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelFormulario.add(btnLimpiar, gbc);
        
        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelFormulario.add(btnReportePDF, gbc);

        add(panelFormulario, BorderLayout.EAST);
                comboPracticas.addActionListener(e -> cargarActividades());

        tablaActividades.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        btnGuardar.addActionListener(e -> guardarActividad());

        btnOcultarMostrar.addActionListener(e -> cambiarVisibilidad());

        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnReportePDF.addActionListener(e -> generarReportePDF());
    }

    private void cargarPracticasDocente() {

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

        modeloTabla.setRowCount(0);

        String practica = comboPracticas.getSelectedItem() != null
                ? comboPracticas.getSelectedItem().toString()
                : "TODAS";

        String sql =
                "SELECT a.idActividad, " +
                "p.idPractica, " +
                "p.codigoPractica, " +
                "a.titulo, " +
                "a.descripcion, " +
                "a.fechaHoraCreacion, " +
                "a.fechaHoraCierre, " +
                "a.visibilidad " +
                "FROM Actividades a " +
                "INNER JOIN Practica p ON a.idPractica = p.idPractica " +
                "WHERE a.idUsuario = ? ";

        if (!practica.equals("TODAS")) {
            sql += "AND p.codigoPractica = ? ";
        }

        sql += "ORDER BY a.fechaHoraCreacion DESC";

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

                modeloTabla.addRow(new Object[]{
                        rs.getInt("idActividad"),
                        rs.getString("codigoPractica"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getTimestamp("fechaHoraCreacion"),
                        rs.getTimestamp("fechaHoraCierre"),
                        rs.getInt("visibilidad") == 1 ? "VISIBLE" : "OCULTA"
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
                modeloTabla.getValueAt(fila, 2).toString()
        );

        txtDescripcion.setText(
                modeloTabla.getValueAt(fila, 3).toString()
        );

        txtFechaCierre.setText(
                modeloTabla.getValueAt(fila, 5).toString()
        );

        buscarIdPractica();
    }

    private void buscarIdPractica() {

        String sql =
                "SELECT idPractica " +
                "FROM Practica " +
                "WHERE codigoPractica = ?";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    comboPracticas.getSelectedItem().toString()
            );

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idPracticaSeleccionada = rs.getInt("idPractica");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error buscando práctica: " + ex.getMessage()
            );
        }
    }
        private void guardarActividad() {

        if (comboPracticas.getSelectedItem() == null ||
                comboPracticas.getSelectedItem().toString().equals("TODAS")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una práctica."
            );
            return;
        }

        if (txtTitulo.getText().trim().isEmpty() ||
                txtFechaCierre.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete los campos obligatorios."
            );
            return;
        }

        buscarIdPractica();

        try (
                Connection conn = ConexionBD.getConnection()
        ) {

            if (idActividadSeleccionada == null) {

                String sql =
                        "INSERT INTO Actividades " +
                        "(idActividad, idUsuario, idPractica, titulo, descripcion, fechaHoraCreacion, fechaHoraCierre, visibilidad) " +
                        "VALUES " +
                        "(SEQ_ACTIVIDADES.NEXTVAL, ?, ?, ?, ?, SYSDATE, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), 1)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setInt(1, SesionUsuario.idUsuario);
                    stmt.setInt(2, idPracticaSeleccionada);
                    stmt.setString(3, txtTitulo.getText().trim());
                    stmt.setString(4, txtDescripcion.getText().trim());
                    stmt.setString(5, txtFechaCierre.getText().trim());

                    stmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Actividad creada correctamente."
                );

            } else {

                String sql =
                        "UPDATE Actividades " +
                        "SET titulo = ?, " +
                        "descripcion = ?, " +
                        "fechaHoraCierre = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') " +
                        "WHERE idActividad = ? " +
                        "AND idUsuario = ?";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, txtTitulo.getText().trim());
                    stmt.setString(2, txtDescripcion.getText().trim());
                    stmt.setString(3, txtFechaCierre.getText().trim());
                    stmt.setInt(4, idActividadSeleccionada);
                    stmt.setInt(5, SesionUsuario.idUsuario);

                    stmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Actividad actualizada correctamente."
                );
            }

            cargarActividades();
            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error guardando actividad: " + ex.getMessage()
            );
        }
    }

    private void cambiarVisibilidad() {

        if (idActividadSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una actividad."
            );
            return;
        }

        try (
                Connection conn = ConexionBD.getConnection()
        ) {

            String sqlConsulta =
                    "SELECT visibilidad " +
                    "FROM Actividades " +
                    "WHERE idActividad = ? " +
                    "AND idUsuario = ?";

            int visibilidadActual = 1;

            try (PreparedStatement stmt = conn.prepareStatement(sqlConsulta)) {

                stmt.setInt(1, idActividadSeleccionada);
                stmt.setInt(2, SesionUsuario.idUsuario);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    visibilidadActual = rs.getInt("visibilidad");
                }
            }

            int nuevaVisibilidad = visibilidadActual == 1 ? 0 : 1;

            String sqlUpdate =
                    "UPDATE Actividades " +
                    "SET visibilidad = ? " +
                    "WHERE idActividad = ? " +
                    "AND idUsuario = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

                stmt.setInt(1, nuevaVisibilidad);
                stmt.setInt(2, idActividadSeleccionada);
                stmt.setInt(3, SesionUsuario.idUsuario);

                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Visibilidad actualizada."
            );

            cargarActividades();
            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error actualizando visibilidad: " + ex.getMessage()
            );
        }
    }

    private void limpiarFormulario() {

        idActividadSeleccionada = null;
        idPracticaSeleccionada = null;

        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtFechaCierre.setText("");

        tablaActividades.clearSelection();
    }
    
    private void generarReportePDF() {

        JFileChooser chooser = new JFileChooser();

        if (chooser.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String ruta =
                chooser.getSelectedFile()
                        .getAbsolutePath()
                + ".pdf";

        try {

            Document documento =
                    new Document(PageSize.A4.rotate());

            PdfWriter.getInstance(
                    documento,
                    new FileOutputStream(ruta)
            );

            documento.open();

            documento.add(
                    new Paragraph(
                            "REPORTE DE ACTIVIDADES"
                    )
            );

            documento.add(
                    new Paragraph(
                            "Docente: "
                            + SesionUsuario.nombreUsuario
                    )
            );

            documento.add(
                    new Paragraph(
                            "Práctica: "
                            + comboPracticas.getSelectedItem()
                    )
            );

            documento.add(
                    new Paragraph(" ")
            );

            PdfPTable tabla =
                    new PdfPTable(6);

            tabla.setWidthPercentage(100);

            tabla.addCell("Práctica");
            tabla.addCell("Título");
            tabla.addCell("Descripción");
            tabla.addCell("Fecha Creación");
            tabla.addCell("Fecha Cierre");
            tabla.addCell("Estado");

            for (int i = 0;
                 i < modeloTabla.getRowCount();
                 i++) {

                tabla.addCell(
                        modeloTabla.getValueAt(i,1).toString()
                );

                tabla.addCell(
                        modeloTabla.getValueAt(i,2).toString()
                );

                tabla.addCell(
                        modeloTabla.getValueAt(i,3).toString()
                );

                tabla.addCell(
                        modeloTabla.getValueAt(i,4).toString()
                );

                tabla.addCell(
                        modeloTabla.getValueAt(i,5).toString()
                );

                tabla.addCell(
                        modeloTabla.getValueAt(i,6).toString()
                );
            }

            documento.add(tabla);

            documento.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Reporte generado correctamente."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new GestionActividadesDocenteFrame().setVisible(true)
        );
    }
}