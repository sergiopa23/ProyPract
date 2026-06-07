/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reportes;

/**
 *
 * @author parra
 */


import java.sql.Connection;
import javax.swing.JOptionPane;
import modelo.ConexionBD;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

public class JasperManager {

    public static void mostrarReporte(String rutaReporte) {

        try {

            Connection conn = ConexionBD.getConnection();

            JasperReport reporte =
                    JasperCompileManager.compileReport(rutaReporte);

            JasperPrint print =
                    JasperFillManager.fillReport(
                            reporte,
                            null,
                            conn
                    );

            JasperViewer.viewReport(print, false);

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error al generar reporte:\n"
                    + ex.getMessage()
            );
        }
    }

    public static void exportarPDF(
            String rutaReporte,
            String destinoPDF
    ) {

        try {

            Connection conn = ConexionBD.getConnection();

            JasperReport reporte =
                    JasperCompileManager.compileReport(rutaReporte);

            JasperPrint print =
                    JasperFillManager.fillReport(
                            reporte,
                            null,
                            conn
                    );

            JasperExportManager.exportReportToPdfFile(
                    print,
                    destinoPDF
            );

            JOptionPane.showMessageDialog(
                    null,
                    "PDF generado correctamente"
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error:\n"
                    + ex.getMessage()
            );
        }
    }
}