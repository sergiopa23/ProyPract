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

public class GestionHorasSupervisorFrame extends JFrame {

    private JComboBox<String> comboPracticas;
    private JTextField txtBuscarDocumento;

    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;

    private JTextField txtDocumento;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtPractica;

    private JSpinner spinnerHoras;

    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnCerrarSesion;
    private JButton btnReportePDF;

    private Integer idUsuarioSeleccionado = null;
    private Integer idPracticaSeleccionada = null;

    public GestionHorasSupervisorFrame() {
        setTitle("Gestión de Horas - Supervisor");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        iniciarComponentes();
        cargarPracticasSupervisor();
        cargarEstudiantes();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel(
                "GESTIÓN DE HORAS DE ESTUDIANTES",
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

        panelFiltros.add(new JLabel("Buscar Documento:"));

        txtBuscarDocumento = new JTextField(15);
        panelFiltros.add(txtBuscarDocumento);

        JButton btnBuscar = new JButton("Buscar");
        panelFiltros.add(btnBuscar);

        panelCentro.add(panelFiltros, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("Documento");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("Práctica");
        modeloTabla.addColumn("Horas");

        tablaEstudiantes = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaEstudiantes);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDocumento = new JTextField(15);
        txtDocumento.setEditable(false);

        txtNombre = new JTextField(15);
        txtNombre.setEditable(false);

        txtApellido = new JTextField(15);
        txtApellido.setEditable(false);

        txtPractica = new JTextField(15);
        txtPractica.setEditable(false);

        spinnerHoras = new JSpinner(
                new SpinnerNumberModel(0, 0, 180, 1)
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
        panelFormulario.add(new JLabel("Práctica:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(txtPractica, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(new JLabel("Horas:"), gbc);

        gbc.gridx = 1;
        panelFormulario.add(spinnerHoras, gbc);

        fila++;

        btnGuardar = new JButton("Guardar / Actualizar Horas");
        btnLimpiar = new JButton("Limpiar");
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnReportePDF = new JButton("Generar PDF");
        
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelFormulario.add(btnGuardar, gbc);

        gbc.gridx = 1;
        panelFormulario.add(btnLimpiar, gbc);
        
        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelFormulario.add(btnReportePDF, gbc);

        fila++;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        panelFormulario.add(btnCerrarSesion, gbc);

        add(panelFormulario, BorderLayout.EAST);
                comboPracticas.addActionListener(e -> cargarEstudiantes());

        btnBuscar.addActionListener(e -> cargarEstudiantes());

        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        btnGuardar.addActionListener(e -> guardarHoras());

        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnReportePDF.addActionListener(e -> generarPDF());
        btnCerrarSesion.addActionListener(e -> {
            SesionUsuario.limpiarSesion();
            new LoginFramePro().setVisible(true);
            dispose();
        });
    }

    private void cargarPracticasSupervisor() {

        String sql =
                "SELECT DISTINCT p.codigoPractica " +
                "FROM PracticaUsuario pu " +
                "INNER JOIN Practica p ON pu.idPractica = p.idPractica " +
                "WHERE pu.idUsuario = ? " +
                "AND pu.idRol = 4 " +
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

    private void cargarEstudiantes() {

        modeloTabla.setRowCount(0);

        String practica = comboPracticas.getSelectedItem() != null
                ? comboPracticas.getSelectedItem().toString()
                : "TODAS";

        String documento = txtBuscarDocumento.getText().trim();

        String sql =
                "SELECT u.idUsuario, " +
                "u.idUsuario, " +
                "u.nombreUsuario, " +
                "u.apellidoUsuario, " +
                "p.idPractica, " +
                "p.codigoPractica, " +
                "pu.cantidadHoras " +
                "FROM PracticaUsuario pu " +
                "INNER JOIN Usuarios u ON pu.idUsuario = u.idUsuario " +
                "INNER JOIN Practica p ON pu.idPractica = p.idPractica " +
                "WHERE pu.idRol = 2 " +
                "AND pu.idPractica IN ( " +
                "   SELECT idPractica " +
                "   FROM PracticaUsuario " +
                "   WHERE idUsuario = ? " +
                "   AND idRol = 4 " +
                ")";

        if (!practica.equals("TODAS")) {
            sql += " AND p.codigoPractica = ?";
        }

        if (!documento.isEmpty()) {
            sql += " AND u.idUsuario LIKE ?";
        }

        sql += " ORDER BY u.nombreUsuario";

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

                modeloTabla.addRow(new Object[]{
                        rs.getString("idUsuario"),
                        rs.getString("nombreUsuario"),
                        rs.getString("apellidoUsuario"),
                        rs.getString("codigoPractica"),
                        rs.getInt("cantidadHoras")
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error cargando estudiantes: " + ex.getMessage()
            );
        }
    }

    private void cargarSeleccion() {

        int fila = tablaEstudiantes.getSelectedRow();

        if (fila < 0) {
            return;
        }

        txtDocumento.setText(
                modeloTabla.getValueAt(fila, 0).toString()
        );

        txtNombre.setText(
                modeloTabla.getValueAt(fila, 1).toString()
        );

        txtApellido.setText(
                modeloTabla.getValueAt(fila, 2).toString()
        );

        txtPractica.setText(
                modeloTabla.getValueAt(fila, 3).toString()
        );

        spinnerHoras.setValue(
                Integer.parseInt(
                        modeloTabla.getValueAt(fila, 4).toString()
                )
        );

        buscarIdsInternos();
    }
        private void buscarIdsInternos() {

        String sql =
                "SELECT u.idUsuario, p.idPractica " +
                "FROM Usuarios u " +
                "INNER JOIN PracticaUsuario pu ON u.idUsuario = pu.idUsuario " +
                "INNER JOIN Practica p ON pu.idPractica = p.idPractica " +
                "WHERE u.idUsuario = ? " +
                "AND p.codigoPractica = ? " +
                "AND pu.idRol = 2";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, txtDocumento.getText().trim());
            stmt.setString(2, txtPractica.getText().trim());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idUsuarioSeleccionado = rs.getInt("idUsuario");
                idPracticaSeleccionada = rs.getInt("idPractica");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error obteniendo datos internos: " + ex.getMessage()
            );
        }
    }

    private void guardarHoras() {

        if (idUsuarioSeleccionado == null || idPracticaSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante."
            );
            return;
        }

        int horas = (Integer) spinnerHoras.getValue();

        String sql =
                "UPDATE PracticaUsuario " +
                "SET cantidadHoras = ? " +
                "WHERE idUsuario = ? " +
                "AND idPractica = ? " +
                "AND idRol = 2";

        try (
                Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, horas);
            stmt.setInt(2, idUsuarioSeleccionado);
            stmt.setInt(3, idPracticaSeleccionada);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Horas actualizadas correctamente."
            );

            cargarEstudiantes();
            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error actualizando horas: " + ex.getMessage()
            );
        }
    }

    private void limpiarFormulario() {

        idUsuarioSeleccionado = null;
        idPracticaSeleccionada = null;

        txtDocumento.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtPractica.setText("");

        spinnerHoras.setValue(0);

        tablaEstudiantes.clearSelection();
    }
    
    private void generarPDF() {

        JFileChooser chooser = new JFileChooser();

        if (chooser.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String ruta =
                chooser.getSelectedFile().getAbsolutePath()
                + ".pdf";

        try {

            Document documento =
                    new Document(PageSize.A4);

            PdfWriter.getInstance(
                    documento,
                    new FileOutputStream(ruta)
            );

            documento.open();

            documento.add(
                    new Paragraph(
                            "REPORTE DE HORAS DE ESTUDIANTES"
                    )
            );

            documento.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(5);

            tabla.addCell("Documento");
            tabla.addCell("Nombre");
            tabla.addCell("Apellido");
            tabla.addCell("Práctica");
            tabla.addCell("Horas");

            for (int i = 0;
                 i < modeloTabla.getRowCount();
                 i++) {

                tabla.addCell(
                        modeloTabla.getValueAt(i,0).toString()
                );

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
            }

            documento.add(tabla);

            documento.close();

            JOptionPane.showMessageDialog(
                    this,
                    "PDF generado correctamente."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new GestionHorasSupervisorFrame().setVisible(true)
        );
    }
}